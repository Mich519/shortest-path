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

public class BellmanFord implements Algorithm {

    private final Graph graph;
    private final Map<Vertex, Vertex> predecessors;
    public BellmanFord(Graph graph) {
        this.graph = graph;
        this.predecessors =  new HashMap<>();
    }

    @Override
    public void run() throws Exception {
        Map<Vertex, Double> distances = new HashMap<>();
        predecessors.put(graph.getStartVertex(), null); // starting vertex doesn't have predecessor
        for (Vertex v : graph.getVertices()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(graph.getStartVertex(), 0.0); // distance to source is zero

        // perform relaxation4
        for (int i=0; i<graph.getVertices().size(); i++) {
            for(Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges()) {
                    Vertex neighbour = e.getNeighbourOf(v);
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
                Vertex neighbour = e.getNeighbourOf(v);
                if (distances.get(v) + e.getLength().get() < distances.get(neighbour)) {
                    throw new Exception("Graph contains negative weight cycle.");
                }
            }
        }
    }

    @Override
    public void animate(PrimaryController controller) {
        // animation
        controller.toogleButtonsActivity(true);
        Set<Vertex> shortestPath = new LinkedHashSet<>();
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v = graph.getEndVertex(); v != null ; v = predecessors.get(v)) {
            transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
            shortestPath.add(v);
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            controller.toogleButtonsActivity(false);
        });
    }
}
