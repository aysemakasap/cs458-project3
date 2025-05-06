# CS458 Backend Project

This is a Spring Boot backend project developed using a test-driven development (TDD) approach.

## Project Structure

The project follows a standard multi-layer architecture:

```
src/main/java/com/cs458/part1/
├── config/              # Configuration classes
│   ├── CorsConfig.java
│   └── DataInitializer.java
├── controller/          # REST controllers
│   ├── AuthController.java
│   └── UserController.java
├── dto/                 # Data Transfer Objects
│   └── LoginRequest.java
├── exception/           # Custom exceptions
│   ├── AuthenticationException.java
│   ├── UserNotFoundException.java
│   └── UserRegistrationException.java
├── model/               # Entity classes
│   └── Users.java
├── repository/          # Data access layer
│   └── UsersRepository.java
├── service/             # Business logic
│   ├── AuthService.java
│   └── UserService.java
└── Part1Application.java # Main application class
```

## Test Structure

Tests are organized to match the main code structure:

```
src/test/java/com/cs458/part1/
├── controller/          # Controller tests
│   ├── AuthControllerTest.java
│   └── UserControllerTest.java
├── integration/         # Integration tests
│   ├── AuthIntegrationTest.java
│   └── UserIntegrationTest.java
├── repository/          # Repository tests
│   └── UsersRepositoryTest.java
└── service/             # Service tests
    ├── AuthServiceTest.java
    └── UserServiceTest.java
```

## API Endpoints

### Authentication
- `POST /api/login` - Login with email and password

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create a new user
- `POST /api/users/register` - Register a new user
- `PUT /api/users/{id}` - Update user by ID
- `DELETE /api/users/{id}` - Delete user by ID

## Running the Application

### Prerequisites
- Java 21
- Maven

### Development
```bash
# Run with H2 in-memory database
mvn spring-boot:run
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest
```

## H2 Database Console

This project uses H2 in-memory database for development and testing. You can access the H2 console to view and interact with the database:

1. Run the application using `mvn spring-boot:run`
2. Open your browser and navigate to `http://localhost:8080/h2-console`
3. Configure the connection settings:
   - JDBC URL: `jdbc:h2:mem:devdb` (as configured in application.properties)
   - Username: `sa` (default)
   - Password: leave empty (default)
   - Click "Connect"

The console provides a web interface to:
- View database tables and their schema
- Execute SQL queries
- Monitor the state of the in-memory database
- Inspect data created during application runtime

This is especially useful for development and debugging purposes to verify that your entities are correctly mapped and your data is being stored as expected.

## Frontend Integration

The backend exposes a REST API that can be consumed by any frontend application. To use it with a frontend, make sure to:

1. Configure the frontend to make requests to `http://localhost:8080`
2. For login, send a POST request to `/api/login` with the following JSON structure:
   ```json
   {
     "email": "test@example.com",
     "password": "password123"
   }
   ```

## Test User

For testing purposes, the application creates a default user:
- Email: test@example.com
- Password: password123 