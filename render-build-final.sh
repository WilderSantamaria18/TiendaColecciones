#!/usr/bin/env bash

# Force Java detection
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "=== INSTALLING JAVA AND MAVEN ==="
echo "Updating package list..."
apt-get update -qq

echo "Installing OpenJDK 17..."
apt-get install -y openjdk-17-jdk

echo "Installing Maven..."
apt-get install -y maven

echo "=== VERIFYING INSTALLATION ==="
echo "Java version:"
java -version
echo "Maven version:"
mvn -version

echo "=== BUILDING APPLICATION ==="
echo "Starting build process..."
mvn clean package -DskipTests

echo "=== BUILD COMPLETED ==="
echo "JAR files generated:"
ls -la target/*.jar

echo "Build script finished successfully!"