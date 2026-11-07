package com.example.ui.screens.yantrafleet

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Equipment
import com.example.ui.theme.*

@Composable
fun ManageEquipmentScreen(
    equipment: Equipment,
    onBackClick: () -> Unit,
    onTogglePublish: (String, Boolean) -> Unit,
    onUpdateRates: (String, Int, Int?) -> Unit,
    onScheduleServiceWithYantraStore: () -> Unit
) {
    val context = LocalContext.current
    var isPublished by remember { mutableStateOf(equipment.isPublicOnFindYantra) }
    var hourlyRateInput by remember { mutableStateOf(equipment.hourlyRate.toString()) }
    var dailyRateInput by remember { mutableStateOf((equipment.dailyRate ?: 8500).toString()) }

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
                            val h = hourlyRateInput.toIntOrNull() ?: equipment.hourlyRate
                            val d = dailyRateInput.toIntOrNull()
                            onUpdateRates(equipment.id, h, d)
                            onTogglePublish(equipment.id, isPublished)
                            Toast.makeText(context, "Equipment settings updated successfully", Toast.LENGTH_SHORT).show()
                            onBackClick()
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
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save equipment changes", fontSize = 15.sp, fontWeight = FontWeight.Bold)
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
            // Header matching Mock 7
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
                                text = "${equipment.registrationNo} · Added 12 Mar 2024",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
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
            }

            // Badges Row (En route, Documents verified, 76 HP)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE0F2FE)
                ) {
                    Text(
                        text = equipment.status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "Documents verified",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "${equipment.powerHp} HP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Publish on FindYantra Toggle Card
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Publish on FindYantra",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YantraTextPrimary
                                )
                                Text(
                                    text = if (isPublished) "Live · Visible to nearby customers" else "Paused · Not listed",
                                    fontSize = 12.sp,
                                    color = if (isPublished) Color(0xFF15803D) else YantraTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = isPublished,
                            onCheckedChange = { isPublished = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = YantraPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your rates, photos and next available slot are public. Registration documents stay private.",
                        fontSize = 11.sp,
                        color = YantraTextSecondary,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "Preview public listing",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rental Rates Inputs
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Rental rates",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = hourlyRateInput,
                        onValueChange = { hourlyRateInput = it },
                        label = { Text("HOURLY · ₹") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = dailyRateInput,
                        onValueChange = { dailyRateInput = it },
                        label = { Text("DAILY · 8 HRS · ₹") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Operator included · Fuel extra · Min. 4 hours",
                        fontSize = 11.sp,
                        color = YantraTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current assignment section matching Mock 7
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current assignment",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "FY-2048 →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AssignmentRow(label = "Operator", value = equipment.assignedDriverName ?: "Ramesh Kale")
                    Spacer(modifier = Modifier.height(6.dp))
                    AssignmentRow(label = "Worksite", value = equipment.worksiteLocation)
                    Spacer(modifier = Modifier.height(6.dp))
                    AssignmentRow(label = "06 Nov · 4 hours", value = "09:00 AM – 01:00 PM")
                    Spacer(modifier = Modifier.height(6.dp))
                    AssignmentRow(label = "Next available", value = equipment.nextAvailableSlot)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Service Reminder Card matching Mock 7
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE0F2FE),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onScheduleServiceWithYantraStore() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = YantraPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Next service in ${equipment.engineHoursToService} engine hours",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "Schedule with YantraStore →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = YantraPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AssignmentRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = YantraTextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YantraTextPrimary)
    }
}
