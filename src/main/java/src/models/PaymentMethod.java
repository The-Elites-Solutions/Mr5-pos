package src.models;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import src.database.DatabaseConnection;

public class PaymentMethod {

	private String PaymentMethodId, PaymentMethodType, PaymentMethodDescription;

	private Double TotalIncome;

	public PaymentMethod(String PaymentMethodId, String PaymentMethodType, String PaymentMethodDescription) {
		setPaymentMethodId(PaymentMethodId);
		setPaymentMethodType(PaymentMethodType);
		setPaymentMethodDescription(PaymentMethodDescription);
		TotalIncome = 0.0;
		AddToDatabase();
	}

	private void setPaymentMethodId(String PaymentMethodId) {
		this.PaymentMethodId = PaymentMethodId;
	}

	private void setPaymentMethodType(String PaymentMethodType) {
		this.PaymentMethodType = PaymentMethodType;
	}

	private void setPaymentMethodDescription(String PaymentMethodDescription) {
		this.PaymentMethodDescription = PaymentMethodDescription;
	}

	private void AddToDatabase() {
		// Add to database
		PaymentMethodDatabase paymentMethodDatabase = new PaymentMethodDatabase();
		paymentMethodDatabase.AddPaymentMethod();
	}

	public static String getPaymentMethodType(String PaymentMethodId) {
		return PaymentMethodDatabase.getPaymentMethodType(PaymentMethodId);
	}

	public static String getPaymentMethodDescription(String PaymentMethodId) {
		return PaymentMethodDatabase.getPaymentMethodDescription(PaymentMethodId);
	}

	public static String getPaymentMethodId(String PaymentMethodType) {
		return PaymentMethodDatabase.getPaymentMethodId(PaymentMethodType);
	}

	public static void updatePaymentMethodDescription(String PaymentMethodId, String PaymentMethodDescription) {
		PaymentMethodDatabase.updatePaymentMethodDescription(PaymentMethodId, PaymentMethodDescription);
	}

	public static void deletePaymentMethod(String PaymentMethodId) {
		PaymentMethodDatabase.deletePaymentMethod(PaymentMethodId);
	}

	public static void addToTotalIncome(String PaymentMethodId, Double Amount) {
		PaymentMethodDatabase.AddToTotalIncome(PaymentMethodId, Amount);
	}

	private class PaymentMethodDatabase {

		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = DatabaseConnection.getdatabaseName();

		public PaymentMethodDatabase() {
			database = mongoClient.getDatabase("TSFPos");
			System.out.println("Database connection successful");
		}

		public void AddPaymentMethod() {
			Document document = new Document("PaymentMethodId", PaymentMethodId)
					.append("PaymentMethodType", PaymentMethodType)
					.append("PaymentMethodDescription", PaymentMethodDescription)
					.append("TotalIncome", TotalIncome);
		}

		public static String getPaymentMethodType(String PaymentMethodId) {
			Document document = database.getCollection("PaymentMethods")
					.find(new Document("PaymentMethodId", PaymentMethodId)).first();
			return document.getString("PaymentMethodType");
		}

		public static String getPaymentMethodDescription(String PaymentMethodId) {
			Document document = database.getCollection("PaymentMethods")
					.find(new Document("PaymentMethodId", PaymentMethodId)).first();
			return document.getString("PaymentMethodDescription");
		}

		public static String getPaymentMethodId(String PaymentMethodType) {
			Document document = database.getCollection("PaymentMethods")
					.find(new Document("PaymentMethodType", PaymentMethodType)).first();
			return document.getString("PaymentMethodId");
		}

		public static void updatePaymentMethodDescription(String PaymentMethodId, String PaymentMethodDescription) {
			Document document = database.getCollection("PaymentMethods")
					.find(new Document("PaymentMethodId", PaymentMethodId)).first();
			document.append("PaymentMethodDescription", PaymentMethodDescription);
		}

		public static void deletePaymentMethod(String PaymentMethodId) {
			database.getCollection("PaymentMethods")
					.deleteOne(new Document("PaymentMethodId", PaymentMethodId));
		}

		public static void AddToTotalIncome(String PaymentMethodId, Double amount) {
			Document document = database.getCollection("PaymentMethods")
					.find(new Document("PaymentMethodId", PaymentMethodId)).first();
			document.append("TotalIncome", document.getDouble("TotalIncome") + amount);
		}


	}
}
