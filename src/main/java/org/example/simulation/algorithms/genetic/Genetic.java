package org.example.simulation.algorithms.genetic;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;
import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.simulation.algorithms.Algorithm;

import java.util.*;

public class Genetic implements Algorithm {
    private final int POPULATION_SIZE = 500;
    private final int MAX_GENERATION = 50;
    private final double CROSSOVER_RATIO = 1;
    private final double MUTATION_RATIO = 1;

    private final Graph graph;
    private final List<Individual> individuals;

    public Genetic(Graph graph) {
        this.graph = graph;
        this.individuals = new ArrayList<>(POPULATION_SIZE);
    }

    private void initialization() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual individual = new Individual(graph.getStartVertex(), graph.getEndVertex());
            individuals.add(individual);
            individual.initialSearch();
        }
    }

    private void selection() {
        // remove individuals that didn't finish the path
        individuals.removeIf(individual -> !individual.isPathSuccessful());
        individuals.forEach(Individual::updateTotalCost);
        Collections.sort(individuals);
    }

    private void swapPaths(Individual parent1, Individual parent2, List<Vertex> commonVertices) {
        // pick random common vertex
        int random = new Random().nextInt(commonVertices.size());
        Vertex randomCommonVertex = commonVertices.get(random);

        // split paths into parts (randomCommonVertex is cut point)
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent1 = parent1.splitPathIntoParts(randomCommonVertex);
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent2 = parent2.splitPathIntoParts(randomCommonVertex);

        // replace
        parent1.getTraveledVertices().clear();
        parent1.getTraveledVertices().addAll(pathPartsOfParent1.getKey());
        parent1.getTraveledVertices().addAll(pathPartsOfParent2.getValue());

        parent2.getTraveledVertices().clear();
        parent2.getTraveledVertices().addAll(pathPartsOfParent2.getKey());
        parent2.getTraveledVertices().addAll(pathPartsOfParent1.getValue());
        try {
            parent1.updateTotalCost();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        try {
            parent2.updateTotalCost();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void mate(Individual parent1, Individual parent2) {

        // get parents' common vertices
        List<Vertex> commonVertices = new ArrayList<>(parent1.getTraveledVertices());
        commonVertices.retainAll(parent2.getTraveledVertices());
        commonVertices.removeAll(List.of(graph.getStartVertex(), graph.getEndVertex())); // remove start and end vertex

        if (commonVertices.isEmpty()) {
            // no common vertices
            System.out.println("No common vertices");
        } else {
            System.out.println("Swapping paths ... ");
            swapPaths(parent1, parent2, commonVertices);
        }
    }

    private void crossover() {

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

    private void mutate() {
        // pick random individual
        Random random = new Random();
        int r = random.nextInt(individuals.size());
        Individual randomIndividual = individuals.get(r);

        // prepare list of vertices to pick randomly
        List<Vertex> temp = new ArrayList<>(randomIndividual.getTraveledVertices());
        temp.removeAll(List.of(graph.getStartVertex(), graph.getEndVertex())); // remove start and end vertex

        // pick random vertex 1
        try {
            int random1 = random.nextInt(temp.size());
            Vertex randomVertex1 = temp.get(random1);
            temp.remove(randomVertex1);

            // pick random vertex 2
            int random2 = random.nextInt(temp.size());
            Vertex randomVertex2 = temp.get(random2);
            temp.remove(randomVertex2);
            Vertex source, destination;

            if (random1 <= random2) {
                source = randomVertex1;
                destination = randomVertex2;
            } else {
                source = randomVertex2;
                destination = randomVertex1;
            }
            randomIndividual.searchForAlternativeRoute(source, destination, graph);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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
            selection();
            if (r < MUTATION_RATIO) {
                System.out.println("Mutation occured!");
                mutate();
            }
            selection();
        }
        System.out.println("Finished");
    }

    @Override
    public void animate(PrimaryController controller) {
        selection();
        controller.toogleButtonsActivity(true);
        List<Transition> transitions = new ArrayList<>();
        for (Individual individual : individuals) {
            for (Vertex v : individual.getTraveledVertices()) {
                transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
            }
        }
        individuals.forEach(individual -> System.out.println(individual.getTraveledVertices()));
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            controller.toogleButtonsActivity(false);
        });
    }
}

