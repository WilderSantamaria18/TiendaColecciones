FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-Dspring.profiles.active=prod", "-Dserver.port=8080", "-jar", "target/Continua3-0.0.1-SNAPSHOT.jar"]