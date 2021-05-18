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
    private final int POPULATION_SIZE = 100;
    private final int MAX_GENERATION = 50;
    private final double CROSSOVER_RATIO = 1;
    private final double MUTATION_RATIO = 1;

    private final Graph graph;
    private final List<Individual> individuals;


    public Genetic(Graph graph) {
        this.graph = graph;
        this.individuals = new ArrayList<>(POPULATION_SIZE);
    }

    /*
    TODO: edge cases
     */

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
        // remove individuals that didn't finish the path
        individuals.removeIf(individual -> !individual.isPathSuccessful());
        individuals.forEach(Individual::updateTotalCost);
        Collections.sort(individuals);
    }

    private Pair<List<Vertex>, List<Vertex>> splitPathIntoParts(Individual parent, Vertex cutPoint) {
        List<Vertex> firstPart = new ArrayList<>();
        List<Vertex> secondPart = new ArrayList<>();
        // first part
        for (int i = 0; i < parent.getTraveledVertices().size(); i++) {
            Vertex v = parent.getTraveledVertices().get(i);
            if (v == cutPoint) break;
            firstPart.add(v);
        }

        // second part
        for (int i = parent.getTraveledVertices().size() - 1; i >= 0; i--) {
            Vertex v = parent.getTraveledVertices().get(i);
            secondPart.add(v);
            if (v == cutPoint) break;
        }
        Collections.reverse(secondPart);

        return new Pair<>(firstPart, secondPart);
    }

    private void swapPaths(Individual parent1, Individual parent2, List<Vertex> commonVertices) {

        // pick random common vertex
        int random = new Random().nextInt(commonVertices.size());
        Vertex randomCommonVertex = commonVertices.get(random);

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

        // find parents' common vertices
        List<Vertex> commonVertices = new ArrayList<>();
        for (Vertex v1 : parent1.getTraveledVertices()) {
            for (Vertex v2 : parent2.getTraveledVertices()) {
                if (v1 == v2 && !commonVertices.contains(v1)) {
                    commonVertices.add(v1);
                }

            }
        }
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

    private void searchForAlternativeRoute(Individual individual, Vertex source, Vertex destination) {
        /*
        Search for alternative route between soruce and destination.
        Avoid vertices that
         */
        Set<Vertex> alternativeRoute = new LinkedHashSet<>();
        Vertex curVertex = source;
        final int TRIALS_THRESHOLD = graph.getVertices().size() / 2;
        for (int step = 0; step < TRIALS_THRESHOLD; step++) {
            alternativeRoute.add(curVertex);
            if (curVertex == destination)
                break;

            // search for alternative path
            List<Vertex> neighbours = curVertex.getNeighbours();
            if (step == 0) {
                // inaccessible in first step
                neighbours.remove(source);
                neighbours.remove(destination);
            }
            int random = new Random().nextInt(neighbours.size());
            curVertex = neighbours.get(random);
        }

        // check if old path does not contain alternative route
        if (individual.getTraveledVertices().containsAll(alternativeRoute)) {

        }

        // replace old path with the new one
        List<Vertex> newPath = new ArrayList<>();

        if (curVertex == destination) {
            System.out.println("Alternative path was found!");

            List<Vertex> firstPartOfThePath = new ArrayList<>();
            List<Vertex> secondPartOfThePath = new ArrayList<>(individual.getTraveledVertices());

            // add part before 'source'
            for (Vertex v : individual.getTraveledVertices()) {
                if (v == source) {
                    break;
                }
                firstPartOfThePath.add(v);
            }
            newPath.addAll(firstPartOfThePath);

            // add alternative path between source and destination
            newPath.addAll(alternativeRoute);

            // add part after 'destination'
            secondPartOfThePath.removeAll(firstPartOfThePath);
            secondPartOfThePath.remove(source);
            secondPartOfThePath.remove(destination);
            newPath.addAll(secondPartOfThePath);

            // replace
            individual.getTraveledVertices().clear();
            individual.getTraveledVertices().addAll(newPath);
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

            // debug
            for (Individual individual : individuals) {
                Set<Vertex> test = new HashSet<>();
                for (Vertex v : individual.getTraveledVertices()) {
                    if (!test.add(v)) {
                        //System.out.println("no hej :)");
                    }
                }
            }
            //

            double r = new Random().nextDouble();
            if (r < CROSSOVER_RATIO) {
                System.out.println("Crossover occured!");
                crossover();
            }
            if (r < MUTATION_RATIO) {
                //System.out.println("Mutation occured!");
                //mutate();
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
