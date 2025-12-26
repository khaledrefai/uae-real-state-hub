#!/bin/bash
# Deployment script for UAE Real State Hub on VPS
# Usage: ./deploy.sh [build|deploy|start|stop|logs|status]

set -e

# Configuration
APP_NAME="uaerealstatehub"
COMPOSE_FILE="docker-compose.prod.yml"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Build the application
build() {
    log_info "Building ${APP_NAME}..."
    docker compose -f ${COMPOSE_FILE} build --no-cache
    log_info "Build completed successfully!"
}

# Deploy (build and start)
deploy() {
    log_info "Deploying ${APP_NAME}..."
    
    # Pull latest images
    docker compose -f ${COMPOSE_FILE} pull mongodb
    
    # Build the app
    build
    
    # Start services
    start
    
    log_info "Deployment completed successfully!"
}

# Start services
start() {
    log_info "Starting ${APP_NAME} services..."
    docker compose -f ${COMPOSE_FILE} up -d
    log_info "Services started. Use './deploy.sh logs' to view logs."
}

# Stop services
stop() {
    log_info "Stopping ${APP_NAME} services..."
    docker compose -f ${COMPOSE_FILE} down
    log_info "Services stopped."
}

# Restart services
restart() {
    log_info "Restarting ${APP_NAME} services..."
    docker compose -f ${COMPOSE_FILE} restart
    log_info "Services restarted."
}

# View logs
logs() {
    docker compose -f ${COMPOSE_FILE} logs -f --tail=100
}

# View app logs only
app_logs() {
    docker compose -f ${COMPOSE_FILE} logs -f --tail=100 app
}

# Check status
status() {
    log_info "Checking ${APP_NAME} status..."
    docker compose -f ${COMPOSE_FILE} ps
    echo ""
    log_info "Health check endpoints:"
    echo "  App: http://localhost:8080/management/health"
    echo ""
    
    # Check if app is responding
    if curl -s -f http://localhost:8080/management/health > /dev/null 2>&1; then
        log_info "Application is ${GREEN}HEALTHY${NC}"
    else
        log_warn "Application may still be starting or is unhealthy"
    fi
}

# Clean up (remove containers, volumes, images)
cleanup() {
    log_warn "This will remove all containers, volumes, and images for ${APP_NAME}"
    read -p "Are you sure? (y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        docker compose -f ${COMPOSE_FILE} down -v --rmi local
        log_info "Cleanup completed."
    else
        log_info "Cleanup cancelled."
    fi
}

# Update (pull latest and redeploy)
update() {
    log_info "Updating ${APP_NAME}..."
    
    # Stop current deployment
    stop
    
    # Pull latest code (if using git)
    if [ -d ".git" ]; then
        log_info "Pulling latest code..."
        git pull
    fi
    
    # Rebuild and deploy
    deploy
}

# Show usage
usage() {
    echo "UAE Real State Hub Deployment Script"
    echo ""
    echo "Usage: $0 <command>"
    echo ""
    echo "Commands:"
    echo "  build     - Build Docker images"
    echo "  deploy    - Build and deploy the application"
    echo "  start     - Start all services"
    echo "  stop      - Stop all services"
    echo "  restart   - Restart all services"
    echo "  logs      - View all logs (follow mode)"
    echo "  app-logs  - View app logs only (follow mode)"
    echo "  status    - Check deployment status"
    echo "  update    - Pull latest and redeploy"
    echo "  cleanup   - Remove containers, volumes, and images"
    echo ""
    echo "Examples:"
    echo "  $0 deploy    # First time deployment"
    echo "  $0 update    # Update existing deployment"
    echo "  $0 logs      # View logs"
}

# Main
case "${1:-}" in
    build)
        build
        ;;
    deploy)
        deploy
        ;;
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    logs)
        logs
        ;;
    app-logs)
        app_logs
        ;;
    status)
        status
        ;;
    update)
        update
        ;;
    cleanup)
        cleanup
        ;;
    *)
        usage
        exit 1
        ;;
esac

