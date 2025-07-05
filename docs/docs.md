---
layout: default
title: Documentation
---

# Documentation

Welcome to the comprehensive documentation for learning proxy patterns and implementations with Spring Boot and WebFlux.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Proxy Fundamentals](#proxy-fundamentals)
3. [Implementation Guides](#implementation-guides)
4. [Advanced Topics](#advanced-topics)
5. [Performance & Optimization](#performance--optimization)
6. [Testing Strategies](#testing-strategies)

---

## Getting Started

### Prerequisites

Before diving into proxy implementations, ensure you have:

- **Java 21** - Latest LTS version
- **Maven 3.6+** - Build tool (or use included wrapper)
- **IDE** - IntelliJ IDEA, VS Code, or Eclipse
- **Basic Spring Boot Knowledge** - Understanding of Spring concepts

### Project Setup

```bash
# Clone the repository
git clone https://github.com/frankxue831/learn_proxy.git
cd learn_proxy

# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Project Structure Overview

```
learn_proxy/
├── src/main/java/org/workshop/learn_proxy/
│   ├── LearnProxyApplication.java     # Main application class
│   ├── config/                        # Configuration classes
│   ├── proxy/                         # Proxy implementations
│   │   ├── forward/                   # Forward proxy patterns
│   │   ├── reverse/                   # Reverse proxy patterns
│   │   └── transparent/               # Transparent proxy patterns
│   ├── handler/                       # Request handlers
│   └── service/                       # Business logic services
├── src/main/resources/
│   ├── application.yml                # Application configuration
│   └── static/                        # Static resources
└── src/test/java/                     # Test implementations
```

---

## Proxy Fundamentals

### What is a Proxy?

A proxy is an intermediary that sits between a client and a server, forwarding requests and responses. Proxies can:

- **Intercept** requests and responses
- **Modify** headers and content
- **Cache** responses for performance
- **Route** requests to different servers
- **Filter** content based on rules

### Types of Proxies

<div class="features-grid">
  <div class="feature-card">
    <h3>Forward Proxy</h3>
    <p>Acts on behalf of clients, hiding their identity from servers. Common for corporate networks and privacy.</p>
    <strong>Use Cases:</strong>
    <ul>
      <li>Corporate firewalls</li>
      <li>Content filtering</li>
      <li>Anonymous browsing</li>
    </ul>
  </div>
  
  <div class="feature-card">
    <h3>Reverse Proxy</h3>
    <p>Acts on behalf of servers, hiding server details from clients. Essential for scalable architectures.</p>
    <strong>Use Cases:</strong>
    <ul>
      <li>Load balancing</li>
      <li>SSL termination</li>
      <li>Caching</li>
    </ul>
  </div>
  
  <div class="feature-card">
    <h3>Transparent Proxy</h3>
    <p>Intercepts communications without client configuration. Often used for monitoring and filtering.</p>
    <strong>Use Cases:</strong>
    <ul>
      <li>Content filtering</li>
      <li>Traffic analysis</li>
      <li>Bandwidth control</li>
    </ul>
  </div>
</div>

---

## Implementation Guides

### Basic HTTP Proxy with WebFlux

```java
@RestController
public class BasicProxyController {
    
    private final WebClient webClient;
    
    public BasicProxyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    @GetMapping("/proxy/**")
    public Mono<ResponseEntity<String>> proxyRequest(
            ServerHttpRequest request,
            @RequestParam String target) {
        
        String path = request.getPath().value().substring("/proxy".length());
        String targetUrl = target + path;
        
        return webClient.get()
                .uri(targetUrl)
                .headers(headers -> {
                    request.getHeaders().forEach((key, values) -> {
                        if (!key.equalsIgnoreCase("host")) {
                            headers.addAll(key, values);
                        }
                    });
                })
                .retrieve()
                .toEntity(String.class);
    }
}
```

### Forward Proxy Implementation

```java
@Component
public class ForwardProxyHandler {
    
    private final WebClient webClient;
    
    public ForwardProxyHandler(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().proxyWithSystemProperties()
                ))
                .build();
    }
    
    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return webClient.method(request.method())
                .uri(request.uri())
                .headers(headers -> headers.addAll(request.headers().asHttpHeaders()))
                .body(request.bodyToMono(String.class), String.class)
                .exchangeToMono(clientResponse -> 
                    ServerResponse.status(clientResponse.statusCode())
                            .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
                            .body(clientResponse.bodyToMono(String.class), String.class)
                );
    }
}
```

### Reverse Proxy with Load Balancing

```java
@Component
public class ReverseProxyHandler {
    
    private final List<String> backendServers;
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final WebClient webClient;
    
    public ReverseProxyHandler(WebClient.Builder webClientBuilder) {
        this.backendServers = Arrays.asList(
            "http://backend1:8080",
            "http://backend2:8080",
            "http://backend3:8080"
        );
        this.webClient = webClientBuilder.build();
    }
    
    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        String backendUrl = getNextBackendServer();
        String targetUrl = backendUrl + request.path();
        
        return webClient.method(request.method())
                .uri(targetUrl)
                .headers(headers -> {
                    headers.addAll(request.headers().asHttpHeaders());
                    headers.remove("host");
                })
                .body(request.bodyToMono(String.class), String.class)
                .exchangeToMono(this::buildResponse);
    }
    
    private String getNextBackendServer() {
        int index = currentIndex.getAndIncrement() % backendServers.size();
        return backendServers.get(index);
    }
    
    private Mono<ServerResponse> buildResponse(ClientResponse clientResponse) {
        return ServerResponse.status(clientResponse.statusCode())
                .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
                .body(clientResponse.bodyToMono(String.class), String.class);
    }
}
```

---

## Advanced Topics

### Caching Strategies

Implement intelligent caching to improve performance:

```java
@Component
public class CachingProxyHandler {
    
    private final LoadingCache<String, String> cache;
    private final WebClient webClient;
    
    public CachingProxyHandler(WebClient.Builder webClientBuilder) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(this::fetchFromBackend);
        this.webClient = webClientBuilder.build();
    }
    
    public Mono<ServerResponse> handleCachedRequest(ServerRequest request) {
        String cacheKey = generateCacheKey(request);
        
        return Mono.fromCallable(() -> cache.get(cacheKey))
                .map(content -> ServerResponse.ok().bodyValue(content))
                .flatMap(responseBuilder -> responseBuilder);
    }
    
    private String fetchFromBackend(String cacheKey) {
        // Implementation to fetch from backend
        return "cached content";
    }
    
    private String generateCacheKey(ServerRequest request) {
        return request.method() + ":" + request.path() + ":" + 
               request.queryParams().toSingleValueMap().toString();
    }
}
```

### Security Considerations

```java
@Component
public class SecureProxyHandler {
    
