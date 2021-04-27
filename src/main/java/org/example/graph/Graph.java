package org.example.graph;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

@Getter
public class Graph implements Serializable {
    private final HashSet<Vertex> vertices;
    private Vertex startVertex;
    private Vertex endVertex;

    public Graph() {
        vertices = new HashSet<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addEdge(Vertex v1, Vertex v2) {
        v1.getAdjEdges().add(new Edge(v1, v2));
        v2.getAdjEdges().add(new Edge(v2, v1));
    }

    public void addDirectedEdge(Vertex src, Vertex dst  ) {
        src.getAdjEdges().add(new Edge(src, dst));
    }

    public void removeVertex(Vertex v) {
        vertices.remove(v);
        vertices.forEach(vertex -> vertex.getAdjEdges().removeIf(edge -> edge.getDestination() == v));
    }

    public void removeAll() {
        vertices.clear();
        startVertex = null;
        endVertex = null;
    }

    public double calcDistanceToGoal(Vertex start) {
        if(endVertex != null) {
            double temp1 = Math.pow(endVertex.getCenterX() - start.getCenterX(), 2);
            double temp2 = Math.pow(endVertex.getCenterY() - start.getCenterY(), 2);
            return Math.sqrt(temp1 + temp2);
        }
        return 0;
    }

    public void setStartVertex(Vertex v) {
        this.startVertex = v;
    }

    public void setEndVertex(Vertex v) {
        this.endVertex = v;
    }
}
