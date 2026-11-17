package com.pending.controllers;

import com.pending.model.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the default JavaFX welcome screen.
 */
public class HelloController implements Initializable {
    /** Displays the welcome message in the user interface. */
    @FXML
    private Label welcomeText;

    private Application application;

    /**
     * Updates the welcome message when the Hello button is clicked.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Hello, " + application.getUserFullName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        application = Application.getInstance();
    }
}