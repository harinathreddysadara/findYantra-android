package com.example.ui.screens.findyantra

import android.widget.Toast
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
import com.example.model.Order
import com.example.ui.components.InteractiveMapCard
import com.example.ui.theme.*

@Composable
fun TrackBookingScreen(
    order: Order,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showMessageDialog by remember { mutableStateOf(false) }
    var chatMessage by remember { mutableStateOf("") }
    var chatHistory by remember {
        mutableStateOf(
            listOf(
                "Driver: Namaste! I am on NH 48, approaching Hinjawadi flyover now.",
                "You: Great, please enter from Gate 2 near the cement batching plant."
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Dark header matching Mock 4
        Surface(
            color = YantraHeaderDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        Toast.makeText(context, "Support hotline: 1800-YANTRA-CARE", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Help",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your machine is on its\nway",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Booking ${order.id} · ${order.dateStr}",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // En route + ETA Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF0369A1)
                    ) {
                        Text(
                            text = "En route",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "${order.etaMinutes} min away",
                        color = Color(0xFF38BDF8),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Live Route Map Canvas
        Box(modifier = Modifier.padding(16.dp)) {
            InteractiveMapCard(isTrackingMode = true)
        }

        // Progress Step Bar matching Mock 4: Confirmed -> On the way -> At worksite
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Step 1: Confirmed
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Confirmed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraPrimary
                    )
                }

                // Step 2: On the way
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "On the way",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraPrimary
                    )
                }

                // Step 3: At worksite
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = YantraTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "At worksite",
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // YOUR OPERATOR Card
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
                    text = "YOUR OPERATOR",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextSecondary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = YantraPrimaryLight,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "RK",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = YantraPrimaryDark
                                )
                            }
                        }

                        Column {
                            Text(
                                text = order.assignedOperatorName ?: "Ramesh Kale",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
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
                                    text = "4.9 · 8 years experience",
                                    fontSize = 12.sp,
                                    color = YantraTextSecondary
                                )
                            }
                        }
                    }

                    // Call Operator button
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Calling operator ${order.assignedOperatorName}...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .background(YantraPrimaryLight, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call operator",
                            tint = YantraPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Machine details card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jcb_backhoe_thumb_1793977099159),
                    contentDescription = order.equipmentName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column {
                    Text(
                        text = order.equipmentName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = "${order.registrationNo} · Patil Earthmovers",
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Verified owner & operator",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF15803D)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Booking Schedule & Total Details
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailRow(label = "Rental window", value = order.timeSlot)
                Spacer(modifier = Modifier.height(10.dp))
                DetailRow(label = "Worksite", value = order.worksite)
                Spacer(modifier = Modifier.height(10.dp))
                DetailRow(label = "Estimated total · ${order.durationText}", value = "₹${order.totalAmount}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Message Operator button matching Mock 4
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedButton(
                onClick = { showMessageDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = YantraPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Message operator",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Security PIN notice matching Mock 4
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = YantraPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "Share the start PIN only once equipment arrives.",
                        fontSize = 11.sp,
                        color = YantraTextSecondary
                    )
                    Text(
                        text = "START PIN: ${order.startPin}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                }
            }
        }
    }

    // Operator chat dialog
    if (showMessageDialog) {
        AlertDialog(
            onDismissRequest = { showMessageDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        tint = YantraPrimary
                    )
                    Text(
                        text = "Chat with ${order.assignedOperatorName ?: "Operator"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chatHistory.forEach { msg ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (msg.startsWith("You:")) YantraPrimaryLight else Color(0xFFF1F5F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = msg,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(8.dp),
                                color = YantraTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = chatMessage,
                        onValueChange = { chatMessage = it },
                        placeholder = { Text("Type worksite directions...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (chatMessage.isNotBlank()) {
                            chatHistory = chatHistory + "You: $chatMessage"
                            chatMessage = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMessageDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = YantraTextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
    }
}
