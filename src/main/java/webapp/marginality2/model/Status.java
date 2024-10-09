package webapp.marginality2.model;

/**
 * Перечисление статусов для сущности Meal.
 * Статусы отражают текущее состояние продукта, такого как "продано", "истек срок годности", "на продаже", "на хранении", и "повреждено".
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
public enum Status {
    SOLD,
    EXPIRED,
    FOR_SALE,
    STORAGE,
    DAMAGED
}
