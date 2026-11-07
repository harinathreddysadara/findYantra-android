package com.example.ui.screens.yantrastore

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
import com.example.model.StoreItem
import com.example.ui.theme.*

@Composable
fun SparePartDetailScreen(
    item: StoreItem,
    onBackClick: () -> Unit,
    onBuyNow: (StoreItem, Int) -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }

    val itemSubtotal = item.price * quantity
    val deliveryFee = 80
    val totalPayable = itemSubtotal + deliveryFee

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
                        onClick = { onBuyNow(item, quantity) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
                    ) {
                        Text(
                            text = "Buy now · ₹$totalPayable",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
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
            // Header matching Mock 11
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
                                text = "Spare parts",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Filters & engine care",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = {
                        Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
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
                    .height(240.dp)
                    .background(Color.White)
            ) {
                if (item.imageRes != null) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }

            // Product Title & Specs
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
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = "In stock",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "★ ${item.rating} · ${item.reviewCount} reviews",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = YantraTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = YantraTextPrimary
                )
                Text(
                    text = "${item.partOrSpecNo} · ${item.specDetails}",
                    fontSize = 13.sp,
                    color = YantraTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "₹${item.price}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = YantraTextPrimary
                    )
                    Text(
                        text = "Inclusive of GST",
                        fontSize = 12.sp,
                        color = YantraTextSecondary,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Machine Fitment Badge matching Mock 11
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE0F2FE),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF0369A1),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Fits your JCB 3DX Backhoe",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                        Text(
                            text = "MH 14 JX 4821 · Fitment verified",
                            fontSize = 11.sp,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Verified Seller Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = item.sellerName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YantraTextPrimary
                                )
                                Text(
                                    text = "Verified seller · GST invoice included",
                                    fontSize = 11.sp,
                                    color = YantraTextSecondary
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = YantraPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = YantraBorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

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
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = YantraPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = item.deliveryTimeText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YantraTextPrimary
                                )
                                Text(
                                    text = "Hinjawadi Phase 1 · 411057",
                                    fontSize = 11.sp,
                                    color = YantraTextSecondary
                                )
                            }
                        }
                        Text(
                            text = "Change",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraPrimary,
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Built for reliable operation description
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Built for reliable operation",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = YantraTextSecondary,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Replay, contentDescription = null, tint = YantraPrimary, modifier = Modifier.size(14.dp))
                            Text("7-day unused returns", fontSize = 11.sp, color = YantraTextSecondary)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = YantraPrimary, modifier = Modifier.size(14.dp))
                            Text("Secure payment", fontSize = 11.sp, color = YantraTextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity selector
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, YantraBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = YantraTextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilledIconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = YantraBorderLight),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                        }
                        Text(
                            text = "$quantity",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        FilledIconButton(
                            onClick = { if (quantity < 10) quantity++ },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = YantraBorderLight),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = YantraTextPrimary)
                        }
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
                    PriceBreakdownRow(label = "Subtotal · $quantity item${if (quantity > 1) "s" else ""}", amount = "₹$itemSubtotal")
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceBreakdownRow(label = "Delivery", amount = "₹$deliveryFee")
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = YantraBorder)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total payable",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = "₹$totalPayable",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = YantraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PriceBreakdownRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = YantraTextSecondary)
        Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YantraTextPrimary)
    }
}
