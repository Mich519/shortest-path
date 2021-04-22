package org.example.controller.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;

public class AddVertexEvent implements EventHandler<MouseEvent> {

    private final PrimaryController controller;

    public AddVertexEvent(PrimaryController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (controller.getAddVertex().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            System.out.println(mouseEvent.getPickResult());
            double clickedXPos = mouseEvent.getX();
            double clickedYPos = mouseEvent.getY();
            controller.addVertexToPane(clickedXPos, clickedYPos);
            System.out.println("Add vertex");
        }
    }
}

