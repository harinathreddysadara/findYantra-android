package com.example.ui.screens.yantrafleet

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderSource
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun FleetOrdersScreen(
    orders: List<Order>,
    onAssignOperator: (String, String) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    val context = LocalContext.current
    var selectedFilterTab by remember { mutableStateOf("today") }
    var showDirectOrderDialog by remember { mutableStateOf(false) }

    val needsAssignmentOrders = orders.filter { it.status == OrderStatus.NEEDS_ASSIGNMENT }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
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
                            text = "Orders & assignments",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "06 Nov 2026 · ${orders.size} orders today",
                            fontSize = 13.sp,
                            color = YantraTextSecondary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = YantraTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            HorizontalDivider(color = YantraBorder)
        }

        // Filter chips (Today 8, Upcoming 5, Completed)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OrderFilterTab(
                        label = "Today ${orders.size}",
                        isSelected = selectedFilterTab == "today",
                        onClick = { selectedFilterTab = "today" }
                    )
                }
                item {
                    OrderFilterTab(
                        label = "Upcoming 5",
                        isSelected = selectedFilterTab == "upcoming",
                        onClick = { selectedFilterTab = "upcoming" }
                    )
                }
                item {
                    OrderFilterTab(
                        label = "Completed",
                        isSelected = selectedFilterTab == "completed",
                        onClick = { selectedFilterTab = "completed" }
                    )
                }
            }
        }

        // Needs Operator Alert Banner matching Mock 9
        if (needsAssignmentOrders.isNotEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "${needsAssignmentOrders.size} orders need an operator",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Assign now to keep your jobs on schedule.",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }
            }
        }

        // Orders list matching Mock 9
        items(orders) { order ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                FleetOrderCard(
                    order = order,
                    onAssignClick = {
                        onAssignOperator(order.id, "op-2") // Assign Sunil Jadhav
                        Toast.makeText(context, "Operator Sunil Jadhav assigned to ${order.id}", Toast.LENGTH_SHORT).show()
                    },
                    onOrderClick = { onOrderClick(order) },
                    onCallOperator = {
                        Toast.makeText(context, "Calling ${order.assignedOperatorName}...", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Footer info & + Add direct order CTA
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All times in IST",
                    fontSize = 11.sp,
                    color = YantraTextSecondary
                )
                TextButton(onClick = { showDirectOrderDialog = true }) {
                    Text(
                        text = "+ Add direct order",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraPrimary
                    )
                }
            }
        }
    }

    if (showDirectOrderDialog) {
        AlertDialog(
            onDismissRequest = { showDirectOrderDialog = false },
            title = { Text("Direct Contract Order", fontWeight = FontWeight.Bold) },
            text = {
                Text("Direct offline bookings bypass customer platform fees and automatically sync operator schedules.")
            },
            confirmButton = {
                Button(
                    onClick = { showDirectOrderDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
                ) {
                    Text("Got it")
                }
            }
        )
    }
}

@Composable
private fun FleetOrderCard(
    order: Order,
    onAssignClick: () -> Unit,
    onOrderClick: () -> Unit,
    onCallOperator: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            .clickable { onOrderClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Source & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.id} · ${order.source.label}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextSecondary,
                    letterSpacing = 0.5.sp
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (order.status) {
                        OrderStatus.NEEDS_ASSIGNMENT -> Color(0xFFFEF3C7)
                        OrderStatus.EN_ROUTE -> Color(0xFFE0F2FE)
                        OrderStatus.IN_PROGRESS -> Color(0xFFDCFCE7)
                        OrderStatus.COMPLETED -> Color(0xFFF1F5F9)
                    }
                ) {
                    Text(
                        text = order.status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.status) {
                            OrderStatus.NEEDS_ASSIGNMENT -> Color(0xFFB45309)
                            OrderStatus.EN_ROUTE -> Color(0xFF0369A1)
                            OrderStatus.IN_PROGRESS -> Color(0xFF15803D)
                            OrderStatus.COMPLETED -> YantraTextSecondary
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vehicle Title
            Text(
                text = order.equipmentName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextPrimary
            )
            Text(
                text = "${order.customerName} · ${order.worksite}",
                fontSize = 13.sp,
                color = YantraTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Time & Amount Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = order.timeSlot,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = YantraTextPrimary
                    )
                }

                Text(
                    text = "₹${order.totalAmount}${if (order.source == OrderSource.FINDYANTRA) " incl. GST" else ""}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = YantraBorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            // Needs assignment state vs Assigned state
            if (order.status == OrderStatus.NEEDS_ASSIGNMENT) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "Sunil Jadhav is available",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "Heavy vehicle licence · Wakad",
                            fontSize = 11.sp,
                            color = YantraTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onAssignClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Assign Sunil Jadhav", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = order.assignedOperatorName ?: "Assigned Operator",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "${order.registrationNo} · Arriving in ${order.etaMinutes} min",
                                fontSize = 11.sp,
                                color = YantraTextSecondary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(onClick = onCallOperator, modifier = Modifier.size(34.dp)) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call",
                                tint = YantraPrimary,
                                modifier = Modifier.size(18.dp)
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
    }
}

@Composable
private fun OrderFilterTab(
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
