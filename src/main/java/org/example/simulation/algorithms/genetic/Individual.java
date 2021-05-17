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
    private final Set<Vertex> traveledVertices;

    public Individual(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        curVertex = startVertex;
        traveledVertices = new LinkedHashSet<>();
    }

    public void search() {
        while (true) {
            traveledVertices.add(curVertex);
            List<Vertex> accessibleVerices = new ArrayList<>();
            for (Edge adjEdge : curVertex.getAdjEdges()) {
                for(Vertex neighbour : adjEdge.getVertices())
                if (!traversedEdges.contains(e))
                    accessibleRoutes.add(e);
            }
            if(curVertex == endVertex || accessibleRoutes.size() == 0) {
                if (curVertex == endVertex) {
                    System.out.println("Successful path was found");
                }
                break;
            }

            int random = new Random().nextInt(accessibleRoutes.size());
            Edge randomEdge = accessibleRoutes.get(random);
            Vertex nextVertex = randomEdge.getNeighbourOf(curVertex);
            traversedEdges.add(randomEdge);
            curVertex = nextVertex;
        }
    }

    public boolean isPathSuccessful() {
        return curVertex == endVertex;
    }
}
