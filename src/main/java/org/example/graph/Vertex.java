package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.eventHandlers.OnMouseDraggedEventHandler;
import org.example.graph.eventHandlers.OnMousePressedEventHandler;
import org.example.graph.eventHandlers.OnMouseReleasedEventHandler;
import org.example.graph.eventHandlers.RemoveVertexEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Vertex extends Circle implements Serializable {
    public static final Color DEFAULT_COLOR = Color.valueOf("#ffd166");
    public static final Color PATH_COLOR = Color.AQUAMARINE;
    public static final Color RELAXATION_COLOR = Color.valueOf("#3a0ca3");
    public static final Color TRANSITION_COLOR = Color.PURPLE;
    public static final Color START_COLOR = Color.GREEN;
    public static final Color END_COLOR = Color.RED;

    private HashSet<Edge> adjEdges;
    private transient PrimaryController controller;
    private double curLowestCost = Double.POSITIVE_INFINITY;

    public static class DragData implements Serializable{
        public Vertex startVertex;
        public Edge draggedEdge;
    }

    public Vertex(PrimaryController controller, DoubleProperty centerX, DoubleProperty centerY) {
        super(centerX.get(), centerY.get(), 20.0, Vertex.DEFAULT_COLOR);
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

    public List<Vertex> getNeighbours() {
        List<Vertex> neighbours = new ArrayList<>();
        for (Edge e : getAdjEdges()) {
            Vertex neighbour = e.getNeighbourOf(this);
            neighbours.add(neighbour);
        }
        return neighbours;
    }

    public double distanceTo(Vertex v) {
        return Math.sqrt(Math.pow(getCenterX() - v.getCenterX(), 2) + Math.pow(getCenterY() - v.getCenterY(), 2));
    }
}

