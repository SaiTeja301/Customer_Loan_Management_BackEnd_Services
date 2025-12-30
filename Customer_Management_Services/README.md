# Spring Boot Customer Management System

# Customer_Loan_Management_BackEnd_Services
A loan management backend that stores customer loan details and uses an AI model to track and analyze customer records. Provides intelligent insights, secure storage, and efficient loan lifecycle management.


A RESTful API application for managing customer records with interest calculation functionality, built using Spring Boot, Hibernate/JPA, and MySQL.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)

## Features

- ✅ **CRUD Operations**: Complete Create, Read, Update, Delete functionality for customers
- ✅ **Interest Calculation**: Automatic simple interest and total amount calculation
- ✅ **Context-Aware AI Assistant**: Gemini AI integration that uses your customer database as context to answer questions via `/askAgent`
- ✅ **API Documentation**: Interactive Swagger UI for exploring and testing APIs
- ✅ **Input Validation**: Server-side validation using Jakarta Validation API
- ✅ **CORS Support**: Configured for cross-origin requests from frontend applications
- ✅ **Global Exception Handling**: Centralized error handling with standardized JSON responses
- ✅ **Constructor Injection**: Best practice dependency injection pattern
- ✅ **RESTful Design**: Well-structured REST API endpoints
-  ✅ **Database Integration**: MySQL database with Hibernate ORM

## Technologies Used

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** - Database access layer
- **Hibernate** - ORM framework
- **MySQL 8** - Relational database
- **Google Vertex AI** - Gemini AI integration
- **Springdoc OpenAPI** - Swagger UI and API documentation
- **Lombok** - Reducing boilerplate code
- **ModelMapper** - Object mapping
- **Jakarta Validation** - Input validation
- **Maven** - Dependency management

## Project Structure

```
src/main/java/com/SprCustomers/
├── Bo/                          # Business Objects (Entities)
│   └── CustomerBo.java
├── Config/                      # Configuration classes
│   └── CorsConfig.java
├── Controller/                  # REST Controllers
│   ├── CustomerController.java
│   └── CustomerControllerInterface.java
├── Dao/                         # Data Access Objects (Repositories)
│   └── CustomerRepository.java
├── Dto/                         # Data Transfer Objects
│   ├── AiRequest.java
│   ├── AiResponse.java
│   ├── CustomerDto.java
│   └── CustomerUpdateRequest.java
├── Exception/                   # Exception handlers
│   └── GlobalExceptionHandler.java
├── Mapper/                      # Mapping configurations
│   └── ModelMapperExp.java
├── Service/                     # Business logic layer
│   ├── CustomerService.java
│   ├── GeminiAiService.java
│   ├── GeminiAiServiceImpl.java
│   └── customerServiceImpl.java
├── Vo/                          # View Objects
│   └── CustomerVo.java
└── YmlSpringHibernateProjectApplication.java
```

## Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Google API Key** for Gemini AI (set as `GOOGLE_API_KEY` environment variable)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Setup Instructions

### 1. Database Setup

Create a MySQL database:
```sql
CREATE DATABASE testschema;
```

### 2. Configure Database Connection

Update `src/main/resources/application.yml` with your MySQL credentials:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/testschema
    username: your_username
    password: your_password
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or using the Maven wrapper:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Base URL: `http://localhost:8080/Spr/customers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/getCustomerById?id={id}` | Get customer by ID |
| GET | `/getAllCustomers` | Get all customers |
| POST | `/insertCustomer` | Create a new customer |
| PUT | `/updateCustomerById?id={id}` | Update customer by ID |
| DELETE | `/deleteCustomerById/{id}` | Delete customer by ID |

### Example Requests

#### Create Customer (POST `/insertCustomer`)
```json
{
  "customerName": "John Doe",
  "customerAddress": "123 Main St",
  "principalAmount": 10000.0,
  "rate": 5.0,
  "time": 2.0
}
```

#### Update Customer (PUT `/updateCustomerById?id=1`)
```json
{
  "customerName": "Jane Doe",
  "customerAddress": "456 Oak Ave",
  "principalAmount": 15000.0,
  "interestRate": 6.0,
  "time": 3.0
}
```

**Note**: All fields in update request are optional for partial updates.

### Response Format

Success responses include:
- Customer details
- Calculated interest amount (Simple Interest = P × R × T / 100)
- Total amount (Principal + Interest)

## API Documentation (Swagger UI)

Access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

The Swagger UI provides:
- ✅ Complete API documentation with request/response schemas
- ✅ Interactive testing of all endpoints
- ✅ Authentication and CORS testing
- ✅ Auto-generated examples

## AI Assistant Endpoint

### Ask AI Agent (POST `/askAgent`)

The AI assistant has access to your customer database context. You can ask questions about specific customers or aggregate data.

**Request:**
```json
{
  "question": "Who has the highest principal amount?"
}
```

**Response:**
```json
{
  "answer": "Based on the database, John Doe has the highest principal amount of 15000.0.",
  "timestamp": "2025-11-22T20:00:00"
}
```

**Prerequisites:**
- Set `GOOGLE_API_KEY` environment variable with your Google API key
- Restart the application after setting the environment variable

**Example Usage:**
```bash
curl -X POST http://localhost:8080/Spr/customers/askAgent \
  -H "Content-Type: application/json" \
  -d '{"question": "Explain compound interest"}'
```

## Configuration

### CORS Settings

The application is configured to accept requests from:
- `http://localhost:4200` (Angular default)
- `http://localhost:3000` (React default)

To add more origins, update `CorsConfig.java`.

### Logging

Logging levels are configured in `application.yml`:
- Application logs: `DEBUG`
- SQL queries: `DEBUG`
- SQL parameters: `TRACE`

### Validation Rules

- **Customer Name**: Required, not blank
- **Customer Address**: Required, not blank
- **Principal Amount**: Required, must be positive
- **Interest Rate**: Required, must be positive
- **Time Period**: Required, must be positive

## Database Schema

The application creates a table `Spring_Customers` with the following structure:

| Column | Type | Description |
|--------|------|-------------|
| CID | INTEGER (PK) | Customer ID (Auto-increment) |
| Customer_Name | VARCHAR | Customer name |
| Customer_Address | VARCHAR | Customer address |
| Principle_Amount | FLOAT | Principal amount |
| Interest_Rate | FLOAT | Rate of interest |
| Time | FLOAT | Time period |

**Note**: `Interest Amount` and `Total Amount` are calculated fields (not stored in database).

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/SpringBootCustomersHibernateAppConfigYml-0.0.1-SNAPSHOT.jar
```

## Improvements Made

- ✅ Removed duplicate code and endpoints
- ✅ Added comprehensive validation
- ✅ Implemented CORS support
- ✅ Added global exception handling
- ✅ Standardized dependency injection
- ✅ Enhanced configuration
- ✅ Improved code documentation

## License

This project is for educational purposes.

## Contact

For questions or suggestions, please contact the development team.
