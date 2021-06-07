package org.example.graph.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class OnMouseDraggedEventHandler implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex.DragData dragData;
    private final Vertex target;

    public OnMouseDraggedEventHandler(PrimaryController controller, Vertex.DragData dragData, Vertex target) {
        this.controller = controller;
        this.dragData = dragData;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            /* dragging vertex */
            /* check if dragged vertex is within a bounds of a 'graphEditor' pane */
            if (mouseEvent.getX() - target.getRadius() > 0 && mouseEvent.getX() + target.getRadius() < controller.getGraphEditor().getWidth())
                target.setCenterX(mouseEvent.getX());
            if (mouseEvent.getY() - target.getRadius() > 0 && mouseEvent.getY() + target.getRadius() < controller.getGraphEditor().getHeight())
                target.setCenterY(mouseEvent.getY());
        } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            /* update edge that is being dragged */

            if (dragData.draggedEdge != null) {
                dragData.draggedEdge.setEndX(mouseEvent.getX());
                dragData.draggedEdge.setEndY(mouseEvent.getY());
                controller.addEdgeToPane(dragData.draggedEdge);
            }
        }
    }
}
