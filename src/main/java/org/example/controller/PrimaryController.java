package org.example.controller;

import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import lombok.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.example.graph.eventHandlers.AddVertexEvent;
import org.example.graph.eventHandlers.RemoveVertexEvent;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

@Getter
public class PrimaryController {
    @FXML
    private Pane graphEditor;

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
    private Button start;

    private Graph graph;

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

        graphEditor.setOnMouseClicked(new AddVertexEvent(this));

        graph = new Graph();
    }

    public void addVertexToPane(Vertex vertexToAdd) {
        graphEditor.getChildren().add(vertexToAdd);
        graph.addVertex(vertexToAdd);
    }

    public void removeVertexFromPane(Vertex vertexToRemove) {

        // remove all edges connected to this vertex
        double centerX = vertexToRemove.getCenterX();
        double centerY = vertexToRemove.getCenterY();
        graphEditor.getChildren().removeIf(node -> {
            if(node instanceof Line) {
                Edge e = (Edge) node;
                return (e.getEndX() == centerX && e.getEndY() == centerY) || (e.getStartX() == centerX && e.getStartY() == centerY);
            }
                return false;
        });


        graphEditor.getChildren().remove(vertexToRemove);
        graph.removeVertex(vertexToRemove);
    }

    public void addEdgeToPane(Edge e) {
        if (!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
        }
    }

    public void removeEdgeFromPane(Edge e) {
        graphEditor.getChildren().remove(e);
    }
}
