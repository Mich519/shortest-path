package org.example.controller.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.example.controller.PrimaryController;
import org.example.view.vertex.VertexView;

public class RemoveVertexEvent implements EventHandler<MouseEvent> {

    private PrimaryController controller;
    private VertexView targetVertex;

    public RemoveVertexEvent(PrimaryController controller, VertexView targetVertex) {
        this.controller = controller;
        this.targetVertex = targetVertex;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (controller.getRemoveVertex().isSelected()) {
            controller.removeVertexFromPane(targetVertex);
        }
    }
}
