package src.models;

public class Address {
	private String addressId;
	private String street;
	private String city;
	private String postalCode;
	private String country;

	public Address(String street, String city, String postalCode, String country) {
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	public Address(String addressId, String street, String city, String postalCode, String country) {
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	// Getters and setters

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city +
				", postalCode=" + postalCode + ", country=" + country + "]";
	}

	public String getFullAddress() {
		return street + ", " + city + ", " + postalCode + ", " + country;
	}
}