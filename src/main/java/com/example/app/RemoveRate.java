package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class RemoveRate {

	private RefreshSettings rateRefresher;
	@FXML
	private Button cancelbtn;
	@FXML
	private ComboBox<String> currencycb;
	@FXML
	Label ErrorLabel;

	public void setRateRefresher(RefreshSettings rateRefresher) {
		this.rateRefresher = rateRefresher;
	}

	@FXML
	protected void initialize() {
		currencycb.getItems().clear();
		currencycb.getItems().addAll(DollarRate.getCurrencyList());
	}

	@FXML
	protected void DeleteRateOnAction() {
		String Deletecurrency = currencycb.getValue();
		if (Deletecurrency == null) {
			ErrorLabel.setText("Please select a currency to delete");
		} else {
			DollarRate.removeDollarRate(Deletecurrency);
			rateRefresher.refreshSettings();
			ErrorLabel.setText("Dollar rate of " + Deletecurrency + " deleted");
			System.out.println("Dollar rate of " + Deletecurrency + " deleted");
			ErrorLabel.setText("");
			cancelbtn.getScene().getWindow().hide();
		}

	}

	@FXML
	protected void cancelbtnOnAction() {
		ErrorLabel.setText("");
		cancelbtn.getScene().getWindow().hide();
	}
}
