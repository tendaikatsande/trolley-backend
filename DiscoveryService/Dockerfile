# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/eureka-server-1.0.0.jar eureka-server.jar

# Expose the Eureka server port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]