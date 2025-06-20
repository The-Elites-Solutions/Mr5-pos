package src.models;

import java.util.ArrayList;

public class Customer {
	private String customerId;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	private ArrayList<String> address;
	private ArrayList<String> orders;
	private Double loyaltyPoints;

	public Customer(String firstname, String lastname, String phone, String email, String addressId) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = new ArrayList<>();
		this.address.add(addressId);
		this.orders = new ArrayList<>();
		this.loyaltyPoints = 0.0;
		// The service class will assign and save the customerId
	}

	public Customer(String customerId, String firstname, String lastname, String email, String phone,
                    ArrayList<String> address, ArrayList<String> orders, Double loyaltyPoints) {
		this.customerId = customerId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = orders;
		this.loyaltyPoints = loyaltyPoints;
	}

	// Getters and setters
	public String getCustomerId() { return customerId; }
	public void setCustomerId(String customerId) { this.customerId = customerId; }
	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }
	public String getLastname() { return lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }
	public String getFullName() {
		if ("N/A".equals(lastname)) return firstname;
		else return firstname + " " + lastname;
	}
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public ArrayList<String> getAddress() { return address; }
	public void setAddress(ArrayList<String> address) { this.address = address; }
	public ArrayList<String> getOrders() { return orders; }
	public void setOrders(ArrayList<String> orders) { this.orders = orders; }
	public Double getLoyaltyPoints() { return loyaltyPoints; }
	public void setLoyaltyPoints(Double loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}