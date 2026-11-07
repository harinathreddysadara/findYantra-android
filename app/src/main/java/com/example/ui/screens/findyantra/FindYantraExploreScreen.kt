package com.example.ui.screens.findyantra

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Equipment
import com.example.model.EquipmentCategory
import com.example.model.RentalType
import com.example.ui.components.InteractiveMapCard
import com.example.ui.theme.*

@Composable
fun FindYantraExploreScreen(
    equipments: List<Equipment>,
    currentWorksite: String,
    onWorksiteClick: () -> Unit,
    onEquipmentClick: (Equipment) -> Unit,
    onOpenYantraStore: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<EquipmentCategory?>(null) }
    var showAllNearbyList by remember { mutableStateOf(false) }
    var rentalFilter by remember { mutableStateOf(RentalType.HOURLY) }
    var selectedPinRate by remember { mutableStateOf<String?>("1200") }

    val filteredEquipments = remember(equipments, searchQuery, selectedCategory, rentalFilter) {
        equipments.filter { eq ->
            eq.isPublicOnFindYantra &&
            (searchQuery.isEmpty() || eq.name.contains(searchQuery, ignoreCase = true) || eq.category.label.contains(searchQuery, ignoreCase = true)) &&
            (selectedCategory == null || eq.category == selectedCategory) &&
            when (rentalFilter) {
                RentalType.HOURLY -> true
                RentalType.DAILY -> eq.dailyRate != null
                RentalType.PER_TRIP -> eq.perTripRate != null
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Worksite selector header
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onWorksiteClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "YOUR WORKSITE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = currentWorksite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change worksite",
                        tint = YantraTextSecondary
                    )
                }
            }
            HorizontalDivider(color = YantraBorder, thickness = 1.dp)
        }

        // Hero Title matching Mock 1
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "The right machine.\nRight where you need it.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = YantraTextPrimary,
                    lineHeight = 30.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rent by the hour, day or trip.",
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )
            }
        }

        // Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Tractor, excavator, tipper…", color = YantraTextMuted) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = YantraTextSecondary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = YantraBorder,
                        focusedBorderColor = YantraPrimary
                    ),
                    singleLine = true
                )
            }
        }

        // Category Chips Row (Farming, Infrastructure, Construction, Heavy lifting)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(EquipmentCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) YantraPrimary else Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, if (isSelected) YantraPrimary else YantraBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedCategory = if (isSelected) null else category
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = when (category) {
                                    EquipmentCategory.FARMING -> Icons.Default.Agriculture
                                    EquipmentCategory.INFRASTRUCTURE -> Icons.Default.AltRoute
                                    EquipmentCategory.CONSTRUCTION -> Icons.Default.Architecture
                                    EquipmentCategory.HEAVY_LIFTING -> Icons.Default.Engineering
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color.White else YantraPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = category.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else YantraTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Rental Type Filter Chips (Hourly, Daily, Per trip)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RentalType.values().forEach { type ->
                    val isSelected = rentalFilter == type
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) YantraHeaderDark else Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (isSelected) YantraHeaderDark else YantraBorder, RoundedCornerShape(20.dp))
                            .clickable { rentalFilter = type }
                    ) {
                        Text(
                            text = type.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else YantraTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Section header: "Machines around you" + "View all 12"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Machines around you",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = if (showAllNearbyList) "Hide map" else "View all 12",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraPrimary,
                    modifier = Modifier.clickable { showAllNearbyList = !showAllNearbyList }
                )
            }
        }

        // Map Canvas Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                InteractiveMapCard(
                    isTrackingMode = false,
                    selectedPricePin = selectedPinRate,
                    onPinClick = { rate ->
                        selectedPinRate = rate
                    }
                )
            }
        }

        // Results count / list info
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredEquipments.size} machines available",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = YantraTextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Sort",
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Nearest first",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraPrimary
                    )
                }
            }
        }

        // List of Machines matching Mock 2
        items(filteredEquipments) { equipment ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                EquipmentListItemCard(
                    equipment = equipment,
                    rentalFilter = rentalFilter,
                    onClick = { onEquipmentClick(equipment) }
                )
            }
        }

        // Verified guarantee banner
        item {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Verified owners. Clear rates. No calls to compare.",
                    fontSize = 12.sp,
                    color = YantraTextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // YantraStore Cross-sell Banner matching Mock 1
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                    .clickable { onOpenYantraStore() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = YantraPrimaryLight,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = YantraPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Parts, materials & service",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "Keep work moving with YantraStore.",
                                fontSize = 12.sp,
                                color = YantraTextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open YantraStore",
                        tint = YantraPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EquipmentListItemCard(
    equipment: Equipment,
    rentalFilter: RentalType,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Equipment thumbnail
            if (equipment.imageRes != null) {
                Image(
                    painter = painterResource(id = equipment.imageRes),
                    contentDescription = equipment.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(92.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(YantraPrimaryLight, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PrecisionManufacturing,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipment.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = equipment.ownerName,
                    fontSize = 12.sp,
                    color = YantraTextSecondary
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = YantraAccentYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${equipment.rating} (${equipment.reviewCount}) · ${equipment.distanceKm} km",
                        fontSize = 11.sp,
                        color = YantraTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Badge (Operator included / Farm-ready / 16 m³ capacity)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE0F2FE)
                ) {
                    Text(
                        text = when (equipment.category) {
                            EquipmentCategory.CONSTRUCTION -> "Operator included"
                            EquipmentCategory.FARMING -> "Farm-ready"
                            EquipmentCategory.INFRASTRUCTURE -> equipment.bucketCapacity
                            else -> "Verified machine"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0369A1),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Rate
                val rateText = when (rentalFilter) {
                    RentalType.HOURLY -> "₹${equipment.hourlyRate} / hour"
                    RentalType.DAILY -> "₹${equipment.dailyRate ?: (equipment.hourlyRate * 8)} / day"
                    RentalType.PER_TRIP -> "₹${equipment.perTripRate ?: 3500} / trip"
                }
                Text(
                    text = rateText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraPrimary
                )
            }
        }
    }
}
