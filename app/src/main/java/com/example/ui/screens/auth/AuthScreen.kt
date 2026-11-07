package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppRole
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    initialRole: AppRole = AppRole.FIND_YANTRA,
    onLoginSuccess: (AppRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf(initialRole) }
    var phoneInput by remember { mutableStateOf("98220 44123") }
    var otpInput by remember { mutableStateOf("4821") }
    var worksiteOrFleetName by remember {
        mutableStateOf(
            when (initialRole) {
                AppRole.FIND_YANTRA -> "Hinjawadi Phase 1, Pune"
                AppRole.YANTRA_FLEET -> "Patil Earthmovers"
                AppRole.YANTRA_STORE -> "Deccan Equipment Care & Spares"
            }
        )
    }

    LaunchedEffect(selectedRole) {
        worksiteOrFleetName = when (selectedRole) {
            AppRole.FIND_YANTRA -> "Hinjawadi Phase 1, Pune"
            AppRole.YANTRA_FLEET -> "Patil Earthmovers"
            AppRole.YANTRA_STORE -> "Deccan Equipment Care & Spares"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header Logo
            Surface(
                shape = CircleShape,
                color = YantraHeaderDark,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PrecisionManufacturing,
                        contentDescription = "Yantra Ecosystem",
                        tint = YantraAccentYellow,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = YantraHeaderDark, fontWeight = FontWeight.Black)) {
                        append("yantra")
                    }
                    withStyle(SpanStyle(color = YantraPrimary, fontWeight = FontWeight.Bold)) {
                        append(" heavy")
                    }
                },
                fontSize = 28.sp,
                letterSpacing = (-0.5).sp
            )

            Text(
                text = "Unified Farm, Infra & Heavy Equipment Ecosystem",
                fontSize = 13.sp,
                color = YantraTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role / App Selector Tabs
            Text(
                text = "SELECT APPLICATION LOGIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppRole.values().forEach { role ->
                    val isSelected = selectedRole == role
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color.White else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedRole = role }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = role.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) YantraPrimary else YantraTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Role Description Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, YantraBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (selectedRole) {
                                AppRole.FIND_YANTRA -> Color(0xFFE0F2FE)
                                AppRole.YANTRA_FLEET -> Color(0xFFCCFBF1)
                                AppRole.YANTRA_STORE -> Color(0xFFFEF3C7)
                            },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (selectedRole) {
                                        AppRole.FIND_YANTRA -> Icons.Default.Search
                                        AppRole.YANTRA_FLEET -> Icons.Default.LocalShipping
                                        AppRole.YANTRA_STORE -> Icons.Default.Storefront
                                    },
                                    contentDescription = null,
                                    tint = when (selectedRole) {
                                        AppRole.FIND_YANTRA -> YantraPrimary
                                        AppRole.YANTRA_FLEET -> Color(0xFF0F766E)
                                        AppRole.YANTRA_STORE -> Color(0xFFD97706)
                                    }
                                )
                            }
                        }

                        Column {
                            Text(
                                text = when (selectedRole) {
                                    AppRole.FIND_YANTRA -> "Hirer & Contractor Portal"
                                    AppRole.YANTRA_FLEET -> "Fleet Operator & Owner"
                                    AppRole.YANTRA_STORE -> "Vendor & Workshop Portal"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = when (selectedRole) {
                                    AppRole.FIND_YANTRA -> "Book JCB, tractors & tippers near worksite"
                                    AppRole.YANTRA_FLEET -> "Manage 12 machines, drivers, orders & rates"
                                    AppRole.YANTRA_STORE -> "Sell spare parts, cement & on-site service"
                                },
                                fontSize = 12.sp,
                                color = YantraTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Phone Number Field
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Mobile Phone Number") },
                        leadingIcon = {
                            Text(
                                "+91",
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Context / Worksite / Fleet name
                    OutlinedTextField(
                        value = worksiteOrFleetName,
                        onValueChange = { worksiteOrFleetName = it },
                        label = {
                            Text(
                                when (selectedRole) {
                                    AppRole.FIND_YANTRA -> "Current Worksite Location"
                                    AppRole.YANTRA_FLEET -> "Fleet / Agency Name"
                                    AppRole.YANTRA_STORE -> "Store / Workshop Name"
                                }
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = when (selectedRole) {
                                    AppRole.FIND_YANTRA -> Icons.Default.Place
                                    AppRole.YANTRA_FLEET -> Icons.Default.Business
                                    AppRole.YANTRA_STORE -> Icons.Default.Store
                                },
                                contentDescription = null,
                                tint = YantraPrimary
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // OTP / Passcode field
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it },
                        label = { Text("Verification OTP / Security PIN") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = YantraPrimary
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Primary Sign In Button
                    Button(
                        onClick = { onLoginSuccess(selectedRole) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (selectedRole) {
                                AppRole.FIND_YANTRA -> YantraPrimary
                                AppRole.YANTRA_FLEET -> Color(0xFF0F766E)
                                AppRole.YANTRA_STORE -> Color(0xFFD97706)
                            }
                        )
                    ) {
                        Text(
                            text = "Sign in to ${selectedRole.title}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Demo bypass button
                    OutlinedButton(
                        onClick = { onLoginSuccess(selectedRole) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = YantraAccentYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Quick Demo Access (${selectedRole.defaultUserName})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Protected by Yantra Industrial Security & GST E-way verification",
                fontSize = 11.sp,
                color = YantraTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
