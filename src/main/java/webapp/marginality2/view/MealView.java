package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.MealServiceImpl;

import java.util.Arrays;

@Route("")
public class MealView extends VerticalLayout {

    private final MealServiceImpl mealService;
    private Grid<Meal> grid = new Grid<>(Meal.class);
    private TextField nameField = new TextField();
    private TextField costField = new TextField();
    private TextField profitField = new TextField();
    private TextField countField = new TextField();
    private ComboBox<Status> statusComboBox = new ComboBox<>();
    private DatePicker datePicker = new DatePicker();

    public MealView(MealServiceImpl mealService) {
        this.mealService = mealService;
        configureGrid();
        configureToolbar();

        add(grid);
        updateGridItems();
    }

    private void configureGrid() {
        grid.removeColumnByKey("id"); // Убираем колонку с id
        grid.setColumns("name", "cost", "profit", "count", "status", "date");

        // Увеличиваем размер заголовков столбцов
        grid.getColumns().forEach(column -> column.getElement().getStyle().set("font-size", "20px"));

        // Фильтры над каждым полем
        HeaderRow filterRow = grid.appendHeaderRow();

        // Фильтр по имени
        nameField.setPlaceholder("Фильтр по имени...");
        nameField.setClearButtonVisible(true);
        nameField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("name")).setComponent(nameField);

