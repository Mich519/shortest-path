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
    private final double ELITISM_RATIO = 0.2;
    private final double MUTATION_TRIALS = 50;
    private final double CROSSOVER_RATIO = 1;
    private final double MUTATION_RATIO = 1;

    private final Graph graph;

    private final List<List<Individual>> generations;

    public Genetic(Graph graph) {
        this.graph = graph;
        this.generations = new ArrayList<>();
    }

    private void initialization(List<Individual> initialGen) {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual individual = new Individual(graph.getStartVertex(), graph.getEndVertex());
            initialGen.add(individual);
            individual.initialSearch();
        }
        initialGen.removeIf(individual -> !individual.isPathSuccessful());
        Collections.sort(initialGen);
    }

    private List<Vertex> createCombinedPath(Pair<Individual, Individual> parents, List<Vertex> commonVertices) {
        // pick random common vertex
        int random = new Random().nextInt(commonVertices.size());
        Vertex randomCommonVertex = commonVertices.get(random);

        // split paths into parts (randomCommonVertex is cut point)
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent1 = parents.getKey().splitPathIntoParts(randomCommonVertex);
        Pair<List<Vertex>, List<Vertex>> pathPartsOfParent2 = parents.getValue().splitPathIntoParts(randomCommonVertex);

        // replace
        List<Vertex> combinedPath = new ArrayList<>(pathPartsOfParent1.getKey());
        combinedPath.addAll(pathPartsOfParent2.getValue());
        return combinedPath;
    }

    private Individual mate(Pair<Individual, Individual> parents) {

        // get parents' common vertices
        Individual child = new Individual(graph.getStartVertex(), graph.getEndVertex());

        List<Vertex> commonVertices = new ArrayList<>(parents.getKey().getTraveledVertices());
        commonVertices.retainAll(parents.getValue().getTraveledVertices());
        commonVertices.removeAll(List.of(graph.getStartVertex(), graph.getEndVertex())); // remove start and end vertex

        if (!commonVertices.isEmpty()) {
            System.out.println("Swapping paths ... ");
            List<Vertex> combinedPath = createCombinedPath(parents, commonVertices);
            child.setTraveledVertices(combinedPath);
            child.updateTotalCost();
            return child;
        }
        return null;
    }

    private Pair<Individual, Individual> pickRandomParents(List<Individual> gen) {
        Map<Individual, Double> probabilityMap = new LinkedHashMap<>();
        double sumOfProbabilities = 0;
        try {
            sumOfProbabilities = gen.stream().mapToDouble(individual -> 1 / individual.getTotalCost()).sum();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double scale = 1 / sumOfProbabilities;
        for (Individual individual : gen) {
            probabilityMap.put(individual, 1 / individual.getTotalCost());
        }

        Individual parent1 = null;
        double curSum = 0;
        double random = new Random().nextDouble();
        for (Individual individual : probabilityMap.keySet()) {
            curSum += probabilityMap.get(individual) * scale;
            if (random < curSum) {
                parent1 = individual;
                sumOfProbabilities -= probabilityMap.get(individual);
                probabilityMap.remove(individual);
                break;
            }
        }

        scale = 1 / sumOfProbabilities;
        Individual parent2 = null;
        curSum = 0;
        random = new Random().nextDouble();
        for (Individual individual : probabilityMap.keySet()) {
            curSum += probabilityMap.get(individual) * scale;
            if (random < curSum) {
                parent2 = individual;
                probabilityMap.remove(individual);
                break;
            }
        }
        if (parent1 == null || parent2 == null)
            throw new NullPointerException();
        return new Pair<>(parent1, parent2);
    }

    private Individual crossover(List<Individual> gen) {
        System.out.println("Crossover occured!");
        Individual child = null;
        if (gen.size() >= 2) {
            // roulette selection
            Pair<Individual, Individual> parents = pickRandomParents(gen);
            child = mate(parents);
        } else {
            System.out.println("Individuals can't crossover!");
        }
        return child;
    }

    private void mutate(List<Individual> gen) {
        System.out.println("Mutation occured!");
        Random random = new Random();
        int r = random.nextInt(gen.size());
        Individual randomIndividual = gen.get(r);

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
        List<Individual> currentGen = new ArrayList<>();
        initialization(currentGen);
        int generationSize = currentGen.size();
        for (int gen = 1; gen <= MAX_GENERATION; gen++) {
            List<Individual> elite = currentGen.subList(0, (int) (currentGen.size() * ELITISM_RATIO));
            List<Individual> newGen = new ArrayList<>(elite);
            while (newGen.size() < generationSize) {
                Individual newChild = crossover(currentGen);
                if (newChild != null)
                    newGen.add(newChild);
            }

            for (Individual ignored : newGen)
                mutate(newGen);
            Collections.sort(newGen);
            generations.add(newGen);
            currentGen.clear();
            currentGen.addAll(newGen);
        }
        for (List<Individual> gen : generations) {
            double a = 0;
            for (Individual ind : gen) {
                a += gen.stream().mapToDouble(Individual::getTotalCost).sum();
            }
            System.out.println(a / generationSize);
        }
        System.out.println("Finished");
    }

    @Override

    public void animate(PrimaryController controller) {
        controller.toogleButtonsActivity(true);
        List<Transition> transitions = new ArrayList<>();

        /*for (Individual individual : currentGen) {
            for (Vertex v : individual.getTraveledVertices()) {
                transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
            }
        }*/
        //currentGen.forEach(individual -> System.out.println(individual.getTraveledVertices()));
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            controller.toogleButtonsActivity(false);
        });
    }
}

