package org.example.fileIO;

import org.example.controller.PrimaryController;
import org.example.graph.Graph;
import org.example.graph.serializer.GraphSerialized;
import org.example.graph.serializer.Serializer;

import java.io.*;

public class FileInOutHandler {
    private final PrimaryController controller;
    private final Serializer serializer;

    public FileInOutHandler(PrimaryController controller) {
        this.controller = controller;
        this.serializer = new Serializer();
    }

    public void safeGraphToFile(Graph graph) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/main/resources/org/example/savedGraphs/graph");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        GraphSerialized graphSerialized = serializer.serialize(graph);
        oos.writeObject(graphSerialized);
        oos.close();
        fos.close();
    }

    public Graph loadGraphFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("src/main/resources/org/example/savedGraphs/graph");
        ObjectInputStream ois = new ObjectInputStream(fis);
        GraphSerialized graphSerialized = (GraphSerialized) ois.readObject();
        Graph graph = serializer.deserialize(graphSerialized, controller);

        ois.close();
        fis.close();
        return graph;
    }
}
