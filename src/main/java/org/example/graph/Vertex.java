package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.eventHandlers.OnMouseDraggedEventHandler;
import org.example.graph.eventHandlers.OnMousePressedEventHandler;
import org.example.graph.eventHandlers.OnMouseReleasedEventHandler;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Vertex<T> extends Circle {
    private final PrimaryController controller;
    private final T weight;

    public static class DragData {
        public Edge e;
    }

    public Vertex(PrimaryController controller, T weight, DoubleProperty centerX, DoubleProperty centerY) {
        super(centerX.get(), centerY.get(), 20.0, Paint.valueOf("#A7ABDD"));
        this.controller = controller;
        this.weight = weight;
        centerX.bind(centerXProperty());
        centerY.bind(centerYProperty());

        // set event handlers
        final DragData dragData = new DragData();
        setOnMousePressed(new OnMousePressedEventHandler(dragData, this));
        setOnMouseDragged(new OnMouseDraggedEventHandler(controller, dragData, this));
        setOnMouseReleased(new OnMouseReleasedEventHandler(controller, dragData));
    }
}

