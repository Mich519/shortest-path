package org.example.simulation.algorithms.genetic;

import lombok.Getter;
import org.example.graph.Vertex;

import java.util.*;

@Getter
public class Individual implements Comparable<Individual>{
    private final Vertex startVertex;
    private final Vertex endVertex;
    private Vertex curVertex;
    private final List<Vertex> traveledVertices;
    private double totalCost;

    public Individual(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        curVertex = startVertex;
        traveledVertices = new ArrayList<>();
        totalCost = 0;
    }

    public void search() {
        while (true) {
            traveledVertices.add(curVertex);
            List<Vertex> neighbours = curVertex.getNeighbours();
            List<Vertex> accessibleVertices = new ArrayList<>(neighbours);
            accessibleVertices.removeAll(traveledVertices);

            if(isPathSuccessful() || accessibleVertices.size() == 0) {
                if (isPathSuccessful() ) {
                    System.out.println("Successful path was found");
                }
                break;
            }

            // choose random vertex
            int random = new Random().nextInt(accessibleVertices.size());
            curVertex = accessibleVertices.get(random);
        }
    }

    public void updateTotalCost() {
        totalCost = 0;
        for (int i=1; i<traveledVertices.size(); i++) {
            Vertex v1 = traveledVertices.get(i-1);
            Vertex v2 = traveledVertices.get(i);
            try {
                totalCost += v1.findEdgeConnectedTo(v2).getLength().get();
            }
            catch (NullPointerException e) {
                e.printStackTrace();

            }
        }
    }

    public boolean isPathSuccessful() {
        return curVertex == endVertex;
    }

    @Override
    public int compareTo(Individual individual) {
        if(getTotalCost() < individual.getTotalCost())
            return -1;
        return 1;
    }
}
