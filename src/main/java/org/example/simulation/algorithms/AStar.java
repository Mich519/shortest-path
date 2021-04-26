package org.example.simulation.algorithms;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.graph.comparators.VertexByTotalCostComparator;

import java.util.*;

public class AStar {
    private final Graph graph;

    public AStar(Graph graph) {
        this.graph = graph;
    }

    private void simulateTraversal(LinkedHashSet<Vertex> s, HashMap<Vertex, Vertex> mapVertexToPrev) {
        /* vertices animation */
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v : s) {
            transitions.add(new FillTransition(Duration.millis(500), v, Color.ORANGE, Color.BLUEVIOLET));
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);

        /* color edges that represent a path */
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            Vertex pred = mapVertexToPrev.get(ver);
            transitions.add(new FillTransition(Duration.millis(500), ver, Color.ORANGE, Color.RED));
            for (Edge e : ver.getAdjEdges()) {
                if (pred != null && e.getDestination() == pred) {
                    transitions.add(new StrokeTransition(Duration.millis(500), e, Color.LIMEGREEN, Color.BLUE));
                    for (Edge d : pred.getAdjEdges()) {
                        if (d.getDestination() == ver) {
                            transitions.add(new StrokeTransition(Duration.millis(500), d, Color.LIMEGREEN, Color.BLUE));
                        }
                    }
                }
            }
        }
        st.getChildren().addAll(transitions);
        st.play();
    }

    public void run() {
        HashMap<Vertex, Vertex> mapVertexToPrev = new HashMap<>(); // maps vertex to its predecessor in a path
        LinkedHashSet<Vertex> s = new LinkedHashSet<>(); // s - will be storing ordered vertices that represent shortest path at the end
        mapVertexToPrev.put(graph.getStartVertex(), null);
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, new VertexByTotalCostComparator(graph));
        q.addAll(graph.getVertices());
        while (!q.isEmpty()) {
            Vertex v = q.poll();
            v.setFill(Paint.valueOf("#3366ff"));
            s.add(v);
            for (Edge w : v.getAdjEdges()) {
                /* perform relaxation for every vertex adjacent to v */
                Vertex u = w.getDestination();
                System.out.println(w.calculateWeight());
                if (v.getCurLowestCost() + w.calculateWeight() < u.getCurLowestCost()) {
                    u.setCurLowestCost(v.getCurLowestCost() + w.calculateWeight());
                    // update predecessor of current vertex
                    if (mapVertexToPrev.get(u) == null)
                        mapVertexToPrev.put(u, v);
                    else
                        mapVertexToPrev.replace(u, v);
                    // update the priority queue by reinserting the vertex
                    q.remove(u);
                    q.add(u);
                }
            }
            if (v == graph.getEndVertex())
                break;
        }
        simulateTraversal(s, mapVertexToPrev);
    }
}
