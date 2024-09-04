package webapp.marginality2.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meal;

public interface MealService {
    Mono<Meal> create(Meal meal);
    Mono<Meal> update(Integer id, Meal meal);
    Mono<Meal> findById(Integer id);
    Flux<Meal> findAll();
    Mono<Void> deleteById(Integer id);
}
