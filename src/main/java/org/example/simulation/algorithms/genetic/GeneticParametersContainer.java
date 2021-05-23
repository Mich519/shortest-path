package org.example.simulation.algorithms.genetic;

import org.example.controller.PrimaryController;

public class GeneticParametersContainer {
    public final double elitismRate;
    public final double numOfGenerations;
    public final double initialPopulation;
    public final double mutationRatio;

    public GeneticParametersContainer(PrimaryController controller) {
        elitismRate = controller.getElitismRate().getValue();
        numOfGenerations = controller.getNumOfGenerations().getValue();
        initialPopulation = controller.getInitialPopulation().getValue();
        mutationRatio = controller.getMutationRatio().getValue();
    }
}