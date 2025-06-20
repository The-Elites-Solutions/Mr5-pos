package src.models;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import src.database.DatabaseConnection;

import java.util.ArrayList;
import java.util.Date;

public class Order {
	private String OrderId;
	private String CustomerId;
	private String EmployeeId;
	private Date DateOrdered;
	private Boolean OrderStatus;
	private ArrayList<String> OrderItems;
	private double Total;
	private String PaymentMethodId;
	private Boolean isPaid;
	private Date DateCompleted;
	private double Discount;
	private double Tax;
	private String OrderType;
	private double LoyaltyPoints;
	private String SpecialRequest;
	private ArrayList<String> UsedCurrency;
	private int TableNumber;
	private String deliveryAddressid;

	public Order() {
	}

	public Order(String employeeId) {
		initializeOrder(employeeId);
		this.OrderType = "Takeaway";
		addToDb();
	}

	public Order(String employeeId, int tableNumber) {
		initializeOrder(employeeId);
		this.OrderType = "DineIn";
		this.TableNumber = tableNumber;
		if (!this.isPaid()){
			Table.setOccupied(tableNumber, true);
		}
		addToDb();
	}

	private boolean isPaid() {
		return this.isPaid;
	}

	public Order(String customerId, String employeeId, int tableNumber) {
		initializeOrder(customerId, employeeId);
		this.OrderType = "DineIn";
		this.TableNumber = tableNumber;
		addToDb();
	}

	public Order(String customerId, String employeeId, String deliveryAddress) {
		initializeOrder(customerId, employeeId);
		this.OrderType = "Delivery";
		this.deliveryAddressid = deliveryAddress;
		addToDb();
	}

	public Order(String customerId, String employeeId) {
		initializeOrder(customerId, employeeId);
		this.OrderType = "Takeaway";
		addToDb();
	}

	private void initializeOrder(String employeeId) {
		setOrderId();
		setCustomerId("");
		setEmployeeId(employeeId);
		setDateOrdered(new Date());
		setOrderStatus(true);
		setOrderItems(new ArrayList<>());
		Total = 0.0;
		setPaymentMethodId("");
		setPaymentStatus(false);
		setDiscount(0.0);
		setTax();
		setLoyaltyPoints(0.0);
		setSpecialRequest("");
		UsedCurrency = new ArrayList<>();
	}

	private void initializeOrder(String customerId, String employeeId) {
		setOrderId();
		setCustomerId(customerId);
		setEmployeeId(employeeId);
		setDateOrdered(new Date());
		setOrderStatus(true);
		setOrderItems(new ArrayList<>());
		Total = 0.0;
		setPaymentMethodId("");
		setPaymentStatus(false);
		setDiscount(0.0);
		setTax();
		setLoyaltyPoints(0.0);
		setSpecialRequest("");
		UsedCurrency = new ArrayList<>();
	}

	private void addToDb() {
		OrderDatabase orderDatabase = new OrderDatabase();
		orderDatabase.AddOrder();
	}

	private void setisPaid(Boolean isPaid) {
		this.isPaid = isPaid;
		OrderDatabase.updatePaymentStatus(this.OrderId, isPaid);
		if (isPaid && this.OrderType.equals("DineIn")) {
			Table.setOccupied(this.TableNumber, false);
			setDateCompleted();
		}
	}

	public static void DeleteOrder(String orderid) {
		OrderDatabase.DeleteOrder(orderid);
	}

	public static Order getOrderFromdb(String orderid) {
		Order order = OrderDatabase.getOrderFromdb(orderid);
		return order;
	}

	private void setOrderId() {
		Date date = new Date();
		this.OrderId = "O" + date.getTime();
	}

	private void setOrderId(String OrderId) {
		this.OrderId = OrderId;
	}

	private void setCustomerId(String CustomerId) {
		this.CustomerId = CustomerId;
	}

	private void setEmployeeId(String EmployeeId) {
		this.EmployeeId = EmployeeId;
	}

	private void setDateOrdered() {
		Date date = new Date();
		this.DateOrdered = date;
	}

	private void setDateOrdered(Date DateOrdered) {
		this.DateOrdered = DateOrdered;
	}

	private void setOrderStatus(Boolean OrderStatus) {
		this.OrderStatus = OrderStatus;
	}

