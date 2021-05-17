package org.example.simulation.algorithms.genetic;

import lombok.Getter;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;

@Getter
public class Individual {
    private final Vertex startVertex;
    private final Vertex endVertex;
    private Vertex curVertex;
    private final List<Vertex> traveledVertices;

    public Individual(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        curVertex = startVertex;
        traveledVertices = new ArrayList<>();
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

    public boolean isPathSuccessful() {
        return curVertex == endVertex;
    }
}
