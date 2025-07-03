---
layout: default
title: Home
---

# Learn Proxy

Welcome to the Learn Proxy project - a comprehensive learning resource for understanding proxy concepts and patterns using modern Java and Spring Boot.

## What You'll Learn

This repository demonstrates various proxy patterns and implementations:

- **Forward Proxy** - Client-side proxy for accessing external resources
- **Reverse Proxy** - Server-side proxy for load balancing and caching
- **Transparent Proxy** - Invisible proxy for traffic interception
- **HTTP Proxying** - Request/response manipulation and routing

## Technology Stack

- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.5.3** - Application framework
- **Spring WebFlux** - Reactive web programming
- **Project Reactor** - Non-blocking I/O operations
- **Maven** - Build and dependency management

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included wrapper)

### Quick Start

```bash
# Clone the repository
git clone <your-repo-url>
cd learn_proxy

# Build and run
./mvnw spring-boot:run
```

## Project Structure

```
learn_proxy/
├── src/main/java/          # Java source code
├── src/main/resources/     # Application configuration
├── src/test/java/          # Unit tests
└── docs/                   # Documentation (this site)
```

## Learning Path

1. **Basic Concepts** - Understanding proxy fundamentals
2. **Implementation** - Building proxy components with Spring WebFlux
3. **Testing** - Writing tests for proxy behavior
4. **Advanced Topics** - Performance optimization and monitoring

## Contributing

This is a learning project. Feel free to experiment with different implementations and share your discoveries!

## Resources

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Project Reactor Reference](https://projectreactor.io/docs/core/release/reference/)
- [Proxy Pattern Overview](https://en.wikipedia.org/wiki/Proxy_pattern)