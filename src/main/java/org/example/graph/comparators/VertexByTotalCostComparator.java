package org.example.graph.comparators;

import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.Comparator;

public class VertexByTotalCostComparator implements Comparator<Vertex> {
    private final Graph graph;

    public VertexByTotalCostComparator(Graph graph) {
        this.graph = graph;
    }

    @Override
    public int compare(Vertex o1, Vertex o2) {
        if(o1.getCurLowestCost() + graph.calcDistanceToGoal(o1) > o2.getCurLowestCost() + graph.calcDistanceToGoal(o2))
            return 1;
        return -1;
    }
}
