package webapp.marginality2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.model.Status;
import webapp.marginality2.repository.ChickenReactiveRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChickenServiceImplTest {

    private ChickenReactiveRepository chickenRepository;
    private ChickenServiceImpl chickenService;

    @BeforeEach
    void setUp() {
        chickenRepository = Mockito.mock(ChickenReactiveRepository.class);
        chickenService = new ChickenServiceImpl(chickenRepository);
    }

    @Test
    void testCreateChicken() {
        Chicken chicken = new Chicken(1, "Free Range Chicken", 100, Status.EXPIRED, 50, null);
        when(chickenRepository.save(any(Chicken.class))).thenReturn(Mono.just(chicken));

        StepVerifier.create(chickenService.create(chicken))
            .expectNextMatches(savedChicken -> savedChicken.getName().equals("Free Range Chicken"))
            .verifyComplete();
    }

    @Test
    void testUpdateChicken() {
        Chicken existingChicken = new Chicken(1, "Old Chicken", 50, Status.EXPIRED, 30, null);
        Chicken updatedChicken = new Chicken(1, "Updated Chicken", 75, Status.EXPIRED, 25, null);
        when(chickenRepository.findById(existingChicken.getId())).thenReturn(Mono.just(existingChicken));
        when(chickenRepository.save(any(Chicken.class))).thenReturn(Mono.just(updatedChicken));

        StepVerifier.create(chickenService.update(existingChicken.getId(), updatedChicken))
            .expectNextMatches(newChicken -> 
                newChicken.getName().equals("Updated Chicken") && 
                newChicken.getCost() == 75 &&
                newChicken.getStatus() == Status.EXPIRED &&
                newChicken.getCount() == 25)
            .verifyComplete();
    }

    @Test
    void testFindById() {
        Chicken chicken = new Chicken(1, "Free Range Chicken", 100, Status.EXPIRED, 50, null);
        when(chickenRepository.findById(1)).thenReturn(Mono.just(chicken));

        StepVerifier.create(chickenService.findById(1))
            .expectNext(chicken)
            .verifyComplete();
    }

    @Test
    void testFindAllChickens() {
        Chicken chicken1 = new Chicken(1, "Chicken One", 80, Status.EXPIRED, 40, null);
        Chicken chicken2 = new Chicken(2, "Chicken Two", 90, Status.EXPIRED, 60, null);
        when(chickenRepository.findAll()).thenReturn(Flux.just(chicken1, chicken2));

        StepVerifier.create(chickenService.findAll())
            .expectNext(chicken1)
            .expectNext(chicken2)
            .verifyComplete();
    }

    @Test
    void testDeleteChickenById() {
        when(chickenRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(chickenService.deleteById(1))
            .verifyComplete();
    }
}
