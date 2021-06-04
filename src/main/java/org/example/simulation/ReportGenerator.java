package org.example.simulation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReportGenerator {
    public ReportGenerator() {

    }

    public void generateRaport(Set<LineChart<Number, Number>> charts, List<Label> labels) throws IOException {
        VBox labelsContainer = new VBox();
        for (Label l : labels)
            labelsContainer.getChildren().add(l);

        FlowPane chartContainer = new FlowPane();
        chartContainer.getChildren().addAll(charts);
        chartContainer.getChildren().add(labelsContainer);

        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("chart.fxml")));
        Scene scene = new Scene(chartContainer, 800, 640);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Report");
        stage.setScene(scene);
        stage.show();
    }
}
