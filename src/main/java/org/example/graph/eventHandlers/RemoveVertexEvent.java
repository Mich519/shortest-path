package org.example.graph.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.graph.Vertex;

public class RemoveVertexEvent implements EventHandler<MouseEvent> {

    private final PrimaryController controller;
    private final Vertex target;

    public RemoveVertexEvent(PrimaryController controller, Vertex target) {
        this.controller = controller;
        this.target = target;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        if (controller.getRemoveVertex().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            /* remove vertex */

            controller.getGraph().removeVertex(target);
            if (target == controller.getGraph().getStartVertex())
                controller.getGraph().setStartVertex(null);
            else if (target == controller.getGraph().getEndVertex())
                controller.getGraph().setEndVertex(null);
            controller.drawGraph();

        } else if (controller.getStartNode().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            /* set vertex as start node */

            if (target != controller.getGraph().getEndVertex())
                controller.getGraph().setStartVertex(target);
            controller.drawGraph();

        } else if (controller.getEndNode().isSelected() && mouseEvent.getButton() == MouseButton.SECONDARY) {
            /* set vertex as end node */

            if (target != controller.getGraph().getStartVertex())
                controller.getGraph().setEndVertex(target);
            controller.drawGraph();
        }
    }
}
