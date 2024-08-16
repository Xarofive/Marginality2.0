package webapp.marginality2.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webapp.marginality2.model.Chicken;

public interface ChickenReactiveRepository extends ReactiveCrudRepository<Chicken, Integer> {
}
