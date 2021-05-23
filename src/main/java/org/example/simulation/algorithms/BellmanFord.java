package org.example.simulation.algorithms;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
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
    private final List<Vertex> shortestPath;
    public BellmanFord(Graph graph) {
        this.graph = graph;
        this.predecessors =  new HashMap<>();
        this.shortestPath = new ArrayList<>();
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

        for (Vertex v = graph.getEndVertex(); v != null ; v = predecessors.get(v)) {
            shortestPath.add(v);
        }
        Collections.reverse(shortestPath);
    }

    @Override
    public void animate(PrimaryController controller) {
        controller.toogleButtonsActivity(true);
        List<Transition> transitions = new ArrayList<>();
        for (int i=1; i<shortestPath.size(); i++) {
            Vertex v1 = shortestPath.get(i - 1);
            Vertex v2 = shortestPath.get(i);
            Edge e = v1.findEdgeConnectedTo(v2);
            transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v1, Vertex.defaultColor, Vertex.transitionColor));
            transitions.add(new StrokeTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), e, Edge.defaultColor, Edge.transitionColor));
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
