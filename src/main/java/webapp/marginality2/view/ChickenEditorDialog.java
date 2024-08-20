package webapp.marginality2.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.select.Select;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.model.Status;

import java.util.function.Consumer;

public class ChickenEditorDialog extends Dialog {

    private final TextField nameField = new TextField("Название");
    private final TextField costField = new TextField("Цена");
    private final TextField countField = new TextField("Кол-во");
    private final Select<Status> statusSelect = new Select<>();
    private final DatePicker datePicker = new DatePicker("Дата");
    private final Button saveButton = new Button("Сохранить");
    private final Button cancelButton = new Button("Отменить");

    private Chicken chicken;

    public ChickenEditorDialog(Consumer<Chicken> saveConsumer) {
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

    private void configureButtons(Consumer<Chicken> saveConsumer) {
        saveButton.addClickListener(event -> {
            if (validateForm()) {
                chicken.setName(nameField.getValue());
                chicken.setCost(Integer.parseInt(costField.getValue()));
                chicken.setCount(Integer.parseInt(countField.getValue()));
                chicken.setStatus(statusSelect.getValue());
                chicken.setDate(datePicker.getValue());

                saveConsumer.accept(chicken);
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

    public void openEditor(Chicken chicken) {
        this.chicken = chicken;
        nameField.setValue(chicken.getName());
        costField.setValue(String.valueOf(chicken.getCost()));
        countField.setValue(String.valueOf(chicken.getCount()));
        statusSelect.setValue(chicken.getStatus());
        datePicker.setValue(chicken.getDate());

        open();
    }
}
