package org.example.graph.serializer;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class EdgeSerialized implements Serializable {
    VertexSerialized source;
    VertexSerialized destination;

    public EdgeSerialized(VertexSerialized source, VertexSerialized destination) {
        this.source = source;
        this.destination = destination;
    }
}
