# Use an official Maven image as the base image
FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM amazoncorretto:21
# Set the working directory in the container

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/ryanair-flight-search-service-1.0.0.jar .
WORKDIR /app
EXPOSE 8080

ENTRYPOINT ["java","-jar","/ryanair-flight-search-service-1.0.0.jar"]