        // Фильтр по стоимости
        costField.setPlaceholder("Фильтр по стоимости...");
        costField.setClearButtonVisible(true);
        costField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("cost")).setComponent(costField);

        // Фильтр по прибыли
        profitField.setPlaceholder("Фильтр по прибыли...");
        profitField.setClearButtonVisible(true);
        profitField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("profit")).setComponent(profitField);

        // Фильтр по количеству
        countField.setPlaceholder("Фильтр по количеству...");
        countField.setClearButtonVisible(true);
        countField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("count")).setComponent(countField);

        // Фильтр по статусу
        statusComboBox.setItems(Status.values()); // Добавляем возможные значения для ComboBox
        statusComboBox.setPlaceholder("Фильтр по статусу...");
        statusComboBox.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("status")).setComponent(statusComboBox);

        // Фильтр по дате
        datePicker.setPlaceholder("Фильтр по дате...");
        datePicker.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("date")).setComponent(datePicker);

        // Редактирование конкретного поля
        Binder<Meal> binder = new Binder<>(Meal.class);
        grid.getEditor().setBinder(binder);
        grid.getEditor().setBuffered(true);

        // Добавляем редактирование для каждого поля отдельно
        TextField nameEditor = new TextField();
        binder.forField(nameEditor).bind(Meal::getName, Meal::setName);
        grid.getColumnByKey("name").setEditorComponent(nameEditor);

        TextField costEditor = new TextField();
        binder.forField(costEditor).withConverter(new StringToIntegerConverter("Введите число")).bind(Meal::getCost, Meal::setCost);
        grid.getColumnByKey("cost").setEditorComponent(costEditor);

        TextField profitEditor = new TextField();
        binder.forField(profitEditor).withConverter(new StringToIntegerConverter("Введите число")).bind(Meal::getProfit, Meal::setProfit);
        grid.getColumnByKey("profit").setEditorComponent(profitEditor);

        TextField countEditor = new TextField();
        binder.forField(countEditor).withConverter(new StringToIntegerConverter("Введите число")).bind(Meal::getCount, Meal::setCount);
        grid.getColumnByKey("count").setEditorComponent(countEditor);

        // Редактирование поля "status" с ComboBox
        ComboBox<Status> statusEditor = new ComboBox<>();
        statusEditor.setItems(Status.values()); // Добавляем возможные значения
        binder.forField(statusEditor).bind(Meal::getStatus, Meal::setStatus);
        grid.getColumnByKey("status").setEditorComponent(statusEditor);

        DatePicker dateEditor = new DatePicker();
        binder.forField(dateEditor).bind(Meal::getDate, Meal::setDate);
        grid.getColumnByKey("date").setEditorComponent(dateEditor);

        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());

            // Ловим событие нажатия Enter для сохранения данных
            grid.getElement().addEventListener("keydown", e -> {
                if ("Enter".equals(e.getEventData().getString("event.key"))) {
                    grid.getEditor().save();
                }
            }).addEventData("event.key");
        });

        grid.getEditor().addSaveListener(event -> {
            mealService.update(event.getItem().getId(), event.getItem()).subscribe(
                    updatedMeal -> {
                        Notification.show("Meal updated");
                        updateGridItems();
                    },
                    error -> Notification.show("Error updating meal: " + error.getMessage())
            );
        });

        // Кнопка "Удалить"
        grid.addComponentColumn(meal -> {
            Button deleteButton = new Button("Удалить", clickEvent -> {
                mealService.deleteById(meal.getId()).subscribe(
                        null,
                        error -> Notification.show("Error deleting meal: " + error.getMessage()),
                        () -> {
                            Notification.show("Meal deleted");
                            updateGridItems();
                        }
                );
            });
            deleteButton.getStyle().set("background-color", "red");
            deleteButton.getStyle().set("color", "white");
            return deleteButton;
        });
    }


    private void configureToolbar() {
        Button addButton = new Button("Добавить новую запись", clickEvent -> openMealDialog(null));
        add(addButton);
    }

    private void openMealDialog(Meal meal) {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Название");
        TextField costField = new TextField("Стоимость");
        TextField profitField = new TextField("Прибыль");
        TextField countField = new TextField("Количество");
        ComboBox<Status> statusComboBox = new ComboBox<>("Статус", Status.values());
        DatePicker datePicker = new DatePicker("Дата");

        if (meal != null) {
            nameField.setValue(meal.getName());
            costField.setValue(String.valueOf(meal.getCost()));
            profitField.setValue(String.valueOf(meal.getProfit()));
            countField.setValue(String.valueOf(meal.getCount()));
            statusComboBox.setValue(meal.getStatus());
            datePicker.setValue(meal.getDate());
        }

        final Meal[] mutableMeal = {meal};

        Button saveButton = new Button("Сохранить", clickEvent -> {
            if (mutableMeal[0] == null) {
                mutableMeal[0] = new Meal();
            }
            mutableMeal[0].setName(nameField.getValue());
            mutableMeal[0].setCost(Integer.parseInt(costField.getValue()));
            mutableMeal[0].setProfit(Integer.parseInt(profitField.getValue()));
            mutableMeal[0].setCount(Integer.parseInt(countField.getValue()));
            mutableMeal[0].setStatus(statusComboBox.getValue());
            mutableMeal[0].setDate(datePicker.getValue());

            mealService.create(mutableMeal[0]).subscribe(
                    newMeal -> {
                        Notification.show("Meal created");
                        dialog.close();
                        updateGridItems();
                    },
                    error -> Notification.show("Error creating meal: " + error.getMessage())
            );
        });

        Button cancelButton = new Button("Отменить", clickEvent -> dialog.close());
        dialog.add(nameField, costField, profitField, countField, statusComboBox, datePicker, saveButton, cancelButton);
        dialog.open();
    }

    private void updateGridItems() {
        // Обновляем фильтрацию для всех полей
        mealService.findAll()
                .filter(meal -> nameField.isEmpty() || meal.getName().contains(nameField.getValue()))
                .filter(meal -> costField.isEmpty() || String.valueOf(meal.getCost()).contains(costField.getValue()))
                .filter(meal -> profitField.isEmpty() || String.valueOf(meal.getProfit()).contains(profitField.getValue()))
                .filter(meal -> countField.isEmpty() || String.valueOf(meal.getCount()).contains(countField.getValue()))
                .filter(meal -> statusComboBox.isEmpty() || meal.getStatus().equals(statusComboBox.getValue()))
                .filter(meal -> datePicker.isEmpty() || meal.getDate().equals(datePicker.getValue()))
                .collectList()
                .subscribe(
                        meals -> grid.setItems(meals),
                        error -> Notification.show("Error fetching meals: " + error.getMessage())
                );
    }
}
