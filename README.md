# Meal Tracker Web Application

A comprehensive Spring Boot application for tracking meals and nutritional information with JWT authentication.

## Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - User registration and login
  - Role-based access control (USER, ADMIN)

- **Meal Management**
  - Create, read, update, delete meals
  - Track meal items with nutritional information
  - Search meals by date range
  - Calculate total calories and macronutrients

- **Food Database**
  - Comprehensive food database with nutritional information
  - Food categories and verification system
  - Search functionality

- **Security**
  - Spring Security with JWT tokens
  - Password encryption with BCrypt
  - CORS configuration
  - Global exception handling

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **Build Tool**: Maven
- **Utilities**: Lombok, MapStruct
- **Testing**: JUnit 5, TestContainers

## Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional, for TestContainers)

## Setup Instructions

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE meal_tracker;
CREATE USER meal_tracker_user WITH PASSWORD 'meal_tracker_pass';
GRANT ALL PRIVILEGES ON DATABASE meal_tracker TO meal_tracker_user;
```

### 2. Environment Variables

Set the following environment variables:

```bash
export DB_USERNAME=meal_tracker_user
export DB_PASSWORD=meal_tracker_pass
export JWT_SECRET=your-secret-key-here
```

### 3. Build and Run

```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/health` - Health check

### Meals
- `POST /api/meals` - Create a new meal
- `GET /api/meals/{id}` - Get meal by ID
- `GET /api/meals` - Get all meals for user
- `GET /api/meals/paginated` - Get paginated meals
- `GET /api/meals/date-range` - Get meals by date range
- `GET /api/meals/date` - Get meals by specific date
- `PUT /api/meals/{id}` - Update meal
- `DELETE /api/meals/{id}` - Delete meal
- `GET /api/meals/stats/calories` - Get total calories for date
- `GET /api/meals/stats/count` - Get meal count for date

## Project Structure

```
src/
├── main/
│   ├── java/com/example/meal_tracker/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # Security configuration
│   │   └── service/       # Business logic services
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application-prod.yml
└── test/
    ├── java/              # Test classes
    └── resources/
        └── application-test.yml
```

## Configuration Profiles

- **default**: Production configuration
- **dev**: Development configuration with detailed logging
- **prod**: Production configuration with minimal logging
- **test**: Test configuration with H2 in-memory database

## Security

The application uses JWT tokens for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Testing

Run tests with:

```bash
mvn test
```

## Docker Support

The application includes TestContainers for integration testing with PostgreSQL.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.
