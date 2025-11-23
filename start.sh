#!/bin/bash
# Render deployment script

echo "Starting Spring Boot application..."

# Use production profile
export SPRING_PROFILES_ACTIVE=prod

# Run the application with the JAR file
java -Dserver.port=$PORT -jar target/Continua3-0.0.1-SNAPSHOT.jar