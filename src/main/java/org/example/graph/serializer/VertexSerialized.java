package org.example.graph.serializer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

import java.io.Serializable;
import java.util.HashSet;

@Setter
@Getter
@EqualsAndHashCode
public class VertexSerialized implements Serializable {
    String centerX, centerY;
    HashSet<EdgeSerialized> adjEdges;

    public VertexSerialized(Vertex v) {
        this.centerX = String.valueOf(v.getCenterX());
        this.centerY = String.valueOf(v.getCenterY());
        this.adjEdges = new HashSet<>();
    }

    public Vertex unwrap(PrimaryController controller) {
        DoubleProperty centerX = new SimpleDoubleProperty(Double.parseDouble(this.centerX));
        DoubleProperty centerY = new SimpleDoubleProperty(Double.parseDouble(this.centerY));
        return new Vertex(controller, centerX, centerY);
    }
}
