package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;
import org.example.fileIO.FileInOutHandler;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.GraphGenerator;
import org.example.graph.Vertex;
import org.example.graph.eventHandlers.OnMouseClickedEventHandler;
import org.example.simulation.Simulation;

import java.io.IOException;
import java.io.Serializable;

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
    private Button save;

    @FXML
    private Button load;

    @FXML
    private Slider vertexCount;

    @FXML
    private RadioButton dijkstra;

    @FXML
    private RadioButton aStar;

    @FXML
    private Button start;

    private Graph graph;
    private GraphGenerator graphGenerator;
    private Simulation simulation;
    private FileInOutHandler fIleInOutHandler;

    private void initRadioButtons() {
        ToggleGroup toggleGroup1 = new ToggleGroup();
        startNode.setToggleGroup(toggleGroup1);
        endNode.setToggleGroup(toggleGroup1);
        addVertex.setToggleGroup(toggleGroup1);
        removeVertex.setToggleGroup(toggleGroup1);
        addEdge.setToggleGroup(toggleGroup1);
        removeEdge.setToggleGroup(toggleGroup1);
        addVertex.setSelected(true);

        ToggleGroup toggleGroup2 = new ToggleGroup();
        dijkstra.setToggleGroup(toggleGroup2);
        aStar.setToggleGroup(toggleGroup2);
    }

    public void initButtons() {
        graphEditor.setOnMouseClicked(new OnMouseClickedEventHandler(this));
        clearGraphEditor.setOnMouseClicked(mouseEvent -> clearAll());
        generate.setOnMouseClicked(mouseEvent -> {
            try {
                graphGenerator.generate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        start.setOnMouseClicked(mouseEvent -> {
            if (dijkstra.isSelected()) {
                simulation.simulateDijkstra();
            } else if (aStar.isSelected()) {
                simulation.simulateAStar();
            }
        });

        save.setOnMouseClicked(mouseEvent -> {
            try {
                fIleInOutHandler.safeGraphToFile(graph);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        load.setOnMouseClicked(mouseEvent -> {
            try {
                clearAll();
                graph = fIleInOutHandler.loadGraphFromFile();
                System.out.println(graph.getVertices());
                drawGraph();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void initSliders() {
        vertexCount.setMin(3);
        vertexCount.setMax(100);
        vertexCount.setValue(20);
        vertexCount.setShowTickLabels(true);
        vertexCount.setShowTickMarks(true);
    }

    @FXML
    private void initialize() {
        initRadioButtons();
        initSliders();
        this.graph = new Graph();
        this.graphGenerator = new GraphGenerator(this);
        this.simulation = new Simulation(this, graph);
        this.fIleInOutHandler = new FileInOutHandler(this);
        initButtons();
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
            v.setFill(Vertex.defaultColor);
            addVertexToPane(v);
        });

        // draw edges
        graph.getVertices().forEach(v -> v.getAdjEdges().forEach(e -> {
            e.setStroke(Edge.defaultColor);
            addEdgeToPane(e);
        }));

        // set unique colors for start and end nodes
        if (graph.getStartVertex() != null)
            graph.getStartVertex().setFill(Vertex.startColor);
        if (graph.getEndVertex() != null)
            graph.getEndVertex().setFill(Vertex.endColor);
    }

    public void clearAll() {
        graphEditor.getChildren().clear();
        graph.removeAll();
        System.out.println(graph.getStartVertex());
        System.out.println(graph.getEndVertex());
    }
}
