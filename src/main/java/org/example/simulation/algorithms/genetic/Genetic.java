package org.example.simulation.algorithms.genetic;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.simulation.algorithms.Algorithm;

import java.util.*;

public class Genetic implements Algorithm {
    private final int POPULATION_SIZE = 100;
    private final int MAX_GENERATION = 50;
    private final double CROSSOVER_RATIO = 1;
    private final double MUTATION_RATIO = 0.1;

    private final Graph graph;
    private final List<Individual> individuals;

    public Genetic(Graph graph) {
        this.graph = graph;
        this.individuals = new ArrayList<>(POPULATION_SIZE);
    }

    private void initialization() {
        /*
        For each individual generate random path from start to end (initial chromosomes)
         */
        for (int i = 0; i < POPULATION_SIZE; i++) {
            individuals.add(new Individual(graph.getStartVertex(), graph.getEndVertex()));
        }

        for (Individual individual : individuals) {
            individual.search();
        }
    }

    private void selection() {
        // individuals.sort(new IndividualByShortestPathComparator());
        individuals.removeIf(individual -> !individual.isPathSuccessful());
    }

    private void swapPaths(Individual parent1, Individual parent2, List<Vertex> commonVertices) {

        // pick random common vertex
        int random = new Random().nextInt(commonVertices.size());
        Vertex randomCommonVertex = commonVertices.get(random);

        // extract first part of paths
        List<Vertex> firstPart1 = new ArrayList<>();
        List<Vertex> firstPart2 = new ArrayList<>();

        for (Vertex v : parent1.getTraveledVertices()) {
            if (v == randomCommonVertex) break;
            firstPart1.add(v);
        }
        for (Vertex v : parent2.getTraveledVertices()) {
            if (v == randomCommonVertex) break;
            firstPart2.add(v);
        }

        // extract second part of paths
        List<Vertex> secondPart1 = new ArrayList<>(parent1.getTraveledVertices());
        List<Vertex> secondPart2 = new ArrayList<>(parent2.getTraveledVertices());
        secondPart1.removeAll(firstPart1);
        secondPart2.removeAll(firstPart2);
        firstPart1.addAll(secondPart2);
        firstPart2.addAll(secondPart1);

        // replace
        parent1.getTraveledVertices().clear();
        parent1.getTraveledVertices().addAll(firstPart1);

        parent2.getTraveledVertices().clear();
        parent2.getTraveledVertices().addAll(firstPart2);
    }

    private void mate(Individual parent1, Individual parent2) {

        // find parents' common vertices
        List<Vertex> commonVertices = new ArrayList<>(parent1.getTraveledVertices());
        commonVertices.retainAll(parent2.getTraveledVertices());

        if (commonVertices.isEmpty()) {
            // no common vertices
            System.out.println("No common vertices");
        } else {
            System.out.println("Swapping paths ... s");
            swapPaths(parent1, parent2, commonVertices);
        }
    }

    private void crossover() {
        /*
        Pick two best candidates.
            - if they have two common vertices -> swap their paths starting from that point
            - else -> connect two points with the random path
         */
        if (individuals.size() >= 2) {
            List<Individual> temp = new ArrayList<>(individuals);

            // pick random parent 1
            int random1 = new Random().nextInt(temp.size());
            Individual parent1 = temp.get(random1);
            temp.remove(parent1);

            // pick random parent 2
            int random2 = new Random().nextInt(temp.size());
            Individual parent2 = temp.get(random2);
            temp.remove(parent2);

            mate(parent1, parent2);

        } else {
            System.out.println("Individuals cant crossover!");
        }
    }

    private void mutate(Individual individual) {
        int random = new Random().nextInt(individuals.size());
        Individual randomIndividual = individuals.get(random);

        random = new Random().nextInt(individual.getTraveledVertices().size());

    }

    @Override
    public void run() {
        System.out.println("Started");
        initialization();
        selection();
        for (int gen = 1; gen <= MAX_GENERATION; gen++) {

            double r = new Random().nextDouble();
            if (r < CROSSOVER_RATIO) {
                System.out.println("Crossover occured!");
                crossover();
            }
            if (r < MUTATION_RATIO) {
                // mutation();
            }
        }
        System.out.println("Finished");
    }

    @Override
    public void animate(PrimaryController controller) {

    }
}
