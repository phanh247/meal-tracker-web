# Meal Tracker Web Application

A comprehensive Spring Boot application for tracking meals and nutritional information with JWT authentication.

## Database
Link: https://app.diagrams.net/#G1Btwkklp3seC7DrvXCmJ66wegoclScq9O#%7B%22pageId%22%3A%22vcllqK506OVeHguMUkUp%22%7D

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

### 1. Install maven 
Link download: https://phoenixnap.com/kb/install-maven-windows

### 2. Clone the repository

1. Choose a folder in local to store the project
2. In the folder, open terminal -> Clone the repository using git command:
   ```bash
   git clone https://github.com/phanh247/meal-tracker-web.git
   ```
3. Open the project in your IDE
4. Set up maven in your IDE by choosing the maven home directory (the folder where you installed maven)
Example: `C:\Program Files\apache-maven-3.6.3`
4. Wait for Maven to download all dependencies
5. Run the application at file MealTrackerWebApplication.java

## Project Structure

```
src/
├── main/
│   ├── java/com/example/meal_tracker/
│   │   ├── config/         # Configuration classes
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # Security configuration
│   │   └── service/        # Business logic services
│   └── resources/          # Application resources 
└── test/
    ├── java/              # Test classes
```

## Common git commands
1. Git clone: `git clone <repository-url>`
2. Check status: `git status`
3. Add changes: `git add .`
4. Commit changes: `git commit -m "Your commit message"`
5. Push changes: `git push`
6. Pull latest changes: `git pull`
7. Check current branch: `git branch`
8. Create a new branch: `git checkout -b <branch-name>`
9. Switch branches: `git checkout <branch-name>`
10. Merge branches: `git merge <branch-name>`
11. View commit history: `git log`
12. Stash changes: `git stash`
