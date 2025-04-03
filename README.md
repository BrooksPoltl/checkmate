# CheckMate: A Java Chess Application

## Description

CheckMate is a web-based chess application built with Java Spring Boot. It provides a platform for users to play chess games, track their moves, and enjoy a clean UI experience.

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.3.10
- **Frontend**: HTML, CSS, Thymeleaf templates
- **Database**: H2 (file-based for persistence, will migrate to postgres once deployed)
- **Build Tool**: Gradle 8.13

## Project Structure

## Controllers

The application uses Spring MVC controllers to handle HTTP requests and manage user interactions:

### WebController

The main controller handling web page requests and game flow:

The WebController manages:
- User session handling
- Game creation and initialization
- Page navigation and view rendering
- Board state management for the UI

## Chess Engine

The chess logic is implemented in the `ChessUtils` class, which includes:

- Move validation for different piece types
- Rules for pawn movement, including two-square starting moves and diagonal captures
- Game state management through the Board class

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Gradle 8.13+ (or use the included Gradle wrapper)
- Git (optional, for cloning the repository)

### Local Development Setup

1. **Clone the repository** (if you haven't already):
   ```bash
   git clone https://github.com/yourusername/checkmate.git
   cd checkmate
   ```

2. **Set up environment variables**:
   - Copy the `.env.example` file to create a new `.env` file:
     ```bash
     cp .env.example .env
     ```
   - Edit the `.env` file with your database credentials:
     ```
     DB_USERNAME=your_username
     DB_PASSWORD=your_password
     ```

3. **Give execute permission to the Gradle wrapper** (if needed):
   ```bash
   chmod +x ./gradlew
   ```

4. **Build the application**:
   ```bash
   ./gradlew build
   ```

5. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

6. **Access the application**:
   - Open your browser and navigate to `http://localhost:8080`
   - You should see the user creation page
   - Create a user and start playing!

### Database

The application uses an H2 file-based database by default:
- The database file is stored in the `./data` directory
- No separate database installation is required
- The database schema is automatically created on first run

### Development Tools

- **Live Reload**: The application is configured with Spring DevTools for automatic reloading during development
- To see changes immediately in the browser, ensure the LiveReload browser extension is installed or use the embedded script

### Troubleshooting

- **Environment Variables**: If you encounter database connection issues, ensure your `.env` file is properly configured

- **Port Conflicts**: If port 8080 is already in use by another application, you have two options:

  1. **Change the port in application.properties**:
     ```properties
     server.port=8081
     ```

  2. **Free up port 8080** by finding and stopping the process using it:
     ```bash
     # For macOS/Linux - Find the process using port 8080
     lsof -i :8080
     
     # Kill the process using its PID
     kill -9 <PID>
     
     # For Windows - Find the process using port 8080
     netstat -ano | findstr :8080
     
     # Kill the process using its PID
     taskkill /PID <PID> /F
     ```

- **Java Version**: If you see compilation errors, ensure you're using Java 17+:
  ```bash
  java -version
  ```

## Testing

The project includes JUnit tests for the chess logic. Run the tests with:

```bash
./gradlew test
```