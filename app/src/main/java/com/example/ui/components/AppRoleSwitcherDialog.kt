package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppRole
import com.example.ui.theme.*

@Composable
fun AppRoleSwitcherDialog(
    currentRole: AppRole,
    onRoleSelected: (AppRole) -> Unit,
    onOpenDocs: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = YantraPrimary
                )
                Text(
                    text = "Switch Application",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Experience all 3 interconnected apps in the Yantra ecosystem:",
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )

                // 1. FindYantra
                AppChoiceCard(
                    title = "FindYantra",
                    subtitle = "Discover & book machines around worksite with live operator tracking",
                    icon = Icons.Default.Search,
                    isSelected = currentRole == AppRole.FIND_YANTRA,
                    badge = "Hirer / Renter App",
                    accentColor = Color(0xFF0284C7),
                    onClick = {
                        onRoleSelected(AppRole.FIND_YANTRA)
                        onDismiss()
                    }
                )

                // 2. YantraFleet
                AppChoiceCard(
                    title = "YantraFleet",
                    subtitle = "Manage fleet machines, operators, dispatch orders & earnings",
                    icon = Icons.Default.LocalShipping,
                    isSelected = currentRole == AppRole.YANTRA_FLEET,
                    badge = "Fleet Owner App",
                    accentColor = Color(0xFF0F766E),
                    onClick = {
                        onRoleSelected(AppRole.YANTRA_FLEET)
                        onDismiss()
                    }
                )

                // 3. YantraStore
                AppChoiceCard(
                    title = "YantraStore",
                    subtitle = "Buy machine spares, cement/gravel, and schedule on-site service",
                    icon = Icons.Default.Storefront,
                    isSelected = currentRole == AppRole.YANTRA_STORE,
                    badge = "Spares & Maintenance",
                    accentColor = Color(0xFFD97706),
                    onClick = {
                        onRoleSelected(AppRole.YANTRA_STORE)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Docs button
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = YantraPrimaryLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDismiss()
                            onOpenDocs()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = YantraPrimaryDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Backend Schema & API Docs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraPrimaryDark
                            )
                            Text(
                                text = "Database schema, REST/WebSocket APIs & deployment guide",
                                fontSize = 11.sp,
                                color = YantraPrimaryDark.copy(alpha = 0.8f)
                            )
                        }
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
private fun AppChoiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.08f) else Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else YantraBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = accentColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = YantraTextSecondary,
                    lineHeight = 16.sp
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
