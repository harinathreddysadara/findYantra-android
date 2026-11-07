# Yantra Heavy Equipment Ecosystem - Database Schema Documentation

## 1. Overview & Architecture
The Yantra heavy equipment platform powers three interconnected applications:
1. **FindYantra**: Customer portal for on-demand equipment booking (hourly, daily, per-trip).
2. **YantraFleet**: Fleet management system for contractors and machine owners to manage vehicles, drivers, orders, and public listings.
3. **YantraStore**: Marketplace for machine spare parts, construction materials, and verified on-site equipment maintenance.

To support high-concurrency geospatial searches (e.g. finding excavators within 10km of a worksite) alongside ACID transaction guarantees for payments and order dispatching, the primary database engine is **PostgreSQL with the PostGIS extension**.

---

## 2. PostgreSQL + PostGIS Schema (DDL)

```sql
-- Enable PostGIS and UUID extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- -----------------------------------------------------------------------------
-- 1. USERS & ORGANIZATIONS
-- -----------------------------------------------------------------------------
CREATE TYPE user_role AS ENUM ('HIRER', 'FLEET_OWNER', 'OPERATOR', 'STORE_VENDOR', 'ADMIN');

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(120) NOT NULL,
    role user_role NOT NULL DEFAULT 'HIRER',
    company_name VARCHAR(150),
    gstin VARCHAR(20),
    is_verified BOOLEAN DEFAULT FALSE,
    profile_photo_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE fleets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    fleet_name VARCHAR(150) NOT NULL, -- e.g. "PATIL EARTHMOVERS"
    pan_number VARCHAR(15),
    gst_invoice_number VARCHAR(25),
    bank_account_no VARCHAR(40),
    bank_ifsc VARCHAR(15),
    total_machines_count INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- -----------------------------------------------------------------------------
-- 2. OPERATORS & DRIVERS
-- -----------------------------------------------------------------------------
CREATE TYPE operator_status AS ENUM ('ON_DUTY', 'AVAILABLE', 'OFF_DUTY');

CREATE TABLE operators (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fleet_id UUID NOT NULL REFERENCES fleets(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id),
    full_name VARCHAR(120) NOT NULL, -- e.g. "Ramesh Kale"
    phone VARCHAR(20) NOT NULL,
    role_specialization VARCHAR(80) NOT NULL, -- "Backhoe operator", "Heavy vehicle driver"
    license_number VARCHAR(50) NOT NULL,
    license_valid_until DATE NOT NULL,
    experience_years INT DEFAULT 5,
    rating NUMERIC(2,1) DEFAULT 4.9,
    status operator_status DEFAULT 'AVAILABLE',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- -----------------------------------------------------------------------------
-- 3. HEAVY EQUIPMENTS & ASSETS
-- -----------------------------------------------------------------------------
CREATE TYPE equipment_category AS ENUM ('FARMING', 'INFRASTRUCTURE', 'CONSTRUCTION', 'HEAVY_LIFTING');
CREATE TYPE equipment_status AS ENUM ('AVAILABLE', 'EN_ROUTE', 'WORKING', 'IN_SERVICE');

CREATE TABLE equipments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fleet_id UUID NOT NULL REFERENCES fleets(id) ON DELETE CASCADE,
    model_name VARCHAR(100) NOT NULL, -- e.g. "JCB 3DX Backhoe"
    registration_no VARCHAR(40) UNIQUE NOT NULL, -- e.g. "MH 14 JX 4821"
    category equipment_category NOT NULL,
    engine_hp INT NOT NULL,
    bucket_capacity VARCHAR(50), -- e.g. "1 m³", "16 m³"
    operator_included BOOLEAN DEFAULT TRUE,
    
    -- Rental Pricing Rates
    hourly_rate NUMERIC(10,2) NOT NULL,
    daily_rate NUMERIC(10,2),
    per_trip_rate NUMERIC(10,2),
    
    -- Marketplace Visibility & Dispatch State
    status equipment_status DEFAULT 'AVAILABLE',
    is_public_on_findyantra BOOLEAN DEFAULT TRUE,
    current_operator_id UUID REFERENCES operators(id),
    
    -- Telemetry & Maintenance tracking
    cumulative_engine_hours INT DEFAULT 454,
    hours_to_next_service INT DEFAULT 46,
    
    -- Geospatial coordinates
    current_location GEOGRAPHY(POINT, 4326),
    location_address VARCHAR(255) DEFAULT 'Hinjawadi Phase 1, Pune',
    
    rating NUMERIC(2,1) DEFAULT 4.8,
    review_count INT DEFAULT 32,
    image_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Spatial index for sub-millisecond radius search
CREATE INDEX idx_equipments_location ON equipments USING GIST (current_location);
CREATE INDEX idx_equipments_status_public ON equipments (status, is_public_on_findyantra);

-- -----------------------------------------------------------------------------
-- 4. RENTAL BOOKINGS & DISPATCH ORDERS
-- -----------------------------------------------------------------------------
CREATE TYPE booking_source AS ENUM ('FINDYANTRA', 'DIRECT_BOOKING');
CREATE TYPE rental_type AS ENUM ('HOURLY', 'DAILY', 'PER_TRIP');
CREATE TYPE booking_status AS ENUM ('NEEDS_ASSIGNMENT', 'EN_ROUTE', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

CREATE TABLE rental_orders (
    id VARCHAR(30) PRIMARY KEY, -- e.g. "FY-2048"
    source booking_source NOT NULL DEFAULT 'FINDYANTRA',
    customer_id UUID NOT NULL REFERENCES users(id),
    fleet_id UUID NOT NULL REFERENCES fleets(id),
    equipment_id UUID NOT NULL REFERENCES equipments(id),
    assigned_operator_id UUID REFERENCES operators(id),
    
    rental_type rental_type NOT NULL,
    duration_hours INT,
    trip_count INT,
    
    worksite_address VARCHAR(255) NOT NULL,
    worksite_location GEOGRAPHY(POINT, 4326),
    
    scheduled_date DATE NOT NULL,
    start_time VARCHAR(20) NOT NULL,
    
    base_amount NUMERIC(10,2) NOT NULL,
    transport_amount NUMERIC(10,2) DEFAULT 500.00,
    gst_amount NUMERIC(10,2) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    
    start_pin VARCHAR(6) NOT NULL, -- e.g. "4821"
    pin_verified_at TIMESTAMPTZ,
    
    status booking_status DEFAULT 'NEEDS_ASSIGNMENT',
    eta_minutes INT DEFAULT 12,
    
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- -----------------------------------------------------------------------------
-- 5. TELEMETRY & GPS TRACKING LOGS
-- -----------------------------------------------------------------------------
CREATE TABLE telemetry_logs (
    id BIGSERIAL PRIMARY KEY,
    equipment_id UUID NOT NULL REFERENCES equipments(id),
    order_id VARCHAR(30) REFERENCES rental_orders(id),
    coordinates GEOGRAPHY(POINT, 4326) NOT NULL,
    speed_kmh NUMERIC(5,2),
    heading_deg INT,
    fuel_level_percent INT,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_telemetry_order_time ON telemetry_logs (order_id, recorded_at DESC);

-- -----------------------------------------------------------------------------
-- 6. YANTRASTORE: SPARES, MATERIALS & SERVICES
-- -----------------------------------------------------------------------------
CREATE TYPE store_category AS ENUM ('SPARE_PARTS', 'MATERIALS', 'SERVICES');

CREATE TABLE store_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vendor_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(150) NOT NULL, -- e.g. "Hydraulic oil filter"
    category store_category NOT NULL,
    part_or_spec_no VARCHAR(80), -- e.g. "Part no. 32/925346"
    spec_details VARCHAR(150), -- e.g. "OEM specification"
    price NUMERIC(10,2) NOT NULL,
    stock_quantity INT DEFAULT 100,
    compatible_equipment_model VARCHAR(150), -- "JCB 3DX Backhoe"
    description TEXT,
    is_service BOOLEAN DEFAULT FALSE,
    service_duration_hours VARCHAR(30),
    rating NUMERIC(2,1) DEFAULT 4.8,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE store_orders (
    id VARCHAR(30) PRIMARY KEY, -- e.g. "ORD-88219"
    customer_id UUID NOT NULL REFERENCES users(id),
    delivery_address TEXT NOT NULL,
    delivery_pincode VARCHAR(10) NOT NULL,
    subtotal_amount NUMERIC(10,2) NOT NULL,
    delivery_fee NUMERIC(10,2) DEFAULT 80.00,
    total_payable NUMERIC(10,2) NOT NULL,
    status VARCHAR(30) DEFAULT 'DISPATCHED',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE service_appointments (
    id VARCHAR(30) PRIMARY KEY, -- e.g. "SVC-500H"
    store_item_id UUID NOT NULL REFERENCES store_items(id),
    equipment_id UUID NOT NULL REFERENCES equipments(id),
    customer_id UUID NOT NULL REFERENCES users(id),
    scheduled_date DATE NOT NULL,
    scheduled_slot VARCHAR(30) NOT NULL, -- "2 PM–4 PM"
    service_address TEXT NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(30) DEFAULT 'SCHEDULED',
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```
