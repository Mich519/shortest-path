package org.example.simulation.report;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class ReportContent {
    private final Set<LineChart<Number, Number>> charts;
    private final Set<Label> labels;

    public ReportContent() {
        this.charts = new LinkedHashSet<>();
        this.labels = new LinkedHashSet<>();
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public void addChart(XYChart.Series<Number, Number> series , String title, String xlabel, String ylabel) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xlabel);
        yAxis.setLabel(ylabel);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getData().add(series);
        chart.setTitle(title);

        charts.add(chart);
    }
}
