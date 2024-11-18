
# PharmacyApp

![Alt text](screenshots/PharmacyApp.png);

Pharmacy App Software Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Technology Stack](#technology-stack)
3. [Functional Requirements](#functional-requirements)
4. [Non-Functional Requirements](#non-functional-requirements)
5. [Deployment on AWS EC2 with Docker](#deployment)
6. [Conclusion](#conclusion)

## 1. Introduction

The PharmacyApp is a full-stack web application designed to streamline pharmacy operations, offering an efficient platform for managing customers, inventory, prescriptions, medications, and invoices. It empowers pharmacies to enhance customer experience, improve operational efficiency, and simplify day-to-day tasks.

## Screenshots

[View Slideshow](<iframe src="https://docs.google.com/presentation/d/e/2PACX-1vSx_eLXkXLsAhjZKELLzqw6XpnLQnjN7jiVcbOaY_AnEytZeyylM6Vi7QzvAzNl7WbRqlCtx_ok1UnL/embed?start=true&loop=true&delayms=3000" frameborder="0" width="1440" height="839" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>)

## 2. Technology Stack

The Pharmacy App utilizes the following technology stack:

### Backend

- Java 19
- Spring Boot framework
- MySQL Workbench Relational Database
- Lombok for code generation
- Spring JDBC for database connectivity

### Frontend

- Angular (TypeScript)
- Bootstrap for responsive UI design
- HTML and CSS (latest versions) for markup and styling

### Deployment:
- Amazon EC2 for cloud hosting
- Docker for containerization to ensure consistent execution environments

# 3. Functional Requirements

The Pharmacy App includes the following functional requirements:

# Application Overview

### User Management:
- Create user accounts with roles (e.g., User, Manager, Admin, SysAdmin)
- Implement two-factor authentication for enhanced security
- Manage user profiles and roles

### Customer Management:
-  Create customer profiles with relevant information
- Associate customers with users
  
### Security and Confirmation:
- Secure user authentication and authorization
- Implement email verification using confirmation tokens
- Facilitate password resets using secure verification links
  
### Inventory and Invoices:
- Manage inventory of medications
- Create invoices for customer purchases
- Track inventory levels and update accordingly

### Prescriptions and Medications:
- Manage customer prescriptions associated with medications
- Track medication details for better oversight
- Database Structure and Initialization:
- Clear and concise schema definition in schema.sql
- Sample data included for initial testing

# 4. Non-Functional Requirements

The Pharmacy App includes the following non-functional requirements:

### Performance

- The application should handle a large number of concurrent users without significant performance degradation
- Database queries should be optimized to ensure fast and efficient data retrieval and updates

### Security

- User authentication and authorization should be implemented securely using industry best practices
- User passwords should be stored securely using encryption techniques
- User sessions should be managed securely to prevent unauthorized access

### Usability

- The user interface should be intuitive and user-friendly
- Proper validation and error handling should be implemented to provide informative feedback to users
- The application should be responsive and compatible with different devices and screen sizes

### Reliability

- The application should be highly reliable and available, with minimal downtime
- Data integrity should be maintained, ensuring accurate and consistent information storage and retrieval

# Deployment on AWS EC2 with Docker

## This project outlines the steps for deploying the PharmacyApp on AWS EC2 using Docker for containerization:

1. Prerequisites:
- An AWS account with appropriate permissions and resources
- Docker installed locally
- AWS CLI configured locally
  
2. Building Docker Images:
- Create Dockerfiles for both the backend and frontend applications, specifying dependencies and environment variables.
- Build the Docker images using docker build commands.

3. Pushing Images to Docker Hub (Optional):
- Create a Docker Hub repository (optional, but recommended for easier sharing)
- Push the built Docker images to your Docker Hub repository.

4. Launching on AWS EC2:
- Create an EC2 instance in your AWS account with appropriate configuration (e.g., instance type, security group, storage).
- Install Docker on the EC2 instance.
- Run the Docker images on the EC2 instance to launch the backend and frontend containers.

5. Configuration and Management:
- Configure environment variables within the Docker containers to point to the database and other external resources.
- Utilize tools like Docker Compose for streamlined multi-container deployments and management (optional).

# Conclusion

This documentation provides a high-level overview of the PharmacyApp, its functionalities, technical choices, and deployment approach. By leveraging Docker containerization and AWS EC2 cloud hosting, you can ensure consistent execution environments and a scalable solution for your pharmacy management needs. Remember to stay updated with best practices and continuously refine your deployment strategy for an optimized and secure production environment.
