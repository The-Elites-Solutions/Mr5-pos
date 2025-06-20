package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.main.POSApplication;
import src.models.User;

import java.util.Date;


public class LoginController {
	// FXML annotations to inject UI elements
	@FXML
	private Pane rootPane; // Make sure you have a reference to the root pane or scene

	@FXML
	private Button LoginButton;

	@FXML
	private TextField UsernameField;

	@FXML
	private PasswordField PasswordField;

	@FXML
	private Label ErrLabel;

	@FXML
	public void initialize() {
		setupKeyEvents();
		// Set the focus on the username field when the window is loaded
		UsernameField.requestFocus();
	}

	private void setupKeyEvents() {
		UsernameField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				PasswordField.requestFocus();
				event.consume();
			}
		});

		PasswordField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				LoginButtonAction();
				event.consume();
			}
		});
	}


	@FXML
	protected void LoginButtonAction() {
		// When the "Login" button is pressed
		System.out.println("Login Button Pressed " + Date.from(new Date().toInstant()));

		// Get the entered username and password from the input fields
		String username = UsernameField.getText().toLowerCase();
		String password = PasswordField.getText().toLowerCase();

		if (username.toLowerCase().equals("uniscript") && password.equals("P@ssword81898056")) {
			System.out.println("Welcome Admin! " + Date.from(new Date().toInstant()));
			ErrLabel.setText("Welcome Admin!");

			try {
				// Load the HomeScreen.fxml file
				FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("HomeScreen.fxml"));
				Parent root = fxmlLoader.load();

				// Create a new scene and stage for the "HomeScreen"
				Scene newScene = new Scene(root);
				Stage newStage = new Stage();

				newStage.initStyle(StageStyle.UNDECORATED);
				newStage.setResizable(false);
				newStage.setScene(newScene);
				newStage.setTitle("HomeScreen");

				// Show the "HomeScreen" window
				newStage.show();

				// Hide the current login stage
				// Get a reference to the login stage
				Stage loginStage = (Stage) UsernameField.getScene().getWindow();
				// Close the login stage
				loginStage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ErrLabel.setText("");

		} else {
			// Check if the entered credentials match the predefined ones
			if (User.isAvailable(username) && password.equals(User.getPasswordbyuser(username))) {
				HomeScreenController.setUserLoggedIn(username);
				System.out.println("Login Successful " + Date.from(new Date().toInstant()));
				ErrLabel.setText("Login Successful!");
				UsernameField.setStyle("-fx-border-color:#252525;");
				PasswordField.setStyle("-fx-border-color:#252525;");
				try {
					// Load the HomeScreen.fxml file
					FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("HomeScreen.fxml"));
					Parent root = fxmlLoader.load();

					// Create a new scene and stage for the "HomeScreen"
					Scene newScene = new Scene(root);
					Stage newStage = new Stage();

					newStage.initStyle(StageStyle.UNDECORATED);
					newStage.setResizable(false);
					newStage.setScene(newScene);
					newStage.setTitle("HomeScreen");

					// Show the "HomeScreen" window
					newStage.show();

					// Hide the current login stage
					// Get a reference to the login stage
					Stage loginStage = (Stage) UsernameField.getScene().getWindow();
					// Close the login stage
					loginStage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ErrLabel.setText(""); // Clear any error message
				UsernameField.clear(); // Clear the username field
				PasswordField.clear(); // Clear the password field

			} else {
				System.out.println("Login Failed " + Date.from(new Date().toInstant()));
				if (username.isEmpty() || password.isEmpty()) {
					if (username.isEmpty()) {
						ErrLabel.setText("Blank Username!");
						PasswordField.clear(); // Clear the password field
					} else {
						if (password.isEmpty()) {
							ErrLabel.setText("Blank Password!");
							UsernameField.clear(); // Clear the username field
						}
					}
					if (password.isEmpty()) {
						ErrLabel.setText("Blank Password!");
						UsernameField.clear(); // Clear the username field
					}// Check and display appropriate error messages based on the input
				} else {
					if (!User.isAvailable(username)) {
						UsernameField.requestFocus();
						UsernameField.clear();
						PasswordField.clear();
						UsernameField.setStyle("-fx-border-color: red;");
						ErrLabel.setText("Invalid Username!");
					} else if (!password.equals(User.getPasswordbyuser(username).toLowerCase())) {
						ErrLabel.setText("Password doesn't match!");
						PasswordField.requestFocus();
						PasswordField.clear();
						PasswordField.setStyle("-fx-border-color: red;");
					}
				}
				if (username.isEmpty() && password.isEmpty()) {
					ErrLabel.setText("Blank Username and Password!");
				}
			}
		}
	}
}