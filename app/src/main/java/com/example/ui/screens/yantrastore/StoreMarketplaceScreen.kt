package com.example.ui.screens.yantrastore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreCategory
import com.example.model.StoreItem
import com.example.ui.theme.*

@Composable
fun StoreMarketplaceScreen(
    storeItems: List<StoreItem>,
    shoppingForEquipment: String,
    currentWorksite: String,
    onWorksiteClick: () -> Unit,
    onChangeEquipmentContext: () -> Unit,
    onProductClick: (StoreItem) -> Unit,
    onServiceClick: (StoreItem) -> Unit,
    onAddToCart: (StoreItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(StoreCategory.ALL) }

    val filteredItems = remember(storeItems, searchQuery, selectedCategory) {
        storeItems.filter { item ->
            (searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true) || item.partOrSpecNo.contains(searchQuery, ignoreCase = true)) &&
            (selectedCategory == StoreCategory.ALL || item.category == selectedCategory)
        }
    }

    val productsList = filteredItems.filter { !it.isService }
    val serviceItems = storeItems.filter { it.isService }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(YantraBg),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Delivery to worksite address header
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onWorksiteClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = YantraAccentYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "DELIVER TO WORKSITE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "$currentWorksite · 411057",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change",
                        tint = YantraTextSecondary
                    )
                }
            }
            HorizontalDivider(color = YantraBorder)
        }

        // Hero Title matching Mock 10
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Every part. Every job.\nOne trusted store.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = YantraTextPrimary,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "For your machines, from site to service.",
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )
            }
        }

        // Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search parts, materials or services") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = YantraTextSecondary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        // Category Pills (All, Spare parts, Materials, Services)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(StoreCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) YantraHeaderDark else Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (isSelected) YantraHeaderDark else YantraBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else YantraTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Equipment Context Selector Card matching Mock 10
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                        Column {
                            Text(
                                text = "SHOPPING FOR",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = shoppingForEquipment,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                        }
                    }

                    Text(
                        text = "Change",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraPrimary,
                        modifier = Modifier.clickable { onChangeEquipmentContext() }
                    )
                }
            }
        }

        // "Ready for your worksite" row + "View all →"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ready for your worksite",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = "View all →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraPrimary,
                    modifier = Modifier.clickable { }
                )
            }
        }

        // Products Grid / Row matching Mock 10 (Hydraulic filter & PPC cement)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                productsList.take(2).forEach { product ->
                    ProductCard(
                        item = product,
                        modifier = Modifier.weight(1f),
                        onClick = { onProductClick(product) },
                        onAddClick = { onAddToCart(product) }
                    )
                }
            }
        }

        // "Expert care, on site" + "All services →" matching Mock 10
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expert care, on site",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
                Text(
                    text = "All services →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraPrimary,
                    modifier = Modifier.clickable {
                        serviceItems.firstOrNull()?.let { onServiceClick(it) }
                    }
                )
            }
        }

        // Workshop Service Card
        items(serviceItems) { serviceItem ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
                        .clickable { onServiceClick(serviceItem) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.service_mechanic_thumb_1793977200886),
                            contentDescription = serviceItem.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Text(
                                    text = "Verified workshop",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = serviceItem.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraTextPrimary
                            )
                            Text(
                                text = "${serviceItem.sellerName} · ★ ${serviceItem.rating}",
                                fontSize = 12.sp,
                                color = YantraTextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${serviceItem.price} + GST →",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = YantraPrimary
                            )
                        }
                    }
                }
            }
        }

        // Ecosystem sync notice matching Mock 10
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = YantraPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "One store, shared across FindYantra & YantraFleet.",
                        fontSize = 12.sp,
                        color = YantraTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    item: StoreItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image
            if (item.imageRes != null) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Badge
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFE0F2FE)
            ) {
                Text(
                    text = if (item.name.contains("filter", ignoreCase = true)) "Fits your JCB" else "Bulk rates available",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0369A1),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = YantraTextPrimary,
                maxLines = 1
            )
            Text(
                text = "${item.partOrSpecNo} · ${item.specDetails}",
                fontSize = 10.sp,
                color = YantraTextSecondary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${item.price}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = item.distanceKmText ?: "Delivery tomorrow",
                        fontSize = 9.sp,
                        color = YantraTextSecondary
                    )
                }

                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(YantraPrimaryLight, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to cart",
                        tint = YantraPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
