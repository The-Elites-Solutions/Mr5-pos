package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.interfaces.RefreshList;
import src.models.DollarRate;
import src.models.Expense;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class AddExpenseController {
	private RefreshList expenseRefresher;

	public AddExpenseController() {
	}

	public void setExpenseRefresher(RefreshList expenseRefresher) {
		this.expenseRefresher = expenseRefresher;
	}

	@FXML
	TextField Nametf, Descriptiontf, Costtf;

	@FXML
	DatePicker DatePick;

	@FXML
	Label ErrorLabel;

	@FXML
	Button btnCancelAddExpense, btnAddExpense;

	@FXML
	ComboBox<String> CurrencyCb;

	@FXML
	protected void initialize() {
		CurrencyCb.getItems().addAll(DollarRate.getCurrencyList());
		CurrencyCb.setValue("USD");
		System.out.println("Initializing AddExpense...");
		ErrorLabel.setText("");
		System.out.println("AddExpense initialized!");
	}

	@FXML
	protected void setBtnAddExpense() {
		String name = Nametf.getText();
		String description = Descriptiontf.getText();
		String cost = Costtf.getText();
		LocalDate localDate = DatePick.getValue();
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String currency = CurrencyCb.getValue().toString();

		if (name.equals("") || description.equals("") || cost.equals("") || date == null || currency.equals("")) {
			System.out.println("Please fill out all fields. " + Date.from(java.time.Instant.now()));
			ErrorLabel.setText("Please fill out all fields.");
		} else {
			Expense newExpense = new Expense(name, description, Integer.parseInt(cost), date, currency);
			System.out.println("Expense added. " + Date.from(java.time.Instant.now()));
			ErrorLabel.setText("Expense added.");
			try {
				Stage homestage = (Stage) Nametf.getScene().getWindow();
				homestage.close();
				if (expenseRefresher != null) {
					expenseRefresher.refreshExpenses();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	protected void CancelBtnOnAction() {
		System.out.println("Cancel Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			Stage homestage = (Stage) ErrorLabel.getScene().getWindow();
			homestage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}