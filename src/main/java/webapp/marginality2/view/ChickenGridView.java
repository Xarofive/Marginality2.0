package webapp.marginality2.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

        HorizontalLayout gridAndButtonsLayout = new HorizontalLayout(grid);
        gridAndButtonsLayout.setSizeFull();
        gridAndButtonsLayout.setFlexGrow(1, grid);

        ChickenForm chickenForm = new ChickenForm(this::addChickenToGrid);

        VerticalLayout mainLayout = new VerticalLayout(gridAndButtonsLayout, chickenForm);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, gridAndButtonsLayout);

        add(mainLayout);

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
        addButtonsColumn(grid);

        return grid;
    }

    private void configureColumns(Grid<Chicken> grid) {
        grid.getColumnByKey("name").setHeader("Название").setSortable(true);
        grid.getColumnByKey("cost").setHeader("Цена").setSortable(true);
        grid.getColumnByKey("count").setHeader("Кол-во").setSortable(true);
        grid.getColumnByKey("date").setHeader("Дата").setSortable(true);

        grid.addColumn(Chicken::getStatus)
                .setHeader("Статус")
                .setSortable(true)
                .setKey("status");
    }

    private void addButtonsColumn(Grid<Chicken> grid) {
        ChickenEditorDialog editorDialog = new ChickenEditorDialog(this::saveEditedChicken);
        grid.addColumn(new ButtonsRenderer(grid, getChickens(), editorDialog::openEditor))
                .setHeader("Действия")
                .setKey("actions")
                .setAutoWidth(true);
    }

    private void saveEditedChicken(Chicken editedChicken) {
        List<Chicken> updatedChickens = new ArrayList<>(getChickens());
        int index = updatedChickens.indexOf(editedChicken);
        if (index >= 0) {
            updatedChickens.set(index, editedChicken);
        }
        grid.setItems(updatedChickens);
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
