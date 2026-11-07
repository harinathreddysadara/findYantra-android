package com.example.ui.screens.yantrafleet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Equipment
import com.example.model.Order
import com.example.ui.theme.*

@Composable
fun FleetOverviewScreen(
    equipments: List<Equipment>,
    orders: List<Order>,
    onNavigateToOrders: () -> Unit,
    onNavigateToFleet: () -> Unit,
    onNavigateToFindYantra: () -> Unit
) {
    val totalMachines = equipments.size.coerceAtLeast(12)
    val workingCount = equipments.count { it.status == com.example.model.EquipmentStatus.WORKING || it.status == com.example.model.EquipmentStatus.EN_ROUTE }.coerceAtLeast(7)
    val availableCount = equipments.count { it.status == com.example.model.EquipmentStatus.AVAILABLE }.coerceAtLeast(3)
    val inServiceCount = equipments.count { it.status == com.example.model.EquipmentStatus.IN_SERVICE }.coerceAtLeast(2)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Welcome Header matching Mock 5
        item {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ready for the day, Ajay.",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Friday, 06 Nov 2026 · Pune",
                            fontSize = 13.sp,
                            color = YantraTextSecondary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = YantraTextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            HorizontalDivider(color = YantraBorder)
        }

        // Two KPI Cards: Fleet Utilisation & Today's Orders
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Utilisation
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "FLEET UTILISATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "78%",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "↑ 6% vs last month",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF15803D)
                        )
                    }
                }

                // Orders
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                        .clickable { onNavigateToOrders() }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "TODAY'S ORDERS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "08",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "2 need assignment",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2410C)
                        )
                    }
                }
            }
        }

        // REVENUE · NOVEMBER Card with Weekly Bar Chart matching Mock 5
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "REVENUE · NOVEMBER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "This month ⌄",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = YantraTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "₹2,84,500",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "↑ 18.4%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Weekly Bar Chart (Sat, Sun, Mon, Tue, Wed, Thu, Fri)
                    val days = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
                    val heights = listOf(0.35f, 0.45f, 0.50f, 0.70f, 0.55f, 0.80f, 1.0f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { index, day ->
                            val heightFraction = heights[index]
                            val isToday = index == 6 // Fri
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(if (isToday) Color(0xFF38BDF8) else Color(0xFF475569))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isToday) YantraTextPrimary else YantraTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = YantraBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹42,800 pending payout",
                            fontSize = 12.sp,
                            color = YantraTextSecondary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable { }
                        ) {
                            Text(
                                text = "View report",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraPrimary
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Fleet at a glance
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fleet at a glance",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = "$totalMachines machines →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraPrimary,
                    modifier = Modifier.clickable { onNavigateToFleet() }
                )
            }
        }

        // Fleet Status Summary 3-pill row: 07 Working, 03 Available, 02 In service
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusPill(number = "07", label = "Working", color = Color(0xFF0284C7), modifier = Modifier.weight(1f))
                StatusPill(number = "03", label = "Available", color = Color(0xFF15803D), modifier = Modifier.weight(1f))
                StatusPill(number = "02", label = "In service", color = Color(0xFFB45309), modifier = Modifier.weight(1f))
            }
        }

        // Needs your attention
        item {
            Text(
                text = "Needs your attention",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextPrimary,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 10.dp)
            )
        }

        // Alert item 1: 2 orders need operators
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                    .clickable { onNavigateToOrders() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFEDD5),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = Color(0xFFC2410C),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "2 orders need operators",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "Next: Tata Signa · 10:00 AM",
                                fontSize = 12.sp,
                                color = YantraTextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Assign",
                        tint = YantraTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Alert item 2: JCB 3DX FY-2048 En route
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "JCB 3DX · FY-2048",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "Ramesh Kale → Hinjawadi Phase 1",
                            fontSize = 12.sp,
                            color = YantraTextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Text(
                            text = "En route",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Alert item 3: 9 machines live on FindYantra
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToFindYantra() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "9 machines live on FindYantra",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraPrimary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.NorthEast,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusPill(
    number: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = modifier.border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = YantraTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
