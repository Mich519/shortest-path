package org.example.graph.serializer;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;

@Getter
public class GraphSerialized implements Serializable {
    private final HashSet<VertexSerialized> wrappedVertices;

    public GraphSerialized() {
        this.wrappedVertices = new HashSet<>();
    }

}
