package org.example.graph;

import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex> {

    @Override
    public int compare(Vertex o1, Vertex o2) {
        if(o1.getCurLowestCost() > o2.getCurLowestCost())
            return 1;
        return -1;
    }
}
