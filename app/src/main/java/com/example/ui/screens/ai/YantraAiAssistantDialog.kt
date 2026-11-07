package com.example.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class ChatMessage(
    val sender: String, // "AI" or "You"
    val text: String,
    val time: String = "Now"
)

@Composable
fun YantraAiAssistantDialog(
    onDismiss: () -> Unit,
    onBookMachineRecommendation: () -> Unit
) {
    var queryInput by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    sender = "AI",
                    text = "Hello! I am your Yantra Machinery & Fleet AI Advisor. I can help calculate excavation machine requirements, estimate hourly rates, verify spare part fitment, or schedule preventive maintenance."
                )
            )
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = YantraAccentYellow.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Yantra AI Intelligence",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = "Machinery Estimator & Parts Fitment AI",
                        fontSize = 11.sp,
                        color = YantraTextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Quick prompt chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickChip(text = "Excavation Cost") {
                        val prompt = "Estimate 400m pipe trenching near Hinjawadi"
                        messages = messages + ChatMessage("You", prompt) + ChatMessage(
                            "AI",
                            "For 400m pipeline trenching in hard soil:\n• Recommended: JCB 3DX Backhoe (76 HP, 1m³ bucket)\n• Estimated duration: 6-8 hours\n• Approximate cost: ₹7,200 - ₹9,600 (₹1,200/hr operator included)\n• Disposal tipper: Tata Signa 2823 (2 trips = ₹7,000)"
                        )
                    }
                    QuickChip(text = "Check Filter Fit") {
                        val prompt = "Will 32/925346 fit JCB 3DX 2024?"
                        messages = messages + ChatMessage("You", prompt) + ChatMessage(
                            "AI",
                            "Yes! Part no. 32/925346 is 100% verified OEM fitment for JCB 3DX Backhoe (MH 14 JX 4821). Available at Deccan Spares for ₹1,850 with tomorrow delivery."
                        )
                    }
                }

                // Chat Messages List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { msg ->
                        val isUser = msg.sender == "You"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isUser) YantraPrimary else Color.White,
                                modifier = Modifier
                                    .widthIn(max = 260.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (isUser) YantraPrimary else YantraBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = msg.sender,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) Color.White.copy(alpha = 0.8f) else YantraPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = msg.text,
                                        fontSize = 12.sp,
                                        color = if (isUser) Color.White else YantraTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = queryInput,
                        onValueChange = { queryInput = it },
                        placeholder = { Text("Ask machine or maintenance question...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    IconButton(
                        onClick = {
                            if (queryInput.isNotBlank()) {
                                val userQ = queryInput
                                val botAns = when {
                                    userQ.contains("service", ignoreCase = true) || userQ.contains("hour", ignoreCase = true) ->
                                        "Patil Earthmovers: JCB 3DX Backhoe (MH 14 JX 4821) has 46 engine hours remaining until 500H periodic maintenance. You can schedule Deccan Equipment Care on-site service via YantraStore directly."
                                    userQ.contains("price", ignoreCase = true) || userQ.contains("rate", ignoreCase = true) ->
                                        "Standard market rates in Pune right now:\n• JCB 3DX: ₹1,200/hr (Min 4h)\n• Mahindra 575 DI: ₹850/hr\n• Tata Signa 2823: ₹1,800/hr or ₹3,500/trip\n• CAT 320: ₹2,200/hr"
                                    else ->
                                        "Analyzed worksite data: Hinjawadi Phase 1 has 12 active machines nearby with an average response time of 12 minutes."
                                }
                                messages = messages + ChatMessage("You", userQ) + ChatMessage("AI", botAns)
                                queryInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(YantraPrimary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = YantraPrimary, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun QuickChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F5F9),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = YantraPrimary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
