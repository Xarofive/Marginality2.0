package webapp.marginality2.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webapp.marginality2.model.Meal;

/**
 * MealReactiveRepository — это репозиторий для работы с сущностью Meal
 * с использованием реактивного подхода на базе Spring Data R2DBC.
 *
 * Интерфейс расширяет ReactiveCrudRepository, предоставляя базовые CRUD-операции
 * для сущности Meal.
 *
 * Основное назначение — взаимодействие с базой данных для создания, обновления,
 * удаления и получения записей сущности Meal.
 *
 * @author Ivan Pshychenko
 * @see ReactiveCrudRepository
 *
 * © 2024 Иван Пщеченко ☁️
 */
public interface MealReactiveRepository extends ReactiveCrudRepository<Meal, Integer> {
}
