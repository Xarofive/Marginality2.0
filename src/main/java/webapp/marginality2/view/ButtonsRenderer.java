//package webapp.marginality2.view;
//
//import com.vaadin.flow.component.Text;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.data.renderer.ComponentRenderer;
//import webapp.marginality2.model.Chicken;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ButtonsRenderer extends ComponentRenderer<Div, Chicken> {
//
//    private final Grid<Chicken> grid;
//    private final List<Chicken> chickens;
//
//    public ButtonsRenderer(Grid<Chicken> grid, List<Chicken> chickens) {
//        super(Div::new, (div, chicken) -> {
//            ButtonsRenderer renderer = new ButtonsRenderer(grid, chickens);
//            div.add(renderer.createButtonLayout(chicken));
//        });
//        this.grid = grid;
//        this.chickens = chickens;
//    }
//
//    private HorizontalLayout createButtonLayout(Chicken chicken) {
//        HorizontalLayout buttonLayout = new HorizontalLayout(createDeleteButton(chicken), createEditButton(chicken));
//        return buttonLayout;
//    }
//
//    private Button createDeleteButton(Chicken chicken) {
//        Button deleteButton = new Button("Удалить");
//        deleteButton.getElement().getStyle().set("background-color", "red");
//        deleteButton.getElement().getStyle().set("color", "white");
//
//        deleteButton.addClickListener(event -> {
//            Notification confirmation = new Notification();
//            confirmation.setPosition(Notification.Position.MIDDLE);
//            confirmation.setDuration(5000);
//
//            Button confirmButton = new Button("Да", e -> {
//                List<Chicken> updatedChickens = new ArrayList<>(chickens);
//                updatedChickens.remove(chicken);
//                grid.setItems(updatedChickens);
//                confirmation.close();
//                Notification.show("Запись удалена");
//            });
//            confirmButton.getElement().getStyle().set("background-color", "green");
//            confirmButton.getElement().getStyle().set("color", "white");
//
//            Button cancelButton = new Button("Нет", e -> confirmation.close());
//            cancelButton.getElement().getStyle().set("background-color", "gray");
//            cancelButton.getElement().getStyle().set("color", "white");
//
//            HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
//            confirmation.add(new Div(new Text("Вы действительно хотите удалить запись?")), buttons);
//            confirmation.open();
//        });
//
//        return deleteButton;
//    }
//
//    private Button createEditButton(Chicken chicken) {
//        Button editButton = new Button("Редактировать");
//        editButton.getElement().getStyle().set("background-color", "blue");
//        editButton.getElement().getStyle().set("color", "white");
//
//        editButton.addClickListener(event -> {
//            // Здесь должна быть логика редактирования
//            Notification.show("Редактирование записи: " + chicken.getName());
//        });
//
//        return editButton;
//    }
//}
package webapp.marginality2.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import webapp.marginality2.model.Chicken;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonsRenderer extends ComponentRenderer<Div, Chicken> {

    private final Grid<Chicken> grid;
    private final List<Chicken> chickens;
    private final Consumer<Chicken> editConsumer;

    public ButtonsRenderer(Grid<Chicken> grid, List<Chicken> chickens, Consumer<Chicken> editConsumer) {
        super(Div::new, (div, chicken) -> {
            ButtonsRenderer renderer = new ButtonsRenderer(grid, chickens, editConsumer);
            div.add(renderer.createButtonLayout(chicken));
        });
        this.grid = grid;
        this.chickens = chickens;
        this.editConsumer = editConsumer;
    }

    private HorizontalLayout createButtonLayout(Chicken chicken) {
        HorizontalLayout buttonLayout = new HorizontalLayout(createDeleteButton(chicken), createEditButton(chicken));
        return buttonLayout;
    }

    private Button createDeleteButton(Chicken chicken) {
        Button deleteButton = new Button("Удалить");
        deleteButton.getElement().getStyle().set("background-color", "red");
        deleteButton.getElement().getStyle().set("color", "white");

        deleteButton.addClickListener(event -> {
            Notification confirmation = new Notification();
            confirmation.setPosition(Notification.Position.MIDDLE);
            confirmation.setDuration(5000);

            Button confirmButton = new Button("Да", e -> {
                List<Chicken> updatedChickens = new ArrayList<>(chickens);
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
    }

    private Button createEditButton(Chicken chicken) {
        Button editButton = new Button("Редактировать");
        editButton.getElement().getStyle().set("background-color", "blue");
        editButton.getElement().getStyle().set("color", "white");

        editButton.addClickListener(event -> {
            editConsumer.accept(chicken);
        });

        return editButton;
    }
}
