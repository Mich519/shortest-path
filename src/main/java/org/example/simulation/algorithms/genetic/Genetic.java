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
import java.util.stream.Collectors;

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
            individual.search();
        }
    }

    private void selection() {
        // remove individuals that didn't finish the path
        individuals.removeIf(individual -> !individual.isPathSuccessful());
        individuals.forEach(Individual::updateTotalCost);
        Collections.sort(individuals);
    }

    private Pair<List<Vertex>, List<Vertex>> splitPathIntoParts(Individual parent, Vertex cutPoint) {
        List<Vertex> firstPart = new ArrayList<>();
        List<Vertex> secondPart = new ArrayList<>();

        List<Vertex> currentPart = firstPart;
        for (Vertex v : parent.getTraveledVertices()) {
            if (v == cutPoint)
                currentPart = secondPart;
            currentPart.add(v);
        }
        return new Pair<>(firstPart, secondPart);
    }

    private void swapPaths(Individual parent1, Individual parent2, Set<Vertex> commonVertices) {
        // pick random common vertex
        int i = 0, random = new Random().nextInt(commonVertices.size());
        Vertex randomCommonVertex = null;
        for (Vertex v : commonVertices) {
            if (i++ == random)
                randomCommonVertex = v;
        }

        // split paths into parts (randomCommonVertex is cut point)
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent1 = splitPathIntoParts(parent1, randomCommonVertex);
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent2 = splitPathIntoParts(parent1, randomCommonVertex);

        // replace
        parent1.getTraveledVertices().clear();
        parent1.getTraveledVertices().addAll(pathPartsOfParent1.getKey());
        parent1.getTraveledVertices().addAll(pathPartsOfParent2.getValue());

        parent2.getTraveledVertices().clear();
        parent2.getTraveledVertices().addAll(pathPartsOfParent2.getKey());
        parent2.getTraveledVertices().addAll(pathPartsOfParent1.getValue());
    }

    private void mate(Individual parent1, Individual parent2) {

        // get parents' common vertices
        Set<Vertex> commonVertices = new HashSet<>();
        commonVertices.addAll(parent1.getTraveledVertices());
        commonVertices.addAll(parent2.getTraveledVertices());
        commonVertices.removeAll(List.of(graph.getStartVertex(), graph.getEndVertex())); // remove start and end vertex

        if (commonVertices.isEmpty()) {
            // no common vertices
            System.out.println("No common vertices");
        } else {
            System.out.println("Swapping paths ... s");
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

    private void replaceWithAlternativePath(Individual individual, List<Vertex> alternativeRoute) {

        System.out.println("Alternative path was found!");
        Vertex source = alternativeRoute.get(0);
        Vertex destination = alternativeRoute.get(alternativeRoute.size() - 1);
        Pair<List<Vertex>, List<Vertex>> firstPartOfThePath = splitPathIntoParts(individual, source);
        Pair<List<Vertex>, List<Vertex>> secondPartOfThePath = splitPathIntoParts(individual, destination);

        // build new path
        List<Vertex> newPath = new ArrayList<>();
        newPath.addAll(firstPartOfThePath.getKey());
        newPath.addAll(alternativeRoute);
        secondPartOfThePath.getValue().remove(destination);
        newPath.addAll(secondPartOfThePath.getValue());

        // replace
        individual.getTraveledVertices().clear();
        individual.getTraveledVertices().addAll(newPath);
    }

    private void searchForAlternativeRoute(Individual individual, Vertex source, Vertex destination) {

        Vertex curVertex = source;
        List<Vertex> alternativeRoute = new ArrayList<>(List.of(curVertex));
        final int TRIALS_THRESHOLD = graph.getVertices().size() / 2;
        for (int step = 0; step < TRIALS_THRESHOLD && curVertex != destination; step++) {
            System.out.println("Searching");
            // get accessible vertices - skip previously visited
            List<Vertex> accessibleVertices = new ArrayList<>(curVertex.getNeighbours());
            if(!accessibleVertices.contains(destination)) {
                accessibleVertices.removeAll(individual.getTraveledVertices());
                accessibleVertices.removeAll(alternativeRoute);
            }
            else {
                accessibleVertices.removeAll(individual.getTraveledVertices());
                accessibleVertices.removeAll(alternativeRoute);
                accessibleVertices.add(destination);
            }

            if (accessibleVertices.size() > 0) {
                Map<Double, Vertex> vertexToProbabilityMap = new HashMap<>();
                double totalProb = 0;
                for (Vertex v : accessibleVertices) {
                    double prob = 1 / v.distanceTo(destination);
                    vertexToProbabilityMap.put(prob, v);
                    totalProb += prob;
                }
                double scale = 1 / totalProb;
                double random = new Random().nextDouble();
                vertexToProbabilityMap.keySet().stream().map(aDouble -> aDouble * scale);
                double temp = 0;
                for (Double d : vertexToProbabilityMap.keySet()) {
                    temp+=d;
                    if(random < temp) {
                        curVertex = vertexToProbabilityMap.get(d);
                        break;
                    }
                }
                alternativeRoute.add(curVertex);
            } else {
                System.out.println("Unable to find alternative path - dead end");
                break;
            }
        }

        if (curVertex == destination) {
            System.out.println("Alternative path found! Replacing ...");
            replaceWithAlternativePath(individual, alternativeRoute);
        }
    }

    private void mutate() {
        // pick random individual
        int random = new Random().nextInt(individuals.size());
        Individual randomIndividual = individuals.get(random);

        // prepare list of vertices to pick randomly
        List<Vertex> temp = new ArrayList<>(randomIndividual.getTraveledVertices());
        temp.removeAll(List.of(graph.getStartVertex(), graph.getEndVertex())); // remove start and end vertex

        // pick random vertex 1
        int random1 = new Random().nextInt(temp.size());
        Vertex randomVertex1 = temp.get(random1);
        temp.remove(randomVertex1);

        // pick random vertex 2
        int random2 = new Random().nextInt(temp.size());
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

        // try to find alternative path between these vertices
        searchForAlternativeRoute(randomIndividual, source, destination);
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
