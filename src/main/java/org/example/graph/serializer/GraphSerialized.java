package org.example.graph.serializer;

import lombok.Getter;
import org.example.graph.Edge;

import java.io.Serializable;
import java.util.HashSet;

@Getter
public class GraphSerialized implements Serializable {
    private final HashSet<VertexSerialized> wrappedVertices;

    public GraphSerialized() {
        this.wrappedVertices = new HashSet<>();
    }

    public void addEdge(VertexSerialized v1, VertexSerialized v2) {
        for (EdgeSerialized e: v1.getAdjEdges())  {
            if(e.getNeighbourOf(v1) == v2) return; // there is already edge connecting v1 and v2
        }
        EdgeSerialized e = new EdgeSerialized(v1, v2);
        v1.getAdjEdges().add(e);
        v2.getAdjEdges().add(e);
    }
}
