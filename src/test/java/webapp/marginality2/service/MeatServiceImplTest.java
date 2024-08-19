package webapp.marginality2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import webapp.marginality2.model.Meat;
import webapp.marginality2.model.Status;
import webapp.marginality2.repository.MeatReactiveRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MeatServiceImplTest {

    private MeatReactiveRepository meatRepository;
    private MeatServiceImpl meatService;

    @BeforeEach
    void setUp() {
        meatRepository = Mockito.mock(MeatReactiveRepository.class);
        meatService = new MeatServiceImpl(meatRepository);
    }

    @Test
    void testCreateMeat() {
        Meat meat = new Meat(1, "Beef", 200, Status.FOR_SALE, 50, null);
        when(meatRepository.save(any(Meat.class))).thenReturn(Mono.just(meat));

        StepVerifier.create(meatService.create(meat))
            .expectNextMatches(savedMeat -> savedMeat.getName().equals("Beef"))
            .verifyComplete();
    }

    @Test
    void testUpdateMeat() {
        Meat existingMeat = new Meat(1, "Pork", 150, Status.STORAGE, 40, null);
        Meat updatedMeat = new Meat(1, "Updated Pork", 180, Status.STORAGE, 35, null);
        when(meatRepository.findById(existingMeat.getId())).thenReturn(Mono.just(existingMeat));
        when(meatRepository.save(any(Meat.class))).thenReturn(Mono.just(updatedMeat));

        StepVerifier.create(meatService.update(existingMeat.getId(), updatedMeat))
            .expectNextMatches(newMeat ->
                newMeat.getName().equals("Updated Pork") &&
                newMeat.getCost() == 180 &&
                newMeat.getStatus() == Status.STORAGE &&
                newMeat.getCount() == 35)
            .verifyComplete();
    }

    @Test
    void testFindById() {
        Meat meat = new Meat(1, "Beef", 200, Status.FOR_SALE, 50, null);
        when(meatRepository.findById(1)).thenReturn(Mono.just(meat));

        StepVerifier.create(meatService.findById(1))
            .expectNext(meat)
            .verifyComplete();
    }

    @Test
    void testFindAllMeats() {
        Meat meat1 = new Meat(1, "Beef", 200, Status.EXPIRED, 50, null);
        Meat meat2 = new Meat(2, "Chicken", 100, Status.STORAGE, 75, null);
        when(meatRepository.findAll()).thenReturn(Flux.just(meat1, meat2));

        StepVerifier.create(meatService.findAll())
            .expectNext(meat1)
            .expectNext(meat2)
            .verifyComplete();
    }

    @Test
    void testDeleteMeatById() {
        when(meatRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(meatService.deleteById(1))
            .verifyComplete();
    }
}
