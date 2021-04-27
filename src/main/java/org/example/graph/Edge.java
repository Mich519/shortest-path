package org.example.graph;

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

    public Edge(Vertex source, Vertex destination) {
        super(source.centerXProperty().get(), source.centerYProperty().get(), destination.centerXProperty().get(), destination.centerYProperty().get());
        this.source = source;
        this.destination = destination;
        this.pheromone = 0.0;
        startXProperty().bind(source.centerXProperty());
        startYProperty().bind(source.centerYProperty());
        endXProperty().bind(destination.centerXProperty());
        endYProperty().bind(destination.centerYProperty());
        setStrokeWidth(4);
        setStroke(Edge.defaultColor);
        setMouseTransparent(true);
    }

    public double calculateWeight() {
        return Math.sqrt(Math.pow(getEndX() - getStartX(), 2) + Math.pow(getEndY() - getStartY(), 2));
    }
}
