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


    private final Vertex source;
    private final Vertex destination;

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
        StringProperty sp = new SimpleStringProperty();
        DoubleProperty dp = new SimpleDoubleProperty();
        dp.bind(length);
        Bindings.bindBidirectional(sp, dp, new NumberStringConverter());
        this.lengthLabel = new Label();
        lengthLabel.setTextFill(Paint.valueOf("ddd8c4"));
        lengthLabel.textProperty().bind(sp);


        lengthLabel.layoutXProperty().bind(endXProperty().subtract(endXProperty().subtract(startXProperty()).divide(2)));
        lengthLabel.layoutYProperty().bind(endYProperty().subtract(endYProperty().subtract(startYProperty()).divide(2)));
    }

    public Edge(Vertex source, Vertex destination) {
        super(source.centerXProperty().get(), source.centerYProperty().get(), destination.centerXProperty().get(), destination.centerYProperty().get());
        this.source = source;
        this.destination = destination;
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
}
