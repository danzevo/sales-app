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
2. **Using docker**
   ```bash
   docker-compose up --build  
   docker-compose down
