package webapp.marginality2.model;

import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@NoArgsConstructor
@Table
public class Egg extends AbstractEntity {
    public Egg(int id, String name, int cost, boolean isForSale, int count, LocalDate date) {
        super(id, name, cost, isForSale, count, date);
    }
}
