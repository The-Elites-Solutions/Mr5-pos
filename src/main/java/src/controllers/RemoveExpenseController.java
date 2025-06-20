package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.interfaces.RefreshList;
import src.models.Expense;

public class RemoveExpenseController {
	private RefreshList expenseRefresher;

	public RemoveExpenseController() {
	}

	public void setExpenseRefresher(RefreshList expenseRefresher) {
		this.expenseRefresher = expenseRefresher;
	}

	@FXML
	ComboBox<String> Expensescb;
	@FXML
	Label ErrorLabel;

	@FXML
	protected void initialize() {
		System.out.println("Initializing RemoveExpense...");
		Expensescb.getItems().addAll(Expense.getAllExpensesNames());
		ErrorLabel.setText("");
		System.out.println("RemoveExpense initialized!");
	}

	@FXML
	protected void btnRemoveExpenseActionPerformed() {
		System.out.println("Remove Button Pressed");
		if (Expensescb.getValue() == null) {
			System.out.println("Please select an expense to remove.");
			ErrorLabel.setText("Please select an expense to remove.");
		} else {
			Expense.removeExpense(Expensescb.getValue());
			ErrorLabel.setText("Expense removed.");
			System.out.println("Expense removed.");
			try {
				Stage homestage = (Stage) ErrorLabel.getScene().getWindow();
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
	protected void btnCancelRemoveExpense() {
		System.out.println("Cancel Button Pressed");
		try {
			Stage homestage = (Stage) ErrorLabel.getScene().getWindow();
			homestage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
