package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.JavaScript;
import webapp.marginality2.model.Meal;
import webapp.marginality2.service.MealServiceImpl;

/**
 * MealView - основной класс представления (View) для отображения и работы с данными о еде.
 * Включает в себя таблицу, отображение времени и диаграммы, а также возможность добавления новых записей.
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
@Route("")
@JavaScript("https://cdn.jsdelivr.net/npm/chart.js")
public class MealView extends VerticalLayout {

    private final MealServiceImpl mealService;
    private final DateTimeDisplayComponent dateTimeDisplayComponent;
    private final MealDataComponent mealDataComponent;
    private final Button addMealButton;

    /**
     * Конструктор, инициализирующий компоненты и создающий основную структуру страницы.
     *
     * @param mealService Сервис для работы с данными о еде.
     */
    public MealView(MealServiceImpl mealService) {
        this.mealService = mealService;

        this.dateTimeDisplayComponent = new DateTimeDisplayComponent();
        this.mealDataComponent = new MealDataComponent(mealService);

        this.addMealButton = new Button("Добавить новую запись", event -> openMealDialog(null));

        HorizontalLayout dateTimeAndChartsLayout = new HorizontalLayout();
        dateTimeAndChartsLayout.setWidthFull();
        dateTimeAndChartsLayout.add(dateTimeDisplayComponent.getLayout(), mealDataComponent.getChartsLayout());

        add(addMealButton, mealDataComponent, dateTimeAndChartsLayout, createFooter());
    }

    /**
     * Метод для создания нижнего колонтитула (footer) с авторской информацией.
     *
     * @return Компонент Div, содержащий текст нижнего колонтитула.
     */
    private Div createFooter() {
        Div footer = new Div();
        footer.getStyle().set("text-align", "center");
        footer.getStyle().set("margin-top", "20px");
        footer.getStyle().set("padding", "10px");
        footer.getStyle().set("background-color", "#f5f5f5");
        footer.getStyle().set("width", "100%");

        footer.setText("© 2024 Ivan Pshychenko ☁️ | Powered by Vaadin");
        return footer;
    }

    /**
     * Открывает диалоговое окно для добавления новой записи о еде.
     * Если передан Meal, данные заполняются для редактирования.
     *
     * @param meal Объект Meal для редактирования. Если null, создается новая запись.
     */
    private void openMealDialog(Meal meal) {
        MealDialog dialog = new MealDialog(mealService, mealDataComponent);
        dialog.openForMeal(meal);
    }
}
