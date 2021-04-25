package org.example.simulation.algorithms;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.graph.VertexComparator;

import java.util.LinkedHashSet;
import java.util.PriorityQueue;

public class Dijkstra {
    private Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public void run() {
        System.out.println("Dijkstra started!");
        System.out.println("Start: " + graph.getStartVertex());
        System.out.println("Start: " + graph.getEndVertex());
        LinkedHashSet<Vertex> s = new LinkedHashSet<>(); // s - will be storing ordered vertices that represent shortest path at the end
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, new VertexComparator());
        q.addAll(graph.getVertices());

        while (!q.isEmpty()) {
            Vertex v = q.poll();
            s.add(v);
            for (Edge w : v.getAdjEdges()) {
                /* perform relaxation for every vertex adjacent to v */

                Vertex u = w.getDestination();
                if (v.getCurLowestCost() + w.getWeight() < u.getCurLowestCost()) {
                    u.setCurLowestCost(v.getCurLowestCost() + w.getWeight());
                    // update the priority queue by reinserting the vertex
                    q.remove(u);
                    q.add(u);
                }
            }
        }
        s.forEach(System.out::println);
    }
}

