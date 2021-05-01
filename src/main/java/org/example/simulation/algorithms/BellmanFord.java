package org.example.simulation.algorithms;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;

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
        predecessors.put(graph.getStartVertex(), null); // starting vertex doesn't have predecessor
        for (Vertex v : graph.getVertices()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(graph.getStartVertex(), 0.0); // distance to source is zero

        // perform relaxation4
        for (int i=0; i<graph.getVertices().size(); i++) {
            for(Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges()) {
                    Vertex neighbour = e.getDestination();
                    if(distances.get(v) + e.getLength().get() < distances.get(neighbour)) {
                        distances.put(neighbour, distances.get(v) + e.getLength().get());
                        predecessors.put(neighbour, v);
                    }
                }
            }
        }

        // check for negative weight cycles
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                Vertex neighbour = e.getDestination();
                if (distances.get(v) + e.getLength().get() < distances.get(neighbour)) {
                    throw new Exception("Graph contains negative weight cycle.");
                }
            }
        }

        // animation
        Set<Vertex> shortestPath = new LinkedHashSet<>();
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v = graph.getEndVertex(); v != null ; v = predecessors.get(v)) {
            transitions.add(new FillTransition(Duration.millis(500), v, Color.ORANGE, Color.BLUEVIOLET));
            shortestPath.add(v);
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
    }
}
