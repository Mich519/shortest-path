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

import java.util.*;
// todo: algorithm doesn't work when graph is loaded from a file

public class AntOptimization implements Algorithm {
    public double pheromonePerAnt; // amount of dropped pheromone
    public int numOfAnts;
    public double evapRate; // evaporation rate
    public double alpha;
    public double beta;
    private int numOfIterations;

    private final PrimaryController controller;
    private Graph graph;
    private final List<Set<Edge>> allPaths;

    public AntOptimization(PrimaryController controller) {
        this.controller = controller;
        this.allPaths = new ArrayList<>();
    }

    private void initParameters() {
        pheromonePerAnt = controller.getPheromonePerAnt().getValue();
        numOfAnts = (int)controller.getNumberOfAnts().getValue();
        evapRate = controller.getEvaporationRate().getValue();
        alpha = controller.getAlpha().getValue();
        beta = controller.getBeta().getValue();
        numOfIterations = (int)controller.getNumberOfIterations().getValue();
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
            sum += e.getLength().get();
        }
        return sum;
    }

    private void initAnts(Set<Ant> ants) {
        for (int i = 0; i < numOfAnts; i++) {
            Ant a = new Ant(graph, alpha, beta, pheromonePerAnt);
            ants.add(a);
        }
    }

    private void evaporation() {
        // evaporation
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                double f = (1 - evapRate) * e.getPheromone();
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
                    Set<Edge> temp =  new LinkedHashSet<>(a.getTraversedEdges());
                    currentShortestPath.addAll(temp); // copy constructor
                    allPaths.add(temp);
                    a.updateTraversedEdges();
                    System.out.println("New shortest found! with length " + sumOfWeight(a.getTraversedEdges()));
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Ants started");
        initParameters();
        graph = controller.getGraph();
        Set<Ant> ants = new HashSet<>();
        Set<Edge> currentShortestPath = new LinkedHashSet<>();

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
        int i = 0;
        for (Set<Edge> s : allPaths) {
            Color randomColor = Color.rgb(colorShades * i++, 0, 0);
            for (Edge e : s) {
                transitions.add(new StrokeTransition(Duration.millis(1001 - controller.getSimulationSpeed().getValue()), e, Edge.defaultColor, randomColor));
                transitions.add(new StrokeTransition(Duration.millis(1001 - controller.getSimulationSpeed().getValue()), graph.findSameEdge(e), Edge.defaultColor, randomColor));
            }
        }

        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            for (Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges()) {
                    // reset edge pheromone
                    e.setPheromone(1.0 / e.getLength().get());
                }
            }
            allPaths.clear();
        });
    }
}
