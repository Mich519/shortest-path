package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Vertex extends Circle implements Serializable {
    public static final Color defaultColor = Color.ORANGE;
    public static final Color pathColor = Color.AQUAMARINE;
    public static final Color startColor = Color.GREEN;
    public static final Color endColor = Color.RED;

    private HashSet<Edge> adjEdges;
    private transient PrimaryController controller;
    private double curLowestCost = Double.POSITIVE_INFINITY;

    public static class DragData implements Serializable{
        public Vertex startVertex;
        public Edge draggedEdge;
    }

    public Vertex(PrimaryController controller, DoubleProperty centerX, DoubleProperty centerY) {
        super(centerX.get(), centerY.get(), 20.0, Vertex.defaultColor);
        this.controller = controller;
        this.adjEdges = new HashSet<>();
        centerX.bind(centerXProperty());
        centerY.bind(centerYProperty());

        radiusProperty().bind(controller.getVertexRadius().valueProperty());

        // set event handlers
        final DragData dragData = new DragData();
        setOnMousePressed(new OnMousePressedEventHandler(dragData, this));
        setOnMouseDragged(new OnMouseDraggedEventHandler(controller, dragData, this));
        setOnMouseReleased(new OnMouseReleasedEventHandler(controller, dragData));
        setOnMouseClicked(new RemoveVertexEvent(controller, this));
    }

    public Edge findEdgeConnectedTo(Vertex v) {
        for (Edge e : adjEdges) {
            if(e.getNeighbourOf(this) == v) {
                return e;
            }
        }
        return null;
    }
}

