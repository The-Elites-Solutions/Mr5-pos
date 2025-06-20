package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.interfaces.RefreshList;
import src.main.POSApplication;
import src.models.Expense;

import java.util.Date;

public class ExpenseTableController implements RefreshList {

	@FXML
	TableView ExpensesTv;
	@FXML
	TableColumn id, nameclmn, descriptionclmn, costclmn, dateclmn, currencyclmn;
	@FXML
	Button btnRemoveExpense, btnAddExpense;


	public void initialize() {
		// Initialize the columns and set up data binding
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameclmn.setCellValueFactory(new PropertyValueFactory<>("name"));
		descriptionclmn.setCellValueFactory(new PropertyValueFactory<>("description"));
		costclmn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		dateclmn.setCellValueFactory(new PropertyValueFactory<>("date"));
		currencyclmn.setCellValueFactory(new PropertyValueFactory<>("currency"));

		// Set up the TableView
		ExpensesTv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<Expense> expenses = FXCollections.observableArrayList(Expense.getAllExpenses());
		ExpensesTv.setItems(expenses);

	}

	@FXML
	public void addExpense() {
		System.out.println("Add Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(AddExpenseController.class.getResource("AddExpense.fxml"));
			Parent root = fxmlLoader.load();
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Add");
			newStage.show();

			AddExpenseController addExpenseController = fxmlLoader.getController();
			addExpenseController.setExpenseRefresher(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void btnRemoveExpenseActionPerformed() {
		System.out.println("Remove Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("RemoveExpense.fxml"));
			Parent root = fxmlLoader.load();
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Remove");
			newStage.show();

			RemoveExpenseController addExpenseController = fxmlLoader.getController();
			addExpenseController.setExpenseRefresher(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void setBtnrefresh() {
		initialize();
	}

	public void refreshExpenses() {
		setBtnrefresh();
	}

	@FXML
	protected void btnReturnHome() {
		System.out.println("Return Button Pressed " + Date.from(java.time.Instant.now()));
		try{
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
			Stage homestage = (Stage) btnAddExpense.getScene().getWindow();
			homestage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


