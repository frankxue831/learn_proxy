package org.workshop.learn_proxy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class BasicProxyController {

    private final WebClient webClient;

    public BasicProxyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/proxy/**")
    public Mono<ResponseEntity<String>> proxyRequest(ServerHttpRequest request,
                                                     @RequestParam String target) {
        String path = request.getPath().value().substring("/proxy".length());
        String targetUrl = target + path;

        return webClient.get()
                .uri(targetUrl)
                .headers(headers ->
                        request.getHeaders().forEach((key, values) -> {
                            if (!key.equalsIgnoreCase("host")) {
                                headers.addAll(key, values);
                            }
                        }))
                .retrieve()
                .toEntity(String.class);
    }
}
