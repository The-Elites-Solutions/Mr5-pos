package com.example.app;

import java.util.ArrayList;
import java.util.Date;

public class DollarRate {

	private static ArrayList<DollarRate> dollarRates = new ArrayList<DollarRate>();
	private String currencyId;
	private String currency;
	private double dollarRate;
	private Date lastUpdated;

	public DollarRate(String currency, double dollarRate) {
		setCurrencyId();
		setCurrency(currency);
		setDollarRate(dollarRate);
		setLastUpdated();
		addDollarRate(this);
	}

	public DollarRate(String currencyid, String currency, double dollarRate, Date lastUpdated) {
		setCurrencyId(currencyid);
		setCurrency(currency);
		setDollarRate(dollarRate);
		setLastUpdated(lastUpdated);
		addDollarRate(this);
	}

	private void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	private void setCurrency(String currency) {
		this.currency = currency;
	}

	private void setCurrencyId() {
		Date date = new Date();
		this.currencyId = "D" + date.getTime();
	}

	private void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public static void clearDollarRates() {
		dollarRates.clear();
		System.out.println("Dollar rates cleared " + Date.from(java.time.Instant.now()));
	}

	public static void loadDollarRates() {
		dollarRates = DatabaseConnection.getDollarRates();
		System.out.println("Dollar rates loaded " + Date.from(java.time.Instant.now()));
	}

	public static String getCurrencies() {
		String currencies = "";
		for (DollarRate dollarRate1 : dollarRates) {
			currencies += dollarRate1.getCurrency() + " ";
		}
		return currencies;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public String getCurrency() {
		return currency;
	}

	public double getDollarRate() {
		return dollarRate;
	}

	public void setDollarRate(double dollarRate) {
		this.dollarRate = dollarRate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	//set the date to the current date
	public void setLastUpdated() {
		lastUpdated = new Date();
	}

	public static void addDollarRate(DollarRate dollarRate) {
		dollarRates.add(dollarRate);
	}

	//remove specified currency
	public static void removeDollarRate(String currency) {
		for (DollarRate dollarRate1 : dollarRates) {
			if (dollarRate1.getCurrency().equals(currency)) {
				dollarRates.remove(dollarRate1);
				System.out.println("Dollar rate of " + currency + " deleted" + " on " + dollarRate1.getLastUpdated());
				//update the database
				DatabaseConnection database = new DatabaseConnection();
				database.deleteDollarRate(currency);
			}
		}
	}

	public static ArrayList<DollarRate> getDollarRates() {
		return dollarRates;
	}

	//set rate of specified currency
	public static void ChangeDollarRate(String currency, double dollarRate) {
		for (DollarRate dollarRate1 : dollarRates) {
			if (dollarRate1.getCurrency().equals(currency)) {
				dollarRate1.setDollarRate(dollarRate);
				dollarRate1.setLastUpdated();
				System.out.println("Dollar rate of " + currency + " changed to " + dollarRate + " on " + dollarRate1.getLastUpdated());
				//update the database
				DatabaseConnection database = new DatabaseConnection();
				database.ChangeDollarRate(currency, dollarRate, dollarRate1.getLastUpdated());
			}
		}
	}

	//get rate of specified currency
	public static double getDollarRate(String currency) {
		for (DollarRate dollarRate1 : dollarRates) {
			if (dollarRate1.getCurrency().equals(currency)) {
				return dollarRate1.getDollarRate();
			}
		}
		return 0;
	}

	public static ArrayList<String> getCurrencyList() {
		ArrayList<String> currencyList = new ArrayList<String>();
		for (DollarRate dollarRate1 : dollarRates) {
			currencyList.add(dollarRate1.getCurrency());
		}
		return currencyList;
	}

	//add new currency
	private void addDbDollarRate() {
		DatabaseConnection database = new DatabaseConnection();
		database.addDollarRate(this);
	}

	public static Boolean isCurrency(String currency) {
		for (DollarRate dollarRate1 : dollarRates) {
			if (dollarRate1.getCurrency().equals(currency)) {
				return false;
			}
		}
		return true;
	}

}

