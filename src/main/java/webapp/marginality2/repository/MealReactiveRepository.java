package webapp.marginality2.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webapp.marginality2.model.Meal;

public interface MealReactiveRepository extends ReactiveCrudRepository<Meal, Integer> {
}
