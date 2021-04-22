package org.example.controller;

import lombok.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.example.controller.eventHandlers.AddVertexEvent;
import org.example.controller.eventHandlers.RemoveVertexEvent;
import org.example.view.vertex.VertexView;

import java.util.ArrayList;

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
    private Button start;

    private ArrayList<VertexView> graphVertices;

    private AddVertexEvent addVertexEvent;

    @FXML
    private void initialize() {
        ToggleGroup startEndNodeVertexToggleGroup = new ToggleGroup();
        startNode.setToggleGroup(startEndNodeVertexToggleGroup);
        endNode.setToggleGroup(startEndNodeVertexToggleGroup);
        startNode.setSelected(true);

        ToggleGroup addRemoveVertexToggleGroup = new ToggleGroup();
        addVertex.setToggleGroup(addRemoveVertexToggleGroup);
        removeVertex.setToggleGroup(addRemoveVertexToggleGroup);
        addVertex.setSelected(true);

        graphEditor.setOnMouseClicked(new AddVertexEvent(this));

        graphVertices = new ArrayList<>();
    }

    public void addVertexToPane(double centerPosX, double centerPosY) {
        VertexView v = new VertexView(centerPosX, centerPosY);
        v.setOnMouseClicked(new RemoveVertexEvent(this, v));
        graphEditor.getChildren().add(v);
        graphVertices.add(v);
    }

    public void removeVertexFromPane(VertexView vertexToRemove) {
        graphEditor.getChildren().remove(vertexToRemove);
        graphVertices.remove(vertexToRemove);
    }
}
