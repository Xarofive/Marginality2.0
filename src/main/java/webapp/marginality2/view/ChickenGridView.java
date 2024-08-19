package webapp.marginality2.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import webapp.marginality2.model.Chicken;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@PageTitle("Chicken Grid")
@Route(value = "")
@RouteAlias(value = "")
public class ChickenGridView extends Div {

    private final Grid<Chicken> grid;

    public ChickenGridView() {
        addClassName("chicken-grid-view");
        setSizeFull();

        grid = createGrid();
        add(grid);
        addFiltersToGrid();
    }

    private Grid<Chicken> createGrid() {
        Grid<Chicken> grid = new Grid<>(Chicken.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        grid.setItems(getChickens());

        grid.setColumns("name", "cost", "count", "date");

        configureColumns(grid);

        return grid;
    }

    private void configureColumns(Grid<Chicken> grid) {
        grid.getColumnByKey("name").setHeader("Название").setSortable(true);
        grid.getColumnByKey("cost").setHeader("Цена").setSortable(true);
        grid.getColumnByKey("count").setHeader("Кол-во").setSortable(true);
        grid.getColumnByKey("date").setHeader("Дата").setSortable(true)
                .setRenderer(new LocalDateRenderer<>(Chicken::getDate, "MM/dd/yyyy"));

        grid.addColumn(chicken -> chicken.isForSale() ? "на продажу" : "склад")
                .setHeader("Статус")
                .setSortable(true);
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Фильтр по названию");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> applyFilters(nameFilter.getValue(), null, null, null, null));
        filterRow.getCell(grid.getColumnByKey("name")).setComponent(nameFilter);

        TextField costFilter = new TextField();
        costFilter.setPlaceholder("Фильтр по стоимости");
        costFilter.setClearButtonVisible(true);
        costFilter.setValueChangeMode(ValueChangeMode.EAGER);
        costFilter.addValueChangeListener(event -> applyFilters(null, costFilter.getValue(), null, null, null));
        filterRow.getCell(grid.getColumnByKey("cost")).setComponent(costFilter);

        TextField countFilter = new TextField();
        countFilter.setPlaceholder("Фильтр по кол-ву");
        countFilter.setClearButtonVisible(true);
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.addValueChangeListener(event -> applyFilters(null, null, countFilter.getValue(), null, null));
        filterRow.getCell(grid.getColumnByKey("count")).setComponent(countFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Фильтр по дате");
        dateFilter.setClearButtonVisible(true);
        dateFilter.addValueChangeListener(event -> applyFilters(null, null, null, dateFilter.getValue(), null));
        filterRow.getCell(grid.getColumnByKey("date")).setComponent(dateFilter);

        TextField statusFilter = new TextField();
        statusFilter.setPlaceholder("Фильтр по статусу");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setValueChangeMode(ValueChangeMode.EAGER);
        statusFilter.addValueChangeListener(event -> applyFilters(null, null, null, null, statusFilter.getValue()));
        filterRow.getCell(grid.getColumns().get(4)).setComponent(statusFilter);
    }

    private void applyFilters(String name, String cost, String count, LocalDate date, String status) {
        List<Chicken> filteredChickens = getChickens().stream()
                .filter(chicken -> {
                    boolean matchesName = (name == null || chicken.getName().toLowerCase().contains(name.toLowerCase()));
                    boolean matchesCost = (cost == null || String.valueOf(chicken.getCost()).contains(cost));
                    boolean matchesCount = (count == null || String.valueOf(chicken.getCount()).contains(count));
                    boolean matchesDate = (date == null || chicken.getDate().equals(date));
                    boolean matchesStatus = (status == null ||
                            (chicken.isForSale() ? "на продажу" : "склад").contains(status.toLowerCase()));
                    return matchesName && matchesCost && matchesCount && matchesDate && matchesStatus;
                })
                .toList();
        grid.setItems(filteredChickens);
    }

    private List<Chicken> getChickens() {
        return Arrays.asList(
                new Chicken(1, "Chicken 1", 100, true, 10, LocalDate.now()),
                new Chicken(2, "Chicken 2", 150, false, 5, LocalDate.now().minusDays(1)),
                new Chicken(3, "Chicken 3", 200, true, 20, LocalDate.now().minusDays(2))
        );
    }
}
