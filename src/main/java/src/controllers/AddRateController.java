package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.database.DatabaseConnection;
import src.interfaces.RefreshSettings;
import src.models.DollarRate;

import java.util.Date;

public class AddRateController {

	private RefreshSettings rateRefresher;
	@FXML
	private Button addbtn, cancelbtn;
	@FXML
	private TextField currencytf, ratetf;
	@FXML
	Label ErrorLabel;

	public void setRateRefresher(RefreshSettings rateRefresher) {
		this.rateRefresher = rateRefresher;
	}

	@FXML
	protected void addRateOnAction() {
		String currency = currencytf.getText();
		double rate = Double.parseDouble(ratetf.getText());
		if (currency.isEmpty() || ratetf.getText().isEmpty()) {
			System.out.println("Please fill in all fields " + Date.from(java.time.Instant.now()));
			ErrorLabel.setText("Please fill in all fields");
		} else {
			if (DollarRate.isCurrency(currency)) {
				DollarRate dollarRate = new DollarRate(currency, rate);
				System.out.println("New dollar rate added:" + currency + " " + rate + "/USD " + Date.from(java.time.Instant.now()));
				//add to database
				DatabaseConnection.addDollarRate(dollarRate);
				ErrorLabel.setText("New dollar rate added:" + currency + " " + rate + "/USD");
				rateRefresher.refreshSettings();
				addbtn.getScene().getWindow().hide();
			} else {
				System.out.println("Currency already exists " + Date.from(java.time.Instant.now()));
				ErrorLabel.setText("Currency already exists");
			}
		}
		currencytf.clear();
		ratetf.clear();
	}

	@FXML
	protected void cancelbtnOnAction() {
		System.out.println("Add rate cancelled " + Date.from(java.time.Instant.now()));
		currencytf.clear();
		ratetf.clear();
		cancelbtn.getScene().getWindow().hide();
	}
}
