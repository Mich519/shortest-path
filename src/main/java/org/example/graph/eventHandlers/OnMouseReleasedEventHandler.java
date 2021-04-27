package org.example.graph.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class OnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex.DragData dragData;

    public OnMouseReleasedEventHandler(PrimaryController controller, Vertex.DragData dragData) {
        this.controller = controller;
        this.dragData = dragData;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        /* append edge to second vertex */
        if (dragData.draggedEdge != null) {
            Node target = mouseEvent.getPickResult().getIntersectedNode();
            if (target instanceof Vertex) {
                controller.getGraph().addEdge(dragData.startVertex, (Vertex)target);
                controller.drawGraph();
            }
            controller.removeEdgeFromPane(dragData.draggedEdge);
        }

        /* drag has ended so clear drag data */
        dragData.draggedEdge = null;
        dragData.startVertex = null;
    }
}
