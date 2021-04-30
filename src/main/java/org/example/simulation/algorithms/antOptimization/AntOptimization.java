package org.example.simulation.algorithms.antOptimization;

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

public class AntOptimization {
    public static final int numOfAnts = 2000;
    public static final double evapRate = 0.7; // evaporation rate
    public static final double alpha = 2.1;
    public static final double beta = 2.1;
    private static final int numOfIterations = 100;
    private final List<Set<Edge>> allPaths;

    public AntOptimization() {
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
                if (currentShortestPath.isEmpty() || Double.compare(sumOfWeight(a.getTraversedEdges()), sumOfWeight(currentShortestPath)) < 0) {
                    currentShortestPath.clear();
                    currentShortestPath.addAll(a.getTraversedEdges());
                    allPaths.add(currentShortestPath);
                    a.updateTraversedEdges(graph);
                    System.out.println("New shortest found! with length " + sumOfWeight(a.getTraversedEdges()));
                }
            }
        }
    }

    public void run(PrimaryController controller) {
        Graph graph = controller.getGraph();
        System.out.println("Ants started");
        Set<Ant> ants = new HashSet<>();
        Set<Edge> currentShortestPath = new HashSet<>();


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
            ants.forEach(Ant::resetPosition);
        }

        System.out.println("Ants finished");

        // animate
        /* vertices animation */
        List<Transition> transitions = new ArrayList<>();
        SequentialTransition st = new SequentialTransition();
        //System.out.println(allPaths);
        st.setCycleCount(1);
        allPaths.forEach(System.out::println);

        for (Set<Edge> s : allPaths) {
            Color randomColor = Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            for (Edge e : s) {
                transitions.add(new StrokeTransition(Duration.millis(50), e, Edge.defaultColor, randomColor));
                transitions.add(new StrokeTransition(Duration.millis(50), graph.findSameEdge(e), Edge.defaultColor, randomColor));
            }
        }
        st.getChildren().addAll(transitions);
        st.play();

    }
}
