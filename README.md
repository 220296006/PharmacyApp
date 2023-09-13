# PharmacyApp

![Alt text](PharmacyApp.png);


Pharmacy App Software Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Functional Requirements](#functional-requirements)
3. [Non-Functional Requirements](#non-functional-requirements)
4. [Technology Stack](#technology-stack)
5. [Conclusion](#conclusion)

## 1. Introduction
The Pharmacy App is a web-based application designed to streamline pharmacy operations and provide an efficient platform for managing customers, inventory, prescriptions, medications, and invoices. The app aims to simplify the process of managing pharmacy-related tasks, enhancing customer experience, and improving overall operational efficiency.

## 2. Functional Requirements
The Pharmacy App includes the following functional requirements:

### User Management
- Allow users to register and create an account
- Enable users to log in and authenticate securely
- Provide role-based access control to manage user permissions

### Customer Management
- Allow users to add, view, update, and delete customer records
- Capture customer information, including name, contact details, and address
- Associate customers with user accounts for tracking purposes

### Inventory Management
- Provide functionalities to add, view, update, and delete medication inventory
- Capture medication details, including name, description, quantity, and price
- Track medication stock levels and provide notifications for low inventory

### Prescription Management
- Enable users to create, view, update, and delete prescriptions
- Capture prescription details, including customer information, doctor's name, and issue date
- Associate prescriptions with customers for easy reference

### Medication Management
- Allow users to add, view, update, and delete medications within prescriptions
- Capture medication details, including name, dosage, and frequency
- Track medications associated with specific prescriptions

### Invoice Management
- Provide functionalities to generate and manage customer invoices
- Capture invoice details, including customer information, amount, due date, and payment status
- Allow users to mark invoices as paid and track payment status

## 3. Non-Functional Requirements
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

## 4. Technology Stack
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

## 5. Conclusion
This Pharmacy App Software Documentation provides an overview of the application, including its functional and non-functional requirements, as well as the technology stack used for development. By adhering to these requirements and leveraging the specified technologies, the app aims to provide an efficient and user-friendly solution for managing pharmacy operations.

