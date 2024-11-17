# AccountMS - Microservice for Account Management

This repository contains the **AccountMS** microservice, which handles the creation, reading, updating, and deletion (CRUD) of bank accounts in the XYZ Bank system. This microservice is part of the **Project II - NTTDATA Bootcamp Tech Girls Power**, and is built using **Spring Boot** and **JPA/Hibernate**, connecting to a relational database (MySQL) for data persistence.

## System Requirements

- **Java 8+** or **Java 11+**
- **Spring Boot 2.x**
- **MySQL** for the database
- **Postman** or any other tool to test API endpoints

## Microservice Functionalities

The **AccountMS** microservice provides the following endpoints to manage bank accounts:

### 1. Create Account
- **Method**: `POST`
- **Endpoint**: `/accounts`
- **Description**: Creates a new bank account for a customer.
- **Request Body** (JSON):
  ```json
  {
    "accountType": "SAVINGS",
    "balance": 0.0,
    "customerId": 1
  }
  ```

### 2. List Accounts
- **Method**: `GET`
- **Endpoint**: `/accounts`
- **Description**: Retrieves a list of all bank accounts in the system.
- **Response** (200 OK):
  ```json
  [
    {
      "id": 1,
      "accountNumber": "1234567890",
      "balance": 1000.0,
      "accountType": "SAVINGS",
      "customerId": 1
    },
    {
      "id": 2,
      "accountNumber": "9876543210",
      "balance": 500.0,
      "accountType": "CHECKING",
      "customerId": 2
    }
  ]
  ```

### 3. Get Account by ID
- **Method**: `GET`
- **Endpoint**: `/accounts/{id}`
- **Description**: Retrieves the details of a specific account by its ID.
- **Path Parameter**: `id` - The unique identifier of the account.
- **Successful Response** (200 OK):
  ```json
  {
    "id": 1,
    "accountNumber": "1234567890",
    "balance": 1000.0,
    "accountType": "SAVINGS",
    "customerId": 1
  }
  ```

### 4. Deposit into Account
- **Method**: `PUT`
- **Endpoint**: `/accounts/{accountId}/deposit`
- **Description**: Deposits an amount into a specific account.
- **Path Parameter**: `accountId` - The ID of the account to deposit into.
- **Request Body** (JSON):
  ```json
  {
    "amount": 500.0
  }
  ```

### 5. Withdraw from Account
- **Method**: `PUT`
- **Endpoint**: `/accounts/{accountId}/withdraw`
- **Description**: Withdraws an amount from a specific account.
- **Path Parameter**: `accountId` - The ID of the account to withdraw from.
- **Request Body** (JSON):
  ```json
  {
    "amount": 200.0
  }
  ```

### 6. Delete Account
- **Method**: `DELETE`
- **Endpoint**: `/accounts/{id}`
- **Description**: Deletes a specific account from the system.
- **Path Parameter**: `id` - The unique identifier of the account.
- **Successful Response** (200 OK):
  ```json
  {
    "message": "Account deleted successfully"
  }
  ```

## Business Rules

- **Initial Account Balance**: The initial balance of an account should be greater than 0.
- **Withdrawal Rules**:
  - **Savings accounts**: Cannot have a negative balance after withdrawal.
  - **Checking accounts**: Can have an overdraft limit of up to -500.

## Architecture

### Component Diagram
This system follows a **microservices architecture**, where **AccountMS** communicates with other microservices, such as **CustomerMS**. Both microservices interact with a MySQL database for data persistence.

### Sequence Diagram
The sequence diagrams illustrate the communication flow during the creation of a customer.

![Banking System UML Diagram](https://raw.githubusercontent.com/avsoto/NTTDATA-CustomerMS/refs/heads/main/diagram/secuenceDiagramCustomer.jpg)

## Technologies Used

- **Spring Boot**: For developing the microservice.
- **JPA/Hibernate**: For data persistence.
- **MySQL**: As the relational database.
- **OpenAPI**: For API documentation (contract-first).
- **Java 8/11**: For applying both functional and object-oriented programming concepts.

## Installation and Execution

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/account-ms.git
cd account-ms
```

### 2. Configure the Database

Ensure that MySQL is installed and create a database for the project:

```sql
CREATE DATABASE accountms;
```

### 3. Configure `application.properties`

In the `src/main/resources/application.properties` file, configure the database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/accountms
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```

### 4. Run the Microservice

To run the microservice, use the following command:

```bash
mvn spring-boot:run
```

### 5. Test the Endpoints

Use **Postman** or any similar tool to test the documented endpoints.
