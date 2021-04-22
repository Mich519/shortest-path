package org.example.graph;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
public class Vertex<T> extends Circle {
    private final T weight;

    public Vertex(T weight, double centerX, double centerY) {
        super(centerX, centerY, 20.0, Paint.valueOf("#A7ABDD"));
        this.weight = weight;
    }
}
