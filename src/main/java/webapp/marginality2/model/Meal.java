package webapp.marginality2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * Класс Meal представляет собой сущность, описывающую продукт питания.
 * Он наследуется от AbstractEntity и включает в себя информацию о стоимости, прибыли, количестве, имени, статусе и дате.
 *
 * @author Ivan Pshychenko
 *
 * @see AbstractEntity
 *
 * © 2024 Ivan Pshychenko ☁️
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table // Указывает на то, что этот класс сопоставляется с таблицей базы данных
@Data
public class Meal extends AbstractEntity {

    /**
     * Конструктор для создания объекта Meal с заданными параметрами.
     *
     * @param id     Идентификатор продукта
     * @param cost   Стоимость продукта
     * @param profit Прибыль от продажи продукта
     * @param count  Количество единиц продукта
     * @param name   Название продукта
     * @param status Статус продукта (например, продано, на хранении)
     * @param date   Дата, относящаяся к продукту (например, дата продажи или производства)
     */
    public Meal(int id, int cost, int profit, int count, String name, Status status, LocalDate date) {
        super(id, cost, profit, count, name, status, date);
    }
}
