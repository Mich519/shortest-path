package org.example.graph.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.graph.Edge;
import org.example.graph.Vertex;

public class OnMousePressedEventHandler implements EventHandler<MouseEvent> {
    private final Vertex.DragData dragData;
    private final Vertex target;

    public OnMousePressedEventHandler(Vertex.DragData dragData, Vertex target) {
        this.dragData = dragData;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // record a delta distance for the drag and drop operation.
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ;
        } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {

            /* append edge to first vertex */
            dragData.startVertex = target;
            dragData.draggedEdge = new Edge(target, target);
            dragData.draggedEdge.endXProperty().unbind();
            dragData.draggedEdge.endYProperty().unbind();
        }
    }
}
