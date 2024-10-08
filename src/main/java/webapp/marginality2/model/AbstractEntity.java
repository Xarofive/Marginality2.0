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
    private int cost;
    private int profit;
    private int count;
    private String name;
    private Status status;
    private LocalDate date;

    protected AbstractEntity(int id, int cost, int profit, int count, String name, Status status, LocalDate date) {
        this.id = id;
        this.cost = cost;
        this.profit = profit;
        this.count = count;
        this.name = name;
        this.status = status;
        this.date = date;
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
