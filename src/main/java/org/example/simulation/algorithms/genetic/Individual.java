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

    private Vertex pickRandomVertexToTraverse(List<Vertex> accessibleVertices) {
        Vertex result = null;

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

    public void searchForAlternativeRoute(Vertex from, int maxSearchDepth, int numOfTrials) {
        Random random = new Random();
        boolean success = false;
        for (int i = 0; i < numOfTrials; i++) {
            Vertex cur = from;
            List<Vertex> alternativeRoute = new ArrayList<>(List.of(cur));
            for (int step = 0; step < maxSearchDepth && !success; step++) {
                List<Vertex> accessibleVertices = new ArrayList<>(cur.getNeighbours());
                accessibleVertices.removeAll(alternativeRoute);
                if (accessibleVertices.size() > 0) {
                    int r = random.nextInt(accessibleVertices.size());
                    cur = accessibleVertices.get(r);
                    alternativeRoute.add(cur);
                    if(traveledVertices.contains(cur) && step > 0)
                        success = true;
                } else {
                    System.out.println("Unable to find alternative path - dead end");
                    break;
                }
            }
            if(success) {
                System.out.println("Alternative path found! Replacing ...");
                replaceWithAlternativePath(alternativeRoute, from, cur);
                break;
            }
        }
    }

    public void mutate(int maxSearchDepth, int numOfTrials) {
        Random random = new Random();
        int r = random.nextInt(traveledVertices.size());
        Vertex randomVertex = traveledVertices.get(r);
        searchForAlternativeRoute(randomVertex, maxSearchDepth, numOfTrials);
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
