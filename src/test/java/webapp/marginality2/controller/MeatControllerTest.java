package webapp.marginality2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meat;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.MeatService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MeatController.class)
class MeatControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MeatService meatService;

    @Test
    void testCreateMeat() {
        Meat meat = new Meat(1, "Beef", 200, Status.EXPIRED, 5, null);

        when(meatService.create(any(Meat.class))).thenReturn(Mono.just(meat));

        webTestClient.post()
                .uri("/meats")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meat)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Meat.class)
                .isEqualTo(meat);
    }

    @Test
    void testUpdateMeat() {
        Meat meat = new Meat(1, "Pork", 250, Status.STORAGE, 8, null);

        when(meatService.update(anyInt(), any(Meat.class))).thenReturn(Mono.just(meat));

        webTestClient.put()
                .uri("/meats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meat)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Meat.class)
                .isEqualTo(meat);
    }

    @Test
    void testGetMeatById() {
        Meat meat = new Meat(1, "Beef", 200, Status.FOR_SALE, 5, null);

        when(meatService.findById(1)).thenReturn(Mono.just(meat));

        webTestClient.get()
                .uri("/meats/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Meat.class)
                .isEqualTo(meat);
    }

    @Test
    void testGetAllMeats() {
        Meat meat1 = new Meat(1, "Beef", 200, Status.EXPIRED, 5, null);
        Meat meat2 = new Meat(2, "Pork", 150, Status.EXPIRED, 7, null);

        when(meatService.findAll()).thenReturn(Flux.just(meat1, meat2));

        webTestClient.get()
                .uri("/meats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Meat.class)
                .hasSize(2)
                .contains(meat1, meat2);
    }

    @Test
    void testDeleteMeat() {
        when(meatService.deleteById(1)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/meats/1")
                .exchange()
                .expectStatus().isOk();
    }
}
