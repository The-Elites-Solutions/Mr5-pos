package com.example.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class OrderItem {

	private String Name, orderItemId, MenuItemId, OrderId;

	private ArrayList<String> removedingredients;

	private Double price, TotalPrice;

	private int Quantity;

	public OrderItem(String Name, String MenuItemId, ArrayList<String> removedingredients, String OrderId, int Quantity) {
		setOrderItemId();
		setName(Name);
		setRemovedIngredients(removedingredients);
		setMenuItemId(MenuItemId);
		setOrderId(OrderId);
		setPrice();
		setQuantity(Quantity);
		setTotalPrice();
		AddToDatabase();
	}

	public OrderItem(String orderItemId, String Name, String MenuItemId, ArrayList<String> removedingredients, String OrderId, double price ,int Quantity) {
		setOrderItemId(orderItemId);
		setName(Name);
		setRemovedIngredients(removedingredients);
		setMenuItemId(MenuItemId);
		setOrderId(OrderId);
		setPrice(price);
		setQuantity(Quantity);
		setTotalPrice();
	}

	public static void DeleteOrderItemByOrder(String orderId) {
		OrderItemDatabase.DeleteOrderItembyOrder(orderId);
	}

	private void setOrderItemId() {
		Date date = new Date();
		this.orderItemId = "OI" + date.getTime();
		System.out.println("Order Item ID: " + this.orderItemId);
	}

	private void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	private void setRemovedIngredients(ArrayList<String> strings) {
		this.removedingredients = strings;
	}

	private void setName(String Name) {
		this.Name = Name;
	}

	private void setMenuItemId(String MenuItemId) {
		this.MenuItemId = MenuItemId;
	}

	private void setOrderId(String OrderId) {
		this.OrderId = OrderId;
		System.out.println("Order ID: " + this.OrderId);
	}

	private void setPrice() {
		this.price = MenuItem.getPrice(this.MenuItemId);
	}

	private void setPrice(double price) {
		this.price = price;
	}

	public void setQuantity(int Quantity) {
		this.Quantity = Quantity;
	}

	private void setTotalPrice() {
		this.TotalPrice = this.price * this.Quantity;
	}

	private void AddToDatabase() {
		// Add to database
		OrderItemDatabase orderItemDatabase = new OrderItemDatabase();
		orderItemDatabase.AddOrderItem();
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public static String getOrderId(String orderItemId) {
		return OrderItemDatabase.getOrderId(orderItemId);
	}

	public static String getMenuItemId(String orderItemId) {
		return OrderItemDatabase.getMenuItemId(orderItemId);
	}

	public static String getName(String orderItemId) {
		return OrderItemDatabase.getName(orderItemId);
	}

	public static Double getPrice(String orderItemId) {
		return OrderItemDatabase.getPrice(orderItemId);
	}

	public static int getQuantity(String orderItemId) {
		return OrderItemDatabase.getQuantity(orderItemId);
	}

	public static Double getTotalPrice(String orderItemId) {
		return OrderItemDatabase.getTotalPrice(orderItemId);
	}

	public static void setQuantityDb(String orderItemId, int Quantity) {
		OrderItemDatabase.setQuantity(orderItemId, Quantity);
	}

	public static void setTotalPriceDb(String orderItemId) {
		Double TotalPrice = OrderItemDatabase.getPrice(orderItemId) * OrderItemDatabase.getQuantity(orderItemId);
		OrderItemDatabase.setTotalPrice(orderItemId, TotalPrice);
	}

	public static void DeleteOrderItem(String orderItemId) {
		OrderItemDatabase.DeleteOrderItem(orderItemId);
	}

	public String getItemName() {
		return Name;
	}

	public Double getPrice() {
		return this.price;
	}

	public int getQuantity() {
		return this.Quantity;
	}

	public ArrayList<String> getRemovedIngredients() {
		return this.removedingredients;
	}

	public Double getTotalPrice() {
		return this.TotalPrice;
	}

	public static OrderItem getOrderItemFromdb(String orderItem) {
		return OrderItemDatabase.getOrderItem(orderItem);
	}

	public String getMenuItemId() {
		return MenuItemId;
	}

	public String getName() {
		return Name;
	}

	private class OrderItemDatabase {
		// Database connection
		// Database connection
		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = DatabaseConnection.getdatabaseName();

		public OrderItemDatabase() {
			database = mongoClient.getDatabase("TSFPos");
			System.out.println("Database connection successful");
		}



		private void AddOrderItem() {
			Document document = new Document("name", Name)
					.append("orderItemId", orderItemId)
					.append("removedIngredients", removedingredients)
					.append("menuItemId", MenuItemId)
					.append("orderId", OrderId)
					.append("price", price)
					.append("quantity", Quantity)
					.append("totalPrice", TotalPrice);
			database.getCollection("OrderItems").insertOne(document);
		}

		private static String getOrderId(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getString("orderId");
		}

		private static String getMenuItemId(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getString("menuItemId");
		}

		private static String getName(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getString("name");
		}

		private static Double getPrice(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getDouble("price");
		}

		private static int getQuantity(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getInteger("quantity");
		}

		private static Double getTotalPrice(String orderItemId) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			return document.getDouble("totalPrice");
		}

		private static void setQuantity(String orderItemId, int Quantity) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			database.getCollection("OrderItems").updateOne(document, new Document("$set", new Document("quantity", Quantity)));
		}

		private static void setTotalPrice(String orderItemId, Double TotalPrice) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
			database.getCollection("OrderItems").updateOne(document, new Document("$set", new Document("totalPrice", TotalPrice)));
		}

		private static void DeleteOrderItem(String orderItemId) throws NullPointerException, IllegalArgumentException {
			try {
				Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItemId)).first();
				database.getCollection("OrderItems").deleteOne(document);
			} catch (NullPointerException e) {
				System.out.println("Order Item not found");
			}
			catch (IllegalArgumentException e) {
				System.out.println("Order Item not found");
			}
		}

		public static OrderItem getOrderItem(String orderItem) {
			Document document = database.getCollection("OrderItems").find(new Document("orderItemId", orderItem)).first();
			return new OrderItem(document.getString("orderItemId"), document.getString("name"), document.getString("menuItemId"),
					document.get("removedIngredients", ArrayList.class), document.getString("orderId"),
					document.getDouble("price"), document.getInteger("quantity"));
		}

		public static void DeleteOrderItembyOrder(String orderId) {
			for (Document document : database.getCollection("OrderItems").find(new Document("orderId", orderId))) {
				DeleteOrderItem(document.getString("orderItemId"));
			}
		}
	}
}
