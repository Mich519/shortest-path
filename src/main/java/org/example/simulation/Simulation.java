package org.example.simulation;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.comparators.VertexByCurLowestCostComparator;
import org.example.graph.comparators.VertexByTotalCostComparator;
import org.example.simulation.algorithms.Algorithm;
import org.example.simulation.algorithms.BellmanFord;
import org.example.simulation.algorithms.Greedy;
import org.example.simulation.algorithms.antOptimization.AntOptimization;
import org.example.simulation.algorithms.antOptimization.AntParametersContainer;
import org.example.simulation.algorithms.genetic.Genetic;
import org.example.simulation.algorithms.genetic.GeneticParametersContainer;
import org.example.simulation.report.ReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final PrimaryController controller;

    public Simulation(PrimaryController controller) {
        this.controller = controller;
    }

    private void measureExecutionTime(Algorithm algorithm) throws Exception {
        if (controller.getBenchmark().isSelected()) {
            final int skip = 20; // warmup faze
            final int numOfTests = 100;
            for (int j = 0; j < (int) controller.getNumOfTests().getValue(); j++) {
                List<Long> times = new ArrayList<>(numOfTests);
                for (int i = 0; i < numOfTests + skip; i++) {
                    long startTime = System.nanoTime();
                    algorithm.run();
                    long finishTime = System.nanoTime();
                    long elapsed = finishTime - startTime;
                    if (i > skip) {
                        times.add(elapsed / 100);
                    }
                }
                long avg = times.stream().reduce(Long::sum).get() / times.size();
                System.out.println("Avg time: " + avg);
            }
        } else
            algorithm.run();
    }

    public void simulateDijkstra() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */

            Graph graph = controller.getGraph();
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            Greedy dijkstra = new Greedy(graph, new VertexByCurLowestCostComparator());
            measureExecutionTime(dijkstra);
            if (controller.getGenerateReport().isSelected()) {
                ReportGenerator reportGenerator = new ReportGenerator();
                reportGenerator.generateRaport(dijkstra);
            }
            if (controller.getShowAnimation().isSelected()) {
                dijkstra.animate(controller);
            }
        }
    }

    public void simulateAStar() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            Greedy aStar = new Greedy(graph, new VertexByTotalCostComparator(controller.getGraph()));
            measureExecutionTime(aStar);
            if (controller.getGenerateReport().isSelected()) {
                ReportGenerator reportGenerator = new ReportGenerator();
                reportGenerator.generateRaport(aStar);
            }
            if (controller.getShowAnimation().isSelected()) {
                aStar.animate(controller);
            }
        }
    }

    public void simulateAntOptimization() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            AntParametersContainer parameters = new AntParametersContainer(controller);
            double simulationSpeed = controller.getSimulationSpeed().getValue();
            AntOptimization antOptimization = new AntOptimization(graph, parameters);
            measureExecutionTime(antOptimization);
            if (controller.getGenerateReport().isSelected()) {
                ReportGenerator reportGenerator = new ReportGenerator();
                reportGenerator.generateRaport(antOptimization);
            }
            if (controller.getShowAnimation().isSelected()) {
                antOptimization.animate(controller);
            }
        }
    }

    public void simulateBellmanFord() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */

            Graph graph = controller.getGraph();
            BellmanFord bellmanFord = new BellmanFord(graph);
            measureExecutionTime(bellmanFord);
            if (controller.getShowAnimation().isSelected()) {
                bellmanFord.animate(controller);
            }
        }
    }

    public void simulateGenetic() throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            /* check if start and end vertices are set */
            Graph graph = controller.getGraph();
            GeneticParametersContainer parameters = new GeneticParametersContainer(controller);
            Genetic genetic = new Genetic(graph, parameters);
            measureExecutionTime(genetic);
            if (controller.getGenerateReport().isSelected()) {
                ReportGenerator reportGenerator = new ReportGenerator();
                reportGenerator.generateRaport(genetic);
            }
            if (controller.getShowAnimation().isSelected()) {
                genetic.animate(controller);
            }
        }
    }
}
