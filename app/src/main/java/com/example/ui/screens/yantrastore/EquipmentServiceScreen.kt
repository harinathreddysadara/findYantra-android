package com.example.ui.screens.yantrastore

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreItem
import com.example.ui.theme.*

@Composable
fun EquipmentServiceScreen(
    serviceItem: StoreItem,
    shoppingForEquipment: String,
    currentWorksite: String,
    onBackClick: () -> Unit,
    onScheduleService: (StoreItem, String, String, String, String, String, Int) -> Unit
) {
    val context = LocalContext.current
    val dates = listOf("FRI 06 Nov", "SAT 07 Nov", "SUN 08 Nov", "MON 09 Nov")
    var selectedDate by remember { mutableStateOf("FRI 06 Nov") }

    val times = listOf("10 AM–12 PM", "2 PM–4 PM", "4 PM–6 PM")
    var selectedTime by remember { mutableStateOf("2 PM–4 PM") }

    val baseFee = serviceItem.price
    val gstFee = (baseFee * 0.18f).toInt()
    val totalPayable = baseFee + gstFee

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
                            val equipParts = shoppingForEquipment.split("·")
                            val eqName = equipParts.firstOrNull()?.trim() ?: "JCB 3DX"
                            val regNo = equipParts.getOrNull(1)?.trim() ?: "MH 14 JX 4821"
                            onScheduleService(
                                serviceItem,
                                eqName,
                                regNo,
                                selectedDate,
                                selectedTime,
                                "$currentWorksite · 411057",
                                totalPayable
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
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Schedule service · ₹$totalPayable",
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
            // Header matching Mock 12
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
                                text = "Equipment service",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Expert maintenance at your worksite",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = {
                        Toast.makeText(context, "Shared service card", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }
            }

            // Big Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.service_mechanic_thumb_1793977200886),
                    contentDescription = serviceItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Title & Workshop Badges
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Text(
                            text = "Verified workshop",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "★ 4.8 · 126 jobs",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = YantraTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = serviceItem.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = YantraTextPrimary
                )
                Text(
                    text = "${serviceItem.sellerName} · Pune",
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹${serviceItem.price}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "+ GST",
                            fontSize = 13.sp,
                            color = YantraTextSecondary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "On-site · 2–3 hrs",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = YantraTextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // What's included checklist matching Mock 12
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What’s included",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    InclusionItem(text = "Engine & hydraulic system inspection")
                    Spacer(modifier = Modifier.height(6.dp))
                    InclusionItem(text = "Filter replacement labour & fluid checks")
                    Spacer(modifier = Modifier.height(6.dp))
                    InclusionItem(text = "Greasing, safety checks & service report")

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Parts and fluids charged separately, only after your approval. Doorstep visit included.",
                        fontSize = 11.sp,
                        color = YantraTextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // EQUIPMENT TO SERVICE
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
                        text = "EQUIPMENT TO SERVICE",
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
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = shoppingForEquipment,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Choose a service slot
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Choose a service slot",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Date Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dates.forEach { d ->
                        val isSelected = selectedDate == d
                        val parts = d.split(" ")
                        val dayName = parts.getOrNull(0) ?: ""
                        val dateNum = parts.drop(1).joinToString(" ")

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) YantraPrimaryLight else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) YantraPrimary else YantraBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedDate = d }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dayName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) YantraPrimary else YantraTextSecondary
                                )
                                Text(
                                    text = dateNum,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) YantraPrimaryDark else YantraTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Time Slot Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    times.forEach { t ->
                        val isSelected = selectedTime == t
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) YantraHeaderDark else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) YantraHeaderDark else YantraBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedTime = t }
                        ) {
                            Text(
                                text = t,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else YantraTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Smart scheduling advisory matching Mock 12
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "After your current rental ends at 01:00 PM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF15803D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SERVICE LOCATION
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
                        text = "SERVICE LOCATION",
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
                            text = "$currentWorksite · 411057",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Price Breakdown
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Service & doorstep visit", fontSize = 13.sp, color = YantraTextSecondary)
                        Text(text = "₹$baseFee", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YantraTextPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "GST (18%)", fontSize = 13.sp, color = YantraTextSecondary)
                        Text(text = "₹$gstFee", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YantraTextPrimary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = YantraBorder)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Payable on booking", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                        Text(text = "₹$totalPayable", fontSize = 18.sp, fontWeight = FontWeight.Black, color = YantraTextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Free rescheduling up to 4 hours before your slot. Your service record will be saved to YantraFleet.",
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
private fun InclusionItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF15803D),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = YantraTextPrimary
        )
    }
}
