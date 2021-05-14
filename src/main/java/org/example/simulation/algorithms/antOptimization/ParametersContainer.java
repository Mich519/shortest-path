package org.example.simulation.algorithms.antOptimization;

import org.example.controller.PrimaryController;

public class ParametersContainer {
    public final double pheromonePerAnt; // amount of dropped pheromone
    public final int numOfAnts;
    public final double evapRate; // evaporation rate
    public final double alpha;
    public final double beta;
    public final int numOfIterations;

    public ParametersContainer(PrimaryController controller) {
        pheromonePerAnt = controller.getPheromonePerAnt().getValue();
        numOfAnts = (int) controller.getNumberOfAnts().getValue();
        evapRate = controller.getEvaporationRate().getValue();
        alpha = controller.getAlpha().getValue();
        beta = controller.getBeta().getValue();
        numOfIterations = (int) controller.getNumberOfIterations().getValue();
    }
}
