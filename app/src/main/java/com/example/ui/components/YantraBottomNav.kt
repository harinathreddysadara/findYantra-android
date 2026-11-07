package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppRole
import com.example.ui.theme.YantraBorder
import com.example.ui.theme.YantraPrimary
import com.example.ui.theme.YantraTextPrimary
import com.example.ui.theme.YantraTextSecondary

data class NavItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun YantraBottomNav(
    currentRole: AppRole,
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = when (currentRole) {
        AppRole.FIND_YANTRA -> listOf(
            NavItem("explore", "Explore", Icons.Default.Explore),
            NavItem("bookings", "Bookings", Icons.Default.ReceiptLong),
            NavItem("yantrastore", "YantraStore", Icons.Default.Storefront),
            NavItem("account", "Account", Icons.Default.Person)
        )
        AppRole.YANTRA_FLEET -> listOf(
            NavItem("overview", "Overview", Icons.Default.Dashboard),
            NavItem("fleet", "Fleet", Icons.Default.LocalShipping),
            NavItem("orders", "Orders", Icons.Default.Assignment),
            NavItem("team", "Team", Icons.Default.People),
            NavItem("yantrastore", "YantraStore", Icons.Default.Storefront)
        )
        AppRole.YANTRA_STORE -> listOf(
            NavItem("browse", "Browse", Icons.Default.Storefront),
            NavItem("my_orders", "My orders", Icons.Default.Inventory2),
            NavItem("services", "Services", Icons.Default.Build),
            NavItem("account", "Account", Icons.Default.Person)
        )
    }

    NavigationBar(
        containerColor = Color.White,
        contentColor = YantraTextPrimary,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        items.forEach { item ->
            val isSelected = currentTab == item.id
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.id) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) YantraPrimary else YantraTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) YantraPrimary else YantraTextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = YantraPrimary.copy(alpha = 0.12f),
                    selectedIconColor = YantraPrimary,
                    unselectedIconColor = YantraTextSecondary,
                    selectedTextColor = YantraPrimary,
                    unselectedTextColor = YantraTextSecondary
                )
            )
        }
    }
}
