package org.example.simulation.algorithms;

import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class BellmanFord implements Algorithm{

    private final PrimaryController controller;

    public BellmanFord(PrimaryController controller) {
        this.controller = controller;
    }

    @Override
    public void run() throws Exception {
        Graph graph = controller.getGraph();
        Map<Vertex, Double> distances = new HashMap<>();
        Map<Vertex, Vertex> predecessors = new HashMap<>();

        for (Vertex v : graph.getVertices()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(graph.getStartVertex(), 0.0); // distance to source is zero

        // perform relaxation
        for(Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                Vertex neighbour = e.getDestination();
                if(distances.get(v) + e.calculateWeight() < distances.get(neighbour)) {
                    distances.put(neighbour, distances.get(v) + e.calculateWeight());
                    predecessors.put(neighbour, v);
                }
            }
        }

        // check for negative weight cycles
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                Vertex neighbour = e.getDestination();
                if (distances.get(v) + e.calculateWeight() < distances.get(neighbour)) {
                    throw new Exception("Graph contains negative weight cycle.");
                }
            }
        }
    }
}
