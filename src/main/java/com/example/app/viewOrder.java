package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class viewOrder {
	@FXML
	private Button PrintReceipt;

	@FXML
	protected void initialize() {
		System.out.println("View Order Screen");
	}

	@FXML
	protected void btnBackOnAction() {
		System.out.println("Back Button Pressed");
		try {
			// Load the PowerOff.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("HomeScreen.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Power Off" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.setResizable(false);

			// Set the scene on the stage
			newStage.setScene(newScene);
			newStage.setTitle("Home Screen");
			newStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

			// Show the "Power Off" window
			newStage.show();

			Stage homeStage = (Stage) PrintReceipt.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}