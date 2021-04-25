package org.example.graph;

import javafx.beans.property.SimpleDoubleProperty;
import org.example.controller.PrimaryController;

import java.util.*;

// todo: improve randomized vertices distribution

public class GraphGenerator {


    private final PrimaryController controller;

    public GraphGenerator(PrimaryController controller) {
        this.controller = controller;
    }

    public void generate() throws InterruptedException {

        controller.clearAll();
        int numOfVertices = (int) controller.getVertexCount().getValue();
        double width = controller.getGraphEditor().getWidth() * 0.9;
        double height = controller.getGraphEditor().getHeight() * 0.9;

        /* generate vertices */

        Set<Vertex> vertices = controller.getGraph().getVertices();
        Random rand = new Random();
        for (int i = 0; i < numOfVertices; i++) {
            boolean canFit = false;
            double x = rand.nextDouble() * width, y = rand.nextDouble() * height;
            while (!canFit) {
                x = rand.nextDouble() * width;
                y = rand.nextDouble() * height;
                if (vertices.size() == 0)
                    canFit = true;
                for (Vertex other : vertices) {
                    if (Math.sqrt((other.getCenterX() - x) * (other.getCenterX() - x) - (other.getCenterY() - y) * (other.getCenterY() - y)) > 2 * other.getRadius()) {
                        System.out.println(other.getRadius());
                        canFit = true;
                    }
                }

            }
            Vertex v = new Vertex(controller, new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
            controller.getGraph().addVertex(v);

        }
        /* generate edges */

        // create edges so that graph is a tree
        List<Vertex> remaining = new ArrayList<>(vertices);
        Collections.shuffle(remaining);
        Iterator<Vertex> it = remaining.listIterator();
        Vertex prev, next;
        next = it.hasNext() ? it.next() : null;
        while (it.hasNext()) {
            prev = next;
            next = it.next();
            controller.getGraph().addEdge(prev, next, 0);
        }

        // add random edges
        for (Vertex v : vertices) {
            for (Vertex w : vertices) {
                double r = rand.nextDouble();
                if (r < 0.05) {
                    controller.getGraph().addEdge(v, w, 0);
                }
            }
        }
        controller.drawGraph();
    }
}

