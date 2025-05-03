# Use Amazon Corretto 21 (Alpine) as the base image
FROM amazoncorretto:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY build/libs/globeco-trade-blotter-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 