package org.example.simulation;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.comparators.VertexByCurLowestCostComparator;
import org.example.graph.comparators.VertexByTotalCostComparator;
import org.example.simulation.algorithms.BellmanFord;
import org.example.simulation.algorithms.Naive;
import org.example.simulation.algorithms.antOptimization.AntOptimization;
import org.example.simulation.algorithms.antOptimization.Chart;
import org.example.simulation.algorithms.antOptimization.ParametersContainer;
import org.example.simulation.algorithms.genetic.Genetic;

import java.io.IOException;

public class Simulation {

    private final PrimaryController controller;

    public Simulation(PrimaryController controller) {
        this.controller = controller;
    }

    public void simulateDijkstra() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */

            Graph graph = controller.getGraph();
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            Naive dijkstra = new Naive(graph, new VertexByCurLowestCostComparator(), simulationSpeed);
            dijkstra.run();
            if(controller.getShowAnimation().isSelected()) {
                dijkstra.animate(controller);
            }
        }
    }

    public void simulateAStar() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            Naive aStar = new Naive(graph, new VertexByTotalCostComparator(controller.getGraph()), simulationSpeed);
            aStar.run();
            if(controller.getShowAnimation().isSelected()) {
                aStar.animate(controller);
            }
        }
    }

    public void simulateAntOptimization() throws InterruptedException {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            ParametersContainer parameters = new ParametersContainer(controller);
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            AntOptimization antOptimization = new AntOptimization(graph, parameters, simulationSpeed);
            antOptimization.run();

            // generate report if selected
            if (controller.getGenerateReport().isSelected()) {
                try {
                    Chart chart = new Chart(parameters, antOptimization.getSuccessfulPathsPerIteration(), antOptimization.getAllPaths());
                    chart.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(controller.getShowAnimation().isSelected()) {
                antOptimization.animate(controller);
            }
        }
    }

    public void simulateBellmanFord() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */

            Graph graph = controller.getGraph();
            BellmanFord bellmanFord = new BellmanFord(graph);
            bellmanFord.run();
            if(controller.getShowAnimation().isSelected()) {
                bellmanFord.animate(controller);
            }
        }
    }

    public void simulateGenetic() {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            Genetic genetic = new Genetic(graph);
            genetic.run();
            if(controller.getShowAnimation().isSelected()) {
                genetic.animate(controller);
            }
        }
    }
}
