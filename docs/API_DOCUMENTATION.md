# Yantra Heavy Equipment Ecosystem - API Documentation

## Base URL
- **Staging**: `https://api-staging.yantraheavy.com/api/v1`
- **Production**: `https://api.yantraheavy.com/api/v1`
- **WebSocket Gateway**: `wss://telemetry.yantraheavy.com/ws/v1`

---

## 1. Authentication
All secure endpoints require an HTTP `Authorization` header with a valid Bearer JWT:
```http
Authorization: Bearer <jwt_access_token>
```

### `POST /auth/request-otp`
Sends a 4-digit SMS OTP to the user's mobile number.
- **Request Body**:
  ```json
  {
    "phone": "+919822044123",
    "role": "HIRER" // "HIRER" | "FLEET_OWNER" | "STORE_VENDOR"
  }
  ```
- **Response 200 OK**:
  ```json
  {
    "success": true,
    "session_id": "sess_89f02cba",
    "expires_in": 300
  }
  ```

### `POST /auth/verify-otp`
- **Request Body**:
  ```json
  {
    "session_id": "sess_89f02cba",
    "otp": "4821"
  }
  ```
- **Response 200 OK**:
  ```json
  {
    "access_token": "eyJhbGciOiJIUzI1...",
    "refresh_token": "d98124ef...",
    "user": {
      "id": "u_980124",
      "full_name": "Sanjay Deshmukh",
      "role": "HIRER",
      "company_name": "Deshmukh Infrastructure"
    }
  }
  ```

---

## 2. FindYantra: Equipment Discovery & Booking

### `GET /equipments/nearby`
Finds active machines within a radius of the client's worksite coordinates.
- **Query Parameters**:
  - `lat` (float, required): Worksite latitude (e.g. `18.5913`)
  - `lng` (float, required): Worksite longitude (e.g. `73.7389`)
  - `radius_km` (int, optional, default: 15)
  - `category` (string, optional): `FARMING`, `INFRASTRUCTURE`, `CONSTRUCTION`, `HEAVY_LIFTING`
  - `rental_type` (string, optional): `HOURLY`, `DAILY`, `PER_TRIP`

- **Response 200 OK**:
  ```json
  {
    "worksite": "Hinjawadi Phase 1, Pune",
    "count": 12,
    "machines": [
      {
        "id": "eq-1",
        "name": "JCB 3DX Backhoe",
        "registration_no": "MH 14 JX 4821",
        "owner_name": "Patil Earthmovers",
        "rating": 4.8,
        "review_count": 32,
        "distance_km": 2.4,
        "power_hp": 76,
        "bucket_capacity": "1 m³",
        "operator_included": true,
        "rates": {
          "hourly": 1200,
          "daily_8hr": 8500,
          "per_trip": null
        },
        "status": "AVAILABLE",
        "coordinates": { "lat": 18.5932, "lng": 73.7345 }
      }
    ]
  }
  ```

### `POST /bookings/create`
Books a machine for hourly, daily, or per-trip rental.
- **Request Body**:
  ```json
  {
    "equipment_id": "eq-1",
    "rental_type": "HOURLY",
    "duration_hours": 4,
    "worksite_address": "Phase 1, Hinjawadi, Pune",
    "worksite_lat": 18.5913,
    "worksite_lng": 73.7389,
    "start_date": "2026-11-06",
    "start_time": "09:00 AM"
  }
  ```
- **Response 201 Created**:
  ```json
  {
    "order_id": "FY-2048",
    "status": "EN_ROUTE",
    "eta_minutes": 12,
    "operator": {
      "name": "Ramesh Kale",
      "phone": "+919822044123",
      "experience_years": 8,
      "rating": 4.9
    },
    "cost_breakdown": {
      "rental_base": 4800,
      "transport": 500,
      "gst_18": 954,
      "total": 6254
    },
    "security": {
      "start_pin": "4821",
      "message": "Share start PIN only when equipment arrives at worksite"
    }
  }
  ```

---

## 3. Real-Time Telemetry & Tracking WebSocket

### `WebSocket /ws/v1/telemetry/{order_id}`
Establishes bidirectional live GPS streaming between the machine/driver device and hirer frontend.

**Server-to-Client Update Message**:
```json
{
  "type": "LOCATION_TICK",
  "order_id": "FY-2048",
  "driver_id": "op-1",
  "coordinates": {
    "lat": 18.5942,
    "lng": 73.7321
  },
  "speed_kmh": 32.5,
  "heading_degrees": 88,
  "eta_minutes": 11,
  "status": "EN_ROUTE",
  "timestamp": "2026-11-06T09:02:15Z"
}
```

---

## 4. YantraFleet: Fleet Management APIs

### `GET /fleet/overview`
Retrieves daily utilization, revenue metrics, and pending dispatch actions.
- **Response 200 OK**:
  ```json
  {
    "fleet_utilisation_percent": 78,
    "todays_orders": 8,
    "orders_needing_assignment": 2,
    "monthly_revenue": 284500,
    "revenue_growth_percent": 18.4,
    "pending_payout": 42800,
    "fleet_counts": {
      "working": 7,
      "available": 3,
      "in_service": 2
    }
  }
  ```

### `PATCH /fleet/machines/{id}/publish`
Toggles public availability of a machine on the FindYantra marketplace.
- **Request Body**:
  ```json
  {
    "is_public_on_findyantra": true,
    "hourly_rate": 1200,
    "daily_rate": 8500
  }
  ```

### `POST /fleet/orders/{id}/assign-operator`
Assigns a driver to an order waiting for dispatch.
- **Request Body**:
  ```json
  {
    "operator_id": "op-2" // Sunil Jadhav
  }
  ```
- **Response 200 OK**:
  ```json
  {
    "order_id": "FY-2049",
    "status": "EN_ROUTE",
    "assigned_operator": "Sunil Jadhav"
  }
  ```

---

## 5. YantraStore: Marketplace & Service Booking

### `GET /store/products`
Lists available parts and materials filtered by machine compatibility.
- **Query Parameters**:
  - `category`: `SPARE_PARTS`, `MATERIALS`, `SERVICES`
  - `machine_model`: `JCB 3DX`

### `POST /store/services/schedule`
Schedules an on-site equipment maintenance visit.
- **Request Body**:
  ```json
  {
    "service_code": "SVC-500H",
    "equipment_id": "eq-1",
    "scheduled_date": "2026-11-06",
    "scheduled_slot": "2 PM–4 PM",
    "worksite_address": "Hinjawadi Phase 1, Pune · 411057"
  }
  ```
- **Response 201 Created**:
  ```json
  {
    "appointment_id": "SVC-921",
    "status": "SCHEDULED",
    "total_payable": 2949,
    "advisory": "Scheduled after your current rental ends at 01:00 PM"
  }
  ```
