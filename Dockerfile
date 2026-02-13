####
# This Dockerfile is used to build a Quarkus application in JVM mode
####

# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn package -DskipTests -Dquarkus.package.jar.type=uber-jar

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus

# Copy application from build stage
COPY --from=build --chown=quarkus:quarkus /app/target/*-runner.jar /app/application.jar

# Set user
USER quarkus

# Expose port
EXPOSE 8080

# Set environment
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV JAVA_APP_JAR="/app/application.jar"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health/live || exit 1

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar $JAVA_APP_JAR"]
