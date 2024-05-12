package com.example.eerstejavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private VBox mainContainer;
    private HelloController mainController;

    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void loginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidLogin(username, password)) {
            // Navigate to the main application screen
            mainController.showMainScreen();
            System.out.println("Login successvol!");
        } else {
            // Display an error message or handle unsuccessful login
            System.out.println("Login mislukt!");
        }
    }

    @FXML
    private void backButtonAction() {
        try {
            // Load the FXML file for the main screen (hello-view.fxml)
            Parent mainScreen = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

            // Get the current stage from the button
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Set the scene to the main screen
            stage.setScene(new Scene(mainScreen));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            // Handle the exception (e.g., log it or show an error dialog)
            e.printStackTrace();
        }
    }

    private boolean isValidLogin(String username, String password) {
        // Implement your login validation logic here
        if (username.equals("jan") && password.equals("jan123")) {
            return true;  // Valid login
        } else if (username.equals("petra") && password.equals("petra123")) {
            return true;  // Valid login
        } else if (!usernameExists(username)) {
            System.out.println("Username not found!");
        } else {
            System.out.println("Password is wrong!");
        }

        // For simplicity, let's assume an invalid login
        return false;
    }

    private boolean usernameExists(String username) {
        // Implement a check to see if the username exists
        return username.equals("jan") || username.equals("petra");
    }
}
