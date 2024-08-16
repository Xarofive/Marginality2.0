package webapp.marginality2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Egg;
import webapp.marginality2.repository.EggReactiveRepository;

@Service
public class EggServiceImpl implements EggService {
    private final EggReactiveRepository eggRepository;

    public EggServiceImpl(EggReactiveRepository eggRepository) {
        this.eggRepository = eggRepository;
    }

    @Override
    public Mono<Egg> create(Egg egg) {
        return eggRepository.save(egg);
    }

    @Override
    public Mono<Egg> update(Integer id, Egg egg) {
        return eggRepository.findById(id)
                .flatMap(existingEgg -> {
                    existingEgg.setName(egg.getName());
                    existingEgg.setCost(egg.getCost());
                    existingEgg.setForSale(egg.isForSale());
                    existingEgg.setCount(egg.getCount());
                    existingEgg.setDate(egg.getDate());
                    return eggRepository.save(existingEgg);
                });
    }

    @Override
    public Mono<Egg> findById(Integer id) {
        return eggRepository.findById(id);
    }

    @Override
    public Flux<Egg> findAll() {
        return eggRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return eggRepository.deleteById(id);
    }
}
