package webapp.marginality2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.repository.MealReactiveRepository;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MealServiceImplTest {

    @Mock
    private MealReactiveRepository mealReactiveRepository;

    @InjectMocks
    private MealServiceImpl mealService;

    private Meal meal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meal = new Meal(1, "Pizza", 10, Status.FOR_SALE, 5, LocalDate.now());
    }

    @Test
    void testCreateMeal() {
        when(mealReactiveRepository.save(any(Meal.class))).thenReturn(Mono.just(meal));

        StepVerifier.create(mealService.create(meal))
                .expectNext(meal)
                .verifyComplete();

        verify(mealReactiveRepository, times(1)).save(meal);
    }

    @Test
    void testFindByIdMealExists() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.just(meal));

        StepVerifier.create(mealService.findById(1))
                .expectNext(meal)
                .verifyComplete();

        verify(mealReactiveRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdMealNotFound() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(mealService.findById(1))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Meal not found with id: 1"))
                .verify();

        verify(mealReactiveRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateMealExists() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.just(meal));
        when(mealReactiveRepository.save(any(Meal.class))).thenReturn(Mono.just(meal));

        Meal updatedMeal = new Meal(1, "Updated Pizza", 12, Status.FOR_SALE, 3, LocalDate.now());

        StepVerifier.create(mealService.update(1, updatedMeal))
                .expectNext(meal)
                .verifyComplete();

        verify(mealReactiveRepository, times(1)).findById(1);
        verify(mealReactiveRepository, times(1)).save(meal);
    }

    @Test
    void testUpdateMealNotFound() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.empty());

        Meal updatedMeal = new Meal(1, "Updated Pizza", 12, Status.FOR_SALE, 3, LocalDate.now());

        StepVerifier.create(mealService.update(1, updatedMeal))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Meal not found with id: 1"))
                .verify();

        verify(mealReactiveRepository, times(1)).findById(1);
        verify(mealReactiveRepository, times(0)).save(any(Meal.class));
    }

    @Test
    void testFindAllMeals() {
        when(mealReactiveRepository.findAll()).thenReturn(Flux.just(meal));

        StepVerifier.create(mealService.findAll())
                .expectNext(meal)
                .verifyComplete();

        verify(mealReactiveRepository, times(1)).findAll();
    }

    @Test
    void testDeleteByIdMealExists() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.just(meal));
        when(mealReactiveRepository.deleteById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(mealService.deleteById(1))
                .verifyComplete();

        verify(mealReactiveRepository, times(1)).findById(1);
        verify(mealReactiveRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteByIdMealNotFound() {
        when(mealReactiveRepository.findById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(mealService.deleteById(1))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Meal not found with id: 1"))
                .verify();

        verify(mealReactiveRepository, times(1)).findById(1);
        verify(mealReactiveRepository, times(0)).deleteById(1);
    }
}
