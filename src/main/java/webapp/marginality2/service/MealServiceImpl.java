package webapp.marginality2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meal;
import webapp.marginality2.repository.MealReactiveRepository;

/**
 * Реализация сервиса для работы с сущностями Meal.
 * Этот класс предоставляет основные CRUD-операции, такие как создание, обновление, получение и удаление записей Meal.
 * Логирование осуществляется с помощью Lombok аннотации @Slf4j для упрощения отладки.
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
@Slf4j
@Service
public class MealServiceImpl implements MealService {

    private final MealReactiveRepository mealReactiveRepository;

    /**
     * Конструктор класса MealServiceImpl.
     *
     * @param mealReactiveRepository реактивный репозиторий для работы с сущностями Meal
     */
    public MealServiceImpl(MealReactiveRepository mealReactiveRepository) {
        this.mealReactiveRepository = mealReactiveRepository;
    }

    /**
     * Создает новую сущность Meal.
     *
     * @param meal объект Meal для создания
     * @return Mono<Meal> с созданным объектом
     */
    @Override
    public Mono<Meal> create(Meal meal) {
        log.info("Creating meal: {}", meal);
        return mealReactiveRepository.save(meal);
    }

    /**
     * Обновляет существующую сущность Meal.
     *
     * @param id   идентификатор обновляемой сущности Meal
     * @param meal объект Meal с новыми данными
     * @return Mono<Meal> с обновленным объектом
     */
    @Override
    public Mono<Meal> update(Integer id, Meal meal) {
        log.info("Updating meal with id: {}", id);
        return findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Meal not found with id: " + id)))
                .doOnError(error -> log.error("Error updating meal: {}", error.getMessage()))
                .flatMap(existingMeal -> {
                    // Обновляем поля существующей записи
                    existingMeal.setName(meal.getName());
                    existingMeal.setCost(meal.getCost());
                    existingMeal.setProfit(meal.getProfit());
                    existingMeal.setStatus(meal.getStatus());
                    existingMeal.setCount(meal.getCount());
                    existingMeal.setDate(meal.getDate());
                    log.info("Saving updated meal: {}", existingMeal);
                    return mealReactiveRepository.save(existingMeal);
                });
    }

    /**
     * Находит Meal по идентификатору.
     *
     * @param id идентификатор искомой сущности Meal
     * @return Mono<Meal> с найденной сущностью
     */
    @Override
    public Mono<Meal> findById(Integer id) {
        log.info("Finding meal with id: {}", id);
        return mealReactiveRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Meal not found with id: " + id)))
                .doOnNext(meal -> log.info("Found meal: {}", meal))
                .doOnError(error -> log.error("Error finding meal: {}", error.getMessage()));
    }

    /**
     * Находит все Meal записи.
     *
     * @return Flux<Meal> поток найденных записей Meal
     */
    @Override
    public Flux<Meal> findAll() {
        log.info("Finding all meals");
        return mealReactiveRepository.findAll()
                .doOnNext(meal -> log.info("Found meal: {}", meal));
    }

    /**
     * Удаляет Meal по идентификатору.
     *
     * @param id идентификатор удаляемой сущности Meal
     * @return Mono<Void> сигнал о завершении операции
     */
    @Override
    public Mono<Void> deleteById(Integer id) {
        log.info("Deleting meal with id: {}", id);
        return mealReactiveRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Meal not found with id: " + id)))
                .flatMap(meal -> mealReactiveRepository.deleteById(id))
                .doOnSuccess(unused -> log.info("Deleted meal with id: {}", id))
                .doOnError(error -> log.error("Error deleting meal: {}", error.getMessage()));
    }
}
