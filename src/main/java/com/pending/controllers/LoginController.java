package com.pending.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button btn_create_account;

    @FXML
    private Button btn_sign_in;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField password_field;

    @FXML
    void login(ActionEvent event) {
        String email = email_field.getText();
        String password = password_field.getText();
        System.out.println(email + " " + password);
    }

}
