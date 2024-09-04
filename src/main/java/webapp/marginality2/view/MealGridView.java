package webapp.marginality2.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import webapp.marginality2.model.Meal;
import webapp.marginality2.service.MealService;

import java.util.List;

@PageTitle("Chicken Grid")
@Route(value = "")
@RouteAlias(value = "")
public class MealGridView extends Div {

    private final MealService mealService;
    private final Grid<Meal> grid;
    private final List<Meal> meals;

    public MealGridView(MealService mealService) {
        this.mealService = mealService;
        addClassName("chicken-grid-view");
        setSizeFull();

        // Загружаем данные из базы данных один раз
        this.meals = getMeal();

        grid = createGrid();
        grid.setWidth("90%");

        HorizontalLayout gridAndButtonsLayout = new HorizontalLayout(grid);
        gridAndButtonsLayout.setSizeFull();
        gridAndButtonsLayout.setFlexGrow(1, grid);

        MealForm mealForm = new MealForm(this::addChickenToGrid, this.mealService);

        VerticalLayout mainLayout = new VerticalLayout(gridAndButtonsLayout, mealForm);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, gridAndButtonsLayout);

        add(mainLayout);

        MealGridFilters filters = new MealGridFilters(grid, meals);
        filters.addFiltersToGrid();
    }

    private Grid<Meal> createGrid() {
        Grid<Meal> grid = new Grid<>(Meal.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        // Используем загруженные данные
        grid.setItems(meals);

        grid.setColumns("name", "cost", "count", "date");

        configureColumns(grid);
        addButtonsColumn(grid);

        return grid;
    }

    private void configureColumns(Grid<Meal> grid) {
        grid.getColumnByKey("name").setHeader("Название").setSortable(true);
        grid.getColumnByKey("cost").setHeader("Цена").setSortable(true);
        grid.getColumnByKey("count").setHeader("Кол-во").setSortable(true);
        grid.getColumnByKey("date").setHeader("Дата").setSortable(true);

        grid.addColumn(Meal::getStatus)
                .setHeader("Статус")
                .setSortable(true)
                .setKey("status");
    }

    private void addButtonsColumn(Grid<Meal> grid) {
        MealEditorDialog editorDialog = new MealEditorDialog(this::saveEditedMeal);
        grid.addColumn(new ButtonsRenderer(grid, meals, editorDialog::openEditor))
                .setHeader("Действия")
                .setKey("actions")
                .setAutoWidth(true);
    }

    private void saveEditedMeal(Meal editedMeal) {
        int index = meals.indexOf(editedMeal);
        if (index >= 0) {
            meals.set(index, editedMeal);
        }
        grid.setItems(meals);
    }

    private void addChickenToGrid(Meal newMeal) {
        meals.add(newMeal);
        grid.setItems(meals);
    }

    private List<Meal> getMeal() {
        return mealService.findAll()
                .collectList()
                .block();
    }
}
