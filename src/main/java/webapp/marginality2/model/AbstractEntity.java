package webapp.marginality2.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.time.LocalDate;
import java.util.Objects;

/**
 * AbstractEntity is a base class for entity definitions in the application.
 * It provides common fields and functionality to its subclasses.
 */
@lombok.Data
@NoArgsConstructor
public abstract class AbstractEntity {
    @Id
    private int id;
    private String name;
    private int cost;
    private boolean isForSale;
    private int count;
    private LocalDate date;

    protected AbstractEntity(int id, String name, int cost, boolean isForSale, int count, LocalDate data) {
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
