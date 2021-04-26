package org.example.simulation;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.simulation.algorithms.AStar;
import org.example.simulation.algorithms.Dijkstra;

public class Simulation {

    private final PrimaryController controller;
    private final Graph graph;

    private final Dijkstra dijkstra;
    private final AStar aStar;


    public Simulation(PrimaryController controller, Graph graph) {
        this.controller = controller;
        this.graph = graph;
        this.dijkstra = new Dijkstra(graph);
        this.aStar = new AStar(graph);
    }

    public void simulateDijkstra() {
        if (graph.getStartVertex() != null && graph.getEndVertex() != null) {
            /* check if start and end vertices are set */
            dijkstra.run();
        }
    }

    public void simulateAStar() {
        if (graph.getStartVertex() != null && graph.getEndVertex() != null) {
            /* check if start and end vertices are set */
            aStar.run();
        }
    }
}
