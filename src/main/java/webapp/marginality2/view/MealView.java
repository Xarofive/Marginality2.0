package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;

import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.MealServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("")
@JavaScript("https://cdn.jsdelivr.net/npm/chart.js") // Подключаем Chart.js
public class MealView extends VerticalLayout {

    private final MealServiceImpl mealService;
    private Grid<Meal> grid = new Grid<>(Meal.class);
    private TextField nameField = new TextField();
    private TextField costField = new TextField();
    private TextField profitField = new TextField();
    private TextField countField = new TextField();
    private ComboBox<Status> statusComboBox = new ComboBox<>();
    private DatePicker datePicker = new DatePicker();

    private TextField profitResultField = new TextField("Прибыль");
    private Span currentTime = new Span();
    private DatePicker currentDate = new DatePicker();

    // Контейнер для графика
    private Div chartDiv = new Div();

    public MealView(MealServiceImpl mealService) {
        this.mealService = mealService;
        configureGrid();
        configureToolbar();
        configureDateTimeDisplay(); // Настройка времени и даты

        // Настраиваем поле прибыли
        profitResultField.setReadOnly(true);
        profitResultField.getStyle().set("font-size", "20px");
        profitResultField.setWidthFull();

        // Настраиваем диаграмму
        chartDiv.setId("chart-container");
        chartDiv.setWidth("400px");
        chartDiv.setHeight("400px");

        // Создаем горизонтальный слой для даты, времени и диаграммы
        HorizontalLayout dateTimeAndChartLayout = new HorizontalLayout();
        dateTimeAndChartLayout.setWidthFull();

        // Выравниваем текущую дату и время по центру
        HorizontalLayout dateLayout = new HorizontalLayout(currentDate);
        dateLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout timeLayout = new HorizontalLayout(currentTime);
        timeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        timeLayout.setAlignItems(Alignment.CENTER);

        // Добавляем диаграмму в правую часть
        dateTimeAndChartLayout.add(dateLayout, chartDiv);
        dateTimeAndChartLayout.setAlignItems(Alignment.STRETCH); // Выравниваем все по центру по вертикали

        // Добавляем элементы на экран
        add(grid, profitResultField, timeLayout, dateTimeAndChartLayout); // Теперь диаграмма и дата с временем в одном слое
        updateGridItems();
    }

    private void configureGrid() {
        grid.removeColumnByKey("id");
        grid.setColumns("name", "cost", "profit", "count", "status", "date");

        // Переводим названия колонок на русский
        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("cost").setHeader("Цена закупки");
        grid.getColumnByKey("profit").setHeader("Цена продажи");
        grid.getColumnByKey("count").setHeader("Количество");
        grid.getColumnByKey("status").setHeader("Статус");
        grid.getColumnByKey("date").setHeader("Дата");

        // Фильтры над каждым полем
        HeaderRow filterRow = grid.appendHeaderRow();

        nameField.setPlaceholder("Фильтр по имени...");
        nameField.setClearButtonVisible(true);
        nameField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("name")).setComponent(nameField);

