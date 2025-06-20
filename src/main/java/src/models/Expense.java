package src.models;

import src.database.DatabaseConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Expense {
	private static ArrayList<Expense> allExpenses = new ArrayList<Expense>();
	private static int totalExpenses = 0;
	private static int totalAmount = 0;
	private String ExpenseId;
	private int amount;
	private String description;
	private String name;
	private int id;
	private String date;
	private String currency;

	public Expense() {
		this("Unknown", "Unknown", 0, new Date(0), "");
	}

	public Expense(String name, String description, int amount, Date date, String currency) {
		setExpenseId();
		setName(name);
		setDescription(description);
		setAmount(amount);
		setDate(date);
		setCurrency(currency);
		allExpenses.add(this);
		totalExpenses++;
		totalAmount += amount;
		DatabaseConnection.addExpense(this);
	}

	private void setExpenseId() {
		Date date = new Date();
		this.ExpenseId = "E" + date.getTime();
	}

	private void setAmount(int amount) {
		this.amount = amount;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		// Format the date
		String formattedDate = formatter.format(date);
		this.date = formattedDate;
	}

	private void setCurrency(String currency) {
		this.currency = currency;
	}

	public static int getTotalExpenses() {
		return totalExpenses;
	}

	public static int getTotalAmount() {
		return totalAmount;
	}

	public static ArrayList<Expense> getAllExpenses() {
		return allExpenses;
	}

	public int getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getExpenseId() {
		return ExpenseId;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getCurrency() {
		return currency;
	}

	public String getDate() {
		return date;
	}


	public static ArrayList<String> getAllExpensesNames() {
		ArrayList<String> allExpensesNames = new ArrayList<String>();
		for (Expense expense : allExpenses) {
			allExpensesNames.add(expense.getName());
		}
		return allExpensesNames;
	}

	public static void removeExpense(String name) {
		for (Expense expense : allExpenses) {
			if (expense.getName().equals(name)) {
				totalAmount -= expense.getAmount();
				totalExpenses--;
				allExpenses.remove(expense);
				DatabaseConnection.deleteExpense(name);
				break;
			}
		}
	}

	public static Boolean checkExpense(String name) {
		for (Expense expense : allExpenses) {
			if (expense.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void removeAllExpenses() {
		allExpenses.clear();
		totalAmount = 0;
		totalExpenses = 0;
	}


}
