package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Date;

public class PowerOff {

	@FXML
	Button btnLogout;
	@FXML
	Button btnCancel;
	@FXML
	Button btnExit;

	@FXML
	protected void btnLogoutAction() {
		// When the "Logout" button is pressed
		System.out.println("Logout Button Pressed " + Date.from(java.time.Instant.now()));
		try {

			// Load the LoginScreen.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("LoginScreen.fxml"));
			Parent root = fxmlLoader.load();

			// Create a new scene and stage for the login screen
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("LoginScreen");

			// Show the login screen
			newStage.show();

			// Close both the PowerOff and HomeScreen stages
			Stage powerOffStage = (Stage) btnLogout.getScene().getWindow();
			powerOffStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnCancelAction() {

		// When the "Cancel" button is pressed
		System.out.println("Cancel Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the PowerOff.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("HomeScreen.fxml"));
			Parent root = fxmlLoader.load();

			// Create a new scene and stage for the "Power Off" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);

			// Set the scene on the stage
			newStage.setScene(newScene);
			newStage.setTitle("Home Screen");

			// Show the "Power Off" window
			newStage.show();

			Stage homeStage = (Stage) btnCancel.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnExitAction() {
		// When the "Exit" button is pressed
		System.out.println("Exit Button Pressed " + Date.from(java.time.Instant.now()));
		System.exit(0);
	}
}
