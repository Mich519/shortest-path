package org.example.controller.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;

public class AddEdgeEvent implements EventHandler<MouseEvent> {

    private PrimaryController controller;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (controller.getAddEdge().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            System.out.println("drag started at "+mouseEvent.getPickResult());
        }
    }
}
