package com.pending.hurricanerelief;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controls the default JavaFX welcome screen.
 */
public class HelloController {
    /** Displays the welcome message in the user interface. */
    @FXML
    private Label welcomeText;

    /**
     * Updates the welcome message when the Hello button is clicked.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}