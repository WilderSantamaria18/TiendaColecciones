#!/bin/bash
# Render Build Script
echo "Starting build process..."

# Install Maven dependencies and build the project
mvn clean package -DskipTests

echo "Build completed successfully!"