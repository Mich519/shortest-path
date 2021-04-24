package org.example.graph.eventHandlers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class AddVertexEvent implements EventHandler<MouseEvent> {

    private final PrimaryController controller;

    public AddVertexEvent(PrimaryController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (controller.getAddVertex().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            DoubleProperty clickedXPos = new SimpleDoubleProperty(mouseEvent.getX());
            DoubleProperty clickedYPos = new SimpleDoubleProperty(mouseEvent.getY());

            /* put vertex if clicked element is graphPane */

            String targetID = mouseEvent.getPickResult().getIntersectedNode().getId();
            String graphEditorID = controller.getGraphEditor().getId();

            if(targetID != null && targetID.equals(graphEditorID)) {
                Vertex v = new Vertex(controller, 0.0,  clickedXPos, clickedYPos);
                controller.getGraph().addVertex(v);
                controller.drawGraph();
            }
        }
    }
}

