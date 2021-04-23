package org.example.graph.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class RemoveVertexEvent implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex<Double> target;

    public RemoveVertexEvent(PrimaryController controller, Vertex<Double> target) {
        this.controller = controller;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (controller.getRemoveVertex().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            controller.removeVertexFromPane(target);
        }
    }
}
