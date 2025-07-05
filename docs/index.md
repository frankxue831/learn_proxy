---
layout: default
title: Home
---

<div class="hero-section">
    <div class="wrapper">
        <h1>Learn Proxy</h1>
        <p>Master proxy patterns and implementations with Spring Boot and WebFlux</p>
        <a href="{{ '/docs' | relative_url }}" class="btn">Get Started</a>
    </div>
</div>

Welcome to the Learn Proxy project - a comprehensive learning resource for understanding proxy concepts and patterns using modern Java and Spring Boot.

## What You'll Learn

<div class="features-grid">
  <div class="feature-card">
    <div class="icon">🔄</div>
    <h3>Forward Proxy</h3>
    <p>Client-side proxy for accessing external resources, handling authentication, and providing anonymity.</p>
  </div>
  
  <div class="feature-card">
    <div class="icon">⚡</div>
    <h3>Reverse Proxy</h3>
    <p>Server-side proxy for load balancing, caching, SSL termination, and request routing.</p>
  </div>
  
  <div class="feature-card">
    <div class="icon">🔍</div>
    <h3>Transparent Proxy</h3>
    <p>Invisible proxy for traffic interception, content filtering, and network monitoring.</p>
  </div>
  
  <div class="feature-card">
    <div class="icon">🛠️</div>
    <h3>HTTP Proxying</h3>
    <p>Request/response manipulation, header modification, and protocol handling.</p>
  </div>
</div>

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
git clone https://github.com/frankxue831/learn_proxy.git
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