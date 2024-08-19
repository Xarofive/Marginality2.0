package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.model.Status;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ChickenForm extends VerticalLayout {

    private final TextField nameField = new TextField("Название");
    private final TextField costField = new TextField("Цена");
    private final TextField countField = new TextField("Кол-во");
    private final Select<Status> statusSelect = new Select<>();
    private final DatePicker datePicker = new DatePicker("Дата");

    public ChickenForm(Consumer<Chicken> addChickenConsumer) {
        configureFields();
        add(createFormLayout(), createAddButton(addChickenConsumer));
    }

    private void configureFields() {
        statusSelect.setLabel("Статус");
        statusSelect.setItems(Status.values());
        statusSelect.setItemLabelGenerator(Status::toString);
    }

    private HorizontalLayout createFormLayout() {
        HorizontalLayout fieldsLayout = new HorizontalLayout(nameField, costField, countField, statusSelect, datePicker);
        fieldsLayout.setWidthFull();
        return fieldsLayout;
    }

    private Button createAddButton(Consumer<Chicken> addChickenConsumer) {
        return new Button("Добавить", event -> {
            if (validateForm()) {
                Chicken newChicken = new Chicken(
                        0,
                        nameField.getValue(),
                        Integer.parseInt(costField.getValue()),
                        statusSelect.getValue(),
                        Integer.parseInt(countField.getValue()),
                        datePicker.getValue()
                );
                addChickenConsumer.accept(newChicken);
                clearForm();
            } else {
                Notification.show("Пожалуйста, заполните все поля.");
            }
        });
    }

    private boolean validateForm() {
        try {
            Integer.parseInt(costField.getValue());
            Integer.parseInt(countField.getValue());
            return !nameField.isEmpty() && !costField.isEmpty() && !countField.isEmpty() && statusSelect.getValue() != null && datePicker.getValue() != null;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm() {
        nameField.clear();
        costField.clear();
        countField.clear();
        statusSelect.clear();
        datePicker.clear();
    }
}
