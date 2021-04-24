package org.example.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class Edge extends Line {


    public Edge(Vertex start, Vertex end) {
        super(start.centerXProperty().get(), start.centerYProperty().get(), end.centerXProperty().get(), end.centerYProperty().get());
        startXProperty().bind(start.centerXProperty());
        startYProperty().bind(start.centerYProperty());
        endXProperty().bind(end.centerXProperty());
        endYProperty().bind(end.centerYProperty());
        setStrokeWidth(2);
        setStroke(Color.LIMEGREEN);
        setStrokeLineCap(StrokeLineCap.BUTT);
        getStrokeDashArray().setAll(10.0, 5.0);
        setMouseTransparent(true);
    }
}
