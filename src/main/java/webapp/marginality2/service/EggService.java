package webapp.marginality2.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Egg;

public interface EggService {
    Mono<Egg> create(Egg egg);
    Mono<Egg> update(Integer id, Egg egg);
    Mono<Egg> findById(Integer id);
    Flux<Egg> findAll();
    Mono<Void> deleteById(Integer id);
}
