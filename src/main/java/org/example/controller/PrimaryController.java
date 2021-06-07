package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import org.example.fileIO.FileInOutHandler;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.GraphGenerator;
import org.example.graph.Vertex;
import org.example.graph.eventHandlers.OnMouseClickedEventHandler;
import org.example.simulation.Simulation;

import java.io.File;
import java.io.IOException;

@Getter
public class PrimaryController {
    @FXML
    private SplitPane splitPane;

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
    private RadioButton edgeLabels;

    @FXML
    private Slider vertexRadius;

    @FXML
    private Slider edgeWidth;

    @FXML
    private Button generate;

    @FXML
    private MenuItem neww;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem load;

    @FXML
    private Slider vertexCount;

    @FXML
    private Slider simulationSpeed;

    @FXML
    private RadioButton dijkstra;

    @FXML
    private RadioButton aStar;

    @FXML
    private RadioButton antOptimization;

    @FXML
    private RadioButton bellmanFord;

    @FXML
    private RadioButton genetic;

    @FXML
    private Slider elitismRate;

    @FXML
    private Slider initialPopulation;

    @FXML
    private Slider numOfGenerations;

    @FXML
    private Slider mutationRatio;

    @FXML
    private Slider numberOfAnts;

    @FXML
    private Slider pheromonePerAnt;

    @FXML
    private Slider alpha;

    @FXML
    private Slider beta;

    @FXML
    private Slider evaporationRate;

    @FXML
    private Slider numberOfIterations;

    @FXML
    private RadioButton showWeights;

    @FXML
    private RadioButton generateReport;

    @FXML
    private RadioButton showAnimation;

    @FXML
    private RadioButton benchmark;

    @FXML
    private Slider numOfTests;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    private Graph graph;
    private GraphGenerator graphGenerator;
    private Simulation simulation;
    private FileInOutHandler fileInOutHandler;

    private void initRadioButtons() {
        ToggleGroup toggleGroup1 = new ToggleGroup();
        startNode.setToggleGroup(toggleGroup1);
        endNode.setToggleGroup(toggleGroup1);
        addVertex.setToggleGroup(toggleGroup1);
        removeVertex.setToggleGroup(toggleGroup1);
        addVertex.setSelected(true);

        ToggleGroup toggleGroup2 = new ToggleGroup();
        dijkstra.setToggleGroup(toggleGroup2);
        aStar.setToggleGroup(toggleGroup2);
        antOptimization.setToggleGroup(toggleGroup2);
        bellmanFord.setToggleGroup(toggleGroup2);
        genetic.setToggleGroup(toggleGroup2);

        edgeLabels.setSelected(true);
        edgeLabels.setOnMouseClicked(mouseEvent -> {
            for (Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges())
                    e.getLengthLabel().setVisible(edgeLabels.isSelected());
            }
        });

