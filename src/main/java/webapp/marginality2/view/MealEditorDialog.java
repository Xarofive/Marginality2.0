package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.select.Select;
import webapp.marginality2.model.Meal;
import webapp.marginality2.model.Status;

import java.util.function.Consumer;

public class MealEditorDialog extends Dialog {

    private final TextField nameField = new TextField("Название");
    private final TextField costField = new TextField("Цена");
    private final TextField countField = new TextField("Кол-во");
    private final Select<Status> statusSelect = new Select<>();
    private final DatePicker datePicker = new DatePicker("Дата");
    private final Button saveButton = new Button("Сохранить");
    private final Button cancelButton = new Button("Отменить");

    private Meal meal;

    public MealEditorDialog(Consumer<Meal> saveConsumer) {
        configureForm();
        configureButtons(saveConsumer);
        add(createFormLayout(), createButtonLayout());
    }

    private void configureForm() {
        statusSelect.setLabel("Статус");
        statusSelect.setItems(Status.values());
        statusSelect.setItemLabelGenerator(Status::toString);
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, costField, countField, statusSelect, datePicker);
        return formLayout;
    }

    private HorizontalLayout createButtonLayout() {
        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void configureButtons(Consumer<Meal> saveConsumer) {
        saveButton.addClickListener(event -> {
            if (validateForm()) {
                meal.setName(nameField.getValue());
                meal.setCost(Integer.parseInt(costField.getValue()));
                meal.setCount(Integer.parseInt(countField.getValue()));
                meal.setStatus(statusSelect.getValue());
                meal.setDate(datePicker.getValue());

                saveConsumer.accept(meal);
                close();
                Notification.show("Изменения сохранены");
            } else {
                Notification.show("Пожалуйста, заполните все поля.");
            }
        });

        cancelButton.addClickListener(event -> close());
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

    public void openEditor(Meal meal) {
        this.meal = meal;
        nameField.setValue(meal.getName());
        costField.setValue(String.valueOf(meal.getCost()));
        countField.setValue(String.valueOf(meal.getCount()));
        statusSelect.setValue(meal.getStatus());
        datePicker.setValue(meal.getDate());

        open();
    }
}
