package src.services;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import src.database.DatabaseConnection;
import src.models.Address;

import java.util.Date;

public class AddressService {
    private static final MongoClient mongoClient = DatabaseConnection.getmongoClient();
    private static final MongoDatabase database = mongoClient.getDatabase("TSFPos");

    public static String addAddress(Address address) {
        String addressId = "L" + new Date().getTime();
        Document document = new Document("addressId", addressId)
                .append("street", address.getStreet())
                .append("city", address.getCity())
                .append("postalCode", address.getPostalCode())
                .append("country", address.getCountry());
        database.getCollection("Addresses").insertOne(document);
        return addressId;
    }

    public static Address getAddress(String addressId) {
        Document document = database.getCollection("Addresses")
                .find(new Document("addressId", addressId)).first();
        if (document == null) return null;
        return new Address(
                addressId,
                document.getString("street"),
                document.getString("city"),
                document.getString("postalCode"),
                document.getString("country")
        );
    }

    public static void removeAddress(String addressId) {
        database.getCollection("Addresses").deleteOne(new Document("addressId", addressId));
    }

    public static String getAddressIdByText(String value) {
        String[] parts = value.split(", ");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Incomplete address information provided.");
        }
        Document document = database.getCollection("Addresses").find(new Document("street", parts[0])
                .append("city", parts[1])
                .append("postalCode", parts[2])
                .append("country", parts[3])).first();
        if (document == null) return null;
        return document.getString("addressId");
    }

    public static String getAddressText(String addressId) {
        Address address = getAddress(addressId);
        if (address == null) return null;
        return address.getFullAddress();
    }
}