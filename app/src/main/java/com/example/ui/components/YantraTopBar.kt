package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppRole
import com.example.ui.theme.YantraAccentYellow
import com.example.ui.theme.YantraHeaderDark
import com.example.ui.theme.YantraPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YantraTopBar(
    currentRole: AppRole,
    title: String? = null,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onAppSwitchClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    cartItemCount: Int = 0,
    onCartClick: (() -> Unit)? = null
) {
    Surface(
        color = YantraHeaderDark,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left section: Back button or App Branding
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (showBackButton && onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back",
                                tint = Color.White
                            )
                        }
                    }

                    // Brand logo styling matching the mocks
                    when (currentRole) {
                        AppRole.FIND_YANTRA -> {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                        append("find")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF38BDF8), fontWeight = FontWeight.ExtraBold)) {
                                        append("yantra")
                                    }
                                },
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                        }
                        AppRole.YANTRA_FLEET -> {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                        append("yantra")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF38BDF8), fontWeight = FontWeight.ExtraBold)) {
                                        append("fleet")
                                    }
                                },
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                        }
                        AppRole.YANTRA_STORE -> {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                        append("yantra")
                                    }
                                    withStyle(SpanStyle(color = YantraAccentYellow, fontWeight = FontWeight.ExtraBold)) {
                                        append("store")
                                    }
                                },
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }
                }

                // Right actions: App Switcher pill + AI Sparkle + Notification/Cart
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Quick Switcher Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF1F2937),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFF374151), RoundedCornerShape(20.dp))
                            .clickable { onAppSwitchClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Apps",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Apps",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Fleet Owner Tenant Badge in YantraFleet mode
                    if (currentRole == AppRole.YANTRA_FLEET) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "PATIL EARTHMOVERS",
                                color = Color(0xFFE2E8F0),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // AI Advisor button
                    IconButton(
                        onClick = onAiAssistantClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Heavy Machinery Advisor",
                            tint = YantraAccentYellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Cart or Notification
                    if (currentRole == AppRole.YANTRA_STORE && onCartClick != null) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(containerColor = YantraAccentYellow, contentColor = Color.Black) {
                                        Text("$cartItemCount", fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            IconButton(
                                onClick = onCartClick,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Store Cart",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // Optional subtitle/title in custom header
            if (title != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
