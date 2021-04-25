package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.eventHandlers.OnMouseDraggedEventHandler;
import org.example.graph.eventHandlers.OnMousePressedEventHandler;
import org.example.graph.eventHandlers.OnMouseReleasedEventHandler;
import org.example.graph.eventHandlers.RemoveVertexEvent;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Vertex extends Circle {

    private HashSet<Edge> adjEdges;
    private final PrimaryController controller;
    private double curLowestCost = Double.POSITIVE_INFINITY;

    public static class DragData {
        public Vertex startVertex;
        public Edge draggedEdge;
    }

    public Vertex(PrimaryController controller, DoubleProperty centerX, DoubleProperty centerY) {
        super(centerX.get(), centerY.get(), 20.0, Paint.valueOf("#A7ABDD"));
        this.controller = controller;
        this.adjEdges = new HashSet<>();
        centerX.bind(centerXProperty());
        centerY.bind(centerYProperty());

        // set event handlers
        final DragData dragData = new DragData();
        setOnMousePressed(new OnMousePressedEventHandler(dragData, this));
        setOnMouseDragged(new OnMouseDraggedEventHandler(controller, dragData, this));
        setOnMouseReleased(new OnMouseReleasedEventHandler(controller, dragData));
        setOnMouseClicked(new RemoveVertexEvent(controller, this));
    }
}

