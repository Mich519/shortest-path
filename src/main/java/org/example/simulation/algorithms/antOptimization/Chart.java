package org.example.simulation.algorithms.antOptimization;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.example.App;
import org.example.graph.Edge;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// todo: previous data is not deleted
public class Chart {
    private final int numOfAnts;
    private final int numOfIterations;
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
            series.getData().add(new XYChart.Data<>(i, (double) (antsThatReachedGoalPerIteration.get(i) * 100 / numOfAnts)));
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

    public Chart(int numOfAnts, int numOfIterations, List<Integer> antsThatReachedGoalPerIteration,
                 List<Set<Edge>> allPaths) throws IOException {

        this.numOfAnts = numOfAnts;
        this.numOfIterations = numOfIterations;
        this.antsThatReachedGoalPerIteration = antsThatReachedGoalPerIteration;
        this.allPaths = allPaths;

        LineChart<Number, Number> lineChart1 = successfulPathChart();
        LineChart<Number, Number> lineChart2 = shortestPathsChart();

        FlowPane chartContainer = new FlowPane(lineChart1, lineChart2);
        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("chart.fxml")));
        Scene scene = new Scene(chartContainer, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("My New Stage Title");
        stage.setScene(scene);
        stage.show();

    }
}
