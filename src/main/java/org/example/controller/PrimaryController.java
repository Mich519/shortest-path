package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.example.controller.eventHandlers.DragEventHandler;
import org.example.controller.eventHandlers.MouseEventHandler;

public class PrimaryController {

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

    private ToggleGroup startEndNodeVertexToggleGroup;
    private ToggleGroup addRemoveVertexToggleGroup;

    private DragEventHandler dragEventHandler;
    private MouseEventHandler mouseEventHandler;

    @FXML
    private void initialize() {
        startEndNodeVertexToggleGroup = new ToggleGroup();
        startNode.setToggleGroup(startEndNodeVertexToggleGroup);
        endNode.setToggleGroup(startEndNodeVertexToggleGroup);
        startNode.setSelected(true);

        addRemoveVertexToggleGroup = new ToggleGroup();
        addVertex.setToggleGroup(addRemoveVertexToggleGroup);
        removeVertex.setToggleGroup(addRemoveVertexToggleGroup);
        addVertex.setSelected(true);

        dragEventHandler =  new DragEventHandler();
        mouseEventHandler = new MouseEventHandler();
    }


}
