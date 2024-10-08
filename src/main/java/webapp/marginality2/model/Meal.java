package webapp.marginality2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table
@Data
public class Meal extends AbstractEntity {
    public Meal(int id, int cost, int profit, int count, String name, Status status, LocalDate date) {
        super(id, cost, profit, count, name, status, date);
    }
}
