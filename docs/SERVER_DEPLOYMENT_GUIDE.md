# Yantra Server-Side Deployment & DevOps Guide

## 1. System Architecture Overview
The backend is designed for high-availability heavy equipment dispatching, handling real-time GPS telemetry from hundreds of machines while maintaining zero-loss financial records:

```
                  [ Clients: Android Apps / Web Portal ]
                                    │
                                    ▼ (HTTPS / WSS)
                          [ NGINX / Cloud CDN ]
                                    │
               ┌────────────────────┴────────────────────┐
               ▼                                         ▼
     [ Core REST API Service ]                 [ Telemetry WebSocket Gateway ]
      (Go / Node.js / FastAPI)                   (Redis Pub/Sub WebSocket Hub)
               │                                         │
               ├────────────────────┬────────────────────┘
               ▼                    ▼
     [ PostgreSQL + PostGIS ]   [ Redis Cluster ]
     (Persistent ACID Data)     (In-Memory Cache & Location Broadcast)
```

---

## 2. Local & Staging Setup (Docker Compose)

### `docker-compose.yml`
```yaml
version: '3.8'

services:
  db:
    image: postgis/postgis:15-3.3-alpine
    container_name: yantra-postgres
    restart: always
    environment:
      POSTGRES_USER: yantra_admin
      POSTGRES_PASSWORD: SecretPostgresPassword123
      POSTGRES_DB: yantradb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./migrations:/docker-entrypoint-initdb.d

  redis:
    image: redis:7.2-alpine
    container_name: yantra-redis
    restart: always
    ports:
      - "6379:6379"

  yantra-api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: yantra-core-api
    restart: always
    environment:
      - PORT=8080
      - DATABASE_URL=postgres://yantra_admin:SecretPostgresPassword123@db:5432/yantradb?sslmode=disable
      - REDIS_URL=redis://redis:6379
      - JWT_SECRET=ProductionSuperSecretKeyChangeMe
      - ENVIRONMENT=production
    ports:
      - "8080:8080"
    depends_on:
      - db
      - redis

volumes:
  pgdata:
```

### Starting the Local Stack
```bash
docker-compose up -d
docker-compose logs -f yantra-api
```

---

## 3. Production Deployment on Google Cloud (Cloud Run + Cloud SQL)

### Step 1: Provision Managed PostgreSQL with PostGIS
```bash
# Create Cloud SQL Instance
gcloud sql instances create yantra-db-prod \
    --database-version=POSTGRES_15 \
    --tier=db-custom-4-16384 \
    --region=asia-south1 \
    --storage-type=SSD \
    --storage-size=100GB

# Enable PostGIS
gcloud sql databases create yantradb --instance=yantra-db-prod
gcloud sql users create yantra_user --instance=yantra-db-prod --password="STRONG_PASSWORD"
```

### Step 2: Provision Memorystore for Redis (Telemetry Pub/Sub)
```bash
gcloud redis instances create yantra-redis-prod \
    --size=4 \
    --region=asia-south1 \
    --redis-version=redis_7_0
```

### Step 3: Build & Deploy Core Container to Google Cloud Run
```bash
# Build & tag image in Google Artifact Registry
gcloud builds submit --tag asia-south1-docker.pkg.dev/$PROJECT_ID/yantra-repo/api:latest

# Deploy with Auto-scaling and Serverless VPC Access
gcloud run deploy yantra-api-service \
    --image=asia-south1-docker.pkg.dev/$PROJECT_ID/yantra-repo/api:latest \
    --region=asia-south1 \
    --platform=managed \
    --allow-unauthenticated \
    --min-instances=2 \
    --max-instances=20 \
    --cpu=2 \
    --memory=4Gi \
    --set-env-vars="ENVIRONMENT=production,REDIS_HOST=10.0.0.4" \
    --set-secrets="DATABASE_URL=yantra_db_url:latest,JWT_SECRET=yantra_jwt_secret:latest"
```

---

## 4. Kubernetes Manifest (Production K8s / GKE)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: yantra-api-deployment
  labels:
    app: yantra-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: yantra-api
  template:
    metadata:
      labels:
        app: yantra-api
    spec:
      containers:
      - name: yantra-api
        image: asia-south1-docker.pkg.dev/yantra-prod/yantra-repo/api:v1.0.0
        ports:
        - containerPort: 8080
        resources:
          requests:
            cpu: "500m"
            memory: "1Gi"
          limits:
            cpu: "2000m"
            memory: "2Gi"
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 15
          periodSeconds: 20
---
apiVersion: v1
kind: Service
metadata:
  name: yantra-api-service
spec:
  type: ClusterIP
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: yantra-api
```

---

## 5. Security & High Availability Best Practices
1. **GST E-Way Bill Integration**: Machines transporting gravel or soil exceeding ₹50,000 threshold invoke the National Informatics Centre (NIC) API to generate automated RFID e-way passes.
2. **Start PIN Verification**: Equipment rental timers start only when the worksite foreman provides the 4-digit start PIN (`FY-2048` -> `4821`), preventing disputed billing.
3. **Database Read Replicas**: Geospatial queries (`ST_DWithin` on `equipments`) are served from PostGIS read-replicas, while transactional booking writes go to the primary instance.
