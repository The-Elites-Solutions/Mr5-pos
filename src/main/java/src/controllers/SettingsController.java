package src.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import src.database.DatabaseConnection;
import src.interfaces.RefreshSettings;
import src.main.POSApplication;
import src.models.DollarRate;
import src.models.Role;
import src.models.User;

import java.util.Date;

/**
 * The Settings class is responsible for managing the settings view of the application.
 * It provides functionalities to manage functions, users, user roles, and to update passwords.
 */
public class SettingsController implements RefreshSettings {

	// JavaFX components declaration
	@FXML
	private Pane SettingsPane;
	@FXML
	private TextField newFonctiontf, NewUsernametf, NewPasswordtf, newpasswordtf, confirmpasswordtf, ratetf;
	@FXML
	private ComboBox<String> addFonctioncb, Userchangecb, fonctionUrcb, Userfccb, showcurrency,
			changecurrency, Userdeletecb, fonctiondeletecb;
	@FXML
	private CheckBox neworder, vieworder, menuitems, reports, settings, expenses;
	@FXML
	private Label ErrorLabel, ErrorLabel1, ErrorLabel2,
			ErrorLabel3, ErrorLabel4, ErrorLabel5, ErrorLabel6, ErrorLabel7, rateLabel;


	/**
	 * Initializes the comboboxes with data.
	 */
	@FXML
	public void initialize() {
		// clear all
		addFonctioncb.getItems().clear();
		Userchangecb.getItems().clear();
		fonctionUrcb.getItems().clear();
		Userfccb.getItems().clear();
		Userdeletecb.getItems().clear();
		fonctiondeletecb.getItems().clear();
		showcurrency.getItems().clear();
		changecurrency.getItems().clear();
		// add data
		addFonctioncb.getItems().addAll(Role.getFonctionlist());
		Userchangecb.getItems().addAll(User.getUsernamelist());
		fonctionUrcb.getItems().addAll(Role.getFonctionlist());
		Userfccb.getItems().addAll(User.getUsernamelist());
		Userdeletecb.getItems().addAll(User.getUsernamelist());
		fonctiondeletecb.getItems().addAll(Role.getFonctionlist());
		showcurrency.getItems().addAll(DollarRate.getCurrencyList());
		rateLabel.setText("0");
		changecurrency.getItems().addAll(DollarRate.getCurrencyList());
		ratetf.setPromptText("ex: 97000");
	}

	public void refreshSettings() {
		initialize();
	}

	/**
	 * Handles the action when "Back" button is pressed.
	 * It navigates back to the "Home Screen" view.
	 */
	@FXML
	protected void btnBackOnAction() {
		System.out.println("Back Button Pressed");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("HomeScreen.fxml"));
			Parent root = fxmlLoader.load();
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Home Screen");
			newStage.show();

			// Close the current window
			Stage homeStage = (Stage) SettingsPane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the action when "Add Fonction" button is pressed.
	 * It adds a new function to the system if it does not already exist.
	 */
	@FXML
	protected void btnAddFonction() {
		String newFonction = newFonctiontf.getText();
		Boolean vieworderPanel = vieworder.isSelected();
		Boolean neworderPanel = neworder.isSelected();
		Boolean expensesPanel = expenses.isSelected();
		Boolean settingsPanel = settings.isSelected();
		Boolean reportsPanel = reports.isSelected();
		Boolean menuitemsPanel = menuitems.isSelected();
		if (newFonction != null && !newFonction.isEmpty()) {
			if (Role.getFonctionlist().contains(newFonction)) {
				System.out.println("Fonction Already Added!");
				ErrorLabel.setText("Fonction Already Added!");
			} else {
				System.out.println("Fonction Added!");
				ErrorLabel.setText("Fonction Added!");
				newFonctiontf.setText("");
				Role newFonctionObj = new Role(newFonction, vieworderPanel, neworderPanel, expensesPanel,
						settingsPanel, reportsPanel, menuitemsPanel);
				//add the new function to database
				DatabaseConnection.addFonction(newFonction, vieworderPanel, neworderPanel, expensesPanel,
						settingsPanel, reportsPanel, menuitemsPanel);
				System.out.println(Role.getFonctionlist());
				addFonctioncb.getItems().clear();
				addFonctioncb.getItems().addAll(Role.getFonctionlist());
				fonctionUrcb.getItems().clear();
				fonctionUrcb.getItems().addAll(Role.getFonctionlist());
				fonctiondeletecb.getItems().clear();
				fonctiondeletecb.getItems().addAll(Role.getFonctionlist());
			}
		} else {
			System.out.println("No Fonction Added");
			ErrorLabel.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel);
	}