	private void setOrderItems(ArrayList<String> OrderItems) {
		this.OrderItems = OrderItems;
	}

	private void setTotal(double total) {
		this.Total = total;
	}

	public void setTotal() {
		double total = 0.0;
		for (String orderItem : OrderItems) {
			OrderItem item = OrderItem.getOrderItemFromdb(orderItem);
			total += item.getTotalPrice();
		}
		setTotal(total);
	}

	private void setPaymentMethodId(String PaymentMethodId) {
		this.PaymentMethodId = PaymentMethodId;
	}

	private void setPaymentStatus(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	private void setDateCompleted() {
		Date date = new Date();
		this.DateCompleted = date;
	}

	private void setDateCompleted(Date DateCompleted) {
		this.DateCompleted = DateCompleted;
	}

	private void setDiscount(double Discount) {
		this.Discount = Discount;
	}

	private void setTax() {
		Double Tax = OrderDatabase.getTax();
		this.Tax = Tax;
	}

	private void setOrderType(String OrderType) {
		this.OrderType = OrderType;
	}

	private void setLoyaltyPoints(double LoyaltyPoints) {
		this.LoyaltyPoints = LoyaltyPoints;
	}

	private void setSpecialRequest(String SpecialRequest) {
		this.SpecialRequest = SpecialRequest;
	}

	private void setUsedCurrency(String CurrencyId) {
		if (CurrencyId.isEmpty()) {
			if (UsedCurrency == null) {
				UsedCurrency = new ArrayList<>();
				UsedCurrency.add(CurrencyId);
			}
			else {
				UsedCurrency.add(CurrencyId);
			}
		} else {
			if (!UsedCurrency.contains(CurrencyId))
				UsedCurrency.add(CurrencyId);
		}
	}

	public static void setIsPaid(String orderId) {
		OrderDatabase.updatePaymentStatus(orderId, true);
		if (OrderDatabase.getOrderType(orderId).equals("DineIn")) {
			Order order = getOrderFromdb(orderId);
			Table.setOccupied(order.TableNumber, false);
		}
	}

	private void setTableNumber(int tableNumber) {
		this.TableNumber = tableNumber;
	}

	private void setDeliveryAdress(String deliveryAddress) {
		this.deliveryAddressid = deliveryAddress;
	}

	public String getOrderId() {
		return this.OrderId;
	}

	public String getCustomerId() {
		return this.CustomerId;
	}

	public Date getDateOrdered() {
		return this.DateOrdered;
	}

	public ArrayList<String> getOrderItems() {
		return this.OrderItems;
	}

	public double getTotal() {
		return this.Total;
	}

	public double getDiscount() {
		return this.Discount;
	}

	public String getOrderType() {
		return this.OrderType;
	}

	public double getLoyaltyPoints() {
		return this.LoyaltyPoints;
	}

	public static double getTax() {
		return OrderDatabase.getTax();
	}

	public void AddOrderItem(String OrderItem) {
		OrderItems.add(OrderItem);
		OrderDatabase.AddOrderItem(this.OrderId, OrderItem);
		setTotal();
	}

	public void DeleteOrderItem(String OrderItem) {
		OrderItems.remove(OrderItem);
		OrderDatabase.DeleteOrderItem(this.OrderId, OrderItem);
	}

	public void DeleteOrder() {
		OrderDatabase.DeleteOrder(this.OrderId);
	}

	public void DeleteAllOrderItems() {
		OrderDatabase.DeleteAllOrderItems(this.OrderId);
	}

	public static void updateTotal(String orderId) throws NullPointerException {
		try {
			Order order = getOrderFromdb(orderId);
			order.setTotal();
			OrderDatabase.updateTotal(orderId);
		} catch (NullPointerException e) {
			System.out.println("Order not found");
		}
	}

	public static void RemoveOrderItem(String orderId, String orderItemId) {
		OrderDatabase.DeleteOrderItem(orderId, orderItemId);
	}

	public static void AddOrderItem(String orderId, String orderItemId) {
		OrderDatabase.AddOrderItem(orderId, orderItemId);
	}

	public void setLoyaltyPoints() {
		this.LoyaltyPoints = this.Total / OrderDatabase.getPointsRate();
	}

	public String getAddressId() {
		try {
			return this.deliveryAddressid;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void addCurency(String currencyId) {
		setUsedCurrency(currencyId);
	}

	private class OrderDatabase {
		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = DatabaseConnection.getdatabaseName();

		public OrderDatabase() {
			// Add to database
			database = mongoClient.getDatabase("TSFPos");
		}

		public void AddOrder() {
			if (OrderType.equals("Takeaway")) {
				Document document = new Document("OrderId", OrderId)
						.append("CustomerId", CustomerId)
						.append("EmployeeId", EmployeeId)
						.append("DateOrdered", DateOrdered)
						.append("OrderStatus", OrderStatus)
						.append("OrderItems", OrderItems)
						.append("Total", Total)
						.append("isPaid", isPaid)
						.append("PaymentMethodId", PaymentMethodId)
						.append("DateCompleted", DateCompleted)
						.append("Discount", Discount)
						.append("Tax", Tax)
						.append("OrderType", OrderType)
						.append("LoyaltyPoints", LoyaltyPoints)
						.append("SpecialRequest", SpecialRequest)
						.append("UsedCurrency", UsedCurrency);
				database.getCollection("Orders").insertOne(document);
			} else if (OrderType.equals("DineIn")) {
				Document document = new Document("OrderId", OrderId)
						.append("CustomerId", CustomerId)
						.append("EmployeeId", EmployeeId)
						.append("DateOrdered", DateOrdered)
						.append("OrderStatus", OrderStatus)
						.append("OrderItems", OrderItems)
						.append("Total", Total)
						.append("isPaid", isPaid)
						.append("PaymentMethodId", PaymentMethodId)
						.append("DateCompleted", DateCompleted)
						.append("Discount", Discount)
						.append("Tax", Tax)
						.append("OrderType", OrderType)
						.append("LoyaltyPoints", LoyaltyPoints)
						.append("TableNumber", TableNumber);
				database.getCollection("Orders").insertOne(document);
			} else if (OrderType.equals("Delivery")) {
				Document document = new Document("OrderId", OrderId)
						.append("CustomerId", CustomerId)
						.append("EmployeeId", EmployeeId)
						.append("DateOrdered", DateOrdered)
						.append("OrderStatus", OrderStatus)
						.append("OrderItems", OrderItems)
						.append("Total", Total)
						.append("isPaid", isPaid)
						.append("PaymentMethodId", PaymentMethodId)
						.append("DateCompleted", DateCompleted)
						.append("Discount", Discount)
						.append("Tax", Tax)
						.append("OrderType", OrderType)
						.append("LoyaltyPoints", LoyaltyPoints)
						.append("Adress", deliveryAddressid);
				database.getCollection("Orders").insertOne(document);
			}
		}

		public static ArrayList<String> getOrderId() {
			ArrayList<String> orderId = new ArrayList<>();
			for (Document document : database.getCollection("Orders").find()) {
				orderId.add(document.getString("OrderId"));
			}
			return orderId;
		}

		public static String getCustomerId(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("CustomerId");
			} else {
				return null;
			}
		}

		public static String getEmployeeId(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("EmployeeId");
			} else {
				return null;
			}
		}

		public static Date getDateOrdered(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getDate("DateOrdered");
			} else {
				return null;
			}
		}

		public static Boolean getOrderStatus(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getBoolean("OrderStatus");
			} else {
				return null;
			}
		}

