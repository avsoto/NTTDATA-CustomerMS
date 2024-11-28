# Customer Microservice - NTTDATA Bootcamp Tech Girls Power

## Project Description

This microservice is part of a banking system designed to manage customers. The **Customer Microservice** allows the creation, retrieval, update, and deletion of customer information, including their personal details such as name, surname, DNI (ID), and email. This service communicates with other microservices in the banking system, such as the **Account Microservice**, to provide an integrated banking management solution.

## Project Reports

### SOLID Principles Report
This report provides a detailed analysis of how the SOLID principles have been applied throughout the project. It includes explanations of each principle and examples of how they were implemented in the code to enhance maintainability, scalability, and readability.
[SOLID Principles Report](https://docs.google.com/document/d/1tSSjcOaGNktm7uQvlNelBmg7FlEsAfO_/edit?usp=sharing&ouid=111308656360819493585&rtpof=true&sd=true)


### Code Coverage Report
The code coverage report outlines the extent of testing performed in the project, including unit tests and integration tests. It provides insights into the percentage of code covered by tests, highlighting areas that need additional testing to ensure robustness and reliability.
[Code Coverage Report](https://docs.google.com/document/d/1zNvOwxqBcbkEUDoxmEIhLKotHEuPPfHO/edit?usp=sharing&ouid=111308656360819493585&rtpof=true&sd=true)

## Features

This microservice implements the following features:

### Customer Management

- **Create Customer**: Allows the creation of a new customer with the following attributes:
  - `id` (Automatically generated)
  - `firstName` (Required)
  - `lastName` (Required)
  - `dni` (Required, unique)
  - `email` (Valid email format)

  **Endpoint**:  
  `POST /customers`  
  Request body:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "dni": "12345678",
    "email": "john.doe@example.com"
  }
  ```

- **Get All Customers**: Lists all customers in the system.

  **Endpoint**:  
  `GET /customers`  
  Response:
  ```json
  [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "dni": "12345678",
      "email": "john.doe@example.com"
    }
  ]
  ```

- **Get Customer by ID**: Retrieves details of a specific customer using their ID.

  **Endpoint**:  
  `GET /customers/{id}`  
  Response:
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "dni": "12345678",
    "email": "john.doe@example.com"
  }
  ```

- **Update Customer**: Updates the data of an existing customer.

  **Endpoint**:  
  `PUT /customers/{id}`  
  Request body:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "dni": "87654321",
    "email": "john.doe@newdomain.com"
  }
  ```

- **Delete Customer**: Deletes a customer by their ID, with the restriction that a customer with active accounts cannot be deleted.

  **Endpoint**:  
  `DELETE /customers/{id}`

## Business Rules

1. **Customer Validations**:
  - Each customer must have a **unique DNI**.
  - **Customers with active accounts** cannot be deleted.

## Technologies Used

- **Spring Boot**: To create the microservice and manage business logic.
- **JPA/Hibernate**: For data persistence using a relational database (MySQL).
- **MySQL**: Relational database to store customer information.
- **OpenAPI**: For documenting the API contract (using a Contract First approach).
- **Java 8/11**: Object-Oriented Programming (OOP) and Functional Programming.

## System Architecture

The microservice follows a microservices architecture where each component is independent but communicates with others:

- **Customer Microservice**: Manages customer information.
- **Account Microservice**: Manages customer bank accounts (this service interacts with the **Account Microservice** to retrieve customer account details).

### Component Diagram

The system consists of several microservices that communicate with each other to provide full functionality. The **Customer Microservice** interacts with the **Account Microservice** to manage relationships between customers and accounts.

### Sequence Diagram

The communication flow between microservices follows a reactive and asynchronous process, where each microservice communicates with others via RESTful APIs.

![Banking System UML Diagram](https://raw.githubusercontent.com/avsoto/NTTDATA-CustomerMS/refs/heads/main/diagram/secuenceDiagramCustomer.jpg)

## Running the Service

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/CustomerMicroservice.git
   cd CustomerMicroservice
   ```

2. **Install Dependencies**:
   If using Maven:
   ```bash
   mvn install
   ```

3. **Run the Microservice**:
   To run the microservice, execute:
   ```bash
   mvn spring-boot:run
   ```

4. **Testing**:
   Use **Postman** to test the following endpoints:
- `POST /customers` to create a new customer.
- `GET /customers` to list all customers.
- `GET /customers/{id}` to retrieve a customer by their ID.
- `PUT /customers/{id}` to update a customer.
- `DELETE /customers/{id}` to delete a customer.