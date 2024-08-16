package webapp.marginality2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meat;
import webapp.marginality2.repository.MeatReactiveRepository;

@Service
public class MeatServiceImpl implements MeatService {

    private final MeatReactiveRepository meatRepository;

    public MeatServiceImpl(MeatReactiveRepository meatRepository) {
        this.meatRepository = meatRepository;
    }

    @Override
    public Mono<Meat> create(Meat meat) {
        return meatRepository.save(meat);
    }

    @Override
    public Mono<Meat> update(Integer id, Meat meat) {
        return meatRepository.findById(id)
            .flatMap(existingMeat -> {
                existingMeat.setName(meat.getName());
                existingMeat.setCost(meat.getCost());
                existingMeat.setForSale(meat.isForSale());
                existingMeat.setCount(meat.getCount());
                existingMeat.setDate(meat.getDate());
                return meatRepository.save(existingMeat);
            });
    }

    @Override
    public Mono<Meat> findById(Integer id) {
        return meatRepository.findById(id);
    }

    @Override
    public Flux<Meat> findAll() {
        return meatRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return meatRepository.deleteById(id);
    }
}
