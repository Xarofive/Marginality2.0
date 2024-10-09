package webapp.marginality2.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.UI;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;
import webapp.marginality2.service.MealServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент для отображения и управления данными о еде (Meal).
 * Включает таблицу с данными и диаграммы для визуализации.
 *
 * @author Ivan Pshychenko
 * © 2024 Ivan Pshychenko ☁️
 */
public class MealDataComponent extends VerticalLayout {

    private final MealServiceImpl mealService;
    private final Grid<Meal> grid = new Grid<>(Meal.class);

    private final TextField nameField = new TextField();
    private final TextField costField = new TextField();
    private final TextField profitField = new TextField();
    private final TextField countField = new TextField();
    private final ComboBox<Status> statusComboBox = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();

    private final Div pieChartDiv = new Div();
    private final Div barChartDiv = new Div();

    public MealDataComponent(MealServiceImpl mealService) {
        this.mealService = mealService;
        configureGrid();
        configureCharts();
        updateGridItems();
    }


    private void configureGrid() {
        grid.removeColumnByKey("id");
        grid.setColumns("name", "cost", "profit", "count", "status", "date");

        // Устанавливаем заголовки на русском языке
        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("cost").setHeader("Цена закупки");
        grid.getColumnByKey("profit").setHeader("Цена продажи");
        grid.getColumnByKey("count").setHeader("Количество");
        grid.getColumnByKey("status").setHeader("Статус");
        grid.getColumnByKey("date").setHeader("Дата");

        // Добавляем фильтры
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

        add(grid);
    }

    /**
     * Конфигурирует диаграммы для отображения данных.
     */
    private void configureCharts() {
        pieChartDiv.setId("pie-chart-container");
        pieChartDiv.setWidth("400px");
        pieChartDiv.setHeight("400px");

        barChartDiv.setId("bar-chart-container");
        barChartDiv.setWidth("400px");
        barChartDiv.setHeight("400px");

        // Создаем горизонтальный слой для диаграмм
        HorizontalLayout chartsLayout = new HorizontalLayout();
        chartsLayout.setWidthFull();
        chartsLayout.add(pieChartDiv, barChartDiv);

        add(chartsLayout);
    }

    /**
     * Обновляет записи в таблице и диаграммах на основе фильтров.
     */
    public void updateGridItems() {
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
                            updatePieChart(meals); // Обновляем круговую диаграмму
                            updateBarChart(meals); // Обновляем столбчатую диаграмму
                        },
                        error -> Notification.show("Error fetching meals: " + error.getMessage())
                );
    }

    /**
     * Возвращает слой с диаграммами для отображения.
     */
    public HorizontalLayout getChartsLayout() {
        HorizontalLayout chartsLayout = new HorizontalLayout();
        chartsLayout.setWidthFull();
        chartsLayout.add(pieChartDiv, barChartDiv);
        return chartsLayout;
    }

    /**
     * Обновляет круговую диаграмму на основе данных.
     *
     * @param meals Список объектов Meal для отображения.
     */
    private void updatePieChart(List<Meal> meals) {
        String labels = meals.stream().map(Meal::getName).collect(Collectors.joining("\", \"", "[\"", "\"]"));
        String data = meals.stream().map(Meal::getCount).map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));

        // Обновляем круговую диаграмму с помощью Chart.js
        UI.getCurrent().getPage().executeJs(
                "var ctx = document.getElementById('pie-chart-container').querySelector('canvas');" +
                        "if (!ctx) {" +
                        "  ctx = document.createElement('canvas');" +
                        "  document.getElementById('pie-chart-container').appendChild(ctx);" +
                        "}" +
                        "if (window.myPieChart) window.myPieChart.destroy();" + // Удаляем предыдущий график, если он был
                        "window.myPieChart = new Chart(ctx, {" +
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

    /**
     * Обновляет столбчатую диаграмму на основе данных.
     *
     * @param meals Список объектов Meal для отображения.
     */
    private void updateBarChart(List<Meal> meals) {
        String labels = meals.stream().map(Meal::getName).collect(Collectors.joining("\", \"", "[\"", "\"]"));
        String costData = meals.stream().map(Meal::getCost).map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
        String profitData = meals.stream().map(Meal::getProfit).map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));

        // Обновляем столбчатую диаграмму с помощью Chart.js
        UI.getCurrent().getPage().executeJs(
                "var ctx = document.getElementById('bar-chart-container').querySelector('canvas');" +
                        "if (!ctx) {" +
                        "  ctx = document.createElement('canvas');" +
                        "  document.getElementById('bar-chart-container').appendChild(ctx);" +
                        "}" +
                        "if (window.myBarChart) window.myBarChart.destroy();" + // Удаляем предыдущий график, если он был
                        "window.myBarChart = new Chart(ctx, {" +
                        "    type: 'bar'," +
                        "    data: {" +
                        "        labels: " + labels + "," +
                        "        datasets: [{" +
                        "            label: 'Цена закупки'," +
                        "            data: " + costData + "," +
                        "            backgroundColor: '#36a2eb'," +
                        "        }, {" +
                        "            label: 'Цена продажи'," +
                        "            data: " + profitData + "," +
                        "            backgroundColor: '#ff6384'" +
                        "        }]" +
                        "    }," +
                        "    options: {" +
                        "        responsive: true," +
                        "        scales: {" +
                        "            yAxes: [{" +
                        "                ticks: {" +
                        "                    beginAtZero: true" +
                        "                }" +
                        "            }]" +
                        "        }" +
                        "    }" +
                        "});"
        );
    }
}
