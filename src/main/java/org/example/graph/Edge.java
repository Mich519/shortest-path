package org.example.graph;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Edge extends Line implements Serializable {
    public static final Color defaultColor = Color.LIMEGREEN;
    public static final Color pathColor = Color.BLUE;

    private final Pair<Vertex, Vertex> vertices;
    //private final Vertex source;
    //private final Vertex destination;

    private DoubleBinding length;
    @Setter
    private double pheromone;
    @Setter
    private Color color;
    private Label lengthLabel;
    private SimpleStringProperty ssp;

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

    private void bindLabel() {
        this.ssp = new SimpleStringProperty();
        DoubleProperty dp = new SimpleDoubleProperty();
        dp.bind(length);
        Bindings.bindBidirectional(ssp, dp, new NumberStringConverter());
        this.lengthLabel = new Label();
        lengthLabel.setMouseTransparent(true);
        lengthLabel.setTextFill(Paint.valueOf("ddd8c4"));
        lengthLabel.textProperty().bind(ssp);


        lengthLabel.layoutXProperty().bind(endXProperty().subtract(endXProperty().subtract(startXProperty()).divide(2)));
        lengthLabel.layoutYProperty().bind(endYProperty().subtract(endYProperty().subtract(startYProperty()).divide(2)));
    }

    public Edge(Vertex source, Vertex destination) {
        super(source.centerXProperty().get(), source.centerYProperty().get(), destination.centerXProperty().get(), destination.centerYProperty().get());
        vertices = new Pair<>(source, destination);
        this.color = Edge.defaultColor;

        startXProperty().bind(source.centerXProperty());
        startYProperty().bind(source.centerYProperty());
        endXProperty().bind(destination.centerXProperty());
        endYProperty().bind(destination.centerYProperty());
        setStrokeWidth(4);

        setStroke(color);
        setMouseTransparent(true);
        bindLength();
        bindLabel();
        this.pheromone = 1.0 / length.get(); // initial pheromone value
    }

    public Vertex getNeighbourOf(Vertex v) {
        if (vertices.getKey() == v)
            return vertices.getValue();
        else if (vertices.getValue() == v)
            return vertices.getKey();
        return null;
    }
}
