package com.pending.controllers;

import com.pending.hurricanerelief.App;
import com.pending.model.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button btn_create_account;

    @FXML
    private Button btn_sign_in;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField password_field;

    private Application application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        application = Application.getInstance();
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        String email = email_field.getText();
        String password = password_field.getText();
        String loginResult = application.attemptLogin(email, password);
        System.out.println(loginResult);
        App.setRoot("home2");
    }

}
