package org.example.simulation.algorithms.antOptimization;

import lombok.Getter;
import lombok.Setter;
import org.example.graph.Edge;
import org.example.graph.Vertex;

import java.util.*;

@Getter
@Setter
public class Ant {
    private final double q = 3; // amount of dropped pheromone
    private final Vertex startVertex;
    private final Vertex endVertex;

    private Vertex curVertex;
    Set<Edge> traversedEdges;

    Ant(Vertex startVertex, Vertex endVertex, Set<Ant> ants) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.curVertex = startVertex;
        this.traversedEdges = new HashSet<>();
    }

    private void updateTraversedEdges() {
        for (Edge e : traversedEdges) {

            double f = (1 - AntOptimization.evapRate) * e.getPheromone();
            double deltaF = q / e.calculateWeight();
            e.setPheromone(f + deltaF);
        }
    }

    private void calculateProbabilities(Map<Edge, Double> probabilities) {
        /* for each adjacent edge calculate probabilty of chosing that edge */

        // total probabilities
        double p_denominator = 0.0;
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                // skip already traversed edges
                p_denominator += Math.pow(adjEdge.getPheromone(), AntOptimization.alpha) * Math.pow(adjEdge.calculateWeight(), AntOptimization.beta);
            }
        }

        // update probabilities of each adj vertex
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                double p_nominator = Math.pow(adjEdge.getPheromone(), AntOptimization.alpha) * Math.pow(adjEdge.calculateWeight(), AntOptimization.beta);
                probabilities.put(adjEdge, p_nominator / p_denominator);
            }
        }
    }

    public void makeMove(Map<Edge, Double> probabilities) {

        calculateProbabilities(probabilities);

        // pick random node
        double r = new Random().nextDouble();
        double sum = 0.0;
        for (Edge adjEdge : probabilities.keySet()) {
            sum += probabilities.get(adjEdge);
            if (r < sum) {
                traversedEdges.add(adjEdge);
                curVertex = adjEdge.getDestination();
            }
        }

        // ant reached the goal
        if (curVertex == endVertex) {
            updateTraversedEdges();
            curVertex = startVertex;
        }
    }


}