        costField.setPlaceholder("Фильтр по стоимости...");
        costField.setClearButtonVisible(true);
        costField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("cost")).setComponent(costField);

        profitField.setPlaceholder("Фильтр по прибыли...");
        profitField.setClearButtonVisible(true);
        profitField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("profit")).setComponent(profitField);

        countField.setPlaceholder("Фильтр по количеству...");
        countField.setClearButtonVisible(true);
        countField.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("count")).setComponent(countField);

        statusComboBox.setItems(Status.values());
        statusComboBox.setPlaceholder("Фильтр по статусу...");
        statusComboBox.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("status")).setComponent(statusComboBox);

        datePicker.setPlaceholder("Фильтр по дате...");
        datePicker.addValueChangeListener(e -> updateGridItems());
        filterRow.getCell(grid.getColumnByKey("date")).setComponent(datePicker);

        // Добавляем редактирование
        Binder<Meal> binder = new Binder<>(Meal.class);
        grid.getEditor().setBinder(binder);
        grid.getEditor().setBuffered(true);

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

        ComboBox<Status> statusEditor = new ComboBox<>();
        statusEditor.setItems(Status.values());
        binder.forField(statusEditor).bind(Meal::getStatus, Meal::setStatus);
        grid.getColumnByKey("status").setEditorComponent(statusEditor);

        DatePicker dateEditor = new DatePicker();
        binder.forField(dateEditor).bind(Meal::getDate, Meal::setDate);
        grid.getColumnByKey("date").setEditorComponent(dateEditor);

        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
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
        mealService.findAll()
                .filter(meal -> {
                    boolean matchesName = nameField.isEmpty() || meal.getName().toLowerCase().contains(nameField.getValue().toLowerCase());
                    boolean matchesCost = costField.isEmpty() || meal.getCost() == Integer.parseInt(costField.getValue());
                    boolean matchesProfit = profitField.isEmpty() || meal.getProfit() == Integer.parseInt(profitField.getValue());
                    boolean matchesCount = countField.isEmpty() || String.valueOf(meal.getCount()).contains(countField.getValue());
                    boolean matchesStatus = statusComboBox.isEmpty() || meal.getStatus().equals(statusComboBox.getValue());
                    boolean matchesDate = datePicker.isEmpty() || meal.getDate().equals(datePicker.getValue());

                    return matchesName && matchesCost && matchesProfit && matchesCount && matchesStatus && matchesDate;
                })
                .collectList()
                .subscribe(
                        meals -> {
                            grid.setItems(meals);
                            updateProfitField(meals);
                            updatePieChart(meals); // Обновляем диаграмму
                        },
                        error -> Notification.show("Error fetching meals: " + error.getMessage())
                );
    }

    // Метод для обновления поля "Прибыль"
    private void updateProfitField(List<Meal> meals) {
        int totalProfit = meals.stream().mapToInt(Meal::getProfit).sum();
        int totalCost = meals.stream().mapToInt(Meal::getCost).sum();
        int netProfit = totalProfit - totalCost;

        profitResultField.setValue("Прибыль: " + netProfit);
    }

    // Метод для настройки текущего времени и даты
    private void configureDateTimeDisplay() {
        HorizontalLayout dateTimeLayout = new HorizontalLayout();

        currentTime.getStyle().set("font-size", "20px");
        dateTimeLayout.add(currentTime);

        currentDate.setLabel("Сегодняшняя дата");
        currentDate.setValue(LocalDateTime.now().toLocalDate());
        dateTimeLayout.add(currentDate);

        UI ui = UI.getCurrent();
        ui.setPollInterval(1000);
        ui.addPollListener(pollEvent -> {
            String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            currentTime.setText("Текущее время: " + formattedTime);
        });

        add(dateTimeLayout);
    }

    // Метод для обновления данных диаграммы
    private void updatePieChart(List<Meal> meals) {
        String labels = meals.stream().map(Meal::getName).collect(Collectors.joining("\", \"", "[\"", "\"]"));
        String data = meals.stream().map(Meal::getCount).map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));

        // Обновляем диаграмму с помощью Chart.js
        UI.getCurrent().getPage().executeJs(
                "var ctx = document.getElementById('chart-container').querySelector('canvas');" +
                        "if (!ctx) {" +
                        "  ctx = document.createElement('canvas');" +
                        "  document.getElementById('chart-container').appendChild(ctx);" +
                        "}" +
                        "if (window.myChart) window.myChart.destroy();" + // Удаляем предыдущий график, если он был
                        "window.myChart = new Chart(ctx, {" +
                        "    type: 'pie'," +
                        "    data: {" +
                        "        labels: " + labels + "," +
                        "        datasets: [{" +
                        "            data: " + data + "," +
                        "            backgroundColor: ['#ff6384', '#36a2eb', '#cc65fe', '#ffce56']" +
                        "        }]" +
                        "    }," +
                        "    options: {" +
                        "        responsive: true" +
                        "    }" +
                        "});"
        );
    }
}
