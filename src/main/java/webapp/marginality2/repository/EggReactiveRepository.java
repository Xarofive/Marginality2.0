package webapp.marginality2.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webapp.marginality2.model.Egg;

public interface EggReactiveRepository extends ReactiveCrudRepository<Egg, Integer> {
}
