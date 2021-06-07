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
import org.example.simulation.report.ReportContent;
import org.example.simulation.report.ReportGenerator;

public class Simulation {

    private final PrimaryController controller;
    private final Benchmarker benchmarker;

    public Simulation(PrimaryController controller) {
        this.controller = controller;
        this.benchmarker = new Benchmarker(controller);
    }

    private void simulate(Algorithm algorithm) throws Exception {
        if (controller.getGraph().getStartVertex() != null && controller.getGraph().getEndVertex() != null) {
            controller.drawGraph();
            ReportContent benchmarkReport = null;
            if (controller.getBenchmark().isSelected()) {
                benchmarkReport = benchmarker.runWithBenchmark(algorithm);
            } else {
                algorithm.run();
            }
            if (controller.getGenerateReport().isSelected()) {
                ReportGenerator reportGenerator = new ReportGenerator();
                reportGenerator.addReportContent(algorithm.generateReportContent());
                if (benchmarkReport != null)
                    reportGenerator.addReportContent(benchmarkReport);
                reportGenerator.generateRaport();
            }
            if (controller.getShowAnimation().isSelected()) {
                algorithm.animate(controller);
            }
        }
    }

    public void simulateDijkstra() throws Exception {
        Graph graph = controller.getGraph();
        Greedy dijkstra = new Greedy(graph, new VertexByCurLowestCostComparator());
        simulate(dijkstra);
    }

    public void simulateAStar() throws Exception {
        Graph graph = controller.getGraph();
        Greedy aStar = new Greedy(graph, new VertexByTotalCostComparator(graph));
        simulate(aStar);
    }

    public void simulateAntOptimization() throws Exception {
        Graph graph = controller.getGraph();
        AntParametersContainer parameters = new AntParametersContainer(controller);
        AntOptimization antOptimization = new AntOptimization(graph, parameters);
        simulate(antOptimization);
    }

    public void simulateBellmanFord() throws Exception {
        Graph graph = controller.getGraph();
        BellmanFord bellmanFord = new BellmanFord(graph);
        simulate(bellmanFord);
    }

    public void simulateGenetic() throws Exception {
        Graph graph = controller.getGraph();
        GeneticParametersContainer parameters = new GeneticParametersContainer(controller);
        Genetic genetic = new Genetic(graph, parameters);
        simulate(genetic);
    }
}
