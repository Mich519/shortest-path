package org.example.simulation.algorithms.antOptimization;

import lombok.Getter;
import lombok.Setter;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;
import java.util.concurrent.Callable;

@Getter
@Setter
public class Ant implements Callable {

    private Graph graph;
    private final Vertex startVertex;
    private final Vertex endVertex;

    // parameters
    private double alpha;
    private double beta;
    private double pheromonePerAnt;
    private double evapRate;

    private Vertex curVertex;
    private Set<Edge> traversedEdges;
    private boolean isFinished;
    private int numOfMoves;
    private Integer numOfSuccessfulPaths;

    Ant(Graph graph, double alpha, double beta, double pheromonePerAnt, double evapRate) {
        this.graph = graph;
        this.startVertex = graph.getStartVertex();
        this.endVertex = graph.getEndVertex();
        this.curVertex = startVertex;
        this.traversedEdges = new LinkedHashSet<>();
        this.alpha = alpha;
        this.beta = beta;
        this.pheromonePerAnt = pheromonePerAnt;
        this.isFinished = false;
        this.numOfMoves = 0;
    }

    private void calculateProbabilities(Map<Edge, Double> probabilities) {
        /* for each adjacent edge calculate probability of choosing that edge */

        // total probabilities
        double p_denominator = 0.0;
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                // skip already traversed edges
                p_denominator += Math.pow(adjEdge.getPheromone(), alpha) * Math.pow(1 / adjEdge.getLength().get(), beta);
            }
        }

        // update probabilities of each adj vertex
        double p_nominator = 1;
        for (Edge adjEdge : curVertex.getAdjEdges()) {
            if (!traversedEdges.contains(adjEdge)) {
                p_nominator = Math.pow(adjEdge.getPheromone(), alpha) * Math.pow(1 / adjEdge.getLength().get(), beta);
                probabilities.put(adjEdge, p_nominator / p_denominator);
            }
        }
    }

    private void localPheromoneUpdate(Edge e) {
        // update last traversed edge
            double f = e.getPheromone();
            double deltaF = pheromonePerAnt / e.getLength().get();
            e.setPheromone(f + deltaF);
            e.setPheromone(f + f);
    }

    public void updateTraversedEdges() {
        double totalLength = 0;
        for (Edge e : traversedEdges)
            totalLength += e.getLength().get();
        for (Edge e : traversedEdges) {
            double f = e.getPheromone();
            double deltaF = pheromonePerAnt / totalLength;

            e.setPheromone(f + deltaF);
        }
    }

    @Override
    public Object call() {
        while (true) {
            Map<Edge, Double> probabilities = new HashMap<>();
            calculateProbabilities(probabilities);

            // pick random node
            double r = new Random().nextDouble();
            double sum = 0.0;
            for (Edge adjEdge : probabilities.keySet()) {
                sum += probabilities.get(adjEdge);
                if (r < sum) {
                    traversedEdges.add(adjEdge);
                    //localPheromoneUpdate(adjEdge);
                    curVertex = adjEdge.getNeighbourOf(curVertex);
                    numOfMoves++;
                    break;
                }
            }
            // ant reached the goal or can't move forward
            if (curVertex == endVertex || probabilities.isEmpty() || numOfMoves > graph.getVertices().size()) {
                break;
            }
        }
        return null;
    }
}