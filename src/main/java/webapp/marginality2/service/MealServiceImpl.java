package webapp.marginality2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meal;
import webapp.marginality2.repository.MealReactiveRepository;

@Service
public class MealServiceImpl implements MealService {


    private final MealReactiveRepository chickenRepository;


    public MealServiceImpl(MealReactiveRepository chickenRepository) {
        this.chickenRepository = chickenRepository;
    }


    @Override
    public Mono<Meal> create(Meal meal) {
        return chickenRepository.save(meal);
    }

    @Override
    public Mono<Meal> update(Integer id, Meal meal) {
        return chickenRepository.findById(id)
                .flatMap(existingChicken -> {
                    existingChicken.setName(meal.getName());
                    existingChicken.setCost(meal.getCost());
                    existingChicken.setStatus(meal.getStatus());
                    existingChicken.setCount(meal.getCount());
                    existingChicken.setDate(meal.getDate());
                    return chickenRepository.save(existingChicken);
                });
    }

    @Override
    public Mono<Meal> findById(Integer id) {
        return chickenRepository.findById(id);
    }

    @Override
    public Flux<Meal> findAll() {
        return chickenRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return chickenRepository.deleteById(id);
    }
}
