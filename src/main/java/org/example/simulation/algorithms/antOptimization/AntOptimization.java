package org.example.simulation.algorithms.antOptimization;

import javafx.scene.paint.Color;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AntOptimization {
    public static final int numOfAnts = 2000;
    public static final double evapRate = 0.7; // evaporation rate
    public static final double alpha = 2.1;
    public static final double beta = 2.1;
    private static final int numOfIterations = 100;

    public AntOptimization() {
    }

    private boolean antsFinished(Set<Ant> ants) {
        for (Ant a : ants) {
            if (!a.isFinished())
                return false;
        }
        return true;
    }

    private double sumOfWeight(Set<Edge> edges) {
        double sum = 0.0;
        for (Edge e : edges) {
            sum += e.calculateWeight();
        }
        return sum;
    }

    private void initAnts(Set<Ant> ants, Graph graph) {
        for (int i = 0; i < numOfAnts; i++) {
            Ant a = new Ant(graph.getStartVertex(), graph.getEndVertex());
            ants.add(a);
        }
    }

    private void evaporation(Graph graph) {
        // evaporation
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                double f = (1 - AntOptimization.evapRate) * e.getPheromone();
                e.setPheromone(f);
                graph.findSameEdge(e).setPheromone(f);
            }
        }
    }

    private void updatePheromoneOnCurrentBestPath(Set<Ant> ants, Set<Edge> currentShortestPath, Graph graph) {
        // all ants finished - update pheromone for the best path
        for (Ant a : ants) {
            if (a.getCurVertex() == a.getEndVertex()) {
                // ant reached the goal - update pheromone on that path
                if (currentShortestPath.isEmpty() || sumOfWeight(a.getTraversedEdges()) < sumOfWeight(currentShortestPath)) {
                    System.out.println("New shortest found!");
                    currentShortestPath.clear();
                    currentShortestPath.addAll(a.getTraversedEdges());
                    System.out.println(currentShortestPath);
                    a.updateTraversedEdges(graph);
                }
            }
        }
    }

    public void run(PrimaryController controller) {
        Graph graph = controller.getGraph();
        System.out.println("Ants started");
        Set<Ant> ants = new HashSet<>();
        Set<Edge> currentShortestPath = new HashSet<>();
        Edge highestPheromoneEdge = null;

        initAnts(ants, graph);

        for (int i = 0; i < numOfIterations; i++) {
            while (!antsFinished(ants)) {
                for (Ant ant : ants) {
                    if (!ant.isFinished())
                        ant.makeMove(graph);
                }
            }

            evaporation(graph);
            updatePheromoneOnCurrentBestPath(ants, currentShortestPath, graph);
        }
        ants.forEach(Ant::resetPosition);

        double scale = 1.0;
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                if (highestPheromoneEdge == null || e.getPheromone() > highestPheromoneEdge.getPheromone())
                    highestPheromoneEdge = e;
                scale = (255 / highestPheromoneEdge.getPheromone()) - 1;
                //System.out.println(e.getPheromone());
            }
        }
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                e.setStroke(Color.rgb((int) (e.getPheromone() * scale), (int) (e.getPheromone() * scale), (int) (e.getPheromone() * scale)));
            }

        }
        System.out.println(currentShortestPath);
        for (Edge e : currentShortestPath) {
            e.setStroke(Color.BLUE);
            graph.findSameEdge(e).setStroke(Color.BLUE);
        }
        System.out.println("Ants finished");
    }
}
