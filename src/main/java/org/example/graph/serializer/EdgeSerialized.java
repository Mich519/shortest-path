package org.example.graph.serializer;

import javafx.util.Pair;
import lombok.Getter;
import org.example.graph.Vertex;

import java.io.Serializable;

@Getter
public class EdgeSerialized implements Serializable {
    Pair<VertexSerialized, VertexSerialized> vertices;

    public EdgeSerialized(VertexSerialized source, VertexSerialized destination) {
        vertices = new Pair<>(source, destination);
    }

    public VertexSerialized getNeighbourOf(VertexSerialized v) {
        if (vertices.getKey() == v)
            return vertices.getValue();
        else if (vertices.getValue() == v)
            return vertices.getKey();
        return null;
    }
}
