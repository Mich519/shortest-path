package org.example.graph.serializer;

import org.example.controller.PrimaryController;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Vertex;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Serializer implements Serializable {

    public GraphSerialized serialize(Graph graph) {
        GraphSerialized graphSerialized = new GraphSerialized();
        Map<Vertex, VertexSerialized> map = new HashMap<>();
        for (Vertex v : graph.getVertices()) {
            // wrap vertex
            VertexSerialized vw = new VertexSerialized(v);
            graphSerialized.getWrappedVertices().add(vw);
            map.put(v, vw);
        }

        for (Vertex v: graph.getVertices()) {
            for (Edge e : v.getAdjEdges()) {
                // wrap edge
                VertexSerialized source = map.get(e.getSource());
                VertexSerialized destination = map.get(e.getDestination());
                EdgeSerialized ew = new EdgeSerialized(source, destination);
                source.getAdjEdges().add(ew);
            }
        }

        return graphSerialized;
    }

    public Graph deserialize(GraphSerialized graphSerialized, PrimaryController controller) {
        Graph graph = new Graph();
        Map<VertexSerialized, Vertex> map = new HashMap<>();
        for (VertexSerialized vw : graphSerialized.getWrappedVertices()) {
            Vertex v = vw.unwrap(controller);
            v.setController(controller);
            map.put(vw, v);
            graph.addVertex(v);
        }
        for (VertexSerialized vw : graphSerialized.getWrappedVertices()) {
            for (EdgeSerialized ew : vw.getAdjEdges()) {
                Vertex v1 = map.get(ew.getSource());
                Vertex v2 = map.get(ew.getDestination());
                graph.addEdge(v1, v2);
            }
        }

        return graph;
    }
}
