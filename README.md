# Sales Application

This is a Spring Boot-based application for managing transactions, products, and users. It includes features such as:
- User login and authentication using JWT
- CRUD operations for transactions and products
- Reporting functionality based on transaction dates

## Features
- **User Authentication**: JWT-based login for secure access.
- **Transaction Management**: Create and manage transactions and their details.
- **Product Management**: Manage products and their stock.
- **Reporting**: Generate reports based on a given date range.

## Technologies Used
- **Spring Boot**: Java-based framework for creating production-grade applications.
- **Spring Security**: Used for managing authentication and authorization.
- **JWT (JSON Web Tokens)**: Secure authentication using tokens.
- **PostgreSQL**: Database to store application data.
- **JPA (Java Persistence API)**: ORM for database interactions.
- **Logback**: Logging framework for application logs.
- **Test**: Integration tests to ensure the full flow of requests is working as expected, particularly for testing authentication, transaction creation, and product management endpoints.

## 📘 API Documentation (Swagger UI)
This application includes Swagger UI for interactive API documentation and testing.

### 🔧 How to Access Swagger UI
After running the application, visit the following URL in your browser:
```
http://localhost:8080/swagger-ui/index.html
```
> Make sure the application is running on port `8080`, or update the URL according to your configured port.


## Setup and Installation

1. **Install Maven** (if not already installed):
   - On **Windows**: Download the Maven installer from [Maven Download Page](https://maven.apache.org/download.cgi), and follow the installation guide.
   - On **macOS**:
     ```bash
     brew install maven
     ```
   - On **Linux**:
     ```bash
     sudo apt install maven
     ```
2. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/sales-app.git
3. Navigate to the project directory:
   ```bash
   cd sales-app   
5. Run the following command to clean and package the project while skipping tests:
   ```bash
   mvn clean package -DskipTests
6. **Using docker**
   ```bash
   docker-compose up --build  
   docker-compose down
