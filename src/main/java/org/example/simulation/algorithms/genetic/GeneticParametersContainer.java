package org.example.simulation.algorithms.genetic;

import org.example.controller.PrimaryController;

public class GeneticParametersContainer {
    public final double elitismRate;
    public final double mutationRate;
    public final double numOfGenerations;
    public final double initialPopulation;


    public GeneticParametersContainer(PrimaryController controller) {
        elitismRate = controller.getElitismRate().getValue();
        numOfGenerations = controller.getNumOfGenerations().getValue();
        initialPopulation = controller.getInitialPopulation().getValue();
        mutationRate = controller.getMutationRatio().getValue();
    }
}