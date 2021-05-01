package org.example.graph;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Edge extends Line implements Serializable {
    public static final Color defaultColor = Color.LIMEGREEN;
    public static final Color pathColor = Color.BLUE;

    private final Vertex source;
    private final Vertex destination;
    @Setter
    private double pheromone;
    private DoubleBinding length;

    private void bindLength() {
        this.length = new DoubleBinding() {
            {
                super.bind(startXProperty());
                super.bind(startYProperty());
                super.bind(endXProperty());
                super.bind(endYProperty());
            }

            @Override
            protected double computeValue() {
                return Math.sqrt(Math.pow(endXProperty().get() - startXProperty().get(), 2) + Math.pow(endYProperty().get() - startYProperty().get(), 2));
            }
        };
    }

    public Edge(Vertex source, Vertex destination) {
        super(source.centerXProperty().get(), source.centerYProperty().get(), destination.centerXProperty().get(), destination.centerYProperty().get());
        this.source = source;
        this.destination = destination;

        startXProperty().bind(source.centerXProperty());
        startYProperty().bind(source.centerYProperty());
        endXProperty().bind(destination.centerXProperty());
        endYProperty().bind(destination.centerYProperty());
        setStrokeWidth(4);
        setStroke(Edge.defaultColor);
        setMouseTransparent(true);
        bindLength();
        this.pheromone = 1.0 / length.get(); // initial pheromone value
    }
}
