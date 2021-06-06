package org.example.simulation.algorithms.antOptimization;

import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.graph.comparators.VertexByCurLowestCostComparator;
import org.example.simulation.algorithms.Algorithm;
import org.example.simulation.algorithms.Greedy;
import org.example.simulation.report.ReportContent;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AntOptimization implements Algorithm {
    private final AntParametersContainer parameters;
    private final Graph graph;
    private Map<Edge, Double> edgeToPheromoneMap;
    private List<Set<Edge>> allPaths;
    private List<Integer> successfulAntsPerIteration;
    private List<Double> shortestPathLengthPerIteration;

    public AntOptimization(Graph graph, AntParametersContainer parameters) {
        this.graph = graph;
        this.parameters = parameters;
    }

    private void initPheromone() {
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                edgeToPheromoneMap.putIfAbsent(e, 1.0 / e.getLength().get());
            }
        }
    }

    private double sumOfWeight(Set<Edge> edges) {
        double sum = 0.0;
        for (Edge e : edges) {
            sum += e.getLength().get();
        }
        return sum;
    }

    private void initAnts(List<Callable<Ant>> ants) {
        for (int i = 0; i < parameters.numOfAnts; i++) {
            Callable<Ant> a = new Ant(graph, parameters, edgeToPheromoneMap);
            ants.add(a);
        }
    }

    private void evaporation() {
        // evaporation
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                double pheromone = (1 - parameters.evapRate) * edgeToPheromoneMap.get(e);
                if (pheromone > 0.000001) {
                    // zero division prevention
                    edgeToPheromoneMap.put(e, pheromone);
                }
            }
        }
    }

    private void updatePheromoneOnSuccessfulPaths(List<Callable<Ant>> ants, Set<Edge> currentShortestPath) {
        // all ants finished - update pheromone for the best path
        int numOfSuccessfulAnts = 0;
        for (Callable<Ant> a : ants) {
            Ant ant = (Ant) a;
            if (ant.getCurVertex() == ant.getEndVertex()) {
                // ant reached the goal - update pheromone on that path
                if (currentShortestPath.isEmpty() || sumOfWeight(ant.getTraversedEdges()) < sumOfWeight(currentShortestPath)) {
                    currentShortestPath.clear();
                    currentShortestPath.addAll(ant.getTraversedEdges());
                    allPaths.add(ant.getTraversedEdges());
                }
                ant.updateTraversedEdges();
                numOfSuccessfulAnts++;
            }
        }
        successfulAntsPerIteration.add(numOfSuccessfulAnts);
        shortestPathLengthPerIteration.add(sumOfWeight(currentShortestPath));
    }

    @Override
    public void run() throws InterruptedException {
        this.edgeToPheromoneMap = new ConcurrentHashMap<>();
        this.shortestPathLengthPerIteration = new ArrayList<>(parameters.numOfIterations);
        this.allPaths = new ArrayList<>();
        Set<Edge> currentShortestPath = new LinkedHashSet<>();
        this.successfulAntsPerIteration = new ArrayList<>(parameters.numOfIterations);
        initPheromone();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < parameters.numOfIterations; i++) {
            List<Callable<Ant>> ants = new ArrayList<>();
            initAnts(ants);
            executorService.invokeAll(ants);
            evaporation();
            updatePheromoneOnSuccessfulPaths(ants, currentShortestPath);
        }
        executorService.shutdown();
    }

    @Override
    public void animate(PrimaryController controller) {
        // animate
        controller.toogleButtonsActivity(true);
        List<Transition> transitions = new ArrayList<>();
        SequentialTransition st = new SequentialTransition();

        int colorShades = 255 / allPaths.size();
        int i = 0;
        for (Set<Edge> s : allPaths) {
            Color randomColor = Color.rgb(255, 255 - colorShades * i, 255 - colorShades * i);
            for (Edge e : s) {
                transitions.add(new StrokeTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), e, Edge.DEFAULT_COLOR, randomColor));
            }
            i++;
        }

        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        controller.getStop().setOnMouseClicked(mouseEvent -> {
            // init stop animation button
            controller.drawGraph();
            st.stop();
            controller.toogleButtonsActivity(false);
        });
        st.play();

        st.setOnFinished(actionEvent -> {
            System.out.println("Animation finished");
            controller.toogleButtonsActivity(false);
        });
    }

    @Override
    public ReportContent generateReportContent() throws InterruptedException {
        ReportContent reportContent = new ReportContent();
        reportContent.addLabel(new Label("Number of vertices in graph: " + graph.getVertices().size()));
        reportContent.addLabel(new Label("Number of iterations: " + parameters.numOfIterations));
        reportContent.addLabel(new Label("Number of ants: " + parameters.numOfAnts));
        reportContent.addLabel(new Label("Pheromone per ant: " + parameters.pheromonePerAnt));
        reportContent.addLabel(new Label("Evaporation rate: " + parameters.evapRate));
        reportContent.addLabel(new Label("Alpha: " + parameters.alpha));
        reportContent.addLabel(new Label("Beta: " + parameters.beta));
        reportContent.addLabel(new Label("Number of found paths: " + allPaths.size()));

        // shortest path per iteration
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        for (int i = 0; i < shortestPathLengthPerIteration.size(); i++)
            series1.getData().add(new XYChart.Data<>(i, shortestPathLengthPerIteration.get(i)));
        reportContent.addChart(series1, "Shortest path per iteration", "iteration", "path length");

        // current shortest path over time
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        for (int i = 0; i < allPaths.size(); i++) {
            double sum = 0.0;
            for (Edge e : allPaths.get(i)) {
                sum += e.getLength().get();
            }
            series2.getData().add(new XYChart.Data<>(i, sum));
        }
        reportContent.addChart(series2, "Shortest path over time", "index", "path length");

        // number of ants that reached the goal
        XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
        for (int i = 0; i < successfulAntsPerIteration.size(); i++)
            series3.getData().add(new XYChart.Data<>(i, successfulAntsPerIteration.get(i) * 100 / parameters.numOfAnts));
        reportContent.addChart(series3, "Percentage of successful ants per iteration", "iteration", "% of ants that reached the goal");

        // convergence
        XYChart.Series<Number, Number> series4 = new XYChart.Series<>();

        // calculate actual shortest path for comparison
        Greedy dijkstra = new Greedy(graph, new VertexByCurLowestCostComparator());
        dijkstra.run();
        double sumOfWeight = sumOfWeight(dijkstra.getShortestPath());

        for (int i = 0; i < allPaths.size(); i++) {
            double sum = 0.0;
            for (Edge e : allPaths.get(i)) {
                sum += e.getLength().get();
            }
            series4.getData().add(new XYChart.Data<>(i, sum - sumOfWeight));
        }
        reportContent.addChart(series4, "Convergence", "index", "Difference");

        return reportContent;
    }
}
