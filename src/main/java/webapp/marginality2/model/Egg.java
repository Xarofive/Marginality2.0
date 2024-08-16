package webapp.marginality2.model;

import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class Egg extends AbstractEntity {
    public Egg(int id, String name, int cost, boolean isForSale, int count, Date date) {
        super(id, name, cost, isForSale, count, date);
    }
}
