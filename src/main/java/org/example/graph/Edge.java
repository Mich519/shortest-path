package org.example.graph;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Random;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Edge extends Line {
    private final Vertex source;
    private final Vertex destination;

    public Edge(Vertex source, Vertex destination) {
        super(source.centerXProperty().get(), source.centerYProperty().get(), destination.centerXProperty().get(), destination.centerYProperty().get());
        this.source = source;
        this.destination = destination;
        startXProperty().bind(source.centerXProperty());
        startYProperty().bind(source.centerYProperty());
        endXProperty().bind(destination.centerXProperty());
        endYProperty().bind(destination.centerYProperty());
        setStrokeWidth(2);
        setStroke(Color.LIMEGREEN);
        //setStrokeLineCap(StrokeLineCap.BUTT);
        //getStrokeDashArray().setAll(10.0, 5.0);
        setMouseTransparent(true);
    }

    public double calculateWeight() {
        return Math.sqrt(Math.pow(getEndX() - getStartX(), 2) + Math.pow(getEndY() - getStartY(), 2));
    }
}
