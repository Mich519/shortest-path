package org.example.simulation.algorithms.antOptimization;

import lombok.Getter;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;

@Getter
@Setter
public class Ant {
    private final double q = 1000; // amount of dropped pheromone
    private final Vertex startVertex;
    private final Vertex endVertex;

    private Vertex curVertex;
    Set<Edge> traversedEdges;
    private boolean isFinished = false;

    Ant(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.curVertex = startVertex;
        this.traversedEdges = new LinkedHashSet<>();
    }

    private void calculateProbabilities(Map<Edge, Double> probabilities) {
        /* for each adjacent edge calculate probabilty of chosing that edge */

        // total probabilities
        double p_denominator = 0.0;
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                // skip already traversed edges
                p_denominator += Math.pow(adjEdge.getPheromone(), AntOptimization.alpha) * Math.pow(1 / adjEdge.calculateWeight(), AntOptimization.beta);
            }
        }

        // update probabilities of each adj vertex
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                double p_nominator = Math.pow(adjEdge.getPheromone(), AntOptimization.alpha) * Math.pow(1 / adjEdge.calculateWeight(), AntOptimization.beta);
                probabilities.put(adjEdge, p_nominator / p_denominator);
            }
        }
    }

    private void localPheromoneUpdate(Edge e) {
        // update last traversed edge
        //e.setPheromone((1-AntOptimization.evapRate) * e.getPheromone() + AntOptimization.evapRate * e.calculateWeight());
    }

    public void updateTraversedEdges(Graph graph) {
        for (Edge e : traversedEdges) {
            double f = e.getPheromone();
            double deltaF = q / e.calculateWeight();
            e.setPheromone(f + deltaF);
            graph.findSameEdge(e).setPheromone(f + deltaF);
        }
    }

    public void makeMove(Graph graph) {

        Map<Edge, Double> probabilities = new HashMap<>();
        calculateProbabilities(probabilities);
        // pick random node
        double r = new Random().nextDouble();
        double sum = 0.0;
        for (Edge adjEdge : probabilities.keySet()) {
            sum += probabilities.get(adjEdge);
            if (r < sum) {
                traversedEdges.add(adjEdge);
                traversedEdges.add(graph.findSameEdge(adjEdge));
                localPheromoneUpdate(adjEdge);
                localPheromoneUpdate(graph.findSameEdge(adjEdge));
                curVertex = adjEdge.getDestination();
                break;
            }
        }

        // ant reached the goal or can't move forward
        if (curVertex == endVertex || probabilities.isEmpty()) {
            isFinished = true;
        }
    }

    public void resetPosition() {
        curVertex = startVertex;
        traversedEdges.clear();
        isFinished = false;
    }
}