package com.pending.hurricanerelief;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creates and displays the default Hurricane Relief JavaFX window.
 */
public class App extends Application {

private static Scene scene;

    /**
     * Loads the welcome-screen layout and displays the application window.
     *
     * @param stage the primary JavaFX window
     * @throws IOException if the FXML layout file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("login.fxml"));

        scene = new Scene(loadFXML("login2"), 540, 960);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}