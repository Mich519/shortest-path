package org.example.simulation.algorithms;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.util.*;

public class Greedy implements Algorithm {

    private final Comparator<Vertex> vertexComparator;
    private final Graph graph;
    private final double simulationSpeed;
    private LinkedHashSet<Vertex> shortestPath;
    private HashMap<Vertex, Vertex> mapVertexToPrev;

    public Greedy(Graph graph, Comparator<Vertex> vertexComparator, double simulationSpeed) {
        this.vertexComparator = vertexComparator;
        this.graph = graph;
        this.simulationSpeed = simulationSpeed;
    }


    @Override
    public void run() {
        // maps vertex to its predecessor in a path
        shortestPath = new LinkedHashSet<>();
        mapVertexToPrev = new HashMap<>();
        graph.getVertices().forEach(v -> v.setCurLowestCost(Double.POSITIVE_INFINITY));
        mapVertexToPrev.put(graph.getStartVertex(), null);
        graph.getStartVertex().setCurLowestCost(0);
        PriorityQueue<Vertex> q = new PriorityQueue<>(10, vertexComparator);
        q.addAll(graph.getVertices());

        while (!q.isEmpty()) {

            Vertex v = q.poll();
            shortestPath.add(v);
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
    }

    @Override
    public void animate(PrimaryController controller) {
        controller.toogleButtonsActivity(true);
        SequentialTransition st = new SequentialTransition();

        /* vertices animation */
        for (Vertex v : shortestPath) {
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
    public void generateReport() {

    }
}
