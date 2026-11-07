package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun InteractiveMapCard(
    modifier: Modifier = Modifier,
    isTrackingMode: Boolean = false,
    selectedPricePin: String? = null,
    onPinClick: ((String) -> Unit)? = null
) {
    // Pulse animation for live vehicle marker
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isTrackingMode) 220.dp else 190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE5EFE5)) // Map land background
            .border(1.dp, YantraBorder, RoundedCornerShape(16.dp))
    ) {
        // Canvas roads and terrain
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Secondary roads
            val secondaryRoadColor = Color.White
            drawLine(
                color = secondaryRoadColor,
                start = Offset(0f, h * 0.78f),
                end = Offset(w, h * 0.74f),
                strokeWidth = 14f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.15f, 0f),
                end = Offset(w * 0.35f, h),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.25f, h * 0.35f),
                end = Offset(w, h * 0.42f),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )

            // Primary Highway (NH 48) - Yellow with orange casing
            val nh48Path = Path().apply {
                moveTo(w * 0.58f, 0f)
                cubicTo(
                    w * 0.56f, h * 0.3f,
                    w * 0.52f, h * 0.7f,
                    w * 0.50f, h
                )
            }
            drawPath(
                path = nh48Path,
                color = Color(0xFFF39C12),
                style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawPath(
                path = nh48Path,
                color = Color(0xFFFEEAA2),
                style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Route polyline for tracking mode
            if (isTrackingMode) {
                val routePath = Path().apply {
                    moveTo(w * 0.38f, h * 0.72f) // vehicle pos
                    lineTo(w * 0.42f, h * 0.48f)
                    lineTo(w * 0.56f, h * 0.48f)
                    lineTo(w * 0.72f, h * 0.56f) // worksite pos
                }
                drawPath(
                    path = routePath,
                    color = Color(0xFF0284C7),
                    style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            // Pulse ring around live vehicle
            val vehicleCenter = if (isTrackingMode) Offset(w * 0.38f, h * 0.72f) else Offset(w * 0.48f, h * 0.62f)
            drawCircle(
                color = YantraPrimary.copy(alpha = pulseAlpha),
                radius = pulseRadius * density,
                center = vehicleCenter
            )
            drawCircle(
                color = Color.White,
                radius = 8f * density,
                center = vehicleCenter
            )
            drawCircle(
                color = YantraPrimary,
                radius = 5f * density,
                center = vehicleCenter
            )

            if (isTrackingMode) {
                // Worksite target pin
                val targetCenter = Offset(w * 0.72f, h * 0.56f)
                drawCircle(
                    color = Color(0xFF0284C7),
                    radius = 6f * density,
                    center = targetCenter
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f * density,
                    center = targetCenter
                )
            }
        }

        // Road Name Badges & Labels
        Text(
            text = "HINJAWADI",
            color = Color(0xFF7A8B7A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Text(
            text = "NH 48",
            color = Color(0xFF948B77),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 42.dp, start = 40.dp)
        )
        Text(
            text = "Maan Road",
            color = Color(0xFF7A8B7A),
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 18.dp)
        )
        Text(
            text = "WAKAD",
            color = Color(0xFF7A8B7A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp, top = 12.dp)
        )

        // Overlay Markers
        if (isTrackingMode) {
            // "JCB • Ramesh" machine marker
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = YantraHeaderDark,
                modifier = Modifier
                    .offset(x = 64.dp, y = 115.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🚜 JCB • Ramesh",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Worksite label
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 40.dp, bottom = 2.dp)
            ) {
                Text(
                    text = "PHASE 1",
                    color = YantraPrimaryDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        } else {
            // Price tags matching mock 1 and mock 2
            // 1. ₹1,200/hr (Selected or default dark pill)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (selectedPricePin == "1200") YantraPrimary else YantraHeaderDark,
                modifier = Modifier
                    .offset(x = 48.dp, y = 80.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable { onPinClick?.invoke("1200") }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "🚜 ₹1,200/hr",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 2. ₹850/hr
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 36.dp, end = 44.dp)
                    .shadow(3.dp, RoundedCornerShape(8.dp))
                    .clickable { onPinClick?.invoke("850") }
            ) {
                Text(
                    text = "₹850/hr",
                    color = YantraTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // 3. ₹1,800/hr or ₹3,500/trip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 36.dp, end = 52.dp)
                    .shadow(3.dp, RoundedCornerShape(8.dp))
                    .clickable { onPinClick?.invoke("3500") }
            ) {
                Text(
                    text = "₹3,500/trip",
                    color = YantraTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // GPS My-Location Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .size(36.dp)
                .shadow(3.dp, CircleShape)
                .background(Color.White, CircleShape)
                .clickable { /* Re-center map */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My location",
                tint = YantraPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
