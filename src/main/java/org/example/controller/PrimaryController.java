package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import lombok.Getter;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.GraphGenerator;
import org.example.graph.Vertex;
import org.example.graph.eventHandlers.OnMouseClickedEventHandler;
import org.example.simulation.Simulation;

// todo: add slider labels
// todo: fix window responsiveness
@Getter
public class PrimaryController {
    @FXML
    private Pane graphEditor;

    @FXML
    private Button clearGraphEditor;

    @FXML
    private RadioButton startNode;

    @FXML
    private RadioButton endNode;

    @FXML
    private RadioButton addVertex;

    @FXML
    private RadioButton removeVertex;

    @FXML
    private RadioButton addEdge;

    @FXML
    private RadioButton removeEdge;

    @FXML
    private Button generate;

    @FXML
    private Slider vertexCount;

    @FXML
    private RadioButton dijkstra;

    @FXML
    private Button start;

    private Graph graph;

    private GraphGenerator graphGenerator;

    private Simulation simulation;

    @FXML
    private void initialize() {
        ToggleGroup toggleGroup2 = new ToggleGroup();
        startNode.setToggleGroup(toggleGroup2);
        endNode.setToggleGroup(toggleGroup2);
        addVertex.setToggleGroup(toggleGroup2);
        removeVertex.setToggleGroup(toggleGroup2);
        addEdge.setToggleGroup(toggleGroup2);
        removeEdge.setToggleGroup(toggleGroup2);

        addVertex.setSelected(true);

        vertexCount.setMin(3);
        vertexCount.setMax(100);
        vertexCount.setValue(20);
        vertexCount.setShowTickLabels(true);
        vertexCount.setShowTickMarks(true);

        graphEditor.setOnMouseClicked(new OnMouseClickedEventHandler(this));

        graph = new Graph();
        graphGenerator = new GraphGenerator(this);
        simulation = new Simulation(this, graph);

        clearGraphEditor.setOnMouseClicked(mouseEvent -> clearAll());
        generate.setOnMouseClicked(mouseEvent -> {
            try {
                graphGenerator.generate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        start.setOnMouseClicked(mouseEvent -> {
            if(dijkstra.isSelected()) {
                simulation.simulateDijkstra();
            }
        });
    }

    public void addVertexToPane(Vertex v) {
        if (!graphEditor.getChildren().contains(v)) {
            graphEditor.getChildren().add(v);
        }
    }

    public void removeEdgeFromPane(Edge e) {
        graphEditor.getChildren().remove(e);
    }

    public void addEdgeToPane(Edge e) {
        if (!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
        }
    }

    public void drawGraph() {
        graphEditor.getChildren().clear();

        // draw vertices
        graph.getVertices().forEach(v -> {
            v.setFill(Paint.valueOf("#A7ABDD"));
            addVertexToPane(v);
        });

        // draw edges
        graph.getVertices().forEach(v -> v.getAdjEdges().forEach(this::addEdgeToPane));

        // set unique colors for start and end nodes
        if (graph.getStartVertex() != null)
            graph.getStartVertex().setFill(Paint.valueOf("#009933"));
        if (graph.getEndVertex() != null)
            graph.getEndVertex().setFill(Paint.valueOf("#FF0000"));
    }

    public void clearAll() {
        graphEditor.getChildren().clear();
        graph.removeAll();
        System.out.println(graph.getStartVertex());
        System.out.println(graph.getEndVertex());
    }
}
