package org.example.simulation.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.App;
import org.example.simulation.algorithms.Algorithm;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReportGenerator {
    public ReportGenerator() {

    }

    public void generateRaport(Algorithm algorithm) throws IOException {
        ReportContent reportContent = algorithm.generateReportContent();
        Set<Label> labels = reportContent.getLabels();
        Set<LineChart<Number, Number>> charts = reportContent.getCharts();

        VBox labelsContainer = new VBox();
        labelsContainer.getChildren().addAll(labels);

        FlowPane chartContainer = new FlowPane();
        chartContainer.getChildren().addAll(charts);

        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("chart.fxml")));
        Scene scene = new Scene(chartContainer, 800, 640);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Report");
        stage.setScene(scene);
        stage.show();
    }
}
