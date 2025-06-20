package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.database.DatabaseConnection;

import java.util.ArrayList;
import java.util.Date;

public class HomeScreenController {
	private static String userLogedIn;
	private Stage primaryStage;

	private static Double widthMultiplier = 1.0;
	private static Double heightMultiplier = 1.0;
	@FXML
	AnchorPane anchorPane;

	@FXML
	GridPane gridPane;

	@FXML
	Pane PowerOffPane;

	@FXML
	protected void initialize() {
		System.out.println("Hello " + userLogedIn + "!!!" + Date.from(java.time.Instant.now()));
		//clear the gridpane
		gridPane.getChildren().clear();
		//add the power off icon
		gridPane.add(PowerOffPane, 2, 1);
		// Generate buttons based on the user logged in
		GenerateButtons();

	}

	public static String getUserLoggedIn() {
		// Get the username of the user logged in
		return userLogedIn;
	}

	public static void setUserLoggedIn(String username) {
		// Set the username of the user logged in
		System.out.println("User Logged In: " + username);
		userLogedIn = username;
	}

	@FXML
	protected void btnNewOrderAction() {
		// When the "New Order" button is pressed
		System.out.println("New Order Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the NewOrder.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("NewOrder.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "New Order" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("New Order");

			// Show the "New Order" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage loginStage = (Stage) gridPane.getScene().getWindow();
			loginStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnViewOrderAction() {
		// When the "View Order" button is pressed
		System.out.println("View Order Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the ViewOrder.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("ViewOrder.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "View Order" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("View Order");

			// Show the "View Order" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage homeStage = (Stage) gridPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnMenuItemsAction() {
		// When the "Menu Items" button is pressed
		System.out.println("Menu Items Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the MenuItems.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("MenuItems.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Menu Items" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Menu Items");

			// Show the "Menu Items" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage homeStage = (Stage) gridPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnExpensesAction() {
		// When the "Expenses" button is pressed
		System.out.println("Expenses Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the Expenses.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("Expenses.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Expenses" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Expenses");

			// Show the "Expenses" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage homeStage = (Stage) gridPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnReportsAction() {
		// When the "Reports" button is pressed
		System.out.println("Reports Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the Reports.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("Reports.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Reports" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Reports");

			// Show the "Reports" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage homeStage = (Stage) gridPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnSettingsAction() {
		// When the "Settings" button is pressed
		System.out.println("Settings Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the Settings.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("Settings.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Settings" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Settings");

			// Show the "Settings" window
			newStage.show();

			// Hide the current HomeScreen stage
			Stage homeStage = (Stage) gridPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnPowerOffAction() {
		// When the "Power Off" button is pressed
		System.out.println("Power Off Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			// Load the PowerOff.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("PowerOff.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Power Off" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Power Off");
			// Show the "Power Off" window
			newStage.show();

			Stage homeStage = (Stage) PowerOffPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void GenerateButtons() {
		ArrayList<String> AccessPanel = DatabaseConnection.getUserAccessPanel(userLogedIn);
		int i = 0;
		for (String panel : AccessPanel) {
			Button btn = new Button(panel);
			btn.setPrefSize(140 * widthMultiplier, 60 * heightMultiplier);
			// Directly call the appropriate method using lambda expression
			btn.setOnAction(e -> {
				switch (panel) {
					case "New Order":
						btnNewOrderAction();
						break;
					case "View Order":
						btnViewOrderAction();
						break;
					case "Menu Items":
						btnMenuItemsAction();
						break;
					case "Expenses":
						btnExpensesAction();
						break;
					case "Reports":
						btnReportsAction();
						break;
					case "Settings":
						btnSettingsAction();
						break;
					case "Power Off":
						btnPowerOffAction();
						break;
					default:
						// Handle unrecognized panel
						break;
				}
			});
			btn.setStyle("-fx-background-color: #252525; -fx-border-color: #000000; -fx-border-width: 1px;" +
					" -fx-border-radius: 5px; -fx-font-size: 20px; -fx-font-family: 'Times New Roman'; " +
					"-fx-text-fill: #fcfcfc;-fx-font-weight:bold; ");
			int x = 0;
			if (AccessPanel.size() < 5)
				x = 1;
			if (i % 2 == 1)
				gridPane.add(btn, 3, (i / 2) + x);
			else
				gridPane.add(btn, (i % 2) + 1, (i / 2) + x);
			i++;
		}
	}
}