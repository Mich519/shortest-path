package org.example.simulation.algorithms.genetic;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;

@Getter
public class Individual implements Comparable<Individual> {
    private final Vertex startVertex;
    private final Vertex endVertex;
    private Vertex curVertex;
    @Setter
    private List<Vertex> traveledVertices;
    private double totalCost;

    public Individual(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        curVertex = startVertex;
        traveledVertices = new ArrayList<>();
        totalCost = 0;
    }

    public void initialSearch() {
        while (true) {
            traveledVertices.add(curVertex);
            List<Vertex> neighbours = curVertex.getNeighbours();
            List<Vertex> accessibleVertices = new ArrayList<>(neighbours);
            accessibleVertices.removeAll(traveledVertices);
            if (isPathSuccessful() || accessibleVertices.size() == 0) {
                updateTotalCost();
                break;
            }

            // choose random vertex
            int random = new Random().nextInt(accessibleVertices.size());
            curVertex = accessibleVertices.get(random);
        }
    }

    public void updateTotalCost() throws NullPointerException {
        totalCost = 0;
        for (int i = 1; i < traveledVertices.size(); i++) {
            Vertex v1 = traveledVertices.get(i - 1);
            Vertex v2 = traveledVertices.get(i);
            try {
                totalCost += v1.findEdgeConnectedTo(v2).getLength().get();
            } catch (NullPointerException e) {
                throw new NullPointerException();
            }
        }
    }

    public Pair<List<Vertex>, List<Vertex>> splitPathIntoParts(Vertex cutPoint) {
        List<Vertex> firstPart = new ArrayList<>();
        List<Vertex> secondPart = new ArrayList<>();

        List<Vertex> currentPart = firstPart;
        for (Vertex v : getTraveledVertices()) {
            if (v == cutPoint)
                currentPart = secondPart;
            currentPart.add(v);
        }
        return new Pair<>(firstPart, secondPart);
    }

    private Vertex pickRandomVertexToTraverse(List<Vertex> accessibleVertices, Vertex destination) {
        Vertex result = null;
        if (accessibleVertices.contains(destination))
            return destination;
        Map<Vertex, Double> vertexToProbabilityMap = new HashMap<>();
        double totalProb = 0;
        for (Vertex v : accessibleVertices) {
            double prob = 1 / v.distanceTo(destination);
            vertexToProbabilityMap.put(v, prob);
            totalProb += prob;
        }
        double scale = 1 / totalProb;
        double random = new Random().nextDouble();
        double temp = 0;
        for (Vertex v : vertexToProbabilityMap.keySet()) {
            temp += vertexToProbabilityMap.get(v) * scale;
            if (random < temp) {
                result = v;
                break;
            }
        }
        //int r = new Random().nextInt(accessibleVertices.size());
        if (result == null)
            throw new NullPointerException();
        return result;
    }

    private void replaceWithAlternativePath(List<Vertex> alternativeRoute, Vertex source, Vertex destination) {

        System.out.println("Alternative path was found!");
        Pair<List<Vertex>, List<Vertex>> firstPartOfThePath = splitPathIntoParts(source);
        Pair<List<Vertex>, List<Vertex>> secondPartOfThePath = splitPathIntoParts(destination);

        // build new path
        List<Vertex> newPath = new ArrayList<>();
        newPath.addAll(firstPartOfThePath.getKey());
        newPath.addAll(alternativeRoute);
        secondPartOfThePath.getValue().remove(destination);
        newPath.addAll(secondPartOfThePath.getValue());

        // replace
        getTraveledVertices().clear();
        getTraveledVertices().addAll(newPath);
        updateTotalCost();
    }

    public void searchForAlternativeRoute(Vertex source, Vertex destination, Graph graph) {

        Vertex cur = source;
        List<Vertex> alternativeRoute = new ArrayList<>(List.of(cur));
        final int TRIALS_THRESHOLD = graph.getVertices().size() / 2;
        for (int i = 0; i < 50; i++) {
            for (int step = 0; step < TRIALS_THRESHOLD &&  cur != destination; step++) {

                // get accessible vertices
                List<Vertex> accessibleVertices = new ArrayList<>(cur.getNeighbours());
                accessibleVertices.removeAll(getTraveledVertices());
                accessibleVertices.removeAll(alternativeRoute);
                if (cur.getNeighbours().contains(destination) && step > 0)
                    accessibleVertices.add(destination);

                if (accessibleVertices.size() > 0) {
                    cur = pickRandomVertexToTraverse(accessibleVertices, destination);
                    if (cur == null) {
                        throw new NullPointerException();
                    }
                    alternativeRoute.add(cur);
                } else {
                    System.out.println("Unable to find alternative path - dead end");
                    break;
                }
            }

            if (cur == destination) {
                System.out.println("Alternative path found! Replacing ...");
                replaceWithAlternativePath(alternativeRoute, source, destination);
                break;
            }
        }

    }

    public boolean isPathSuccessful() {
        return curVertex == endVertex;
    }

    @Override
    public int compareTo(Individual individual) {
        if (getTotalCost() < individual.getTotalCost())
            return -1;
        else if (getTotalCost() > individual.getTotalCost())
            return 1;
        return 0;
    }
}