		public static void setTotal(String orderId) {
			Order order = getOrderFromdb(orderId);
			double total = 0.0;
			for (String orderItem : order.getOrderItems()) {
				OrderItem item = OrderItem.getOrderItemFromdb(orderItem);
				total += item.getTotalPrice();
			}
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("Total", total)));
		}

		public static ArrayList<String> getOrderItems(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return (ArrayList<String>) document.get("OrderItems");
			} else {
				return null;
			}
		}

		public static double getTotal(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getDouble("Total");
			} else {
				return 0.0;
			}
		}

		public static String getPaymentMethodId(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("PaymentMethodId");
			} else {
				return null;
			}
		}

		public static Date getDateCompleted(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getDate("DateCompleted");
			} else {
				return null;
			}
		}

		public static double getDiscount(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getDouble("Discount");
			} else {
				return 0.0;
			}
		}

		public static double getTax() {
			Document document = database.getCollection("Tax").find().first();
			if (document != null) {
				return document.getDouble("Tax");
			} else {
				return 0.0;
			}
		}

		public static String getOrderType(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("OrderType");
			} else {
				return null;
			}
		}

		public static double getLoyaltyPoints(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getDouble("LoyaltyPoints");
			} else {
				return 0.0;
			}
		}

		public static String getSpecialRequest(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("SpecialRequest");
			} else {
				return null;
			}
		}

		public static String getUsedCurrency(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				return document.getString("UsedCurrency");
			} else {
				return null;
			}
		}

		public static void updateOrderStatus(String orderId, Boolean orderStatus) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("OrderStatus", orderStatus)));
		}

		public static void updateDateCompleted(String orderId, Date dateCompleted) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("DateCompleted", dateCompleted)));
		}

		public static void updatePaymentMethodId(String orderId, String paymentMethodId) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("PaymentMethodId", paymentMethodId)));
		}

		public static void updatePaymentStatus(String orderId, Boolean isPaid) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("isPaid", isPaid)));
		}

		public static void updateTotal(String orderId) {
			Order order = getOrderFromdb(orderId);
			double total = 0.0;
			for (String orderItem : order.getOrderItems()) {
				OrderItem item = OrderItem.getOrderItemFromdb(orderItem);
				total += item.getTotalPrice();
			}
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("Total", total)));
		}

		public static void updateDiscount(String orderId, double discount) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("Discount", discount)));
		}

		public static void updateTax(String orderId, double tax) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("Tax", tax)));
		}

		public static void updateLoyaltyPoints(String orderId, double loyaltyPoints) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("LoyaltyPoints", loyaltyPoints)));
		}

		public static void updateSpecialRequest(String orderId, String specialRequest) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("SpecialRequest", specialRequest)));
		}

		public static void updateUsedCurrency(String orderId, String usedCurrency) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("UsedCurrency", usedCurrency)));
		}

		public static void AddOrderItem(String orderId, String orderItem) {
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$push", new Document("OrderItems", orderItem)));
		}

		public static void DeleteOrderItem(String orderId, String orderItem) {
			OrderItem.DeleteOrderItem(orderItem);
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$pull", new Document("OrderItems", orderItem)));
		}

		public static void DeleteOrder(String orderId) {
			DeleteAllOrderItems(orderId);
			database.getCollection("Orders").deleteOne(new Document("OrderId", orderId));
		}

		public static void DeleteAllOrderItems(String orderId) {
			OrderItem.DeleteOrderItemByOrder(orderId);
			database.getCollection("Orders").updateOne(new Document("OrderId", orderId), new Document("$set", new Document("OrderItems", new ArrayList<String>())));
		}

		public static Order getOrderFromdb(String orderId) {
			Document document = database.getCollection("Orders").find(new Document("OrderId", orderId)).first();
			if (document != null) {
				Order order = new Order();
				order.setOrderId(orderId);
				order.setDateOrdered(document.getDate("DateOrdered"));
				order.setOrderStatus(document.getBoolean("OrderStatus"));
				order.setOrderItems((ArrayList<String>) document.get("OrderItems"));
				order.setTotal(document.getDouble("Total"));
				order.setisPaid(document.getBoolean("isPaid"));
				order.setPaymentMethodId(document.getString("PaymentMethodId"));
				order.setPaymentStatus(document.getBoolean("isPaid"));
				order.setDateCompleted(document.getDate("DateCompleted"));
				order.setDiscount(document.getDouble("Discount"));
				order.setTax();
				order.setOrderType(document.getString("OrderType"));
				order.setLoyaltyPoints(document.getDouble("LoyaltyPoints"));
				order.setSpecialRequest(document.getString("SpecialRequest"));
				order.setUsedCurrency(document.getString("UsedCurrency"));
				if (order.getOrderType().equals("DineIn")) {
					order.setTableNumber(document.getInteger("TableNumber"));
				} else if (order.getOrderType().equals("Delivery")) {
					order.setDeliveryAdress(document.getString("Adress"));
				}
				return order;
			}
			return null;
		}

		public static double getPointsRate() {
			Document document = database.getCollection("PointsRate").find().first();
			if (document != null) {
				return document.getDouble("rateperdollar");
			} else {
				return 0.0;
			}
		}
	}
}