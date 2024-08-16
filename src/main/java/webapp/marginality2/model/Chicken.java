package webapp.marginality2.model;

import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class Chicken extends AbstractEntity {
    public Chicken(int id, String name, int cost, boolean isForSale, int count, Date date) {
        super(id, name, cost, isForSale, count, date);
    }
}
