package org.example.controller.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class OnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex<Double> target;

    public OnMouseReleasedEventHandler(PrimaryController controller, Vertex<Double> target) {
        this.controller = controller;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Node target = mouseEvent.getPickResult().getIntersectedNode();


        System.out.println("Mouse released! "+mouseEvent.getPickResult());
    }
}
