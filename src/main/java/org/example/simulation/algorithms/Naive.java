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

public class Naive implements Algorithm {

    private final Comparator<Vertex> vertexComparator;
    private final Graph graph;
    private final double simulationSpeed;
    private final LinkedHashSet<Vertex> shortestPath;
    private final HashMap<Vertex, Vertex> mapVertexToPrev;

    public Naive(Graph graph, Comparator<Vertex> vertexComparator, double simulationSpeed) {
        this.vertexComparator = vertexComparator;
        this.graph = graph;
        this.simulationSpeed = simulationSpeed;
        this.shortestPath = new LinkedHashSet<>();
        this.mapVertexToPrev = new HashMap<>();
    }


    @Override
    public void run() {
        // maps vertex to its predecessor in a path
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
        /* vertices animation */
        List<Transition> transitions = new ArrayList<>();
        for (Vertex v : shortestPath) {
            transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), v, Color.ORANGE, Color.BLUEVIOLET));
        }
        SequentialTransition st = new SequentialTransition();
        st.setCycleCount(1);

        /* color edges that represent a path */
        for (Vertex ver = graph.getEndVertex(); ver != null; ver = mapVertexToPrev.get(ver)) {
            transitions.add(new FillTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), ver, Color.ORANGE, Color.RED));
            Vertex pred = mapVertexToPrev.get(ver);
            if (pred != null) {
                Edge e = ver.findEdgeConnectedTo(pred);
                transitions.add(new StrokeTransition(Duration.millis(controller.getSimulationSpeed().getMax() - controller.getSimulationSpeed().getValue()), e, Color.LIMEGREEN, Color.BLUE));
            }
        }
        st.getChildren().addAll(transitions);
        st.play();

        /* reset everything */
        st.setOnFinished(actionEvent -> {
            for (Vertex v : graph.getVertices()) {
                v.setCurLowestCost(Double.POSITIVE_INFINITY);
                controller.toogleButtonsActivity(false);
            }
        });
    }

}
