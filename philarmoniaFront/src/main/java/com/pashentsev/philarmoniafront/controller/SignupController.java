package com.pashentsev.philarmoniafront.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;

public class SignupController {

    @FXML
    private TextField login;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Button signButton;

    @FXML
    void login(ActionEvent event) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("loginView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signup(ActionEvent event) {

    }

}

