package webapp.marginality2.model;

import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.Objects;

/**
 * AbstractEntity is a base class for entity definitions in the application.
 * It provides common fields and functionality to its subclasses.
 *
 * @Entity marks the class as a JPA entity.
 * @Data is a Lombok annotation to generate getters, setters, toString, equals, and hashCode methods automatically.
 * @NoArgsConstructor is a Lombok annotation that generates a no-argument constructor.
 * This constructor is required by JPA for entity classes.
 * Fields:
 * - id: Unique identifier for the entity.
 * - name: Name of the entity.
 * - cost: Cost associated with the entity.
 * - isForSale: Flag indicating whether the entity is available for sale.
 * - count: The quantity of the entity available.
 * - data: Large object data associated with the entity.
 * The class also overrides equals and hashCode methods to only consider the 'id' field,
 * which is the unique identifier for the entity.
 */
@lombok.Data
@NoArgsConstructor
public abstract class AbstractEntity {
    private int id;
    private String name;
    private int cost;
    private boolean isForSale;
    private int count;
    private Date date;

    protected AbstractEntity(int id, String name, int cost, boolean isForSale, int count, Date data) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.isForSale = isForSale;
        this.count = count;
        this.date = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
