package com.example.app;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBBackup {

	private static final String OFFLINE_DB_URI = "mongodb://localhost:27017";
	private static final String ONLINE_DB_URI = "mongodb+srv://Scotopia:Sc@topia81898056@cluster0.jf1plk4.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
	private static final String DATABASE_NAME = "TSFPos";

	public static void backupData() {
		MongoClient offlineClient = MongoClients.create(OFFLINE_DB_URI);
		MongoClient onlineClient = MongoClients.create(ONLINE_DB_URI);

		try {
			MongoDatabase offlineDb = offlineClient.getDatabase(DATABASE_NAME);
			MongoDatabase onlineDb = onlineClient.getDatabase(DATABASE_NAME);

			for (String collectionName : offlineDb.listCollectionNames()) {
				MongoCollection<Document> offlineCollection = offlineDb.getCollection(collectionName);
				MongoCollection<Document> onlineCollection = onlineDb.getCollection(collectionName);

				onlineCollection.drop(); // Remove existing data in the online collection

				for (Document doc : offlineCollection.find()) {
					onlineCollection.insertOne(doc);
				}
			}
			System.out.println("Backup completed successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			offlineClient.close();
			onlineClient.close();
		}
	}
}