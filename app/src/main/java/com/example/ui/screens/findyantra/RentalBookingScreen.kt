package com.example.ui.screens.findyantra

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.R
import com.example.model.Equipment
import com.example.model.Order
import com.example.model.RentalType
import com.example.ui.theme.*

@Composable
fun RentalBookingScreen(
    equipment: Equipment,
    initialWorksite: String,
    onBackClick: () -> Unit,
    onConfirmBooking: (Equipment, RentalType, Int, String, String, String) -> Unit
) {
    var selectedRentalType by remember { mutableStateOf(RentalType.HOURLY) }
    var durationHours by remember { mutableStateOf(4) }
    var rentalDate by remember { mutableStateOf("06 Nov 2026") }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var worksite by remember { mutableStateOf(initialWorksite) }
    var isFavorite by remember { mutableStateOf(false) }

    // Pricing calculation
    val baseRate = when (selectedRentalType) {
        RentalType.HOURLY -> equipment.hourlyRate * durationHours
        RentalType.DAILY -> equipment.dailyRate ?: (equipment.hourlyRate * 8)
        RentalType.PER_TRIP -> equipment.perTripRate ?: 3500
    }
    val transportRate = 500
    val gstRate = ((baseRate + transportRate) * 0.18f).toInt()
    val totalEstimated = baseRate + transportRate + gstRate

    Scaffold(
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            onConfirmBooking(
                                equipment,
                                selectedRentalType,
                                durationHours,
                                rentalDate,
                                startTime,
                                worksite
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Confirm booking · ₹$totalEstimated",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(YantraBg)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Surface(
                color = YantraHeaderDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = equipment.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${equipment.registrationNo} · ${equipment.ownerName}",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }

            // Big Photo & Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                if (equipment.imageRes != null) {
                    Image(
                        painter = painterResource(id = equipment.imageRes),
                        contentDescription = equipment.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Status tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Available 06 Nov",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Rating pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = YantraAccentYellow,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${equipment.rating} · ${equipment.reviewCount} rentals",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            // Spec Pills Row (Power, Bucket, Operator)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpecCard(
                    title = "${equipment.powerHp} HP",
                    subtitle = "Power",
                    modifier = Modifier.weight(1f)
                )
                SpecCard(
                    title = equipment.bucketCapacity,
                    subtitle = "Bucket",
                    modifier = Modifier.weight(1f)
                )
                SpecCard(
                    title = if (equipment.operatorIncluded) "Included" else "Self-drive",
                    subtitle = "Operator",
                    modifier = Modifier.weight(1f)
                )
            }

            // Choose your rental
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Choose your rental",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RentalOptionCard(
                        title = "Hourly",
                        price = "₹${equipment.hourlyRate}",
                        isSelected = selectedRentalType == RentalType.HOURLY,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedRentalType = RentalType.HOURLY }
                    )
                    RentalOptionCard(
                        title = "Daily · 8 hrs",
                        price = if (equipment.dailyRate != null) "₹${equipment.dailyRate}" else "₹8,500",
                        isSelected = selectedRentalType == RentalType.DAILY,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedRentalType = RentalType.DAILY }
                    )
                    RentalOptionCard(
                        title = "Per trip",
                        price = if (equipment.perTripRate != null) "₹${equipment.perTripRate}" else "Not offered",
                        isSelected = selectedRentalType == RentalType.PER_TRIP,
                        isEnabled = equipment.perTripRate != null,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (equipment.perTripRate != null) {
                                selectedRentalType = RentalType.PER_TRIP
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Start Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedCard(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "RENTAL DATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = rentalDate,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                        }
                    }
                }

                OutlinedCard(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "START TIME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = startTime,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                        }
                    }
                }
            }

            // Duration selector if hourly
            if (selectedRentalType == RentalType.HOURLY) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Duration",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "Minimum 4 hours",
                                fontSize = 11.sp,
                                color = YantraTextSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FilledIconButton(
                                onClick = { if (durationHours > 4) durationHours-- },
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = YantraBorderLight),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                            }
                            Text(
                                text = "$durationHours hours",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            FilledIconButton(
                                onClick = { if (durationHours < 24) durationHours++ },
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = YantraBorderLight),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Worksite
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "WORKSITE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = worksite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cost Breakdown Card matching Mock 3
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val rateDesc = when (selectedRentalType) {
                        RentalType.HOURLY -> "Rental · ₹${equipment.hourlyRate} × $durationHours hours"
                        RentalType.DAILY -> "Rental · Daily (8 hours)"
                        RentalType.PER_TRIP -> "Rental · Per trip"
                    }

                    PriceRow(label = rateDesc, amount = "₹$baseRate")
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceRow(label = "Transport to worksite", amount = "₹$transportRate")
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceRow(label = "GST (18%)", amount = "₹$gstRate")
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = YantraBorder)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated total",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "₹$totalEstimated",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            // Policies text
            Text(
                text = "Operator included. Diesel billed at actuals. Extra hours ₹${equipment.hourlyRate}/hr. Free cancellation up to 12 hours before start.",
                fontSize = 11.sp,
                color = YantraTextSecondary,
                lineHeight = 15.sp,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SpecCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = modifier.border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = YantraTextSecondary
            )
        }
    }
}

@Composable
private fun RentalOptionCard(
    title: String,
    price: String,
    isSelected: Boolean,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) YantraPrimaryLight else Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) YantraPrimary else YantraBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = isEnabled) { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = if (isEnabled) YantraTextSecondary else YantraTextMuted,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = price,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) YantraPrimaryDark else if (isEnabled) YantraTextPrimary else YantraTextMuted
            )
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = YantraTextSecondary)
        Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YantraTextPrimary)
    }
}
