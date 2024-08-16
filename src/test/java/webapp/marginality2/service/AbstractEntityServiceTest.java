package webapp.marginality2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import webapp.marginality2.model.Egg;
import webapp.marginality2.repository.EggReactiveRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AbstractEntityServiceTest {

    private EggReactiveRepository eggRepository;
    private EggServiceImpl eggService;

    @BeforeEach
    void setUp() {
        eggRepository = Mockito.mock(EggReactiveRepository.class);
        eggService = new EggServiceImpl(eggRepository);
    }

    @Test
    void testCreateEgg() {
        Egg egg = new Egg(1, "Fresh Egg", 20, true, 100, null);
        when(eggRepository.save(any(Egg.class))).thenReturn(Mono.just(egg));

        StepVerifier.create(eggService.create(egg))
                .expectNextMatches(savedEgg -> savedEgg.getName().equals("Fresh Egg"))
                .verifyComplete();
    }

    @Test
    void testUpdateEgg() {
        Egg existingEgg = new Egg(1, "Old Egg", 15, true, 50, null);
        Egg updatedEgg = new Egg(1, "Updated Egg", 25, false, 75, null);
        when(eggRepository.findById(existingEgg.getId())).thenReturn(Mono.just(existingEgg));
        when(eggRepository.save(any(Egg.class))).thenReturn(Mono.just(updatedEgg));

        StepVerifier.create(eggService.update(existingEgg.getId(), updatedEgg))
                .expectNextMatches(newEgg -> newEgg.getName().equals("Updated Egg") && !newEgg.isForSale() && newEgg.getCount() == 75)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        Egg egg = new Egg(1, "Fresh Egg", 20, true, 100, null);
        when(eggRepository.findById(1)).thenReturn(Mono.just(egg));

        StepVerifier.create(eggService.findById(1))
                .expectNext(egg)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        Egg egg1 = new Egg(1, "Egg One", 10, true, 30, null);
        Egg egg2 = new Egg(2, "Egg Two", 15, false, 40, null);
        when(eggRepository.findAll()).thenReturn(Flux.just(egg1, egg2));

        StepVerifier.create(eggService.findAll())
                .expectNext(egg1)
                .expectNext(egg2)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(eggRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(eggService.deleteById(1))
                .verifyComplete();
    }
}
