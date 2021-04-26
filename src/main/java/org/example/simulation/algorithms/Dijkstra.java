package org.example.simulation.algorithms;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.graph.VertexComparator;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;

public class Dijkstra {
    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public void run() {
        HashMap<Vertex, Vertex> mapVertexToPrev = new HashMap<>(); // maps vertex to its predecessor in a path
        LinkedHashSet<Vertex> s = new LinkedHashSet<>(); // s - will be storing ordered vertices that represent shortest path at the end
        mapVertexToPrev.put(graph.getStartVertex(), null);
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, new VertexComparator());
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
                    if(mapVertexToPrev.get(u) == null)
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

        /* color edges that represent a path */
        for (Vertex ver = graph.getEndVertex(); ver != null ; ver = mapVertexToPrev.get(ver)) {
            ver.setFill(Color.PINK);
            Vertex pred = mapVertexToPrev.get(ver);
            for (Edge e : ver.getAdjEdges()) {
                if(pred != null && e.getDestination() == pred) {
                    e.setStroke(Color.RED);
                    for (Edge d : pred.getAdjEdges()) {
                        if(d.getDestination() == ver) {
                            d.setStroke(Color.RED);
                        }
                    }
                }
            }
        }

        s.forEach(System.out::println);
    }
}




