# OAuth2-Secured CSV Data Management Microservices

This repository showcases a complete OAuth2-based architecture comprising three services:
- **Authorization Server (auth-service)**
- **Resource Server (data-service)**
- **Client (gateway-service)**

The project is developed using **Java 23** and **Spring Boot 3.4.0**, leveraging the latest features of **Spring Security (6.4)** and **OpenAPI** for documentation.

- **Gateway Service**: Swagger is integrated and accessible for API documentation.
- **Data Service**: H2 Console is enabled for accessing and managing the in-memory database.

This project demonstrates a secure microservice architecture using:
- **OAuth2** for authentication
- **CSV data processing** (customer records)
- **Three-tier design**:
  - Auth Service (OAuth2 Provider)
  - Data Service (Resource Server with CSV operations)
  - Gateway Service (API Gateway with Swagger)

Key Features:
- Upload/download customer data via CSV files
- JWT-based security with Spring Security 6.x
- H2 in-memory database with console access

- Swagger url : http://localhost:8080/swagger-ui/index.html#/
- H2 Console : http://localhost:8081/h2-console/
  - JDBC URL: `jdbc:h2:mem:h2-data-service-db`
  - User Name: `sa`

## Table of Contents
- [OAuth2-Based Architecture Demo Project](#oauth2-based-architecture-demo-project)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
    - [Architecture](#architecture)
  - [Technologies Used](#technologies-used)
  - [Services](#services)
    - [Auth-Service](#auth-service)
      - [Key Features:](#key-features)
    - [Data-Service](#data-service)
      - [Key Features:](#key-features-1)
    - [Gateway-Service](#gateway-service)
      - [Key Features:](#key-features-2)
  - [How to Run](#how-to-run)
    - [Prerequisites](#prerequisites)
    - [Steps](#steps)
  - [Endpoints](#endpoints)
    - [Data-Service](#data-service-1)
    - [Gateway-Service](#gateway-service-1)

---

## Overview

Project implements an OAuth2 secured system consisting of:
1. **Auth-Service**: Acts as an OAuth2 Authorization Server, issuing tokens.
2. **Data-Service**: A secured Resource Server requiring authentication for access.
3. **Gateway-Service**: A client that authenticates with the Authorization Server using the `client_credentials` grant type and proxies requests to the Resource Server.

### Architecture
The system uses OAuth2's secure mechanisms to protect and authenticate access to the Data-Service, ensuring that only authorized clients can consume its resources.

---

## Technologies Used

- **Java**: 23
- **Spring Boot**: 3.4.0
- **Spring Security**: 6.4
- **JUnit**: 5
- **Maven**: For dependency management
- **OpenAPI**: 2.6.0 (Gateway-Service only)

---

## Services

### Auth-Service
**Role**: OAuth2 Authorization Server  
This service handles:
- Authentication of clients
- Issuing Access Tokens using the `client_credentials` grant type

#### Key Features:
- Built with Spring Security 6.4
- Configurable client credentials (`client_id`, `client_secret`) in application properties
- Token generation endpoint: `/oauth2/token`

---

### Data-Service
**Role**: OAuth2 Resource Server  
This service provides secure CSV data management capabilities with:
- CSV file upload and parsing
- Customer data storage in H2 database
- REST API endpoints for data operations

#### Key Features:
- Secure CSV upload and storage
- Customer data management endpoints
- JWT token validation from Auth-Service
- In-memory H2 database with console access

---

### Gateway-Service
**Role**: OAuth2 Client  
The gateway acts as:
- A client authenticated with `auth-service` using `client_credentials`
- A proxy for forwarding requests to `data-service`

#### Key Features:
- Token acquisition via the `client_credentials` grant type
- Includes `client_id` and `client_secret` in requests to the Authorization Server
- Implements OpenAPI 2.6.0 for API documentation and testing

---

## Database Schema
Data-Service uses the following schema:
```sql
CREATE TABLE customers (
   index INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   customer_id VARCHAR(255),
   first_name VARCHAR(255),
   last_name VARCHAR(255),
   company VARCHAR(255),
   city VARCHAR(255),
   country VARCHAR(255),
   phone1 VARCHAR(255),
   phone2 VARCHAR(255),
   email VARCHAR(255),
   subscription_date DATE,
   website VARCHAR(255)
);
```

---

## How to Run

### Prerequisites
- Java 23
- Maven

### Steps
1. Change directory to project folder
   ```bash
   cd spring-oauth2-csv-microservice
   ```

2. Build all modules:
   ```bash
   mvn clean install
   ```

3. Start the services:
    - **Auth-Service**:
      ```bash
      cd auth-service
      mvn spring-boot:run
      ```
    - **Data-Service**:
      ```bash
      cd ../data-service
      mvn spring-boot:run
      ```
    - **Gateway-Service**:
      ```bash
      cd ../gateway-service
      mvn spring-boot:run
      ```
---

## Endpoints

### Data-Service
The **Data-Service** provides endpoints for managing csvs. Below is a list of available endpoints.

- **Get All Csv Records**:
    - `GET /api/customers`
    - Retrieves a list of all csv records.

- **Get CSV record By customerId**:
    - `GET /api/customers/{customerId}`
    - Fetches a specific csv record by its unique field (`customerId`).

- **Upload Csv Records**:
    - `POST /api/customers`
    - Creates a new csv records from given csv file
    - Example Csv File format:
      ```csv
      Index,Customer Id,First Name,Last Name,Company,City,Country,Phone 1,Phone 2,Email,Subscription Date,Website
      1,DD37Cf93aecA6Dc,Sheryl,Baxter,Rasmussen Group,East Leonard,Chile,229.077.5154,397.884.0519x718,zunigavanessa@smith.info,2020-08-24,http://www.stephenson.com/
      2,1Ef7b82A4CAAD10,Preston,Lozano,Vega-Gentry,East Jimmychester,Djibouti,5153435776,686-620-1820x944,vmata@colon.com,2021-04-23,http://www.hobbs.com/
      ```
    - **Example Data File**
      [customers-100.csv](./customers-100.csv)

- **Delete all csv records**:
    - `DELETE /api/customers`
    - Deletes all csv records.

### Gateway-Service
The **Gateway-Service** forwards requests to the **Data-Service** and handles authentication with the **Auth-Service**.

- **Proxy Endpoint for All CSV records**:
    - `GET /api/customers`
    - Authenticates the user with the **Auth-Service** and proxies the request to the **Data-Service**.
    - Returns all csv records

- **Proxy Endpoint for getting a csv record by code**:
    - `GET /api/customers/{customerId}`
    - Authenticates the user with the **Auth-Service** and proxies the request to the **Data-Service**.
    - Returns a csv record by given code
  
- **Proxy Endpoint for uploading csv records from csv file**:
    - `POST /api/customers`
    - Authenticates the user with the **Auth-Service** and proxies the request to the **Data-Service**.
    - Creates CSV records from given csv file

- **Proxy Endpoint for deleting all records**:
    - `DELETE /api/customers`
    - Authenticates the user with the **Auth-Service** and proxies the request to the **Data-Service**.
    - Deletes all records from h2 database
