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
ARG APP_VERSION=1.0.2

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /build/target/PharmacyApp-1.0.2.jar ./PharmacyApp-1.0.2.jar

# Copy the .env file to the application directory
COPY .env /app/.env

# Expose the port the application runs on
EXPOSE 8080

# Set the environment variables
ENV DB_URL=jdbc:mysql://mysql-pharmacyapp:3306/PharmacyApp
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOST_NAME=${EMAIL_HOST_NAME}
ENV EMAIL_USERNAME=${EMAIL_USER_NAME}
ENV EMAIL_PASSWORD=${EMAIL_PASSWORD}
ENV EMAIL_PORT=${EMAIL_PORT}
ENV VERIFY_EMAIL_HOST=${VERIFY_EMAIL_HOST}
ENV JWT_SECRET=${JWT_SECRET}
ENV SERVER_PORT=${SERVER_PORT}
ENV ELASTICSEARCH_HOST=${ELASTICSEARCH_HOST}
ENV ELASTICSEARCH_PORT=${ELASTICSEARCH_PORT}
ENV ELASTICSEARCH_USERNAME=${ELASTICSEARCH_USERNAME}
ENV ELASTICSEARCH_PASSWORD=${ELASTICSEARCH_PASSWORD}


# Run the jar file
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} PharmacyApp-${JAR_VERSION}.jar
