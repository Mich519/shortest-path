package org.example.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    T - weight type of vertex
 */

@Getter
public class Graph {
    private final Map<Vertex, List<Vertex>> vertices;

    public Graph() {
        vertices = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        vertices.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(Vertex v1, Vertex v2) {
        vertices.get(v1).add(v2);
        vertices.get(v2).add(v1);
    }

    public void removeVertex(Vertex v) {
        vertices.values().forEach(adjVertices -> adjVertices.remove(v));
        vertices.remove(v);
    }
}
