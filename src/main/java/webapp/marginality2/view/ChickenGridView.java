package webapp.marginality2.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.model.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("Chicken Grid")
@Route(value = "")
@RouteAlias(value = "")
public class ChickenGridView extends Div {

    private final Grid<Chicken> grid;
    private List<Chicken> chickens;

    public ChickenGridView() {
        addClassName("chicken-grid-view");
        setSizeFull();

        grid = createGrid();
        grid.setWidth("90%");

        // Создаем горизонтальный layout для таблицы и кнопок
        HorizontalLayout gridAndButtonsLayout = new HorizontalLayout(grid);
        gridAndButtonsLayout.setSizeFull();
        gridAndButtonsLayout.setFlexGrow(1, grid);

        // Создаем форму и добавляем её ниже таблицы
        ChickenForm chickenForm = new ChickenForm(this::addChickenToGrid);

        // Создаем вертикальный layout для таблицы и формы
        VerticalLayout mainLayout = new VerticalLayout(gridAndButtonsLayout, chickenForm);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, gridAndButtonsLayout);

        add(mainLayout);

        // Добавляем фильтры через отдельный класс
        ChickenGridFilters filters = new ChickenGridFilters(grid, getChickens());
        filters.addFiltersToGrid();
    }

    private Grid<Chicken> createGrid() {
        Grid<Chicken> grid = new Grid<>(Chicken.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        grid.setItems(getChickens());

        grid.setColumns("name", "cost", "count", "date");

        configureColumns(grid);
        addDeleteButtonColumn(grid); // Добавляем колонку с кнопками удаления

        return grid;
    }

    private void configureColumns(Grid<Chicken> grid) {
        grid.getColumnByKey("name").setHeader("Название").setSortable(true);
        grid.getColumnByKey("cost").setHeader("Цена").setSortable(true);
        grid.getColumnByKey("count").setHeader("Кол-во").setSortable(true);
        grid.getColumnByKey("date").setHeader("Дата").setSortable(true)
                .setRenderer(new LocalDateRenderer<>(Chicken::getDate, "MM/dd/yyyy"));

        grid.addColumn(Chicken::getStatus)
                .setHeader("Статус")
                .setSortable(true)
                .setKey("status");
    }

    private void addDeleteButtonColumn(Grid<Chicken> grid) {
        grid.addColumn(new ComponentRenderer<>(chicken -> {
            Button deleteButton = new Button("Удалить");
            deleteButton.getElement().getStyle().set("background-color", "red");
            deleteButton.getElement().getStyle().set("color", "white");

            deleteButton.addClickListener(event -> {
                // Создаем уведомление с кнопками подтверждения и отмены
                Notification confirmation = new Notification();
                confirmation.setPosition(Notification.Position.MIDDLE);
                confirmation.setDuration(5000); // Уведомление исчезнет через 5 секунд, если не будет выбора

                Button confirmButton = new Button("Да", e -> {
                    List<Chicken> updatedChickens = new ArrayList<>(getChickens());
                    updatedChickens.remove(chicken);
                    grid.setItems(updatedChickens);
                    confirmation.close();
                    Notification.show("Запись удалена");
                });
                confirmButton.getElement().getStyle().set("background-color", "green");
                confirmButton.getElement().getStyle().set("color", "white");

                Button cancelButton = new Button("Нет", e -> confirmation.close());
                cancelButton.getElement().getStyle().set("background-color", "gray");
                cancelButton.getElement().getStyle().set("color", "white");

                HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
                confirmation.add(new Div(new Text("Вы действительно хотите удалить запись?")), buttons);
                confirmation.open();
            });

            return deleteButton;
        })).setHeader("Действия").setKey("actions").setAutoWidth(true);
    }

    private void addChickenToGrid(Chicken newChicken) {
        List<Chicken> updatedChickens = new ArrayList<>(getChickens());
        updatedChickens.add(newChicken);
        grid.setItems(updatedChickens);
    }

    private List<Chicken> getChickens() {
        chickens = Arrays.asList(
                new Chicken(1, "Chicken 1", 100, Status.EXPIRED, 10, LocalDate.now()),
                new Chicken(2, "Chicken 2", 150, Status.FOR_SALE, 5, LocalDate.now().minusDays(1)),
                new Chicken(3, "Chicken 3", 200, Status.FOR_SALE, 20, LocalDate.now().minusDays(2))
        );
        return chickens;
    }
}
