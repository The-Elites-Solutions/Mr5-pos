package com.example.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Customer {
	private String CustomerId;
	private String Firstname, Lastname;
	private String email;
	private String phone;
	private ArrayList<String> address;
	private ArrayList<String> orders;
	private Double LoyaltyPoints;

	public Customer(String Firstname, String Lastname, String phone, String email, String addressId) {
		setCustomerId();
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.email = email;
		this.phone = phone;
		this.address = new ArrayList<>();
		this.address.add(addressId);
		this.orders = new ArrayList<>();
		this.LoyaltyPoints = 0.0;
		addToDb();
	}

	public Customer(String Firstname, String Lastname, String email, String phone,
	                ArrayList<String> address) {
		this.CustomerId = CustomerId;
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = new ArrayList<>();
		this.LoyaltyPoints = 0.0;
	}

	public Customer(String CustomerId, String Firstname, String Lastname, String email, String phone,
	                ArrayList<String> address, ArrayList<String> orders) {
		this.CustomerId = CustomerId;
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = orders;
		this.LoyaltyPoints = 0.0;
	}

	public Customer(String CustomerId, String Firstname, String Lastname, String email, String phone,
	                ArrayList<String> address, ArrayList<String> orders, Double LoyaltyPoints) {
		this.CustomerId = CustomerId;
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = orders;
		this.LoyaltyPoints = LoyaltyPoints;
	}


	public void addToDb() {
		CustomerDatabase customerDatabase = new CustomerDatabase();
		customerDatabase.addCustomer();
	}

	private void setCustomerId() {
		Date date = new Date();
		this.CustomerId = "C" + date.getTime();
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public String getFirstname() {
		return Firstname;
	}

	public String getLastname() {
		return Lastname;
	}

	public String getFullName() {
		if (Lastname.equals("N/A"))
			return Firstname;
		else
			return Firstname + " " + Lastname;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public ArrayList<String> getAddress() {
		return address;
	}

	public ArrayList<String> getOrders() {
		return orders;
	}

	public Double getLoyaltyPoints() {
		return LoyaltyPoints;
	}

	public static String getCustomerName(String clientid) {
		return CustomerDatabase.getFullName(clientid);
	}

	public static String getCustomerPhone(String clientid) {
		return CustomerDatabase.getPhone(clientid);
	}


	public static ArrayList<String> getCustomerIdDb() {
		return CustomerDatabase.getCustomerId();
	}

	public static String getCustomerId(String firstname, String lastname) {
		return CustomerDatabase.getCustomerId(firstname, lastname);
	}

	public static String getCustomerId(String phonenumber) {
		return CustomerDatabase.getCustomerId(phonenumber);
	}

	public static String getFirstname(String CustomerId) {
		return CustomerDatabase.getFirstname(CustomerId);
	}

	public static String getLastname(String CustomerId) {
		return CustomerDatabase.getLastname(CustomerId);
	}

	public static String getFullName(String client) {
		return CustomerDatabase.getFullName(client);
	}

	public static String getEmail(String CustomerId) {
		return CustomerDatabase.getEmail(CustomerId);
	}

	public static String getPhone(String CustomerId) {
		return CustomerDatabase.getPhone(CustomerId);
	}

	public static ArrayList<String> getAddress(String CustomerId) {
		return CustomerDatabase.getAddress(CustomerId);
	}

	public static void updateEmail(String customerId, String email) {
		CustomerDatabase.updateEmail(customerId, email);
	}

	public static void updatePhone(String customerId, String phone) {
		CustomerDatabase.updatePhone(customerId, phone);
	}

	public static void AddAddress(String customerId, String address) {
		CustomerDatabase.AddAddress(customerId, address);
	}

	public static void AddOrder(String customerId, String order) {
		CustomerDatabase.AddOrder(customerId, order);
	}

	public static void IncreaseLoyaltyPoints(String customerId, Double points) {
		CustomerDatabase.IncreaseLoyaltyPoints(customerId, points);
	}

	public static void DecreaseLoyaltyPoints(String customerId, Double points) {
		CustomerDatabase.DecreaseLoyaltyPoints(customerId, points);
	}

	public static void DeleteCustomer(String customerId) {
		CustomerDatabase.DeleteCustomer(customerId);
	}

	public static void DeleteAddress(String customerId, String address) {
		CustomerDatabase.DeleteAddress(customerId, address);
	}

	public static void DeleteOrder(String customerId, String order) {
		CustomerDatabase.DeleteOrder(customerId, order);
	}

	public static ArrayList<String> getCustomerNames() {
		return CustomerDatabase.getCustomerNames();
	}

	public static ArrayList<String> getCustomerPhoneNumbers() {
		return CustomerDatabase.getCustomerPhoneNumbers();
	}

	public static ArrayList<Customer> getCustomerFromDb() {
		return CustomerDatabase.getCustomerFromDb();
	}

	public ComboBox<String> getAddressComboBox() {
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic; -fx-background-color: #fcfcfc;");

		// Populate ComboBox with addresses
		ObservableList<String> addressTexts = FXCollections.observableArrayList();
		for (String addressId : address) {
			String addressText = Address.getAddresstxt(addressId); // Assuming Address.getAddresstxt(addressId) returns the textual representation of an address
			addressTexts.add(addressText);
		}
		comboBox.setItems(addressTexts);

		return comboBox;
	}

	private class CustomerDatabase {
		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = mongoClient.getDatabase("TSFPos");

		public CustomerDatabase() {
			database = mongoClient.getDatabase("TSFPos");
			System.out.println("Database connection successful");
		}

		public void addCustomer() {
			Document document = new Document("CustomerId", CustomerId)
					.append("Firstname", Firstname)
					.append("Lastname", Lastname)
					.append("email", email)
					.append("phone", phone)
					.append("address", address)
					.append("orders", orders)
					.append("LoyaltyPoints", LoyaltyPoints);
			database.getCollection("Customers").insertOne(document);
		}

		public static ArrayList<String> getCustomerId() {
			ArrayList<String> customerId = new ArrayList<>();
			for (Document document : database.getCollection("Customers").find()) {
				customerId.add(document.getString("CustomerId"));
			}
			return customerId;
		}

		public static String getCustomerId(String firstname, String lastname) {
			Document document = database.getCollection("Customers").find(new Document("Firstname", firstname).append("Lastname", lastname)).first();
			return document.getString("CustomerId");
		}

		public static String getCustomerId(String phonenumber) {
			Document document = database.getCollection("Customers").find(new Document("phone", phonenumber)).first();
			return document.getString("CustomerId");
		}

		public static String getFirstname(String customerId) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
			return document.getString("Firstname");
		}

		public static String getLastname(String customerId) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
			return document.getString("Lastname");
		}

		public static String getFullName(String client) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", client)).first();
			if (document.getString("Lastname").equals("N/A"))
				return document.getString("Firstname");
			else
				return document.getString("Firstname") + " " + document.getString("Lastname");
		}

		public static String getEmail(String customerId) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
			return document.getString("email");
		}

		public static String getPhone(String customerId) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
			return document.getString("phone");
		}

		public static ArrayList<String> getAddress(String customerId) {
			Document document = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
			return (ArrayList<String>) document.get("address");
		}

		public static ArrayList<String> getCustomerNames() {
			ArrayList<String> customerNames = new ArrayList<>();
			for (Document document : database.getCollection("Customers").find()) {
				customerNames.add(document.getString("Firstname") + " " + document.getString("Lastname"));
			}
			return customerNames;
		}

		public static ArrayList<String> getCustomerPhoneNumbers() {
			ArrayList<String> customerPhoneNumbers = new ArrayList<>();
			for (Document document : database.getCollection("Customers").find()) {
				customerPhoneNumbers.add(document.getString("phone"));
			}
			return customerPhoneNumbers;
		}

		public static void updateEmail(String customerId, String email) {
			database.getCollection("Customers").updateOne(new Document("email", email), new Document("$set", new Document("email", email)));
		}

		public static void updatePhone(String customerId, String phone) {
			database.getCollection("Customers").updateOne(new Document("phone", phone), new Document("$set", new Document("phone", phone)));
		}

		public static void AddAddress(String customerId, String address) {
			database.getCollection("Customers").updateOne(new Document("address", address), new Document("$push", new Document("address", address)));
		}

		public static void AddOrder(String customerId, String order) {
			database.getCollection("Customers").updateOne(new Document("orders", order), new Document("$push", new Document("orders", order)));
		}

		public static void IncreaseLoyaltyPoints(String customerId, Double points) {
			database.getCollection("Customers").updateOne(new Document("LoyaltyPoints", points), new Document("$inc", new Document("LoyaltyPoints", points)));
		}

		public static void DecreaseLoyaltyPoints(String customerId, Double points) {
			database.getCollection("Customers").updateOne(new Document("LoyaltyPoints", points), new Document("$inc", new Document("LoyaltyPoints", -points)));
		}

		public static void DeleteCustomer(String customerId) {
			database.getCollection("Customers").deleteOne(new Document("CustomerId", customerId));
		}

		public static void DeleteAddress(String customerId, String address) {
			database.getCollection("Customers").updateOne(new Document("address", address), new Document("$pull", new Document("address", address)));
		}

		public static void DeleteOrder(String customerId, String order) {
			database.getCollection("Customers").updateOne(new Document("orders", order), new Document("$pull", new Document("orders", order)));
		}

		public static ArrayList<Customer> getCustomerFromDb() {
			ArrayList<Customer> customers = new ArrayList<>();
			for (Document document : database.getCollection("Customers").find()) {
				customers.add(new Customer(document.getString("CustomerId"), document.getString("Firstname"),
						document.getString("Lastname"), document.getString("email"),
						document.getString("phone"), (ArrayList<String>) document.get("address"),
						(ArrayList<String>) document.get("orders"), document.getDouble("LoyaltyPoints")));
			}
			return customers;
		}
	}
}
