package org.workshop.learn_proxy.proxy.reverse;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
