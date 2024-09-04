package webapp.marginality2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.repository.MealReactiveRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MealServiceImplTest {

    private MealReactiveRepository chickenRepository;
    private MealServiceImpl chickenService;

    @BeforeEach
    void setUp() {
        chickenRepository = Mockito.mock(MealReactiveRepository.class);
        chickenService = new MealServiceImpl(chickenRepository);
    }

    @Test
    void testCreateChicken() {
        Meal meal = new Meal(1, "Free Range Chicken", 100, Status.EXPIRED, 50, null);
        when(chickenRepository.save(any(Meal.class))).thenReturn(Mono.just(meal));

        StepVerifier.create(chickenService.create(meal))
            .expectNextMatches(savedChicken -> savedChicken.getName().equals("Free Range Chicken"))
            .verifyComplete();
    }

    @Test
    void testUpdateChicken() {
        Meal existingMeal = new Meal(1, "Old Chicken", 50, Status.EXPIRED, 30, null);
        Meal updatedMeal = new Meal(1, "Updated Chicken", 75, Status.EXPIRED, 25, null);
        when(chickenRepository.findById(existingMeal.getId())).thenReturn(Mono.just(existingMeal));
        when(chickenRepository.save(any(Meal.class))).thenReturn(Mono.just(updatedMeal));

        StepVerifier.create(chickenService.update(existingMeal.getId(), updatedMeal))
            .expectNextMatches(newChicken -> 
                newChicken.getName().equals("Updated Chicken") && 
                newChicken.getCost() == 75 &&
                newChicken.getStatus() == Status.EXPIRED &&
                newChicken.getCount() == 25)
            .verifyComplete();
    }

    @Test
    void testFindById() {
        Meal meal = new Meal(1, "Free Range Chicken", 100, Status.EXPIRED, 50, null);
        when(chickenRepository.findById(1)).thenReturn(Mono.just(meal));

        StepVerifier.create(chickenService.findById(1))
            .expectNext(meal)
            .verifyComplete();
    }

    @Test
    void testFindAllChickens() {
        Meal meal1 = new Meal(1, "Chicken One", 80, Status.EXPIRED, 40, null);
        Meal meal2 = new Meal(2, "Chicken Two", 90, Status.EXPIRED, 60, null);
        when(chickenRepository.findAll()).thenReturn(Flux.just(meal1, meal2));

        StepVerifier.create(chickenService.findAll())
            .expectNext(meal1)
            .expectNext(meal2)
            .verifyComplete();
    }

    @Test
    void testDeleteChickenById() {
        when(chickenRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(chickenService.deleteById(1))
            .verifyComplete();
    }
}
