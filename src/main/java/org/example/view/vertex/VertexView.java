package org.example.view.vertex;

import javafx.scene.paint.Paint;
import lombok.*;
import javafx.scene.shape.Circle;

@Getter
public class VertexView extends Circle {

    public VertexView(double centerX, double centerY) {
        super(centerX, centerY, 20.0, Paint.valueOf("#A7ABDD"));
    }

}