    private final Set<String> allowedHosts;
    private final RateLimiter rateLimiter;
    
    public SecureProxyHandler() {
        this.allowedHosts = Set.of("api.example.com", "service.example.com");
        this.rateLimiter = RateLimiter.create(100.0); // 100 requests per second
    }
    
    public Mono<ServerResponse> handleSecureRequest(ServerRequest request) {
        return validateRequest(request)
                .flatMap(this::processRequest)
                .onErrorResume(this::handleError);
    }
    
    private Mono<ServerRequest> validateRequest(ServerRequest request) {
        // Rate limiting
        if (!rateLimiter.tryAcquire()) {
            return Mono.error(new TooManyRequestsException("Rate limit exceeded"));
        }
        
        // Host validation
        String targetHost = request.headers().firstHeader("X-Target-Host");
        if (!allowedHosts.contains(targetHost)) {
            return Mono.error(new UnauthorizedException("Host not allowed"));
        }
        
        return Mono.just(request);
    }
    
    private Mono<ServerResponse> processRequest(ServerRequest request) {
        // Process the validated request
        return ServerResponse.ok().bodyValue("Processed securely");
    }
    
    private Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof TooManyRequestsException) {
            return ServerResponse.status(429).bodyValue("Too Many Requests");
        } else if (error instanceof UnauthorizedException) {
            return ServerResponse.status(401).bodyValue("Unauthorized");
        }
        return ServerResponse.status(500).bodyValue("Internal Server Error");
    }
}
```

---

## Performance & Optimization

### Connection Pooling

```java
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(10))
                .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(10))
                        .addHandlerLast(new WriteTimeoutHandler(10)));
        
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.USER_AGENT, "Learn-Proxy/1.0");
    }
}
```

### Monitoring and Metrics

```java
@Component
public class ProxyMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter requestCounter;
    private final Timer responseTimer;
    
    public ProxyMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestCounter = Counter.builder("proxy_requests_total")
                .description("Total number of proxy requests")
                .register(meterRegistry);
        this.responseTimer = Timer.builder("proxy_response_time")
                .description("Proxy response time")
                .register(meterRegistry);
    }
    
    public void recordRequest(String method, String path, int statusCode) {
        requestCounter.increment(
            Tags.of(
                "method", method,
                "path", path,
                "status", String.valueOf(statusCode)
            )
        );
    }
    
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }
}
```

---

## Testing Strategies

### Unit Testing

```java
@ExtendWith(MockitoExtension.class)
class ProxyHandlerTest {
    
    @Mock
    private WebClient webClient;
    
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @InjectMocks
    private ForwardProxyHandler proxyHandler;
    
    @Test
    void shouldProxyRequestSuccessfully() {
        // Given
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .uri(URI.create("http://example.com/api/test"))
                .build();
        
        when(webClient.method(any())).thenReturn(requestHeadersUriSpec);
        // ... setup mock chain
        
        // When
        Mono<ServerResponse> response = proxyHandler.handleRequest(request);
        
        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> 
                    serverResponse.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }
}
```

### Integration Testing

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProxyIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @Test
    void shouldProxyToBackendService() {
        // Given
        String proxyUrl = "http://localhost:" + port + "/proxy?target=http://httpbin.org";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            proxyUrl + "/get", String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("httpbin.org");
    }
}
```

---

## Next Steps

1. **Explore Examples** - Check out the `/src/main/java/proxy/` directory
2. **Run Tests** - Execute `./mvnw test` to see implementations in action
3. **Experiment** - Modify configurations and observe behavior
4. **Contribute** - Add new proxy patterns or improve existing ones

## Additional Resources

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Project Reactor Reference](https://projectreactor.io/docs/core/release/reference/)
- [HTTP Proxy Standards](https://tools.ietf.org/html/rfc7230)
- [Load Balancing Strategies](https://en.wikipedia.org/wiki/Load_balancing_(computing))

---

*Happy learning! 🚀*