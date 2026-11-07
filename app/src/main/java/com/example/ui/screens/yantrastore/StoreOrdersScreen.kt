package com.example.ui.screens.yantrastore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.CartItem
import com.example.model.ServiceBooking
import com.example.ui.theme.*

@Composable
fun StoreOrdersScreen(
    cartItems: List<CartItem>,
    scheduledServices: List<ServiceBooking>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Store Orders & Scheduled Care",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextPrimary
            )
            Text(
                text = "Delivery status for materials and on-site maintenance bookings",
                fontSize = 12.sp,
                color = YantraTextSecondary
            )
        }

        item {
            Text(
                text = "SCHEDULED ON-SITE SERVICES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextSecondary,
                letterSpacing = 0.5.sp
            )
        }

        if (scheduledServices.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No on-site service appointments scheduled yet.",
                            fontSize = 13.sp,
                            color = YantraTextSecondary
                        )
                    }
                }
            }
        } else {
            items(scheduledServices) { svc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = svc.id,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextSecondary
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = svc.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = svc.serviceTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "${svc.equipmentName} (${svc.registrationNo})",
                            fontSize = 13.sp,
                            color = YantraPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Slot: ${svc.scheduledDate} · ${svc.scheduledSlot}",
                            fontSize = 12.sp,
                            color = YantraTextSecondary
                        )
                        Text(
                            text = "Location: ${svc.location}",
                            fontSize = 11.sp,
                            color = YantraTextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total Paid: ₹${svc.totalAmount}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MATERIAL & PART DELIVERIES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextSecondary,
                letterSpacing = 0.5.sp
            )
        }

        // Active delivery example
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ORD-88219 · Deccan Spares",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextSecondary
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = "Dispatched",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hydraulic oil filter (1 unit)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = "Delivery tomorrow by 11:00 AM to Hinjawadi Phase 1",
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Invoice: ₹1,930 (GST paid)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = YantraTextPrimary
                    )
                }
            }
        }
    }
}
