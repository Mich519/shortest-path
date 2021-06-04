package org.example.simulation.algorithms;

import org.example.controller.PrimaryController;

public interface Algorithm {
    void run() throws Exception;
    void animate(PrimaryController controller);
    void generateReport();
}
