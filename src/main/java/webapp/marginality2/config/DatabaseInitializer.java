package webapp.marginality2.config;

import jakarta.annotation.PostConstruct;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;


@Component
public class DatabaseInitializer {

    private final DatabaseClient client;

    public DatabaseInitializer(DatabaseClient client) {
        this.client = client;
    }

    @PostConstruct
    public void initialize() {
        client.sql("CREATE TABLE IF NOT EXISTS CHICKEN (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255), " +
                "COST INT, " +
                "IS_FOR_SALE BOOLEAN, " +
                "COUNT INT, " +
                "DATE DATE)")
            .fetch()
            .rowsUpdated()
            .then()
            .subscribe();
    }
}
