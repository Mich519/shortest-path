package org.example.controller.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class OnDragEventHandler implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex<Double> target;

    public OnDragEventHandler(PrimaryController controller, Vertex<Double> target) {
        this.controller = controller;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            /* drag vertex */

            System.out.println("Dragged! x= " + mouseEvent.getX() + " y= " + mouseEvent.getY());
            target.setCenterX(mouseEvent.getX());
            target.setCenterY(mouseEvent.getY());
        } else if (controller.getAddEdge().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            /* add edge */

            System.out.println("Dragging edge");
        }

    }
}
