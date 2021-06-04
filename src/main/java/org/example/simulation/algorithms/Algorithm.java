package org.example.simulation.algorithms;

import org.example.controller.PrimaryController;
import org.example.simulation.report.ReportContent;

public interface Algorithm {
    void run() throws InterruptedException;
    void animate(PrimaryController controller);
    ReportContent generateReportContent() throws InterruptedException;
}
