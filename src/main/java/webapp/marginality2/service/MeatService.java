package webapp.marginality2.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meat;

public interface MeatService {
    Mono<Meat> create(Meat meat);
    Mono<Meat> update(Integer id, Meat meat);
    Mono<Meat> findById(Integer id);
    Flux<Meat> findAll();
    Mono<Void> deleteById(Integer id);
}
