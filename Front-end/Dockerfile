# front-end/Dockerfile

# Use an official Node runtime as a parent image
FROM node:18-alpine as build-stage

# Set the working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the application
COPY . .

# Build the Angular application
RUN npm run build --prod

# Stage 2: Use an Nginx image to serve the content
FROM nginx:alpine

# Copy the nginx config file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the built application from the previous stage
COPY --from=build-stage /app/dist/pharmacyapp /usr/share/nginx/html

# Expose the port the application runs on
EXPOSE 80

# Command to run the Nginx server
CMD ["nginx", "-g", "daemon off;"]
