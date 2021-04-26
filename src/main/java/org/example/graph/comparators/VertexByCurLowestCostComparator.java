package org.example.graph.comparators;

import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.Comparator;

public class VertexByCurLowestCostComparator implements Comparator<Vertex> {

    @Override
    public int compare(Vertex o1, Vertex o2) {
        if(o1.getCurLowestCost() > o2.getCurLowestCost())
            return 1;
        return -1;
    }
}
