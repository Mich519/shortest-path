package org.example.simulation;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.simulation.algorithms.*;

public class Simulation {

    private final PrimaryController controller;

    private final Dijkstra dijkstra;
    private final AStar aStar;


    public Simulation(PrimaryController controller) {
        this.controller = controller;
        this.dijkstra = new Dijkstra();
        this.aStar = new AStar();
    }

    public void simulateDijkstra() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            dijkstra.run(controller.getGraph());
        }
        controller.drawGraph();
    }

    public void simulateAStar() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            aStar.run(controller.getGraph());
        }
    }
}
