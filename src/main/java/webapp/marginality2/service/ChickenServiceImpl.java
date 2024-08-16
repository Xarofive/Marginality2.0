package webapp.marginality2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.repository.ChickenReactiveRepository;

@Service
public class ChickenServiceImpl implements ChickenService {


    private final ChickenReactiveRepository chickenRepository;


    public ChickenServiceImpl(ChickenReactiveRepository chickenRepository) {
        this.chickenRepository = chickenRepository;
    }


    @Override
    public Mono<Chicken> create(Chicken chicken) {
        return chickenRepository.save(chicken);
    }

    @Override
    public Mono<Chicken> update(Integer id, Chicken chicken) {
        return chickenRepository.findById(id)
                .flatMap(existingChicken -> {
                    existingChicken.setName(chicken.getName());
                    existingChicken.setCost(chicken.getCost());
                    existingChicken.setForSale(chicken.isForSale());
                    existingChicken.setCount(chicken.getCount());
                    existingChicken.setDate(chicken.getDate());
                    return chickenRepository.save(existingChicken);
                });
    }

    @Override
    public Mono<Chicken> findById(Integer id) {
        return chickenRepository.findById(id);
    }

    @Override
    public Flux<Chicken> findAll() {
        return chickenRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return chickenRepository.deleteById(id);
    }
}
