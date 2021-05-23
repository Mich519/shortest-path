package org.example.simulation.algorithms.genetic;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.util.Pair;
import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.simulation.algorithms.Algorithm;

import java.util.*;

public class Genetic implements Algorithm {
    private final GeneticParametersContainer parameters;
    private final Graph graph;

    private final List<List<Individual>> generations;

    public Genetic(Graph graph, GeneticParametersContainer parameters) {
        this.graph = graph;
        this.generations = new ArrayList<>();
        this.parameters = parameters;
    }

    private void initialization(List<Individual> initialGen) {
        for (int i = 0; i < parameters.initialPopulation; i++) {
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
        double sumOfProbabilities = gen.stream().mapToDouble(individual -> 1 / individual.getTotalCost()).sum();
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

    private void mutation(List<Individual> gen) {
        Random random = new Random();
        gen.forEach(individual -> {
            if (random.nextDouble() < parameters.mutationRatio)
                individual.mutate(graph.getVertices().size() / 20, 1);
        });
    }

    @Override
    public void run() {
        List<Individual> currentGen = new ArrayList<>();
        initialization(currentGen);
        int generationSize = currentGen.size();
        for (int gen = 1; gen <= parameters.numOfGenerations; gen++) {
            List<Individual> elite = currentGen.subList(0, (int) (currentGen.size() * parameters.elitismRate));
            List<Individual> newGen = new ArrayList<>(elite);
            while (newGen.size() < generationSize) {
                Individual newChild = crossover(currentGen);
                if (newChild != null)
                    newGen.add(newChild);
            }
            mutation(newGen);
            Collections.sort(newGen);
            generations.add(newGen);
            currentGen.clear();
            currentGen.addAll(newGen);
        }
        for (List<Individual> gen : generations) {
            double a = 0;
            for (Individual ind : gen)
                a += gen.stream().mapToDouble(Individual::getTotalCost).sum();
            //System.out.println(a / generationSize);
        }
        System.out.println(currentGen.get(0).getTotalCost());
        System.out.println("Finished");
    }

    @Override
    public void animate(PrimaryController controller) {
        controller.toogleButtonsActivity(true);
        List<Transition> transitions = new ArrayList<>();
        for (List<Individual> generation : generations) {
            generation.get(0).prepareEdgeTransitions(transitions, controller);
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);
        st.getChildren().addAll(transitions);
        st.play();
        st.setOnFinished(actionEvent -> {
            controller.toogleButtonsActivity(false);
        });
    }
}

