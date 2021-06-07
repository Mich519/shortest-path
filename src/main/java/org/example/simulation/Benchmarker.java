package org.example.simulation;

import javafx.scene.control.Label;
import lombok.NoArgsConstructor;
import org.example.controller.PrimaryController;
import org.example.simulation.algorithms.Algorithm;
import org.example.simulation.report.ReportContent;

import java.util.ArrayList;
import java.util.List;

public class Benchmarker {

    private PrimaryController controller;

    public Benchmarker(PrimaryController controller) {
        this.controller = controller;
    }

    public ReportContent runWithBenchmark(Algorithm algorithm) throws Exception {
        ReportContent reportContent = new ReportContent();
        reportContent.addLabel(new Label(" "));
        reportContent.addLabel(new Label("Benchmark result [10^-7 s]:"));
        final int skip = 20; // warmup faze
        final int numOfTests = 100;
        System.out.println("===================");
        for (int j = 0; j < (int) controller.getNumOfTests().getValue(); j++) {
            List<Long> times = new ArrayList<>(numOfTests);
            for (int i = 0; i < numOfTests + skip; i++) {
                long startTime = System.nanoTime();
                algorithm.run();
                long finishTime = System.nanoTime();
                long elapsed = finishTime - startTime;
                if (i > skip) {
                    times.add(elapsed / 100);
                }
            }
            long avg = times.stream().reduce(Long::sum).get() / times.size();
            reportContent.addLabel(new Label(String.valueOf(avg)));
            System.out.println("Avg time: " + avg);
        }
        return reportContent;
    }
}
