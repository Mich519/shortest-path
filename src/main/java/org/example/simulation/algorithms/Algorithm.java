package org.example.simulation.algorithms;

import org.example.controller.PrimaryController;
import org.example.simulation.report.ReportContent;

public interface Algorithm {
    void run() throws Exception;
    void animate(PrimaryController controller);
    ReportContent generateReportContent();
}
