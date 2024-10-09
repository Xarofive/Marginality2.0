package webapp.marginality2.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.datepicker.DatePicker;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс DateTimeDisplayComponent отвечает за отображение текущего времени и даты в пользовательском интерфейсе.
 * Он автоматически обновляет время каждую секунду и предоставляет пользователю возможность выбрать сегодняшнюю дату.
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
public class DateTimeDisplayComponent {

    private Span currentTime = new Span();

    private DatePicker currentDate = new DatePicker();

    @Getter
    private HorizontalLayout layout = new HorizontalLayout();

    /**
     * Конструктор по умолчанию. Выполняет конфигурацию отображения текущего времени и даты.
     */
    public DateTimeDisplayComponent() {
        configureDateTimeDisplay();
    }

    /**
     * Метод конфигурации компонентов отображения даты и времени.
     * Устанавливает стиль для компонента времени, инициализирует поле выбора даты текущей датой,
     * а также запускает автоматическое обновление времени каждую секунду.
     */
    private void configureDateTimeDisplay() {
        currentTime.getStyle().set("font-size", "30px");

        currentDate.setLabel("Сегодняшняя дата");
        currentDate.setValue(LocalDateTime.now().toLocalDate());

        layout.add(currentTime, currentDate);

        UI ui = UI.getCurrent();
        ui.setPollInterval(2000);
        ui.addPollListener(pollEvent -> {
            String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            currentTime.setText("Текущее время: " + formattedTime);
        });
    }
}
