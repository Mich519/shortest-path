package org.example.simulation;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.simulation.algorithms.*;
import org.example.simulation.algorithms.antOptimization.AntOptimization;

public class Simulation {

    private final PrimaryController controller;

    private final Dijkstra dijkstra;
    private final AStar aStar;
    private final AntOptimization antOptimization;
    private final BellmanFord bellmanFord;


    public Simulation(PrimaryController controller) {
        this.controller = controller;
        this.dijkstra = new Dijkstra(controller);
        this.aStar = new AStar(controller);
        this.antOptimization = new AntOptimization(controller);
        this.bellmanFord = new BellmanFord(controller);

    }

    public void simulateDijkstra() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            dijkstra.run();
        }
        controller.drawGraph();
    }

    public void simulateAStar() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            aStar.run();
        }
    }

    public void simulateAntOptimization() throws InterruptedException {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            antOptimization.run();
        }
    }

    public void simulateBellmanFord() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            bellmanFord.run();
        }
    }
}
