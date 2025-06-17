package com.example.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javafx.collections.ObservableList;
import org.bson.Document;

import java.util.Collection;
import java.util.Date;

public class Address {

	private String addressId;
	private String street;
	private String city;
	private String postalCode;
	private String country;

	public Address(String street, String city, String postalCode, String country) {
		setAddressId();
		setStreet(street);
		setCity(city);
		setPostalCode(postalCode);
		setCountry(country);
		addToDb();
	}

	public Address(String DeliveryAddress) {
		setAddressId();
		String[] address = DeliveryAddress.split(",");
		setStreet(address[0]);
		setCity(address[1]);
		setPostalCode(address[2]);
		setCountry(address[3]);
		addToDb();
	}

	public Address(String addressId, String street, String city, String postalCode, String country) {
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	public static String getAddressId(String value) {
		return AddressDatabase.getAddressId(value);
	}

	public static void removeAddress(String addressid) {
		AddressDatabase.removeAddress(addressid);
	}

	private void addToDb() {
		AddressDatabase addressDatabase = new AddressDatabase();
		addressDatabase.addCustomer();
	}

	private void setAddressId() {
		Date date = new Date();
		this.addressId = "L" + date.getTime();
	}

	private void setStreet(String street) {
		this.street = street;
	}

	private void setCity(String city) {
		this.city = city;
	}

	private void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	private void setCountry(String country) {
		this.country = country;
	}

	public String getAddressId() {
		return addressId;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public static Address getAddress(String addressId) throws NullPointerException {
		try {
			return AddressDatabase.getAddress(addressId);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", postalCode=" + postalCode + ", country=" + country + "]";
	}

	public String getAddress() {
		return street + ", " + city + ", " + postalCode + ", " + country;
	}

	public static String getAddresstxt(String AddressId) {
		return getAddress(AddressId).getAddress();
	}

	private class AddressDatabase {
		private static MongoClient mongoClient = DatabaseConnection.getmongoClient();
		private static MongoDatabase database = mongoClient.getDatabase("TSFPos");

		public AddressDatabase() {
			database = mongoClient.getDatabase("TSFPos");
			System.out.println("Database connection successful");
		}

		public static String getAddresstxt(String addressId) {
			Document document = database.getCollection("Addresses").find(new Document("addressId", addressId)).first();
			return document.getString("street") +
					", " + document.getString("city") +
					", " + document.getString("postalCode") +
					", " + document.getString("country");
		}

		public static String getAddressId(String value) {
			System.out.println(value);
			String[] parts = value.split(", ");
			if (parts.length < 4) {
				throw new IllegalArgumentException("Incomplete address information provided.");
			}
			Document document = database.getCollection("Addresses").find(new Document("street", parts[0])
					.append("city", parts[1])
					.append("postalCode", parts[2])
					.append("country", parts[3])).first();
			if (document == null) {
				return null; // or throw an exception if no matching document is found
			}
			return document.getString("addressId");
		}

		public static void removeAddress(String addressid) {
			database.getCollection("Addresses").deleteOne(new Document("addressId", addressid));
		}

		public void addCustomer() {
			Document document = new Document("addressId", addressId)
					.append("street", street)
					.append("city", city)
					.append("postalCode", postalCode)
					.append("country", country);
			database.getCollection("Addresses").insertOne(document);
		}

		public static Address getAddress(String addressId) {
			Document document = database.getCollection("Addresses").find(new Document("addressId", addressId)).first();
			return new Address(addressId, document.getString("street"),
					document.getString("city"), document.getString("postalCode"),
					document.getString("country"));
		}
	}
}
