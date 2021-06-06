package org.example.graph;

import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;


public class Graph implements Serializable {
    @Getter
    private final HashSet<Vertex> vertices;
    @Getter
    private Vertex startVertex;
    @Getter
    private Vertex endVertex;

    public Graph() {
        vertices = new HashSet<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addEdge(Vertex v1, Vertex v2) {
        for (Edge e : v1.getAdjEdges()) {
            if(e.getNeighbourOf(v1) == v2) return; // there is already edge connecting v1 and v2
        }
        Edge e = new Edge(v1, v2);
        v1.getAdjEdges().add(e);
        v2.getAdjEdges().add(e);
    }

    public void addDirectedEdge(Vertex src, Vertex dst  ) {
        src.getAdjEdges().add(new Edge(src, dst));
    }

    public void removeVertex(Vertex v) {
        vertices.remove(v);
        vertices.forEach(vertex -> vertex.getAdjEdges().removeIf(edge -> edge.getNeighbourOf(vertex) == v));
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
