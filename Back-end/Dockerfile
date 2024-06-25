# back-end/Dockerfile

# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-19 AS build

# Set the working directory in the container
WORKDIR /build

# Copy the pom.xml file and download dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:19-jdk
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /build/target/PharmacyApp-1.0.0.jar ./PharmacyApp-1.0.0.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the environment variables
ENV DB_URL=jdbc:mysql://mysql-pharmacyapp:3306/PharmacyApp
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

# Run the jar file
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} PharmacyApp-${JAR_VERSION}.jar
