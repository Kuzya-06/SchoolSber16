package ru.sber.wiremock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.sber.wiremock.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {WiremocktestApplication.class})
@WireMockTest
class WiremocktestApplicationTests {

    @Autowired
    private WebTestClient webTestClient;
    private final ObjectMapper mapper = new ObjectMapper();


    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("works_base_url", wireMockExtension::baseUrl);
    }

    @Test
    public void testWorksSizeIsEmpty() {
        wireMockExtension.stubFor(
                WireMock.get("/worker")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[]"))
        );

        webTestClient.get().uri("api/v2/works/count")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"count\" : 0}");
    }

    @Test
    @SneakyThrows
    public void testProductsSizeIsNotEmpty() {
        List<Worker> workers = generateWorkers(15);
        String jsonProducts = mapper.writeValueAsString(workers);

        wireMockExtension.stubFor(
                WireMock.get("/worker")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(jsonProducts))
        );

        webTestClient.get().uri("/api/v2/works/count")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"count\" : 15}");
    }

    private List<Worker> generateWorkers(int count) {
        Random random = new Random();
        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Worker temp = Worker.builder()
                    .id(random.nextInt(4000))
                    .name("Имя-" + i)
                    .surName("Фамилия-" + i)
                    .address("Адресс-" + i)
                    .build();
            workers.add(temp);
        }
        return workers;
    }
}
