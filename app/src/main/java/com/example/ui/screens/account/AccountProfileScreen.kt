package com.example.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun AccountProfileScreen(
    currentRole: AppRole,
    onRoleSelected: (AppRole) -> Unit,
    onOpenDocs: () -> Unit,
    onSignOut: () -> Unit
) {
    var showWorksiteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Top profile card
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = when (currentRole) {
                        AppRole.FIND_YANTRA -> YantraPrimaryLight
                        AppRole.YANTRA_FLEET -> Color(0xFFCCFBF1)
                        AppRole.YANTRA_STORE -> Color(0xFFFEF3C7)
                    },
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (currentRole) {
                                AppRole.FIND_YANTRA -> Icons.Default.Engineering
                                AppRole.YANTRA_FLEET -> Icons.Default.Business
                                AppRole.YANTRA_STORE -> Icons.Default.Store
                            },
                            contentDescription = null,
                            tint = when (currentRole) {
                                AppRole.FIND_YANTRA -> YantraPrimary
                                AppRole.YANTRA_FLEET -> Color(0xFF0F766E)
                                AppRole.YANTRA_STORE -> Color(0xFFD97706)
                            },
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = currentRole.defaultUserName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = currentRole.defaultCompany,
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "Active: ${currentRole.title}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = YantraBorder)

        Spacer(modifier = Modifier.height(16.dp))

        // Switch to other App personas
        Text(
            text = "SWITCH APPLICATION MODE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = YantraTextSecondary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
        ) {
            Column {
                AppRole.values().forEachIndexed { index, role ->
                    val isCurrent = currentRole == role
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRoleSelected(role) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    AppRole.FIND_YANTRA -> Icons.Default.Search
                                    AppRole.YANTRA_FLEET -> Icons.Default.LocalShipping
                                    AppRole.YANTRA_STORE -> Icons.Default.Storefront
                                },
                                contentDescription = null,
                                tint = if (isCurrent) YantraPrimary else YantraTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = role.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isCurrent) YantraPrimary else YantraTextPrimary
                                )
                                Text(
                                    text = role.subtitle,
                                    fontSize = 11.sp,
                                    color = YantraTextSecondary
                                )
                            }
                        }

                        if (isCurrent) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Active",
                                tint = YantraPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Switch",
                                tint = YantraTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (index < AppRole.values().size - 1) {
                        HorizontalDivider(color = YantraBorderLight)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Backend Architecture & Deployment Docs
        Text(
            text = "TECHNICAL DOCUMENTATION",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = YantraTextSecondary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                .clickable { onOpenDocs() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = YantraPrimaryLight,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = YantraPrimaryDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Database Schema & API Specs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "View PostgreSQL tables, REST APIs & Docker guide",
                            fontSize = 12.sp,
                            color = YantraTextSecondary
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = YantraPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preferences & Sign Out
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
        ) {
            Column {
                AccountOptionRow(
                    icon = Icons.Default.Security,
                    title = "GST & E-Way Bill Verification",
                    subtitle = "Automated compliance for fleet transport",
                    onClick = {}
                )
                HorizontalDivider(color = YantraBorderLight)
                AccountOptionRow(
                    icon = Icons.Default.HelpCenter,
                    title = "24x7 Heavy Machinery Helpline",
                    subtitle = "1800-YANTRA-CARE (Toll free)",
                    onClick = {}
                )
                HorizontalDivider(color = YantraBorderLight)
                AccountOptionRow(
                    icon = Icons.Default.Logout,
                    title = "Sign out",
                    subtitle = "Switch user account or phone",
                    onClick = onSignOut
                )
            }
        }
    }
}

@Composable
private fun AccountOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = YantraPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = YantraTextSecondary)
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = YantraTextSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}
