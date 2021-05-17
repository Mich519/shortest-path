package org.example.simulation.algorithms.genetic;

import org.example.graph.Edge;

import java.util.Comparator;
import java.util.Set;

public class IndividualByShortestPathComparator implements Comparator<Individual> {

    @Override
    public int compare(Individual individual1, Individual individual2) {
        if (individual1.getTraversedEdges().isEmpty() || !individual1.isPathSuccessful())
            return 1;
        if (individual2.getTraversedEdges().isEmpty() || !individual2.isPathSuccessful())
            return -1;

        int pathLength1 = individual1.getTraversedEdges().stream().mapToInt(edge -> (int) edge.getLength().get()).sum();
        int pathLength2 = individual2.getTraversedEdges().stream().mapToInt(edge -> (int) edge.getLength().get()).sum();
        if (pathLength1 < pathLength2)
            return -1;
        return 1;
    }
}
