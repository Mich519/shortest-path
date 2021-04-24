package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.GraphGenerator;
import org.example.graph.Vertex;
import org.example.graph.eventHandlers.AddVertexEvent;

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
    private Button start;

    @FXML
    private Slider vertexCount;

    private Graph graph;

    private GraphGenerator graphGenerator;

    @FXML
    private void initialize() {
        ToggleGroup toogleGroup1 = new ToggleGroup();
        startNode.setToggleGroup(toogleGroup1);
        endNode.setToggleGroup(toogleGroup1);

        startNode.setSelected(true);

        ToggleGroup toggleGroup2 = new ToggleGroup();
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

        graphEditor.setOnMouseClicked(new AddVertexEvent(this));



        graph = new Graph();
        graphGenerator = new GraphGenerator(this);

        clearGraphEditor.setOnMouseClicked(mouseEvent -> clearAll());
        generate.setOnMouseClicked(mouseEvent -> {
            try {
                graphGenerator.generate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addVertexToPane(Vertex vertexToAdd) {
        if (!graphEditor.getChildren().contains(vertexToAdd)) {
            graphEditor.getChildren().add(vertexToAdd);
        }
    }

    public void removeEdgeFromPane(Edge e) {
        graphEditor.getChildren().remove(e);
    }

    public void drawEdge(Edge e) {
        if (!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
        }
    }

    public void drawEdge(Vertex v1, Vertex v2) {
        Edge e = new Edge(v1, v2);
        if (!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
        }
    }

    public void drawGraph() {
        graphEditor.getChildren().clear();
        /* parse graph structure and draw it on graphEditor */

        // draw vertices
        graph.getVertices().keySet().forEach(this::addVertexToPane);

        // draw edges
        graph.getVertices().forEach((vertex, adjVertices) -> {
            adjVertices.forEach(adjVertex -> drawEdge(vertex, adjVertex));
        });
    }

    public void clearAll() {
        graphEditor.getChildren().clear();
        graph.removeAll();
    }
}
