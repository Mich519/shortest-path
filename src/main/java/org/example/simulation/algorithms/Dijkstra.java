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
import org.example.graph.comparators.VertexByCurLowestCostComparator;

import java.util.*;

// todo: edge label with weight
// todo: path traversing optimization
// todo: AStar and Dijkstra code is similiar - remove redundancy
// todo: create wrapper for vertices

public class Dijkstra implements Algorithm{

    private final PrimaryController controller;
    public Dijkstra(PrimaryController controller) {
        this.controller = controller;
    }

    private void simulateTraversal(Graph graph, LinkedHashSet<Vertex> s, HashMap<Vertex, Vertex> mapVertexToPrev) {
        /* vertices animation */
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v : s) {
            transitions.add(new FillTransition(Duration.millis(1000 - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);

        /* color edges that represent a path */
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            transitions.add(new FillTransition(Duration.millis(1000 - controller.getSimulationSpeed().getValue()), ver, Color.ORANGE, Color.RED));
            Vertex pred = mapVertexToPrev.get(ver);
            if (pred != null) {
                Edge e1 = ver.findEdgeConnectedTo(pred);
                Edge e2 = pred.findEdgeConnectedTo(ver);
                transitions.add(new StrokeTransition(Duration.millis(1000 - controller.getSimulationSpeed().getValue()), e1, Color.LIMEGREEN, Color.BLUE));
                transitions.add(new StrokeTransition(Duration.millis(1000 - controller.getSimulationSpeed().getValue()), e2, Color.LIMEGREEN, Color.BLUE));
            }
        }
        st.getChildren().addAll(transitions);
        st.play();

        /* reset everything */
        st.setOnFinished(actionEvent -> {
            for (Vertex v : graph.getVertices()) {
                v.setCurLowestCost(Double.POSITIVE_INFINITY);
            }
        });
    }

    @Override
    public void run() {
        Graph graph = controller.getGraph();
        HashMap<Vertex, Vertex> mapVertexToPrev = new HashMap<>(); // maps vertex to its predecessor in a path
        LinkedHashSet<Vertex> s = new LinkedHashSet<>(); // s - will be storing ordered vertices that represent shortest path at the end
        mapVertexToPrev.put(graph.getStartVertex(), null);
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, new VertexByCurLowestCostComparator());
        q.addAll(graph.getVertices());
        while (!q.isEmpty()) {
            Vertex v = q.poll();
            s.add(v);
            for (Edge w : v.getAdjEdges()) {
                /* perform relaxation for every vertex adjacent to v */
                Vertex u = w.getDestination();
                if (v.getCurLowestCost() + w.calculateWeight() < u.getCurLowestCost()) {

                    // update predecessor of current vertex
                    u.setCurLowestCost(v.getCurLowestCost() + w.calculateWeight());
                    mapVertexToPrev.put(u, v);

                    // update the priority queue by reinserting the vertex
                    q.remove(u);
                    q.add(u);
                }
            }
            if (v == graph.getEndVertex())
                break;
        }

        /* vertices animation */
        simulateTraversal(graph, s, mapVertexToPrev);
    }
}




