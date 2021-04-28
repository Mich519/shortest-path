package org.example.simulation.algorithms.antOptimization;

import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AntOptimization {
    public static final int numOfAnts = 1000;
    public static final double evapRate = 1; // evaporation rate
    public static final double alpha = 0.5; // evaporation rate
    public static final double beta = 0.5; // between -1 and 0

    private final Set<Ant> ants;
    private final PrimaryController controller;

    public AntOptimization(PrimaryController controller) {
        this.controller = controller;
        this.ants = new HashSet<>();
        for (int i=0; i<numOfAnts; i++) {
            Ant a = new Ant(controller.getGraph().getStartVertex(), controller.getGraph().getEndVertex(), ants);
            ants.add(a);
        }
    }

    public void run() {
        Map<Edge, Double> probabilities = new HashMap<>();
        for (Ant ant : ants) {
            ant.makeMove(probabilities);
        }
    }
}
