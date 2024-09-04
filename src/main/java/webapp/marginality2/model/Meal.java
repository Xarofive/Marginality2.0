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
    public Meal(int id, String name, int cost, Status status, int count, LocalDate date) {
        super(id, name, cost, status, count, date);
    }
}
