# ════════════════════════════════════════════════
# STAGE 1 — BUILD
# ════════════════════════════════════════════════
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first for layer caching
COPY pom.xml .

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# ════════════════════════════════════════════════
# STAGE 2 — RUN
# ════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache curl

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s \
  --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/api/system/health || exit 1

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=docker", \
  "-jar", "app.jar"]