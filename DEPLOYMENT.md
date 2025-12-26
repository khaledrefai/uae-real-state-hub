# UAE Real State Hub - VPS Deployment Guide

## Prerequisites

On your VPS, ensure you have:

- **Docker** (v24+): https://docs.docker.com/engine/install/
- **Docker Compose** (v2+): Usually included with Docker
- **Git** (optional, for code deployment)

### Quick Docker Installation (Ubuntu/Debian)

```bash
# Install Docker
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER
newgrp docker

# Verify installation
docker --version
docker compose version
```

---

## Deployment Options

### Option 1: Deploy with Docker Compose (Recommended)

#### Step 1: Transfer files to VPS

```bash
# From your local machine
scp -r . user@your-vps-ip:/home/user/uae-real-state-hub

# Or use Git
ssh user@your-vps-ip
git clone https://your-repo-url.git
cd uae-real-state-hub
```

#### Step 2: Configure environment

```bash
# Copy example environment file
cp env.example .env

# Edit with your production values
nano .env
```

**Important**: Update these values in `.env`:

- `MONGO_PASSWORD`: Use a strong password
- `JWT_SECRET`: Generate with `openssl rand -base64 64`

#### Step 3: Deploy

```bash
# Make deploy script executable
chmod +x deploy.sh

# Deploy the application
./deploy.sh deploy

# Check status
./deploy.sh status
```

---

### Option 2: Build using Maven Jib (No Dockerfile needed)

Your project already has Jib configured. Build and push to a registry:

```bash
# Build locally and save to Docker daemon
./mvnw compile jib:dockerBuild -Pprod

# Or push to Docker Hub
./mvnw compile jib:build -Pprod -Djib.to.image=yourusername/uaerealstatehub:latest

# On VPS, pull and run
docker pull yourusername/uaerealstatehub:latest
docker compose -f docker-compose.prod.yml up -d
```

---

### Option 3: Use External MongoDB (Already Running)

If MongoDB is already running on your VPS (173.212.199.139), update the compose file:

```yaml
# In docker-compose.prod.yml, comment out the mongodb service and update app:
services:
  app:
    environment:
      - SPRING_DATA_MONGODB_URI=mongodb://admin:Admin_2025!@173.212.199.139:27017/propertieshub?authSource=admin
```

Then deploy without MongoDB:

```bash
docker compose -f docker-compose.prod.yml up -d app
```

---

## Management Commands

```bash
# Start services
./deploy.sh start

# Stop services
./deploy.sh stop

# Restart services
./deploy.sh restart

# View logs
./deploy.sh logs

# View app logs only
./deploy.sh app-logs

# Check status
./deploy.sh status

# Full update (pull code + rebuild + deploy)
./deploy.sh update

# Cleanup everything
./deploy.sh cleanup
```

---

## SSL/HTTPS Setup with Nginx

### Option A: Let's Encrypt (Free SSL)

```bash
# Install Certbot
apt install certbot

# Get certificate
certbot certonly --standalone -d your-domain.com

# Copy certificates
mkdir -p nginx/ssl
cp /etc/letsencrypt/live/your-domain.com/fullchain.pem nginx/ssl/
cp /etc/letsencrypt/live/your-domain.com/privkey.pem nginx/ssl/

# Update nginx.conf - uncomment SSL lines
# Then start with nginx profile
docker compose -f docker-compose.prod.yml --profile with-nginx up -d
```

### Option B: Self-signed Certificate (Development)

```bash
mkdir -p nginx/ssl
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout nginx/ssl/privkey.pem \
  -out nginx/ssl/fullchain.pem
```

---

## Firewall Configuration

```bash
# UFW (Ubuntu)
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw allow 8080/tcp  # App (if not using nginx)
ufw enable
```

---

## Monitoring

### Check Application Health

```bash
curl http://localhost:8080/management/health
```

### View Metrics (Prometheus format)

```bash
curl http://localhost:8080/management/prometheus
```

### Container Resource Usage

```bash
docker stats
```

---

## Troubleshooting

### Application won't start

```bash
# Check logs
docker compose -f docker-compose.prod.yml logs app

# Check if MongoDB is accessible
docker compose -f docker-compose.prod.yml exec app curl -f mongodb:27017
```

### Out of memory

```bash
# Increase JVM memory in docker-compose.prod.yml
environment:
  - _JAVA_OPTIONS=-Xmx1g -Xms512m
```

### Port already in use

```bash
# Find process using port
lsof -i :8080
# Kill it or change port in docker-compose.prod.yml
```

---

## Production Checklist

- [ ] Change default MongoDB password
- [ ] Generate new JWT secret
- [ ] Configure SSL/HTTPS
- [ ] Set up firewall rules
- [ ] Configure backup for MongoDB data
- [ ] Set up monitoring/alerting
- [ ] Configure log rotation
- [ ] Set up CI/CD pipeline (optional)

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                         VPS                              │
│  ┌─────────────┐   ┌─────────────┐   ┌──────────────┐   │
│  │   Nginx     │──▶│  Spring Boot │──▶│   MongoDB    │   │
│  │  (Optional) │   │     App      │   │              │   │
│  │  :80/:443   │   │    :8080     │   │   :27017     │   │
│  └─────────────┘   └─────────────┘   └──────────────┘   │
└─────────────────────────────────────────────────────────┘
```
