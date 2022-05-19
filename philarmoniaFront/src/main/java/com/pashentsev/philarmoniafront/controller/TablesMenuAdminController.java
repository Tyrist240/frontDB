package com.pashentsev.philarmoniafront.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;

public class TablesMenuAdminController {

    @FXML
    private Button ImpresarioButton;

    @FXML
    private Button artistsButton;

    @FXML
    private Button backButton;

    @FXML
    private Button buildingsButton;

    @FXML
    private Button eventsButton;

    @FXML
    private Button organizersButton;

    @FXML
    void back(ActionEvent event) {

    }

    @FXML
    void showArtistsTables(ActionEvent event) {
        try {
            Stage stage = (Stage) artistsButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminArtistsView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Artists");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showBuildingsTables(ActionEvent event) {
        try {
            Stage stage = (Stage) artistsButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminBuildingsView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Buildings");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showEventsTables(ActionEvent event) {
        try {
            Stage stage = (Stage) artistsButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminEventsView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Events");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showImpresarioTables(ActionEvent event) {
        try {
            Stage stage = (Stage) artistsButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminImpresarioView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Impresario");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showOrganizersTables(ActionEvent event) {
        try {
            Stage stage = (Stage) artistsButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminOrganizersView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Organizers");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

