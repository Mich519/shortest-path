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
import org.example.graph.comparators.VertexByTotalCostComparator;

import java.util.*;

public class AStar implements Algorithm {

    private final PrimaryController controller;

    public AStar(PrimaryController controller) {
        this.controller = controller;
    }

    private void simulateTraversal(Graph graph, LinkedHashSet<Vertex> s, HashMap<Vertex, Vertex> mapVertexToPrev) {
        /* vertices animation */
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v : s) {
            transitions.add(new FillTransition(Duration.millis(500), v, Color.ORANGE, Color.BLUEVIOLET));
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);

        /* color edges that represent a path */
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            transitions.add(new FillTransition(Duration.millis(500), ver, Color.ORANGE, Color.RED));
            Vertex pred = mapVertexToPrev.get(ver);
            if (pred != null) {
                Edge e = ver.findEdgeConnectedTo(pred);
                transitions.add(new StrokeTransition(Duration.millis(500), e, Color.LIMEGREEN, Color.BLUE));
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
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, new VertexByTotalCostComparator(graph));
        q.addAll(graph.getVertices());
        while (!q.isEmpty()) {
            Vertex v = q.poll();
            s.add(v);
            for (Edge w : v.getAdjEdges()) {
                /* perform relaxation for every vertex adjacent to v */
                Vertex u = w.getNeighbourOf(v);
                if (v.getCurLowestCost() + w.getLength().get() < u.getCurLowestCost()) {

                    // update predecessor of current vertex
                    u.setCurLowestCost(v.getCurLowestCost() + w.getLength().get());
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
