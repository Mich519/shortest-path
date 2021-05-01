package org.example.graph;

import javafx.scene.paint.Color;
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
        //Color color = Edge.defaultColor;
        Edge e1 = new Edge(v1, v2);
        Edge e2 = new Edge(v2, v1);
        //e1.setColor(color);
        //e2.setColor(color);
        v1.getAdjEdges().add(e1);
        v2.getAdjEdges().add(e2);
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

    public Edge findSameEdge(Edge e) {
        for (Edge e1 : e.getDestination().getAdjEdges()) {
            if(e1.getDestination() == e.getSource())
                return e1;
        }
        return null;
    }

    public void setStartVertex(Vertex v) {
        this.startVertex = v;
    }

    public void setEndVertex(Vertex v) {
        this.endVertex = v;
    }
}
