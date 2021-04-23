package org.example.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class Edge extends Line {

    public Edge(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
        super(startX.get(), startY.get(), endX.get(), endY.get());
        startXProperty().bind(startX);
        startYProperty().bind(startY);
        //endXProperty().bind(endX);
        //endYProperty().bind(endY);
        setStrokeWidth(2);
        setStroke(Color.LIMEGREEN);
        setStrokeLineCap(StrokeLineCap.BUTT);
        getStrokeDashArray().setAll(10.0, 5.0);
        setMouseTransparent(true);
    }
}
