# Render Build Script
# This file tells Render how to build your application

# Install Maven dependencies and build the project
mvn clean package -DskipTests

# Make the start script executable
chmod +x start.sh