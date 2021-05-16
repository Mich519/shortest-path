package org.example.simulation.algorithms.antOptimization;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.App;
import org.example.graph.Edge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// todo: previous data is not deleted
public class Chart {
    private final ParametersContainer parameters;
    private final List<Integer> antsThatReachedGoalPerIteration;
    private final List<Set<Edge>> allPaths;

    private LineChart<Number, Number> successfulPathChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of iteration");
        yAxis.setLabel("% of ants that reached the goal");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Percentage of ants that reached the goal");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < antsThatReachedGoalPerIteration.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, (double) (antsThatReachedGoalPerIteration.get(i) * 100 / parameters.numOfAnts)));
        }
        lineChart.getData().add(series);
        return lineChart;
    }

    private LineChart<Number, Number> shortestPathsChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("nth path (from shortest to longest)");
        yAxis.setLabel("Path length (sum of weight)");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Length of current shortest paths");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < allPaths.size(); i++) {
            double sum = 0.0;
            for (Edge e : allPaths.get(i)) {
                sum += e.getLength().get();
            }
            series.getData().add(new XYChart.Data<>(i, sum));
        }
        lineChart.getData().add(series);
        return lineChart;
    }

    private List<Label> createLabelParameters() {
        List<Label> parameterLabels = new ArrayList<>();
        parameterLabels.add(new Label("Number of iterations: "+ parameters.numOfIterations));
        parameterLabels.add(new Label("Number of ants: "+ parameters.numOfAnts));
        parameterLabels.add(new Label("Pheromone per ant: "+ parameters.pheromonePerAnt));
        parameterLabels.add(new Label("Evaporation rate: "+ parameters.evapRate));
        parameterLabels.add(new Label("Alpha: "+ parameters.alpha));
        parameterLabels.add(new Label("Beta: "+ parameters.beta));
        parameterLabels.add(new Label("Number of found paths: "+ allPaths.size()));
        return parameterLabels;
    }

    public Chart(ParametersContainer parameters, List<Integer> antsThatReachedGoalPerIteration,
                 List<Set<Edge>> allPaths) throws IOException {

        this.parameters = parameters;
        this.antsThatReachedGoalPerIteration = antsThatReachedGoalPerIteration;
        this.allPaths = allPaths;
    }



    public void show() throws IOException {
        LineChart<Number, Number> lineChart1 = successfulPathChart();
        LineChart<Number, Number> lineChart2 = shortestPathsChart();
        List<Label> labels = createLabelParameters();
        VBox labelsContainer = new VBox();
        for (Label l : labels) {
            labelsContainer.getChildren().add(l);
        }

        FlowPane chartContainer = new FlowPane(lineChart1, lineChart2);
        chartContainer.getChildren().add(labelsContainer);
        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("chart.fxml")));
        Scene scene = new Scene(chartContainer, 800, 640);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ant Optimization algorithm report");
        stage.setScene(scene);
        stage.show();
    }
}
