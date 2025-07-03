# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.3 application using Java 21 and Maven for dependency management. The project uses Spring WebFlux for reactive web programming.

## Key Technologies

- **Spring Boot 3.5.3** - Main application framework
- **Spring WebFlux** - Reactive web framework
- **Java 21** - Programming language version
- **Maven** - Build and dependency management
- **JUnit 5** - Testing framework
- **Project Reactor** - Reactive programming library

## Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Package the application
./mvnw clean package

# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=LearnProxyApplicationTests
```

### Maven Wrapper
Use `./mvnw` on Unix/Mac or `mvnw.cmd` on Windows instead of `mvn` to ensure consistent Maven version.

## Project Structure

```
src/
├── main/
│   ├── java/org/workshop/learn_proxy/
│   │   └── LearnProxyApplication.java    # Main Spring Boot application class
│   └── resources/
│       └── application.properties        # Application configuration
└── test/
    └── java/org/workshop/learn_proxy/
        └── LearnProxyApplicationTests.java # Basic Spring Boot test
```

## Architecture Notes

- This is a reactive web application using WebFlux instead of traditional Spring MVC
- The application follows standard Spring Boot project structure
- Currently minimal setup with just the main application class and basic test
- Uses Spring Boot's auto-configuration capabilities
- Configured for reactive programming patterns with Project Reactor

## Application Configuration

- Application name: `learn_proxy` (configured in application.properties)
- Default Spring Boot configuration applies for most settings
- WebFlux runs on Netty server by default (not Tomcat)