package com.example.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Table {
	private int Number;
	private int Capacity;
	private int MergedTableNumber;
	private String OrderId;
	private String ReservationId;
	private String SpecialRequest;
	private boolean Occupied;
	private boolean isMerged;

	public Table(int Number, int Capacity) {
		setNumber(Number);
		setCapacity(Capacity);
		setMergedTableNumber(-1);
		setOrderId("");
		setReservationId("");
		setSpecialRequest("");
		setOccupied(false);
		setMerged(false);
		AddToDatabase();
	}

	private void setNumber(int Number) {
		this.Number = Number;
	}

	private void setCapacity(int Capacity) {
		this.Capacity = Capacity;
	}

	private void setMergedTableNumber(int MergedTableNumber) {
		this.MergedTableNumber = MergedTableNumber;
	}

	private void setOrderId(String OrderId) {
		this.OrderId = OrderId;
	}

	private void setReservationId(String ReservationId) {
		this.ReservationId = ReservationId;
	}

	private void setSpecialRequest(String SpecialRequest) {
		this.SpecialRequest = SpecialRequest;
	}

	private void setOccupied(boolean Occupied) {
		this.Occupied = Occupied;
	}

	private void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	private static int getCapacity(int Number) {
		return TableDatabase.getCapacity(Number);
	}

	private static int getMergedTableNumber(int Number) {
		return TableDatabase.getMergedTableNumber(Number);
	}

	private static String getOrderId(int Number) {
		return TableDatabase.getOrderId(Number);
	}

	private static String getReservationId(int Number) {
		return TableDatabase.getReservationId(Number);
	}

	private static String getSpecialRequest(int Number) {
		return TableDatabase.getSpecialRequest(Number);
	}

	private static boolean getOccupied(int Number) {
		return TableDatabase.getOccupied(Number);
	}

	private static boolean getMerged(int Number) {
		return TableDatabase.getMerged(Number);
	}

	public static void setOccupied(int Number, boolean Occupied) {
		TableDatabase.setOccupied(Number, Occupied);
	}

	public static void setMerged(int Number, boolean isMerged) {
		TableDatabase.setMerged(Number, isMerged);
	}

	public static void setMergedTableNumber(int Number, int MergedTableNumber) {
		TableDatabase.setMergedTableNumber(Number, MergedTableNumber);
	}

	public static void setOrderId(int Number, String OrderId) {
		TableDatabase.setOrderId(Number, OrderId);
	}

	public static void setReservationId(int Number, String ReservationId) {
		TableDatabase.setReservationId(Number, ReservationId);
	}

	public static void setSpecialRequest(int Number, String SpecialRequest) {
		TableDatabase.setSpecialRequest(Number, SpecialRequest);
	}

	public static ArrayList<Integer> getTables() {
		return TableDatabase.getTables();
	}

	public static void deleteTable(int Number) {
		TableDatabase.deleteTable(Number);
	}

	public static void deleteAllTables() {
		TableDatabase.deleteAllTables();
	}

	public void AddToDatabase() {
		// Add the table to the database
		TableDatabase tableDatabase = new TableDatabase();
		tableDatabase.AddTable();
	}

	public static Boolean isAvailable(int Number) {
		return TableDatabase.isAvailable(Number);
	}

	private class TableDatabase {
		// Database connection
		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = DatabaseConnection.getdatabaseName();

		public TableDatabase() {
			database = mongoClient.getDatabase("TSFPos");
			System.out.println("Database connection successful");
		}

		public void AddTable() {
			Document document = new Document("number", Number)
					.append("capacity", Capacity)
					.append("mergedTableNumber", MergedTableNumber)
					.append("orderId", OrderId)
					.append("reservationId", ReservationId)
					.append("specialRequest", SpecialRequest)
					.append("occupied", Occupied)
					.append("isMerged", isMerged);
			database.getCollection("Tables").insertOne(document);
			System.out.println("Menu Item added to the Database " + Date.from(java.time.Instant.now()));
		}

		public static int getCapacity(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getInteger("capacity");
		}

		public static int getMergedTableNumber(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getInteger("mergedTableNumber");
		}

		public static String getOrderId(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getString("orderId");
		}

		public static String getReservationId(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getString("reservationId");
		}

		public static String getSpecialRequest(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getString("specialRequest");
		}

		public static boolean getOccupied(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getBoolean("occupied");
		}

		public static boolean getMerged(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			return document.getBoolean("isMerged");
		}

		public static void setOccupied(int Number, boolean Occupied) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("occupied", Occupied)));
		}

		public static void setMerged(int Number, boolean isMerged) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("isMerged", isMerged)));
		}

		public static void setMergedTableNumber(int Number, int MergedTableNumber) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("mergedTableNumber", MergedTableNumber)));
		}

		public static void setOrderId(int Number, String OrderId) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("orderId", OrderId)));
		}

		public static void setReservationId(int Number, String ReservationId) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("reservationId", ReservationId)));
		}

		public static void setSpecialRequest(int Number, String SpecialRequest) {
			database.getCollection("Tables").updateOne(new Document("number", Number),
					new Document("$set", new Document("specialRequest", SpecialRequest)));
		}

		public static void deleteTable(int Number) {
			database.getCollection("Tables").deleteOne(new Document("number", Number));
		}

		public static void deleteAllTables() {
			database.getCollection("Tables").deleteMany(new Document());
		}

		public static ArrayList<Integer> getTables() {
			ArrayList<Integer> tables = new ArrayList<>();
			for (Document document : database.getCollection("Tables").find()) {
				tables.add(document.getInteger("number"));
			}
			return tables;
		}

		public static ArrayList<Table> getTableObjects() {
			ArrayList<Table> tables = new ArrayList<>();
			for (Document document : database.getCollection("Tables").find()) {
				Table table = new Table(document.getInteger("number"), document.getInteger("capacity"));
				table.setMergedTableNumber(document.getInteger("mergedTableNumber"));
				table.setOrderId(document.getString("orderId"));
				table.setReservationId(document.getString("reservationId"));
				table.setSpecialRequest(document.getString("specialRequest"));
				table.setOccupied(document.getBoolean("occupied"));
				table.setMerged(document.getBoolean("isMerged"));
				tables.add(table);
			}
			return tables;
		}

		public static Boolean isAvailable(int Number) {
			Document document = database.getCollection("Tables").find(new Document("number", Number)).first();
			if (document.getBoolean("occupied")) {
				System.out.println("Table is occupied");
				return false;
			} else {
				System.out.println("Table is available");
				return true;
			}
		}
	}
}