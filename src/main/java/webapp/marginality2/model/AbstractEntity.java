package webapp.marginality2.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.LocalDate;

/**
 * AbstractEntity — это базовый класс для всех сущностей в приложении.
 * Он предоставляет общие поля, такие как id, cost, profit, count, name, status и date,
 * которые могут использоваться в наследуемых классах для минимизации дублирования кода.
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
@Data
@NoArgsConstructor
public abstract class AbstractEntity {

    @Id
    private int id;
    private int cost;
    private int profit;
    private int count;
    private String name;
    private Status status;
    private LocalDate date;

    /**
     * Конструктор с полным набором параметров для инициализации экземпляра сущности.
     *
     * @param id     Уникальный идентификатор для сущности
     * @param cost   Стоимость блюда или товара
     * @param profit Прибыль от продажи блюда или товара
     * @param count  Количество единиц товара или блюда
     * @param name   Название блюда или товара
     * @param status Статус товара или блюда (например, ПРОДАНО, НА СКЛАДЕ)
     * @param date   Дата, связанная с блюдом (например, дата продажи или производства)
     */
    protected AbstractEntity(int id, int cost, int profit, int count, String name, Status status, LocalDate date) {
        this.id = id;
        this.cost = cost;
        this.profit = profit;
        this.count = count;
        this.name = name;
        this.status = status;
        this.date = date;
    }

    /**
     * Переопределение метода equals для сравнения сущностей по полю id.
     *
     * @param o Объект для сравнения
     * @return true, если оба объекта одного типа и имеют одинаковый id, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id == that.id;
    }

    /**
     * Улучшенная версия метода hashCode для генерации уникального хеш-кода на основе всех полей объекта.
     *
     * @return Хеш-код, сгенерированный на основе всех ключевых полей
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
}
