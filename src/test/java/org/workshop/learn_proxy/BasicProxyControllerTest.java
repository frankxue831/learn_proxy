package org.workshop.learn_proxy;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicProxyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    private MockWebServer backend;

    @BeforeEach
    void setUp() throws IOException {
        backend = new MockWebServer();
        backend.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        backend.shutdown();
    }

    @Test
    void proxyForwardsRequestAndReturnsBody() throws Exception {
        backend.enqueue(new MockResponse().setBody("from backend").setResponseCode(200));

        String target = backend.url("/").toString();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/proxy/test")
                        .queryParam("target", target)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("from backend");

        RecordedRequest recorded = backend.takeRequest();
        assertThat(recorded.getPath()).isEqualTo("/test");
    }

    @Test
    void proxyForwardsResponseHeaders() throws Exception {
        backend.enqueue(new MockResponse()
                .setBody("body")
                .addHeader("X-Test", "value"));

        String target = backend.url("/").toString();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/proxy/hello")
                        .queryParam("target", target)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-Test", "value")
                .expectBody(String.class).isEqualTo("body");

        RecordedRequest recorded = backend.takeRequest();
        assertThat(recorded.getPath()).isEqualTo("/hello");
    }
}
