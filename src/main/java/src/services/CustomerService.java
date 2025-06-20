package src.services;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import src.database.DatabaseConnection;
import src.models.Customer;

import java.util.ArrayList;
import java.util.Date;

public class CustomerService {
    private static final MongoClient mongoClient = DatabaseConnection.getmongoClient();
    private static final MongoDatabase database = mongoClient.getDatabase("TSFPos");

    public static String addCustomer(Customer customer) {
        String customerId = "C" + new Date().getTime();
        customer.setCustomerId(customerId);
        Document document = new Document("CustomerId", customerId)
                .append("Firstname", customer.getFirstname())
                .append("Lastname", customer.getLastname())
                .append("email", customer.getEmail())
                .append("phone", customer.getPhone())
                .append("address", customer.getAddress())
                .append("orders", customer.getOrders())
                .append("LoyaltyPoints", customer.getLoyaltyPoints());
        database.getCollection("Customers").insertOne(document);
        return customerId;
    }

    public static Customer getCustomerById(String customerId) {
        Document doc = database.getCollection("Customers").find(new Document("CustomerId", customerId)).first();
        if (doc == null) return null;
        return documentToCustomer(doc);
    }

    public static ArrayList<String> getCustomerIds() {
        ArrayList<String> customerIds = new ArrayList<>();
        for (Document doc : database.getCollection("Customers").find()) {
            customerIds.add(doc.getString("CustomerId"));
        }
        return customerIds;
    }

    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (Document doc : database.getCollection("Customers").find()) {
            customers.add(documentToCustomer(doc));
        }
        return customers;
    }

    public static void updateEmail(String customerId, String email) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$set", new Document("email", email))
        );
    }

    public static void updatePhone(String customerId, String phone) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$set", new Document("phone", phone))
        );
    }

    public static void addAddress(String customerId, String address) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$push", new Document("address", address))
        );
    }

    public static void addOrder(String customerId, String order) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$push", new Document("orders", order))
        );
    }

    public static void increaseLoyaltyPoints(String customerId, Double points) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$inc", new Document("LoyaltyPoints", points))
        );
    }

    public static void decreaseLoyaltyPoints(String customerId, Double points) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$inc", new Document("LoyaltyPoints", -points))
        );
    }

    public static void deleteCustomer(String customerId) {
        database.getCollection("Customers").deleteOne(new Document("CustomerId", customerId));
    }

    public static void deleteAddress(String customerId, String address) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$pull", new Document("address", address))
        );
    }

    public static void deleteOrder(String customerId, String order) {
        database.getCollection("Customers").updateOne(
                new Document("CustomerId", customerId),
                new Document("$pull", new Document("orders", order))
        );
    }

    public static ArrayList<String> getCustomerNames() {
        ArrayList<String> customerNames = new ArrayList<>();
        for (Document doc : database.getCollection("Customers").find()) {
            customerNames.add(doc.getString("Firstname") + " " + doc.getString("Lastname"));
        }
        return customerNames;
    }

    public static ArrayList<String> getCustomerPhoneNumbers() {
        ArrayList<String> customerPhones = new ArrayList<>();
        for (Document doc : database.getCollection("Customers").find()) {
            customerPhones.add(doc.getString("phone"));
        }
        return customerPhones;
    }

    // Helper to convert Document to Customer object
    private static Customer documentToCustomer(Document doc) {
        String customerId = doc.getString("CustomerId");
        String firstname = doc.getString("Firstname");
        String lastname = doc.getString("Lastname");
        String email = doc.getString("email");
        String phone = doc.getString("phone");
        ArrayList<String> address = (ArrayList<String>) doc.get("address");
        ArrayList<String> orders = (ArrayList<String>) doc.get("orders");
        Double loyaltyPoints = doc.getDouble("LoyaltyPoints");
        return new Customer(customerId, firstname, lastname, email, phone, address, orders, loyaltyPoints);
    }
}