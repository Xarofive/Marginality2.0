package webapp.marginality2.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Value;

/**
 * DatabaseConfig — это конфигурационный класс, отвечающий за настройку подключения к базе данных
 * с использованием R2DBC (Reactive Relational Database Connectivity).
 *
 * Данный класс использует параметры подключения, такие как URL, имя пользователя и пароль,
 * которые загружаются из свойств конфигурации приложения.
 *
 *  @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String url;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    /**
     * Метод для создания и настройки фабрики подключений к базе данных.
     * Параметры подключения извлекаются из свойств конфигурации приложения.
     *
     * @return ConnectionFactory — фабрика для создания реактивных подключений к базе данных.
     */
    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(url)
                .mutate()
                .option(ConnectionFactoryOptions.USER, username)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .build();
        return ConnectionFactories.get(options);
    }

    /**
     * Метод для создания менеджера транзакций, который будет управлять реактивными транзакциями.
     *
     * @param connectionFactory фабрика подключений, используемая для создания транзакций.
     * @return ReactiveTransactionManager — менеджер для обработки транзакций в реактивном стиле.
     */
    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory); // Возвращаем менеджер транзакций
    }
}
