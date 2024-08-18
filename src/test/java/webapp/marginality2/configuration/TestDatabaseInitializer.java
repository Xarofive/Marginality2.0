package webapp.marginality2.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.r2dbc.core.DatabaseClient;

import jakarta.annotation.PostConstruct;

@TestConfiguration
public class TestDatabaseInitializer {

    @Bean
    public DatabaseInitializer databaseInitializer(DatabaseClient client) {
        return new DatabaseInitializer(client);
    }

    public static class DatabaseInitializer {

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
}
