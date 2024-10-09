package webapp.marginality2.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meal;

/**
 * MealService — это интерфейс, который определяет основные операции для работы с сущностью Meal.
 *
 * Включает в себя методы для создания, обновления, получения и удаления записей, а также для
 * получения всех записей с использованием реактивных типов Mono и Flux.
 *
 * Основное назначение — определение контрактов для бизнес-логики, связанных с управлением Meal.
 * Реактивные типы Mono и Flux позволяют эффективно обрабатывать асинхронные операции.
 *
 * @author Ivan Pshychenko
 * @version 1.0
 * @see Meal
 * @see Mono
 * @see Flux
 *
 * © 2024 Ivan Pshychenko ☁️
 */
public interface MealService {

    /**
     * Создает новую запись Meal в системе.
     *
     * @param meal объект Meal для создания
     * @return Mono с созданной записью Meal
     */
    Mono<Meal> create(Meal meal);

    /**
     * Обновляет существующую запись Meal по идентификатору.
     *
     * @param id идентификатор записи Meal, которую нужно обновить
     * @param meal объект Meal с обновленными данными
     * @return Mono с обновленной записью Meal
     */
    Mono<Meal> update(Integer id, Meal meal);

    /**
     * Ищет запись Meal по идентификатору.
     *
     * @param id идентификатор записи Meal для поиска
     * @return Mono с найденной записью Meal или ошибкой, если запись не найдена
     */
    Mono<Meal> findById(Integer id);

    /**
     * Возвращает все записи Meal из системы.
     *
     * @return Flux со списком всех записей Meal
     */
    Flux<Meal> findAll();

    /**
     * Удаляет запись Meal по идентификатору.
     *
     * @param id идентификатор записи Meal, которую нужно удалить
     * @return Mono<Void>, сигнализирующий об успешном удалении или ошибке
     */
    Mono<Void> deleteById(Integer id);
}
