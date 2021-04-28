package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.PrimaryController;

import java.io.IOException;

/**
 * JavaFX App
 */

public class App extends Application {

    private static final String fxml = "primary";
    private static final int windowWidth = 1000;
    private static final int windowHeight = 800;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent p = fxmlLoader.load();
        scene = new Scene(p, windowWidth, windowHeight);
        stage.setScene(scene);
        //stage.setResizable(false);
        PrimaryController primaryController = fxmlLoader.getController();
        stage.setOnShown(primaryController::afterInitialize);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}