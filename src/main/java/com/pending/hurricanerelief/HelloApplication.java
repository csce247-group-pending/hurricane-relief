package com.pending.hurricanerelief;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creates and displays the default Hurricane Relief JavaFX window.
 */
public class HelloApplication extends Application {

    /**
     * Loads the welcome-screen layout and displays the application window.
     *
     * @param stage the primary JavaFX window
     * @throws IOException if the FXML layout file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}