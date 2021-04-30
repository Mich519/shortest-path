package org.example.simulation.algorithms.antOptimization;

import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.simulation.algorithms.Algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntOptimization implements Algorithm {
    public static final double droppedPheromone = 1000; // amount of dropped pheromone
    public static final int numOfAnts = 2000;
    public static final double evapRate = 0.7; // evaporation rate
    public static final double alpha = 2.1;
    public static final double beta = 2.1;
    private static final int numOfIterations = 100;

    private final PrimaryController controller;
    private Graph graph;
    private final List<Set<Edge>> allPaths;

    public AntOptimization(PrimaryController controller) {
        this.controller = controller;
        this.controller.getGraph();
        this.allPaths = new ArrayList<>();
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

    private void initAnts(Set<Ant> ants) {
        for (int i = 0; i < numOfAnts; i++) {
            Ant a = new Ant(graph);
            ants.add(a);
        }
    }

    private void evaporation() {
        // evaporation
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                double f = (1 - AntOptimization.evapRate) * e.getPheromone();
                e.setPheromone(f);
                graph.findSameEdge(e).setPheromone(f);
            }
        }
    }

    private void updatePheromoneOnCurrentBestPath(Set<Ant> ants, Set<Edge> currentShortestPath) {
        // all ants finished - update pheromone for the best path
        for (Ant a : ants) {
            if (a.getCurVertex() == a.getEndVertex()) {
                // ant reached the goal - update pheromone on that path
                if (currentShortestPath.isEmpty() || sumOfWeight(a.getTraversedEdges()) < (sumOfWeight(currentShortestPath))) {
                    currentShortestPath.clear();
                    currentShortestPath.addAll(new HashSet<>(a.getTraversedEdges())); // copy constructor
                    allPaths.add(new HashSet<>(a.getTraversedEdges()));
                    a.updateTraversedEdges();
                    System.out.println("New shortest found! with length " + sumOfWeight(a.getTraversedEdges()));
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Ants started");
        graph = controller.getGraph();
        Set<Ant> ants = new HashSet<>();
        Set<Edge> currentShortestPath = new HashSet<>();

        initAnts(ants);

        for (int i = 0; i < numOfIterations; i++) {
            while (!antsFinished(ants)) {
                for (Ant ant : ants) {
                    if (!ant.isFinished())
                        ant.makeMove();
                }
            }

            evaporation();
            updatePheromoneOnCurrentBestPath(ants, currentShortestPath);
            ants.forEach(Ant::resetPosition);
        }

        System.out.println("Ants finished");

        // animate
        List<Transition> transitions = new ArrayList<>();
        SequentialTransition st = new SequentialTransition();

        allPaths.forEach(System.out::println);
        int colorShades = 255 / allPaths.size();
        int i=0;
        for (Set<Edge> s : allPaths) {
            Color randomColor = Color.rgb(colorShades * i++, 0, 0);
            for (Edge e : s) {
                transitions.add(new StrokeTransition(Duration.millis(50), e, Edge.defaultColor, randomColor));
                //transitions.add(new StrokeTransition(Duration.millis(50), graph.findSameEdge(e), Edge.defaultColor, randomColor));
            }
        }
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            for (Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges()) {
                    // reset edge pheromon
                    e.setPheromone(1.0 / e.calculateWeight());
                }
            }
        });
    }
}
