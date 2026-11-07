package com.example.ui.screens.findyantra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun FindYantraBookingsScreen(
    orders: List<Order>,
    onOrderClick: (Order) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg)
            .padding(bottom = 80.dp)
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "My Equipment Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = "Live tracked machine rentals and job history",
                    fontSize = 12.sp,
                    color = YantraTextSecondary
                )
            }
        }

        HorizontalDivider(color = YantraBorder)

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                        .clickable { onOrderClick(order) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Booking ${order.id}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when (order.status) {
                                    OrderStatus.EN_ROUTE -> Color(0xFFE0F2FE)
                                    OrderStatus.IN_PROGRESS -> Color(0xFFFEF3C7)
                                    OrderStatus.COMPLETED -> Color(0xFFDCFCE7)
                                    OrderStatus.NEEDS_ASSIGNMENT -> Color(0xFFFFEDD5)
                                }
                            ) {
                                Text(
                                    text = order.status.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (order.status) {
                                        OrderStatus.EN_ROUTE -> Color(0xFF0369A1)
                                        OrderStatus.IN_PROGRESS -> Color(0xFFB45309)
                                        OrderStatus.COMPLETED -> Color(0xFF15803D)
                                        OrderStatus.NEEDS_ASSIGNMENT -> Color(0xFFC2410C)
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = order.equipmentName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "${order.worksite} · ${order.timeSlot}",
                            fontSize = 13.sp,
                            color = YantraTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total: ₹${order.totalAmount}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraPrimary
                            )
                            Button(
                                onClick = { onOrderClick(order) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Track Live", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
