#!/usr/bin/env bash
# Maven build script for Render

echo "Starting Maven build..."
echo "Java version:"
java -version

echo "Building with Maven..."
./mvnw clean package -DskipTests

echo "Build completed!"
echo "JAR file location:"
ls -la target/*.jar