	/**
	 * Handles the action when "Delete Fonction" button is pressed.
	 * It deletes a function from the system if it exists.
	 */
	@FXML
	protected void btnDeleteFonction() {
		String fonctionname = fonctiondeletecb.getValue();
		if (fonctionname != null && !fonctionname.isEmpty()) {
			if (Role.getFonctionlist().contains(fonctionname)) {
				System.out.println("Fonction Deleted!");
				ErrorLabel7.setText("Fonction Deleted!");
				User.deleteUserbyFunction(fonctionname);
				Role.deleteFonction(fonctionname);
				addFonctioncb.getItems().clear();
				Userchangecb.getItems().clear();
				fonctionUrcb.getItems().clear();
				Userfccb.getItems().clear();
				Userdeletecb.getItems().clear();
				fonctiondeletecb.getItems().clear();
				addFonctioncb.getItems().addAll(Role.getFonctionlist());
				Userchangecb.getItems().addAll(User.getUsernamelist());
				fonctionUrcb.getItems().addAll(Role.getFonctionlist());
				Userfccb.getItems().addAll(User.getUsernamelist());
				Userdeletecb.getItems().addAll(User.getUsernamelist());
				fonctiondeletecb.getItems().addAll(Role.getFonctionlist());

			} else {
				System.out.println("Fonction Not Found!");
				ErrorLabel7.setText("Fonction Not Found!");
			}
		} else {
			System.out.println("No Fonction Deleted");
			ErrorLabel7.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel7);
	}

	/**
	 * Handles the action when "Add User" button is pressed.
	 * It adds a new user to the system if it does not already exist.
	 */
	@FXML
	protected void btnaddUseronAction() {
		String NewUsername = NewUsernametf.getText();
		String NewPassword = NewPasswordtf.getText();
		String NewFonction = addFonctioncb.getValue();

		if (NewUsername != null && !NewUsername.isEmpty() && NewPassword != null && !NewPassword.isEmpty() &&
				NewFonction != null && !NewFonction.isEmpty()) {
			if (User.getUserlist().contains(NewUsername)) {
				System.out.println("Username Already Added!");
				ErrorLabel1.setText("Username Already Added!");
			} else {
				System.out.println("User Added!");
				ErrorLabel1.setText("User Added!");
				NewUsernametf.setText("");
				NewPasswordtf.setText("");
				addFonctioncb.setValue("");
				User newUserObj = new User(NewUsername, NewPassword, NewFonction);
				//add the new user to database
				DatabaseConnection.addUser(newUserObj);

				User.printUserList();
				Userchangecb.getItems().clear();
				Userchangecb.getItems().addAll(User.getUsernamelist());
				Userfccb.getItems().clear();
				Userfccb.getItems().addAll(User.getUsernamelist());
			}
		} else {
			System.out.println("No User Added");
			ErrorLabel1.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel1);
	}

	/**
	 * Handles the action when "Delete User" button is pressed.
	 * It deletes a user from the system if it exists.
	 */

	@FXML
	protected void btnDeleteUseronAction() {
		String username = Userdeletecb.getValue();
		if (username != null && !username.isEmpty()) {
			if (User.getUsernamelist().contains(username)) {
				System.out.println("User Deleted!");
				ErrorLabel6.setText("User Deleted!");
				User.deleteUser(username);
				Userchangecb.getItems().clear();
				Userchangecb.getItems().addAll(User.getUsernamelist());
				Userfccb.getItems().clear();
				Userfccb.getItems().addAll(User.getUsernamelist());
				Userdeletecb.getItems().clear();
				Userdeletecb.getItems().addAll(User.getUsernamelist());
			} else {
				System.out.println("User Not Found!");
				ErrorLabel6.setText("User Not Found!");
			}
		} else {
			System.out.println("No User Deleted");
			ErrorLabel6.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel6);
	}

	/**
	 * Handles the action when "Change Fonction" button is pressed.
	 * It updates the user's function in the system.
	 */
	@FXML
	protected void btnChangefonction() {
		String username = Userfccb.getValue();
		String newfonction = fonctionUrcb.getValue();
		if (username != null && !username.isEmpty() && newfonction != null && !newfonction.isEmpty()) {
			if (User.getFonctionbyuser(username).equals(newfonction)) {
				System.out.println("Fonction Already Used!");
				ErrorLabel2.setText("Fonction Already Used!");
			} else {
				System.out.println("Fonction Changed!");
				ErrorLabel2.setText("Fonction Changed!");
				User.ChangeFonction(username, newfonction);
				fonctionUrcb.getItems().addAll(Role.getFonctionlist());
			}
		} else {
			System.out.println("No Fonction Changed");
			ErrorLabel2.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel2);
	}

