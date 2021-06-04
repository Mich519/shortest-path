package org.example.simulation.algorithms;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Setter;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;
import org.example.simulation.report.ReportContent;

import java.util.*;

public class Greedy implements Algorithm {

    private final Comparator<Vertex> vertexComparator;
    private final Graph graph;
    private LinkedHashSet<Vertex> processedVertices;
    private LinkedHashSet<Vertex> traversedVertices;
    private LinkedHashSet<Edge> shortestPath;
    private HashMap<Vertex, Vertex> mapVertexToPrev;
    @Setter
    private long elapsedTime = 0;

    public Greedy(Graph graph, Comparator<Vertex> vertexComparator, double simulationSpeed) {
        this.vertexComparator = vertexComparator;
        this.graph = graph;
    }


    @Override
    public void run() {
        // maps vertex to its predecessor in a path
        shortestPath = new LinkedHashSet<>();
        traversedVertices = new LinkedHashSet<>();
        processedVertices = new LinkedHashSet<>();
        mapVertexToPrev = new HashMap<>();
        graph.getVertices().forEach(v -> v.setCurLowestCost(Double.POSITIVE_INFINITY));
        mapVertexToPrev.put(graph.getStartVertex(), null);
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, vertexComparator);
        q.addAll(graph.getVertices());

        while (!q.isEmpty()) {

            Vertex v = q.poll();
            processedVertices.add(v);
            for (Edge w : v.getAdjEdges()) {
                /* perform relaxation for every vertex adjacent to v */
                Vertex u = w.getNeighbourOf(v);
                if (v.getCurLowestCost() + w.getLength().get() < u.getCurLowestCost()) {

                    // update predecessor of current vertex
                    u.setCurLowestCost(v.getCurLowestCost() + w.getLength().get());
                    mapVertexToPrev.put(u, v);

                    // update the priority queue by reinserting the vertex
                    q.remove(u);
                    q.add(u);
                }
            }
            if (v == graph.getEndVertex())
                break;
        }

        // build shortest path
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            traversedVertices.add(ver);
            Vertex pred = mapVertexToPrev.get(ver);
            if (pred != null) {
                Edge e = ver.findEdgeConnectedTo(pred);
                shortestPath.add(e);
            }
        }
    }

    @Override
    public void animate(PrimaryController controller) {
        controller.toogleButtonsActivity(true);
        SequentialTransition st = new SequentialTransition();

        /* vertices animation */
        for (Vertex v : processedVertices) {
            st.getChildren().add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
        }

        st.setCycleCount(1);

        /* color edges that represent a path */
        double totalLength = 0;
        List<Transition> pathTransitions = new ArrayList<>();
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            pathTransitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), ver, Vertex.defaultColor, Vertex.transitionColor));
            Vertex pred = mapVertexToPrev.get(ver);
            if (pred != null) {
                Edge e = ver.findEdgeConnectedTo(pred);
                totalLength += e.getLength().get();
                pathTransitions.add(new StrokeTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), e, Edge.DEFAULT_COLOR, Edge.TRANSITION_COLOR));
            }
        }
        Collections.reverse(pathTransitions);
        st.getChildren().addAll(pathTransitions);
        controller.getStop().setOnMouseClicked(mouseEvent -> {
            st.stop();
            controller.drawGraph();
            controller.toogleButtonsActivity(false);
        });
        st.play();

        //System.out.println(totalLength);
        st.setOnFinished(actionEvent -> {
            controller.toogleButtonsActivity(false);
        });
    }

    @Override
    public ReportContent generateReportContent() {
        ReportContent reportContent = new ReportContent();
        reportContent.addLabel(new Label("Number of processed vertices: " + processedVertices.size()));
        double totalLength = 0;
        for (Edge e : shortestPath) {
            totalLength += e.getLength().get();
        }
        reportContent.addLabel(new Label("Shortest path length: " + totalLength));
        reportContent.addLabel(new Label("Number of traversed vertices " + traversedVertices.size()));
        return reportContent;
    }
}