        showWeights.setOnMouseClicked(event -> {
            for (Vertex v : graph.getVertices()) {
                for (Edge e : v.getAdjEdges()) {
                    e.getLengthLabel().setVisible(showWeights.isSelected());
                }
            }
        });
        showAnimation.setSelected(true);
    }

    public void initButtons() {
        graphEditor.setOnMouseClicked(new OnMouseClickedEventHandler(this));
        clearGraphEditor.setOnMouseClicked(mouseEvent -> clearAll());
        neww.setOnAction(event -> clearAll());
        generate.setOnMouseClicked(mouseEvent -> {
            try {
                graphGenerator.generate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        start.setOnMouseClicked(mouseEvent -> {
            if (dijkstra.isSelected()) {
                try {
                    simulation.simulateDijkstra();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (aStar.isSelected()) {
                try {
                    simulation.simulateAStar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (antOptimization.isSelected()) {
                try {
                    simulation.simulateAntOptimization();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bellmanFord.isSelected()) {
                try {
                    simulation.simulateBellmanFord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (genetic.isSelected()) {
                try {
                    simulation.simulateGenetic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void initSliders() {
        vertexCount.setMin(3);
        vertexCount.setMax(100);
        vertexCount.setValue(20);
        vertexCount.setShowTickLabels(true);
        vertexCount.setShowTickMarks(true);

        simulationSpeed.setMin(100);
        simulationSpeed.setMax(1000);
        simulationSpeed.setValue(500);
        simulationSpeed.setShowTickMarks(true);

        // ant optimization sliders
        numberOfAnts.setMin(100);
        numberOfAnts.setMax(2000);
        numberOfAnts.setValue(100);
        numberOfAnts.setShowTickLabels(true);
        numberOfAnts.setShowTickMarks(true);
        pheromonePerAnt.setMin(100);
        pheromonePerAnt.setMax(2000);
        pheromonePerAnt.setValue(1000);
        pheromonePerAnt.setShowTickLabels(true);
        pheromonePerAnt.setShowTickMarks(true);
        alpha.setMin(1);
        alpha.setMax(10);
        alpha.setValue(2.1);
        alpha.setShowTickLabels(true);
        alpha.setShowTickMarks(true);
        beta.setMin(1);
        beta.setMax(10);
        beta.setValue(2.1);
        beta.setShowTickLabels(true);
        beta.setShowTickMarks(true);
        evaporationRate.setMin(0.1);
        evaporationRate.setMax(0.9);
        evaporationRate.setValue(0.7);
        evaporationRate.setShowTickLabels(true);
        evaporationRate.setShowTickMarks(true);
        numberOfIterations.setMin(10);
        numberOfIterations.setMax(2000);
        numberOfIterations.setValue(100);
        numberOfIterations.setShowTickLabels(true);
        numberOfIterations.setShowTickMarks(true);

        // genetic sliders
        elitismRate.setMin(0);
        elitismRate.setMax(1);
        elitismRate.setValue(0.2);
        elitismRate.setShowTickLabels(true);
        elitismRate.setShowTickMarks(true);
        initialPopulation.setMin(400);
        initialPopulation.setMax(5000);
        initialPopulation.setValue(500);
        initialPopulation.setShowTickLabels(true);
        initialPopulation.setShowTickMarks(true);
        numOfGenerations.setMin(10);
        numOfGenerations.setMax(1000);
        numOfGenerations.setValue(40);
        numOfGenerations.setShowTickLabels(true);
        numOfGenerations.setShowTickMarks(true);
        mutationRatio.setMin(0);
        mutationRatio.setMax(1.0);
        mutationRatio.setValue(0.1);
        mutationRatio.setShowTickLabels(true);
        mutationRatio.setShowTickMarks(true);

        vertexRadius.setMin(5);
        vertexRadius.setMax(30);
        vertexRadius.setValue(20);

        edgeWidth.setMin(1);
        edgeWidth.setMax(5);
        edgeWidth.setValue(4);

        numOfTests.setMin(1);
        numOfTests.setValue(5);
        numOfTests.setMax(10);
        numOfTests.setShowTickLabels(true);
        numOfTests.setShowTickMarks(true);
    }

    @FXML
    private void initialize() {
        initRadioButtons();
        initSliders();
        this.graph = new Graph();
        this.graphGenerator = new GraphGenerator(this);
        this.simulation = new Simulation(this);
        this.fileInOutHandler = new FileInOutHandler(this);
        initButtons();
    }

    public void afterInitialize(WindowEvent event) {
        /* this executes after scene is initialized */

        /* initialize load graph button */
        load.setOnAction(event1 -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.setInitialDirectory(new File("src/main/resources/org/example/graphs"));
            Stage owner = (Stage) load.getParentPopup().getOwnerWindow();
            Scene scene = owner.getScene();
            //Scene scene = load.getParentMenu().getScene();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                try {
                    clearAll();
                    this.graph = fileInOutHandler.loadGraphFromFile(file);
                    drawGraph();
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
        });

        /* initialize save graph button */
        save.setOnAction(event1 -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/org/example/graphs"));
            Stage owner = (Stage) save.getParentPopup().getOwnerWindow();
            Scene scene = owner.getScene();
            File file = fileChooser.showSaveDialog(scene.getWindow());
            if (file != null) {
                try {
                    fileInOutHandler.safeGraphToFile(graph, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /* initialize split pane divosor position */
        splitPane.setDividerPositions(0.7);
        SplitPane.setResizableWithParent(splitPane, false);
    }

    public void toogleButtonsActivity(boolean disabled) {
        /* disable or enable buttons */
        startNode.setDisable(disabled);
        endNode.setDisable(disabled);
        addVertex.setDisable(disabled);
        removeVertex.setDisable(disabled);
        showWeights.setDisable(disabled);
        vertexCount.setDisable(disabled);
        generate.setDisable(disabled);
        dijkstra.setDisable(disabled);
        aStar.setDisable(disabled);
        bellmanFord.setDisable(disabled);
        antOptimization.setDisable(disabled);
        start.setDisable(disabled);
        clearGraphEditor.setDisable(disabled);
        edgeLabels.setDisable(disabled);
    }

    public Duration calculateFrameDuration() {
        return Duration.millis(getSimulationSpeed().getMax() + 1 - getSimulationSpeed().getValue());
    }

    public void addVertexToPane(Vertex v) {
        if (!graphEditor.getChildren().contains(v)) {
            graphEditor.getChildren().add(v);
        }
    }

    public void removeEdgeFromPane(Edge e) {
        graphEditor.getChildren().remove(e.getLengthLabel()); // also remove its label
        graphEditor.getChildren().remove(e);
    }

    public void addEdgeToPane(Edge e) {
        if (!graphEditor.getChildren().contains(e)) {
            graphEditor.getChildren().add(e);
            graphEditor.getChildren().addAll(e.getLengthLabel()); // also add its label
        }
    }

    public void drawGraph() {
        graphEditor.getChildren().clear();

        // draw vertices
        graph.getVertices().forEach(v -> {
            v.setFill(Vertex.DEFAULT_COLOR);
            addVertexToPane(v);
        });

        // draw edges
        graph.getVertices().forEach(v -> v.getAdjEdges().forEach(e -> {
            e.setStroke(Edge.DEFAULT_COLOR);
            addEdgeToPane(e);
        }));

        // set unique colors for start and end nodes
        if (graph.getStartVertex() != null)
            graph.getStartVertex().setFill(Vertex.START_COLOR);
        if (graph.getEndVertex() != null)
            graph.getEndVertex().setFill(Vertex.END_COLOR);
    }

    public void clearAll() {
        graphEditor.getChildren().clear();
        graph.removeAll();
    }
}
