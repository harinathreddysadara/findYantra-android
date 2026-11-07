package com.example.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun BackendDocsDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Database Schema", "API Endpoints", "Server Deployment")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = YantraPrimary
                )
                Text(
                    text = "Backend & Server Architecture",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = YantraTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
            ) {
                // Tab row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color.White else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) YantraPrimary else YantraTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    item {
                        when (selectedTab) {
                            0 -> DatabaseSchemaDocContent()
                            1 -> ApiEndpointsDocContent()
                            2 -> ServerDeploymentDocContent()
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
            ) {
                Text("Close Documentation")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun DatabaseSchemaDocContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CodeDocHeader("1. Relational Schema: PostgreSQL + PostGIS")
        CodeDocText("-- Fleets Table")
        CodeDocBlock(
            """
CREATE TABLE fleets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name VARCHAR(150) NOT NULL,
    gstin VARCHAR(20) UNIQUE NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    verified BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
            """.trimIndent()
        )

        CodeDocText("-- Equipments Table with Geo Location")
        CodeDocBlock(
            """
CREATE TABLE equipments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fleet_id UUID REFERENCES fleets(id),
    model_name VARCHAR(100) NOT NULL,
    registration_no VARCHAR(30) UNIQUE NOT NULL,
    category VARCHAR(50) NOT NULL, -- FARMING, INFRA, etc.
    power_hp INT NOT NULL,
    bucket_capacity VARCHAR(50),
    hourly_rate NUMERIC(10,2) NOT NULL,
    daily_rate NUMERIC(10,2),
    per_trip_rate NUMERIC(10,2),
    status VARCHAR(30) DEFAULT 'AVAILABLE',
    is_public BOOLEAN DEFAULT TRUE,
    engine_hours INT DEFAULT 454,
    location GEOGRAPHY(POINT, 4326),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_equip_geo ON equipments USING GIST(location);
            """.trimIndent()
        )

        CodeDocText("-- Rental Bookings Table")
        CodeDocBlock(
            """
CREATE TABLE rental_orders (
    id VARCHAR(30) PRIMARY KEY, -- FY-2048
    customer_id UUID NOT NULL,
    equipment_id UUID REFERENCES equipments(id),
    operator_id UUID,
    rental_type VARCHAR(20) NOT NULL,
    duration_hours INT,
    worksite_name VARCHAR(200) NOT NULL,
    worksite_loc GEOGRAPHY(POINT, 4326),
    start_time TIMESTAMPTZ NOT NULL,
    status VARCHAR(30) DEFAULT 'EN_ROUTE',
    start_pin VARCHAR(6) NOT NULL,
    base_amount NUMERIC(10,2) NOT NULL,
    gst_amount NUMERIC(10,2) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL
);
            """.trimIndent()
        )
    }
}

@Composable
private fun ApiEndpointsDocContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CodeDocHeader("2. Core REST & WebSocket Endpoints")
        CodeDocText("GET /api/v1/equipments/nearby")
        CodeDocBlock(
            """
Query: ?lat=18.5913&lng=73.7389&radius_km=15&category=CONSTRUCTION
Response 200 OK:
{
  "count": 12,
  "machines": [
    {
      "id": "eq-1",
      "model": "JCB 3DX Backhoe",
      "reg_no": "MH 14 JX 4821",
      "hourly_rate": 1200,
      "distance_km": 2.4,
      "operator_included": true,
      "status": "AVAILABLE"
    }
  ]
}
            """.trimIndent()
        )

        CodeDocText("POST /api/v1/bookings/create")
        CodeDocBlock(
            """
Request Payload:
{
  "equipment_id": "eq-1",
  "rental_type": "HOURLY",
  "hours": 4,
  "worksite": "Hinjawadi Phase 1, Pune",
  "start_time": "2026-11-06T09:00:00Z"
}
Response 201 Created:
{
  "order_id": "FY-2048",
  "assigned_operator": "Ramesh Kale",
  "start_pin": "4821",
  "eta_minutes": 12,
  "total_amount": 6254
}
            """.trimIndent()
        )

        CodeDocText("WS /api/v1/telemetry/live/{order_id}")
        CodeDocBlock(
            """
WebSocket Stream:
Client receives continuous GPS coordinates:
{
  "vehicle_id": "eq-1",
  "lat": 18.5925,
  "lng": 73.7360,
  "speed_kmh": 28.4,
  "eta_minutes": 11,
  "status": "EN_ROUTE"
}
            """.trimIndent()
        )
    }
}

@Composable
private fun ServerDeploymentDocContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CodeDocHeader("3. Production Deployment: Docker & Cloud Run")
        CodeDocText("docker-compose.yml configuration:")
        CodeDocBlock(
            """
version: '3.8'
services:
  yantra-api:
    image: yantra/backend:latest
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgres://user:pwd@db:5432/yantradb
      - REDIS_URL=redis://redis:6379
      - JWT_SECRET=supersecretjwtkey
    depends_on:
      - db
      - redis

  db:
    image: postgis/postgis:15-3.3
    environment:
      POSTGRES_DB: yantradb
      POSTGRES_PASSWORD: pwd
    volumes:
      - pgdata:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine

volumes:
  pgdata:
            """.trimIndent()
        )

        CodeDocText("Production Deployment Commands:")
        CodeDocBlock(
            """
# 1. Build & Push Container to GCP Artifact Registry
docker build -t gcr.io/yantra-heavy/api:v1 .
docker push gcr.io/yantra-heavy/api:v1

# 2. Deploy to Google Cloud Run
gcloud run deploy yantra-backend \
  --image gcr.io/yantra-heavy/api:v1 \
  --platform managed \
  --region asia-south1 \
  --allow-unauthenticated \
  --set-env-vars DATABASE_URL=secrets,REDIS_HOST=memorystore
            """.trimIndent()
        )
    }
}

@Composable
private fun CodeDocHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF38BDF8),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
    )
}

@Composable
private fun CodeDocText(text: String) {
    Text(
        text = text,
        color = Color(0xFF94A3B8),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace
    )
}

@Composable
private fun CodeDocBlock(code: String) {
    Surface(
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = code,
            color = Color(0xFFE2E8F0),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(8.dp),
            lineHeight = 14.sp
        )
    }
}
