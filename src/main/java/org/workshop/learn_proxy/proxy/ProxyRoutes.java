package org.workshop.learn_proxy.proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.workshop.learn_proxy.proxy.forward.ForwardProxyHandler;
import org.workshop.learn_proxy.proxy.reverse.ReverseProxyHandler;

@Configuration
public class ProxyRoutes {

    private final ForwardProxyHandler forwardProxyHandler;
    private final ReverseProxyHandler reverseProxyHandler;

    public ProxyRoutes(ForwardProxyHandler forwardProxyHandler,
                       ReverseProxyHandler reverseProxyHandler) {
        this.forwardProxyHandler = forwardProxyHandler;
        this.reverseProxyHandler = reverseProxyHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> proxyRouter() {
        return RouterFunctions.route()
                .path("/proxy/forward", builder ->
                        builder.route(RequestPredicates.all(), forwardProxyHandler::handleRequest))
                .path("/proxy/reverse", builder ->
                        builder.route(RequestPredicates.all(), reverseProxyHandler::handleRequest))
                .build();
    }
}
