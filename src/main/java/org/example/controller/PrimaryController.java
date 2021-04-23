package org.example.controller;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.example.controller.eventHandlers.AddVertexEvent;
import org.example.controller.eventHandlers.RemoveVertexEvent;
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

    public void addVertexToPane(DoubleProperty centerPosX, DoubleProperty centerPosY) {
        Vertex<Double> v = new Vertex<>(this, 0.0, centerPosX, centerPosY);
        v.addEventFilter(MouseEvent.MOUSE_CLICKED, new RemoveVertexEvent(this, v));
        //v.addEventFilter(MouseEvent.MOUSE_DRAGGED, new OnDragEventHandler(this, v));
        //v.addEventFilter(MouseEvent.MOUSE_RELEASED, new OnMouseReleasedEventHandler(this, v));
        //v.addEventFilter(DragEvent.DRAG_ENTERED, );
        graphEditor.getChildren().add(v);
        graph.addVertex(v);
    }

    public void removeVertexFromPane(Vertex<Double> vertexToRemove) {
        graphEditor.getChildren().remove(vertexToRemove);
        graph.removeVertex(vertexToRemove);
    }

    public void addEdgeToPane(Edge e) {
        if(!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
        }
    }

    public void removeEdgeFromPane(Edge e) {
        graphEditor.getChildren().remove(e);
    }
}
