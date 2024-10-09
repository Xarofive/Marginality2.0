package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.MealServiceImpl;

/**
 * Диалоговое окно для добавления или редактирования записи о еде (Meal).
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
public class MealDialog extends Dialog {

    private final MealServiceImpl mealService;
    private final MealDataComponent mealDataComponent;

    // Поля формы
    private final TextField nameField = new TextField("Название");
    private final TextField costField = new TextField("Стоимость");
    private final TextField profitField = new TextField("Прибыль");
    private final TextField countField = new TextField("Количество");
    private final ComboBox<Status> statusComboBox = new ComboBox<>("Статус", Status.values());
    private final DatePicker datePicker = new DatePicker("Дата");

    private Meal meal;  // Текущий объект Meal для редактирования или создания

    /**
     * Конструктор диалогового окна. Инициализирует поля формы и кнопки.
     *
     * @param mealService Сервис для работы с данными о еде.
     * @param mealDataComponent Компонент для обновления таблицы после сохранения данных.
     */
    public MealDialog(MealServiceImpl mealService, MealDataComponent mealDataComponent) {
        this.mealService = mealService;
        this.mealDataComponent = mealDataComponent;

        configureFormFields();

        Button saveButton = new Button("Сохранить", event -> saveMeal());
        Button cancelButton = new Button("Отменить", event -> close());

        add(nameField, costField, profitField, countField, statusComboBox, datePicker, saveButton, cancelButton);
    }

    /**
     * Открывает диалоговое окно для редактирования или создания новой записи.
     *
     * @param meal Объект Meal для редактирования. Если null, создается новая запись.
     */
    public void openForMeal(Meal meal) {
        this.meal = meal;

        if (meal != null) {
            nameField.setValue(meal.getName());
            costField.setValue(String.valueOf(meal.getCost()));
            profitField.setValue(String.valueOf(meal.getProfit()));
            countField.setValue(String.valueOf(meal.getCount()));
            statusComboBox.setValue(meal.getStatus());
            datePicker.setValue(meal.getDate());
        }

        open();
    }

    /**
     * Настраивает поля формы: добавляет подсказки и значения по умолчанию.
     */
    private void configureFormFields() {
        nameField.setPlaceholder("Введите название...");
        costField.setPlaceholder("Введите стоимость...");
        profitField.setPlaceholder("Введите прибыль...");
        countField.setPlaceholder("Введите количество...");
        statusComboBox.setPlaceholder("Выберите статус...");
        datePicker.setPlaceholder("Выберите дату...");
    }

    /**
     * Сохраняет данные о еде (Meal) при нажатии кнопки "Сохранить".
     * Если meal == null, создается новая запись.
     * Обновляет таблицу после успешного сохранения.
     */
    private void saveMeal() {
        try {
            if (meal == null) {
                meal = new Meal();
            }

            meal.setName(nameField.getValue());
            meal.setCost(Integer.parseInt(costField.getValue()));
            meal.setProfit(Integer.parseInt(profitField.getValue()));
            meal.setCount(Integer.parseInt(countField.getValue()));
            meal.setStatus(statusComboBox.getValue());
            meal.setDate(datePicker.getValue());

            mealService.create(meal).subscribe(
                    createdMeal -> {
                        Notification.show("Meal saved successfully!");
                        mealDataComponent.updateGridItems();
                        close();
                    },
                    error -> Notification.show("Error saving meal: " + error.getMessage())
            );
        } catch (NumberFormatException e) {
            Notification.show("Please enter valid numbers for cost, profit, and count.");
        }
    }
}
