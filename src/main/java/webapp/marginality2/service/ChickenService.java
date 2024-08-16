package webapp.marginality2.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Chicken;

public interface ChickenService {
    Mono<Chicken> create(Chicken chicken);
    Mono<Chicken> update(Integer id, Chicken chicken);
    Mono<Chicken> findById(Integer id);
    Flux<Chicken> findAll();
    Mono<Void> deleteById(Integer id);
}
