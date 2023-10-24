# Use the appropriate base image with JDK 17
FROM gradle:8.3.0-jdk17

# Create a directory for your application
RUN mkdir /app

# Copy the JAR file from your local machine to the Docker image
COPY ./build/libs/Code-Challange-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

# Set the working directory
WORKDIR /app

# Define the entry point
CMD ["java", "-jar", "spring-boot-application.jar"]
