package webapp.marginality2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.config.DatabaseConfig;
import webapp.marginality2.configuration.TestDatabaseInitializer;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.service.ChickenService;

import java.time.LocalDate;

@WebFluxTest(controllers = ChickenController.class)
@Import({TestDatabaseInitializer.class, DatabaseConfig.class})
class ChickenControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ChickenService chickenService;

    private Chicken sampleChicken;

    @BeforeEach
    void setUp() {
        sampleChicken = new Chicken(0, "Sample Chicken", 100, true, 10, LocalDate.now());
    }

    @Test
    void testCreateChicken() {
        Chicken createdChicken = new Chicken(1, "Sample Chicken", 100, true, 10, LocalDate.now());

        Mockito.when(chickenService.create(Mockito.any(Chicken.class))).thenReturn(Mono.just(createdChicken));

        webTestClient.post()
                .uri("/chickens")
                .bodyValue(sampleChicken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sample Chicken")
                .jsonPath("$.cost").isEqualTo(100);
    }

    @Test
    void testUpdateChicken() {
        Chicken updatedChicken = new Chicken(1, "Sample Chicken Updated", 150, false, 5, LocalDate.now());

        Mockito.when(chickenService.update(Mockito.eq(1), Mockito.any(Chicken.class))).thenReturn(Mono.just(updatedChicken));

        webTestClient.put()
                .uri("/chickens/1")
                .bodyValue(sampleChicken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sample Chicken Updated")
                .jsonPath("$.cost").isEqualTo(150);
    }

    @Test
    void testGetChickenById() {
        Chicken chicken = new Chicken(1, "Sample Chicken", 100, true, 10, LocalDate.now());

        Mockito.when(chickenService.findById(1)).thenReturn(Mono.just(chicken));

        webTestClient.get()
                .uri("/chickens/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)  // Проверяем, что id = 1
                .jsonPath("$.name").isEqualTo("Sample Chicken")
                .jsonPath("$.cost").isEqualTo(100);
    }

    @Test
    void testGetAllChickens() {
        Chicken chicken1 = new Chicken(1, "Sample Chicken", 100, true, 10, LocalDate.now());
        Chicken chicken2 = new Chicken(2, "Another Chicken", 150, false, 5, LocalDate.now());

        Mockito.when(chickenService.findAll()).thenReturn(Flux.just(chicken1, chicken2));

        webTestClient.get()
                .uri("/chickens")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(1)  // Проверяем, что id = 1
                .jsonPath("$[1].id").isEqualTo(2); // Проверяем, что id = 2
    }

    @Test
    void testDeleteChicken() {
        Mockito.when(chickenService.deleteById(1)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/chickens/1")
                .exchange()
                .expectStatus().isOk();
    }
}