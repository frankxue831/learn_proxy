# Learn Proxy

A Spring Boot 3.5.3 application for learning proxy concepts and patterns using Java 21 and reactive programming.

## Overview

This repository serves as a learning playground for understanding various proxy patterns and implementations in Java. The project uses Spring WebFlux for reactive web programming, providing a modern foundation for exploring proxy concepts.

## Technologies Used

- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.5.3** - Application framework with auto-configuration
- **Spring WebFlux** - Reactive web framework (non-blocking I/O)
- **Project Reactor** - Reactive programming library
- **Maven** - Build and dependency management
- **JUnit 5** - Testing framework

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Building and Running

```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Package the application
./mvnw clean package

# Run tests
./mvnw test
```

### Project Structure

```
src/
├── main/
│   ├── java/org/workshop/learn_proxy/
│   │   └── LearnProxyApplication.java    # Main Spring Boot application
│   └── resources/
│       └── application.properties        # Application configuration
└── test/
    └── java/org/workshop/learn_proxy/
        └── LearnProxyApplicationTests.java # Basic Spring Boot test
```

## Learning Objectives

This repository will help you explore:

- **Proxy Patterns** - Forward proxy, reverse proxy, transparent proxy
- **HTTP Proxying** - Request/response interception and modification
- **Reactive Programming** - Non-blocking I/O with WebFlux
- **Spring Boot Features** - Auto-configuration, embedded server, testing
- **Performance Optimization** - Caching, connection pooling, load balancing

## Development

The application runs on Netty server (default for WebFlux) instead of traditional Tomcat. This provides better performance for reactive applications with high concurrency.

### Maven Wrapper

Use `./mvnw` (Unix/Mac) or `mvnw.cmd` (Windows) instead of `mvn` to ensure consistent Maven version across different environments.

### Customizing documentation URL

The static site under `docs/` is built with Jekyll. Edit `docs/_config.yml`
to change the `url` and `baseurl` values for your deployment. These settings can
also be overridden at build time using the `JEKYLL_URL` and `JEKYLL_BASEURL`
environment variables.

## Contributing

This is a learning project. Feel free to experiment with different proxy implementations and patterns.

## License

This project is licensed under the [MIT License](LICENSE).
