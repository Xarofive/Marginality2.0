package webapp.marginality2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Egg;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.EggService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = EggController.class)
class EggControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EggService eggService;

    @Test
    void testCreateEgg() {
        Egg egg = new Egg(1, "Golden Egg", 100, Status.EXPIRED, 10, null);

        when(eggService.create(any(Egg.class))).thenReturn(Mono.just(egg));

        webTestClient.post()
                .uri("/eggs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(egg)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Egg.class)
                .isEqualTo(egg);
    }

    @Test
    void testUpdateEgg() {
        Egg egg = new Egg(1, "Golden Egg", 150, Status.EXPIRED, 15, null);

        when(eggService.update(anyInt(), any(Egg.class))).thenReturn(Mono.just(egg));

        webTestClient.put()
                .uri("/eggs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(egg)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Egg.class)
                .isEqualTo(egg);
    }

    @Test
    void testGetEggById() {
        Egg egg = new Egg(1, "Golden Egg", 100, Status.EXPIRED, 10, null);

        when(eggService.findById(1)).thenReturn(Mono.just(egg));

        webTestClient.get()
                .uri("/eggs/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Egg.class)
                .isEqualTo(egg);
    }

    @Test
    void testGetAllEggs() {
        Egg egg1 = new Egg(1, "Golden Egg", 100, Status.EXPIRED, 10, null);
        Egg egg2 = new Egg(2, "Silver Egg", 80, Status.EXPIRED, 20, null);

        when(eggService.findAll()).thenReturn(Flux.just(egg1, egg2));

        webTestClient.get()
                .uri("/eggs")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Egg.class)
                .hasSize(2)
                .contains(egg1, egg2);
    }

    @Test
    void testDeleteEgg() {
        when(eggService.deleteById(1)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/eggs/1")
                .exchange()
                .expectStatus().isOk();
    }
}
