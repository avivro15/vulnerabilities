# Use a slim OpenJDK image to run the app
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the local machine
COPY target/vulnerabilities-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]