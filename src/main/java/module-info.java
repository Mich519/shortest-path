module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controller;
    opens org.example.controller to javafx.fxml;
    exports org.example.graph.eventHandlers;
    opens org.example.graph.eventHandlers to javafx.fxml;
}