	/**
	 * Handles the action when "Change Password" button is pressed.
	 * It updates the user's password in the system after validation.
	 */
	@FXML
	protected void btnChangePasswordonAction() {
		String username = Userchangecb.getValue();
		String newpassword = newpasswordtf.getText();
		String confirmpassword = confirmpasswordtf.getText();
		if (username != null && !username.isEmpty() && newpassword != null && !newpassword.isEmpty() &&
				confirmpassword != null && !confirmpassword.isEmpty()) {
			if (newpassword.equals(confirmpassword)) {
				if (User.getPasswordbyuser(username).equals(newpassword)) {
					System.out.println("Password Already Used!");
					ErrorLabel3.setText("Password Already Used!");
				} else {
					User.ChangePassword(username, newpassword);
					Userchangecb.getItems().clear();
					Userchangecb.getItems().addAll(User.getUsernamelist());
					System.out.println("Password Changed! " + Date.from(new Date().toInstant()));
					ErrorLabel3.setText("Password Changed!");
					newpasswordtf.setText("");
					confirmpasswordtf.setText("");
				}
			} else {
				System.out.println("Password Not Match!");
				ErrorLabel3.setText("Password Not Match!");
			}
		} else {
			System.out.println("No Password Changed");
			ErrorLabel3.setText("Empty Field!");
		}
		clearErrorLabelAfterDelay(ErrorLabel3);
	}

	/**
	 * Handles the change of user in the "User for change fonction" combobox.
	 * It updates the displayed function to match the selected user's current function.
	 */
	@FXML
	protected void onUserfccbChanged() {
		String selectedUser = Userfccb.getValue();
		if (selectedUser != null) {
			String currentFonction = User.getFonctionbyuser(selectedUser);
			fonctionUrcb.setValue(currentFonction);
		}
	}

	/**
	 * Clears the error label after a certain delay.
	 *
	 * @param errorLabel The error label to be cleared.
	 */
	private void clearErrorLabelAfterDelay(Label errorLabel) {
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> errorLabel.setText(""));
		pause.play();
	}

	/**
	 * Handles the action when "Save Rate" button is pressed.
	 * It updates the exchange rate in the system.
	 */
	@FXML
	protected void btnChangeRateOnAction() {
		String currency = changecurrency.getValue();
		String rate = ratetf.getText();
		if (currency != null && !currency.isEmpty()) {
			if (rate != null && !rate.isEmpty()) {
				//Change the rate of the selected currency
				DollarRate.ChangeDollarRate(currency, Double.parseDouble(rate));
				System.out.println(currency + " rate Changed!");
				ErrorLabel5.setText(currency + " rate Changed!");
				//set ratetf to green border
				ratetf.setStyle("-fx-border-color: green;");
				//remove border color after 3 seconds
				PauseTransition pause = new PauseTransition(Duration.seconds(3));
				pause.setOnFinished(event -> ratetf.setStyle("-fx-border-color: black;"));
				pause.play();
				//remove ErrorLabel5 after 3 seconds
				clearErrorLabelAfterDelay(ErrorLabel5);
				//refresh the rateLabel if the currency selected is the same as the one just changed
				if (currency.equals(showcurrency.getValue())) {
					rateLabel.setText(DollarRate.getDollarRate(showcurrency.getValue()) + "");
				}
			} else {
				System.out.println("Rate Empty!");
				ErrorLabel4.setText("type a rate!");
			}
		} else {
			System.out.println("Currency Empty!");
			ErrorLabel4.setText("Choose a currency!");
		}
		clearErrorLabelAfterDelay(ErrorLabel4);

	}

	//set the ratetf prompt text to the selected currency rate
	@FXML
	protected void setRatePromptText() {
		String currency = changecurrency.getValue();
		if (currency != null && !currency.isEmpty()) {
			ratetf.setPromptText(DollarRate.getDollarRate(currency) + "");
		}
	}

	@FXML
	protected void ViewRateLabel() {
		String currency = showcurrency.getValue();
		if (currency != null && !currency.isEmpty()) {
			rateLabel.setText(DollarRate.getDollarRate(currency) + "");
		}
	}

	@FXML
	protected void AddRateonAction() {
		//load the AddRate.fxml file
		System.out.println("Add Rate Button Pressed");
		try {
			// Load the Settings.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(AddRateController.class.getResource("Addrate.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Settings" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Add Rate");

			// Show the "Settings" window
			newStage.show();

			AddRateController addExpenseController = fxmlLoader.getController();
			addExpenseController.setRateRefresher(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnRemoveRateonAction() {
		//load the RemoveRate.fxml file
		System.out.println("Remove Rate Button Pressed");
		try {
			// Load the Settings.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(AddRateController.class.getResource("Removerate.fxml"));
			Parent root = fxmlLoader.load();

			// Create a new scene and stage for the "Settings" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Remove Rate");

			// Show the "Settings" window
			newStage.show();

			RemoveRateController RemoveRateController = fxmlLoader.getController();
			RemoveRateController.setRateRefresher(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}