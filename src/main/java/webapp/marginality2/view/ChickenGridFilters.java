package webapp.marginality2.view;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import webapp.marginality2.model.Chicken;

import java.time.LocalDate;
import java.util.List;

public class ChickenGridFilters {

    private final Grid<Chicken> grid;
    private final List<Chicken> chickens;

    public ChickenGridFilters(Grid<Chicken> grid, List<Chicken> chickens) {
        this.grid = grid;
        this.chickens = chickens;
    }

    public void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();
        addNameFilter(filterRow);
        addCostFilter(filterRow);
        addCountFilter(filterRow);
        addDateFilter(filterRow);
        addStatusFilter(filterRow);
    }

    private void addNameFilter(HeaderRow filterRow) {
        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Фильтр по названию");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> {
            List<Chicken> filteredChickens = chickens.stream()
                    .filter(chicken -> chicken.getName().toLowerCase().contains(nameFilter.getValue().toLowerCase()))
                    .toList();
            grid.setItems(filteredChickens);
        });
        filterRow.getCell(grid.getColumnByKey("name")).setComponent(nameFilter);
    }

    private void addCostFilter(HeaderRow filterRow) {
        TextField costFilter = new TextField();
        costFilter.setPlaceholder("Фильтр по стоимости");
        costFilter.setClearButtonVisible(true);
        costFilter.setValueChangeMode(ValueChangeMode.EAGER);
        costFilter.addValueChangeListener(event -> {
            List<Chicken> filteredChickens = chickens.stream()
                    .filter(chicken -> String.valueOf(chicken.getCost()).contains(costFilter.getValue()))
                    .toList();
            grid.setItems(filteredChickens);
        });
        filterRow.getCell(grid.getColumnByKey("cost")).setComponent(costFilter);
    }

    private void addCountFilter(HeaderRow filterRow) {
        TextField countFilter = new TextField();
        countFilter.setPlaceholder("Фильтр по количеству");
        countFilter.setClearButtonVisible(true);
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.addValueChangeListener(event -> {
            List<Chicken> filteredChickens = chickens.stream()
                    .filter(chicken -> String.valueOf(chicken.getCount()).contains(countFilter.getValue()))
                    .toList();
            grid.setItems(filteredChickens);
        });
        filterRow.getCell(grid.getColumnByKey("count")).setComponent(countFilter);
    }

    private void addDateFilter(HeaderRow filterRow) {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Фильтр по дате");
        dateFilter.setClearButtonVisible(true);
        dateFilter.addValueChangeListener(event -> {
            List<Chicken> filteredChickens = chickens.stream()
                    .filter(chicken -> {
                        LocalDate filterDate = dateFilter.getValue();
                        return filterDate == null || chicken.getDate().isEqual(filterDate);
                    })
                    .toList();
            grid.setItems(filteredChickens);
        });
        filterRow.getCell(grid.getColumnByKey("date")).setComponent(dateFilter);
    }

    private void addStatusFilter(HeaderRow filterRow) {
        TextField statusFilter = new TextField();
        statusFilter.setPlaceholder("Фильтр по статусу");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setValueChangeMode(ValueChangeMode.EAGER);
        statusFilter.addValueChangeListener(event -> {
            String filterValue = statusFilter.getValue().toLowerCase();
            List<Chicken> filteredChickens = chickens.stream()
                    .filter(chicken -> chicken.getStatus().name().toLowerCase().contains(filterValue))
                    .toList();
            grid.setItems(filteredChickens);
        });
        filterRow.getCell(grid.getColumnByKey("status")).setComponent(statusFilter);
    }
}
