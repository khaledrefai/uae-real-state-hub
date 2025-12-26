# Multi-stage build for UAE Real State Hub

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# Install Node.js for frontend build
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm@11.3.0

# Copy Maven wrapper and pom.xml first for dependency caching
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code and config files
COPY src src
COPY package.json package-lock.json tsconfig*.json vite.config.mts ./
COPY sonar-project.properties checkstyle.xml ./

# Build the application with production profile
RUN ./mvnw clean package -Pprod -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Create non-root user for security
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown -R jhipster:jhipster /app

USER jhipster

# JVM tuning for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/management/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

