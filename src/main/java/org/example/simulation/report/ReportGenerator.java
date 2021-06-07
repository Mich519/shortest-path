package org.example.simulation.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.App;
import org.example.simulation.algorithms.Algorithm;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReportGenerator {
    private final Set<Label> labels;
    private final Set<LineChart<Number, Number>> charts;


    public ReportGenerator() {
        this.labels = new LinkedHashSet<>();
        this.charts = new LinkedHashSet<>();
    }

    public void addReportContent(ReportContent reportContent) {
        labels.addAll(reportContent.getLabels());
        charts.addAll(reportContent.getCharts());
    }

    public void generateRaport() throws IOException, InterruptedException {
        VBox labelsContainer = new VBox();
        labelsContainer.getChildren().addAll(labels);

        FlowPane chartContainer = new FlowPane();
        chartContainer.getChildren().add(labelsContainer);
        chartContainer.getChildren().addAll(charts);

        ScrollPane scrollPane = new ScrollPane(chartContainer);

        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("chart.fxml")));
        Scene scene = new Scene(scrollPane, 800, 640);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Report");
        stage.show();
    }
}
