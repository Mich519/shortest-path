package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Vertex<T> extends Circle {
    private final PrimaryController controller;
    private final T weight;

    private static class DragData {
        Edge e;
    }

    private void enableDrag() {

        final DragData dragData = new DragData();

        setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                ;
            } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
                dragData.e = new Edge(
                        centerXProperty(),
                        centerYProperty(),
                        centerXProperty(),
                        centerYProperty()
                );
            }
        });

        setOnMouseDragged(mouseEvent -> {
            /* dragging vertex */
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                setCenterX(mouseEvent.getX());
                setCenterY(mouseEvent.getY());
            }

            /* creating edge between vertices */
            else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
                if(dragData.e != null) {
                    dragData.e.setEndX(mouseEvent.getX());
                    dragData.e.setEndY(mouseEvent.getY());
                    controller.addEdgeToPane(dragData.e);
                }
            }
        });

        setOnMouseReleased(mouseEvent -> {

            /* append edge to second vertex */
            Node target = mouseEvent.getPickResult().getIntersectedNode();
            if(target instanceof Circle) {
                Edge nE = new Edge(
                        dragData.e.startXProperty(),
                        dragData.e.startYProperty(),
                        ((Circle) target).centerXProperty(),
                        ((Circle) target).centerYProperty()
                );
                nE.endXProperty().bind(((Circle) target).centerXProperty());
                nE.endYProperty().bind(((Circle) target).centerYProperty());
                System.out.println(nE);
                controller.removeEdgeFromPane(dragData.e);
                controller.addEdgeToPane(nE);
            }
            System.out.println(mouseEvent.getPickResult());
        });
    }

    public Vertex(PrimaryController controller, T weight, DoubleProperty centerX, DoubleProperty centerY) {
        super(centerX.get(), centerY.get(), 20.0, Paint.valueOf("#A7ABDD"));
        this.controller = controller;
        this.weight = weight;
        centerX.bind(centerXProperty());
        centerY.bind(centerYProperty());
        enableDrag();
    }
}

