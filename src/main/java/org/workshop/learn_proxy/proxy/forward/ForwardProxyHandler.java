package org.workshop.learn_proxy.proxy.forward;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

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
                                .headers(h -> h.addAll(clientResponse.headers().asHttpHeaders()))
                                .body(clientResponse.bodyToMono(String.class), String.class)
                );
    }
}
