package webapp.marginality2.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webapp.marginality2.model.Meat;

public interface MeatReactiveRepository extends ReactiveCrudRepository<Meat, Integer> {
}
