#!/bin/bash
echo "Starting Spring Boot application..."

# Set production profile
export SPRING_PROFILES_ACTIVE=prod

# Check if JAR file exists
if [ ! -f target/Continua3-0.0.1-SNAPSHOT.jar ]; then
    echo "ERROR: JAR file not found!"
    exit 1
fi

# Start the application
echo "Running JAR with PORT=$PORT"
java -Dserver.port=$PORT -jar target/Continua3-0.0.1-SNAPSHOT.jar