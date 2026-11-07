package com.example.ui.screens.yantrafleet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Equipment
import com.example.model.EquipmentCategory
import com.example.model.EquipmentStatus
import com.example.ui.theme.*

@Composable
fun FleetInventoryScreen(
    equipments: List<Equipment>,
    onEquipmentClick: (Equipment) -> Unit,
    onAddEquipment: (String, String, EquipmentCategory, Int, Int, Int?) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<EquipmentStatus?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredList = remember(equipments, searchQuery, selectedFilter) {
        equipments.filter { eq ->
            (searchQuery.isEmpty() || eq.name.contains(searchQuery, ignoreCase = true) || eq.registrationNo.contains(searchQuery, ignoreCase = true)) &&
            (selectedFilter == null || eq.status == selectedFilter)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = YantraPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add equipment", fontWeight = FontWeight.Bold) },
                modifier = Modifier.padding(bottom = 60.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(YantraBg)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your fleet",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = YantraTextPrimary
                                )
                                Text(
                                    text = "${equipments.size} machines · ${equipments.count { it.isPublicOnFindYantra }} publicly listed",
                                    fontSize = 13.sp,
                                    color = YantraTextSecondary
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filter",
                                    tint = YantraTextPrimary
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(color = YantraBorder)
            }

            // Search Bar
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search machine or registration") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = YantraTextSecondary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // Filter Chips (All 12, Available 3, Working 7, Service 2)
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterPill(
                            label = "All ${equipments.size}",
                            isSelected = selectedFilter == null,
                            onClick = { selectedFilter = null }
                        )
                    }
                    item {
                        FilterPill(
                            label = "Available ${equipments.count { it.status == EquipmentStatus.AVAILABLE }}",
                            isSelected = selectedFilter == EquipmentStatus.AVAILABLE,
                            onClick = { selectedFilter = EquipmentStatus.AVAILABLE }
                        )
                    }
                    item {
                        FilterPill(
                            label = "Working ${equipments.count { it.status == EquipmentStatus.WORKING || it.status == EquipmentStatus.EN_ROUTE }}",
                            isSelected = selectedFilter == EquipmentStatus.WORKING || selectedFilter == EquipmentStatus.EN_ROUTE,
                            onClick = { selectedFilter = EquipmentStatus.WORKING }
                        )
                    }
                    item {
                        FilterPill(
                            label = "Service ${equipments.count { it.status == EquipmentStatus.IN_SERVICE }}",
                            isSelected = selectedFilter == EquipmentStatus.IN_SERVICE,
                            onClick = { selectedFilter = EquipmentStatus.IN_SERVICE }
                        )
                    }
                }
            }

            // Subtitle status row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${filteredList.size} of ${equipments.size} machines",
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )
                    Text(
                        text = "Recently active ⌄",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = YantraPrimary
                    )
                }
            }

            // List of equipment
            items(filteredList) { equipment ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    FleetEquipmentCard(
                        equipment = equipment,
                        onClick = { onEquipmentClick(equipment) }
                    )
                }
            }

            // Informational tip
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Publish a machine to receive rental orders on FindYantra. You stay in control of availability.",
                        fontSize = 12.sp,
                        color = YantraTextSecondary,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }

    // Add Equipment Dialog
    if (showAddDialog) {
        AddEquipmentDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, reg, cat, hp, hr, dr ->
                onAddEquipment(name, reg, cat, hp, hr, dr)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun FleetEquipmentCard(
    equipment: Equipment,
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
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Thumbnail
                if (equipment.imageRes != null) {
                    Image(
                        painter = painterResource(id = equipment.imageRes),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .background(YantraPrimaryLight, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = equipment.registrationNo,
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (equipment.status) {
                                EquipmentStatus.AVAILABLE -> Color(0xFFDCFCE7)
                                EquipmentStatus.EN_ROUTE -> Color(0xFFE0F2FE)
                                EquipmentStatus.WORKING -> Color(0xFFE0F2FE)
                                EquipmentStatus.IN_SERVICE -> Color(0xFFFEF3C7)
                            }
                        ) {
                            Text(
                                text = equipment.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (equipment.status) {
                                    EquipmentStatus.AVAILABLE -> Color(0xFF15803D)
                                    EquipmentStatus.EN_ROUTE -> Color(0xFF0369A1)
                                    EquipmentStatus.WORKING -> Color(0xFF0369A1)
                                    EquipmentStatus.IN_SERVICE -> Color(0xFFB45309)
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        val rateText = if (equipment.perTripRate != null) "₹${equipment.perTripRate}/trip" else "₹${equipment.hourlyRate}/hr"
                        Text(
                            text = rateText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Operator or next order
                    val subInfo = when {
                        equipment.assignedDriverName != null -> "${equipment.assignedDriverName} · ${equipment.currentOrderNo ?: ""}"
                        equipment.status == EquipmentStatus.IN_SERVICE -> "Hydraulic service · Due today"
                        else -> equipment.nextAvailableSlot
                    }
                    Text(
                        text = subInfo,
                        fontSize = 11.sp,
                        color = YantraTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = YantraBorderLight)
            Spacer(modifier = Modifier.height(8.dp))

            // Public visibility link row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (equipment.isPublicOnFindYantra) Icons.Default.Public else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = if (equipment.isPublicOnFindYantra) YantraPrimary else YantraTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (equipment.isPublicOnFindYantra) "Public on FindYantra" else "Hidden from FindYantra",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (equipment.isPublicOnFindYantra) YantraPrimary else YantraTextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = YantraTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) YantraHeaderDark else Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, if (isSelected) YantraHeaderDark else YantraBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else YantraTextSecondary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun AddEquipmentDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, EquipmentCategory, Int, Int, Int?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regNo by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(EquipmentCategory.CONSTRUCTION) }
    var powerHp by remember { mutableStateOf("76") }
    var hourlyRate by remember { mutableStateOf("1200") }
    var dailyRate by remember { mutableStateOf("8500") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Fleet Equipment", fontWeight = FontWeight.Bold, color = YantraTextPrimary)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Machine Model (e.g. JCB 3DX Backhoe)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = regNo,
                    onValueChange = { regNo = it },
                    label = { Text("Registration (e.g. MH 14 JX 4821)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = powerHp,
                    onValueChange = { powerHp = it },
                    label = { Text("Engine Power (HP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hourlyRate,
                    onValueChange = { hourlyRate = it },
                    label = { Text("Hourly Rate (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dailyRate,
                    onValueChange = { dailyRate = it },
                    label = { Text("Daily Rate (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && regNo.isNotBlank()) {
                        onAdd(
                            name,
                            regNo,
                            category,
                            powerHp.toIntOrNull() ?: 76,
                            hourlyRate.toIntOrNull() ?: 1200,
                            dailyRate.toIntOrNull()
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
            ) {
                Text("Add to Fleet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
