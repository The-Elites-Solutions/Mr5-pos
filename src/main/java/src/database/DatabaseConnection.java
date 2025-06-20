package src.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import src.models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DatabaseConnection {

	private final static String uri = "mongodb://localhost:27017";
	private final static MongoClientURI clientURI = new MongoClientURI(uri);
	private final static MongoClient mongoClient = new MongoClient(clientURI);

	// Accessing the database
	private static MongoDatabase database = mongoClient.getDatabase("TSFPos");

	public static MongoClientURI getclientURI() {
		return clientURI;
	}

	public static MongoClient getmongoClient() {
		return mongoClient;
	}

	public static MongoDatabase getdatabaseName() {
		return database;
	}

	public static void startConnection() {
		database = mongoClient.getDatabase("TSFPos");
	}

	public static void stopConnection() {
		mongoClient.close();
	}

	public static ArrayList<DollarRate> getDollarRates() {
		ArrayList<DollarRate> dollarRates = new ArrayList<DollarRate>();
		for (Document document : database.getCollection("currency").find()) {
			String currencyId = document.getString("currencyid");
			String currency = document.getString("currency");
			double dollarRate = Double.parseDouble(document.getString("dollarRate"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			Date lastUpdated = null;
			try {
				lastUpdated = dateFormat.parse(document.getString("lastUpdated"));
			} catch (Exception e) {
				// Handle parsing errors here
				System.err.println("Error parsing date");
				e.printStackTrace();
			}
			DollarRate dollarRate1 = new DollarRate(currencyId, currency, dollarRate, lastUpdated);
			dollarRates.add(dollarRate1);
		}
		return dollarRates;
	}

	public static ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<String>();
		for (Document document : database.getCollection("categories").find()) {
			categories.add(document.getString("category"));
		}
		return categories;
	}

	public static ArrayList<Expense> getExpenses() {
		ArrayList<Expense> expenses = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		for (Document document : database.getCollection("expenses").find()) {
			try {
				String name = document.getString("name");
				String description = document.getString("description");
				int amount = Integer.parseInt(document.getString("amount"));
				String dateString = document.getString("date");
				Date date = dateFormat.parse(dateString);
				String currency = document.getString("currency");

				Expense expense = new Expense(name, description, amount, date, currency);
				expenses.add(expense);
			} catch (Exception e) {
				// Handle parsing errors here
				e.printStackTrace();
			}
		}
		System.out.println("Expenses loaded from the Database " + Date.from(java.time.Instant.now()));
		return expenses;
	}

	public static ArrayList<String> getFonction() {
		ArrayList<String> fonction = new ArrayList<String>();
		for (Document document : database.getCollection("fonction").find()) {
			fonction.add(document.getString("fonction"));
		}
		System.out.println("Fonctions loaded from database " + Date.from(java.time.Instant.now()));
		return fonction;
	}

	public static void LoadUsers() {
		for (Document document : database.getCollection("users").find()) {
			User user = new User(document.getString("employeeid"), document.getString("username"), document.getString("password"),
					document.getString("fonction"));
		}
		System.out.println("Users loaded from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void changePassword(String username, String password) {
		Document document = new Document("username", username);
		Document newDocument = new Document("$set", new Document("password", password));
		database.getCollection("users").updateOne(document, newDocument);
		System.out.println("Password changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public static void changeFonction(String username, String fonction) {
		Document document = new Document("username", username);
		Document newDocument = new Document("$set", new Document("fonction", fonction));
		database.getCollection("users").updateOne(document, newDocument);
		System.out.println("Fonction changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public static void deleteUser(String username) {
		Document document = new Document("username", username);
		database.getCollection("users").deleteOne(document);
		System.out.println("User deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void deleteFonction(String fonctionname) {
		Document document = new Document("fonction", fonctionname);
		database.getCollection("fonction").deleteOne(document);
		System.out.println("Fonction deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void addUser(User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		String fonction = user.getFonction();
		Document document = new Document("username", username)
				.append("password", password)
				.append("fonction", fonction);
		database.getCollection("users").insertOne(document);
		System.out.println("User added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static Document getAllUsers() {
		return database.getCollection("users").find().first();
	}

	public static void addFonction(String newFonction, Boolean vieworderPanel, Boolean neworderPanel, Boolean expensesPanel, Boolean settingsPanel, Boolean reportsPanel, Boolean menuitemsPanel) {
		Document document = new Document("fonction", newFonction)
				.append("vieworderPanel", vieworderPanel)
				.append("neworderPanel", neworderPanel)
				.append("expensesPanel", expensesPanel)
				.append("settingsPanel", settingsPanel)
				.append("reportsPanel", reportsPanel)
				.append("menuitemsPanel", menuitemsPanel);
		database.getCollection("fonction").insertOne(document);
		System.out.println("Fonction added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static ArrayList<String> getUserAccessPanel(String username) {
		ArrayList<String> accessPanel = new ArrayList<String>();
		if (database.getCollection("users").find(new Document("username", username)).first() != null) {
			Document document = database.getCollection("users").find(new Document("username", username)).first();
			String fonction = document.getString("fonction");
			accessPanel = getFontionAccessPanel(fonction);
		}
		System.out.println("Access Panel loaded from the Database " + Date.from(java.time.Instant.now()));
		return accessPanel;
	}

	public static ArrayList<String> getFontionAccessPanel(String fonction) {
		ArrayList<String> accessPanel = new ArrayList<String>();
		if (database.getCollection("fonction").find(new Document("fonction", fonction)).first() != null) {
			Document document = database.getCollection("fonction").find(new Document("fonction", fonction)).first();
			if (document.getBoolean("vieworderPanel")) {
				accessPanel.add("View Order");
			}
			if (document.getBoolean("neworderPanel")) {
				accessPanel.add("New Order");
			}
			if (document.getBoolean("expensesPanel")) {
				accessPanel.add("Expenses");
			}
			if (document.getBoolean("settingsPanel")) {
				accessPanel.add("Settings");
			}
			if (document.getBoolean("reportsPanel")) {
				accessPanel.add("Reports");
			}
			if (document.getBoolean("menuitemsPanel")) {
				accessPanel.add("Menu Items");
			}
		}
		System.out.println("Access Panel loaded from the Database " + Date.from(java.time.Instant.now()));
		return accessPanel;
	}

	public static String getCurrencyId(String currency) {
		String currencyid = "";
		for (Document document : database.getCollection("currency").find(new Document("currency", currency))) {
			currencyid = document.getString("currencyid");
		}
		System.out.println("Currency ID loaded from the Database " + Date.from(java.time.Instant.now()));
		return currencyid;
	}

	public static Double getCurrencyValue(String currency) {
		Double currencyValue = 0.0;
		for (Document document : database.getCollection("currency").find(new Document("currency", currency))) {
			currencyValue = Double.parseDouble(document.getString("dollarRate"));
		}
		System.out.println("Currency Value loaded from the Database " + Date.from(java.time.Instant.now()));
		return currencyValue;
	}

	public void ChangePassword(String username, String NewPassword) {
		Document document = new Document("username", username);
		Document newDocument = new Document("$set", new Document("password", NewPassword));
		database.getCollection("users").updateOne(document, newDocument);
		System.out.println("Password changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public void DeleteUser(String username) {
		Document document = new Document("username", username);
		database.getCollection("users").deleteOne(document);
		System.out.println("User deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public void ChangeFonction(String username, String NewFonction) {
		Document document = new Document("username", username);
		Document newDocument = new Document("$set", new Document("fonction", NewFonction));
		database.getCollection("users").updateOne(document, newDocument);
		System.out.println("Fonction changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public void ChangeDollarRate(String currency, double newDollarRate, Date lastUpdated) {
		Document document = new Document("currency", currency);
		Document newDocument = new Document("$set", new Document("dollarRate", newDollarRate)
				.append("lastUpdated", lastUpdated));
		database.getCollection("currency").updateOne(document, newDocument);
		System.out.println("Dollar Rate changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public void deleteDollarRate(String currency) {
		Document document = new Document("currency", currency);
		database.getCollection("currency").deleteOne(document);
		System.out.println("Dollar Rate deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void addDollarRate(DollarRate dollarRate) {
		String currencyid = dollarRate.getCurrencyId();
		String currency = dollarRate.getCurrency();
		String dollarRateString = Double.toString(dollarRate.getDollarRate());
		String lastUpdatedString = dollarRate.getLastUpdated().toString();
		Document document = new Document("currencyid", currencyid)
				.append("currency", currency)
				.append("dollarRate", dollarRateString)
				.append("lastUpdated", lastUpdatedString);
		database.getCollection("currency").insertOne(document);
		System.out.println("Dollar Rate added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static void addExpense(Expense expense) {
		String ExpenseId = expense.getExpenseId();
		String name = expense.getName();
		String description = expense.getDescription();
		int amount = expense.getAmount();
		String date = expense.getDate();
		String currency = expense.getCurrency();

		String amountString = Integer.toString(amount);
		String dateString = date.toString();

		Document document = new Document("ExpenseId", ExpenseId)
				.append("name", name)
				.append("description", description)
				.append("amount", amountString)
				.append("date", dateString)
				.append("currency", currency);
		database.getCollection("expenses").insertOne(document);
		System.out.println("Expense added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static void deleteExpense(String name) {
		Document document = new Document("name", name);
		database.getCollection("expenses").deleteOne(document);
		System.out.println("Expense deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void loadMenuItems() {
		for (Document document : database.getCollection("menuitems").find()) {
			String MenuItemId = document.getString("MenuItemId");
			String name = document.getString("name");
			String description = document.getString("description");
			String category = document.getString("category");
			double price = 0;

			Object priceObj = document.get("price");
			if (priceObj instanceof Double) {
				price = (Double) priceObj;
			} else if (priceObj instanceof String) {
				price = Double.parseDouble((String) priceObj);
			}
			ArrayList<String> ingredients = (ArrayList<String>) document.get("ingredients");
			MenuItem menuItem = new MenuItem(MenuItemId, name, description, new Category(category), price, ingredients);
		}
		System.out.println("Menu Items loaded from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void addMenuItem(MenuItem menuItem) {
		String MenuItemId = menuItem.getMenuItemId();
		String name = menuItem.getName();
		String description = menuItem.getDescription();
		String category = menuItem.getCategory().getName();
		double price = menuItem.getPrice();
		ArrayList<String> ingredients = menuItem.getIngredients();
		Document document = new Document("MenuItemId", MenuItemId)
				.append("name", name)
				.append("description", description)
				.append("category", category)
				.append("price", price)
				.append("ingredients", ingredients);
		database.getCollection("menuitems").insertOne(document);
		System.out.println("Menu Item added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static void deleteMenuItem(String name) {
		Document document = new Document("name", name);
		database.getCollection("menuitems").deleteOne(document);
		System.out.println("Menu Item deleted from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void changeMenuItem(String name, String description, String category, double price, ArrayList<String> ingredients) {
		Document document = new Document("name", name);
		Document newDocument = new Document("$set", new Document("description", description)
				.append("category", category)
				.append("price", price)
				.append("ingredients", ingredients));
		database.getCollection("menuitems").updateOne(document, newDocument);
		System.out.println("Menu Item changed in the Database " + Date.from(java.time.Instant.now()));
	}

	public static void loadCategories() {
		for (Document document : database.getCollection("categories").find()) {
			String category = document.getString("category");
			Category newCategory = new Category(category);
		}
		System.out.println("Categories loaded from the Database " + Date.from(java.time.Instant.now()));
	}

	public static void addCategory(String category) {
		Document document = new Document("category", category);
		database.getCollection("categories").insertOne(document);
		System.out.println("Category added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static void deleteCategory(String category) {
		Document document = new Document("category", category);
		database.getCollection("categories").deleteOne(document);
		System.out.println("Category deleted from the Database " + Date.from(java.time.Instant.now()));
	}


	public static ArrayList<String> LoadIngredients(String item) {
		ArrayList<String> ingredients = new ArrayList<String>();
		for (Document document : database.getCollection("menuitems").find(new Document("name", item))) {
			ingredients = (ArrayList<String>) document.get("ingredients");
		}
		System.out.println("Ingredients loaded from the Database " + Date.from(java.time.Instant.now()));
		return ingredients;
	}

	public static void addTax(double tax) {
		String TaxDate = Date.from(java.time.Instant.now()).toString();
		Document document = new Document("tax", tax)
				.append("DateAdded", TaxDate);
		database.getCollection("Tax").insertOne(document);
		System.out.println("Tax added to the Database " + Date.from(java.time.Instant.now()));
	}

	public static double getTax() {
		double tax = 0;
		// Get the latest tax from the database
		Document document = database.getCollection("Tax").find().sort(new Document("DateAdded", -1)).first();
		if (document != null) {
			tax = document.getDouble("tax");
		}
		System.out.println("Tax loaded from the Database " + Date.from(java.time.Instant.now()));
		return tax;
	}
}