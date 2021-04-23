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
    private Vertex target;

    public OnMouseDraggedEventHandler(PrimaryController controller, Vertex.DragData dragData, Vertex target) {
        this.controller = controller;
        this.dragData = dragData;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        /* dragging vertex */
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {

            /* check if dragged vertex is within a bounds of a 'graphEditor' pane */
            if (mouseEvent.getX() - target.getRadius() > 0 && mouseEvent.getX() + target.getRadius() < ((Pane) target.getParent()).getWidth())
                target.setCenterX(mouseEvent.getX());
            if (mouseEvent.getY() - target.getRadius() > 0 && mouseEvent.getX() + target.getRadius() < ((Pane) target.getParent()).getHeight())
                target.setCenterY(mouseEvent.getY());
        }

        /* updating edge that is being dragged */
        else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            if (dragData.e != null) {
                dragData.e.setEndX(mouseEvent.getX());
                dragData.e.setEndY(mouseEvent.getY());
                controller.addEdgeToPane(dragData.e);
            }
        }
    }
}
