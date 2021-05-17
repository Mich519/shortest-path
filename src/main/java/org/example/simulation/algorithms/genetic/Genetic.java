package org.example.simulation.algorithms.genetic;

import org.example.controller.PrimaryController;
import org.example.graph.Edge;
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
        individuals.sort(new IndividualByShortestPathComparator());
        individuals.removeIf(individual -> !individual.isPathSuccessful());
    }

    private void mate(Individual parent1, Individual parent2) {
        List<Vertex> commonVertices = new ArrayList<>();

        for (Edge e1 : parent1.getTraversedEdges()) {
            Vertex key1 = e1.getVertices().getKey();
            Vertex value1 = e1.getVertices().getValue();
            for (Edge e2 : parent2.getTraversedEdges()) {
                Vertex key2 = e2.getVertices().getKey();
                Vertex value2 = e1.getVertices().getValue();
                if (key1 == v2)
                    commonVertices.add(v1);
            }
        }

        if (commonVertices.isEmpty()) {
            // no common vertices
            System.out.println("No common vertices");
        } else {
            int random = new Random().nextInt(commonVertices.size());
            Vertex randomCommonVertex = commonVertices.get(random); // exchange paths starting from 'randomCommonVertex'
            Set<Edge> partialPathOfParent1 = new LinkedHashSet<>();
            Set<Edge> partialPathOfParent2 = new LinkedHashSet<>();
            for (Edge e1 : parent1.getTraversedEdges()) {
                if(e1.getVertices().getValue() == )
            }
        }
    }

    private void crossover() {
        /*
        Pick two best candidates.
            - if they have two common vertices -> swap their paths starting from that point
            - else -> connect two points with the random path
         */
        int random1 = new Random().nextInt(individuals.size());
        int random2 = new Random().nextInt(individuals.size());
        if (random1 != random2) {
            Individual parent1 = individuals.get(random1);
            Individual parent2 = individuals.get(random2);
            mate(parent1, parent2);
        }
    }

    private void mutation(Individual individual) {

    }

    @Override
    public void run() {
        System.out.println("Started");
        initialization();
        selection();
        for (int gen = 1; gen <= MAX_GENERATION; gen++) {

            double r = new Random().nextDouble();
            if (r < CROSSOVER_RATIO) {
                crossover();
            }
            if (r < MUTATION_RATIO) {

            }
        }
        System.out.println("Finished");
    }

    @Override
    public void animate(PrimaryController controller) {

    }
}
