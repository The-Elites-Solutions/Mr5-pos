package src.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.database.DatabaseConnection;
import src.models.DollarRate;
import src.models.Role;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class POSApplication extends Application {
	Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		// Create an FXMLLoader to load the LoginScreen.fxml file
		FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("LoginScreen.fxml"));


		// Create a new scene using the loaded FXML content
		Scene scene = new Scene(fxmlLoader.load());


		// Set the application's primary stage
		this.stage = stage;

		// Configure the stage properties
		this.stage.setResizable(false); // Disable resizing
		this.stage.setTitle("LoginScreen"); // Set the window title
		this.stage.setScene(scene); // Set the scene
		this.stage.show(); // Display the stage
	}

	public static void logWriter() {
		String packagePath = System.getProperty("user.dir") + File.separator;
		// Create a logs folder if it doesn't exist
		File logsFolder = new File(packagePath + File.separator + "logs");
		if (!logsFolder.exists()) {
			logsFolder.mkdirs();
		}

		// Format the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String currentDateTime = dateFormat.format(new Date());

		// File name with the current date and time
		String fileName = "log_" + currentDateTime + ".txt";

		// Redirect standard output stream to the file
		try {
			PrintStream fileStream = new PrintStream(new FileOutputStream(logsFolder.getPath() + File.separator + fileName));
			System.setOut(fileStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("This is a log entry written on " + new Date());
	}

	public static void main(String[] args) {
		// Redirect standard output stream to a log file
		logWriter();
		///////////////////////////////////new InternetCheckThread().start();
		//Launch the JavaFX application
		DatabaseConnection.LoadUsers();
		System.out.println("Users loaded " + Date.from(java.time.Instant.now()));
		Role.loadFonction();
		System.out.println("Fonctions loaded " + Date.from(java.time.Instant.now()));
		DatabaseConnection.getExpenses();
		System.out.println("Expenses loaded " + Date.from(java.time.Instant.now()));
		DatabaseConnection.loadMenuItems();
		System.out.println("Menu items loaded " + Date.from(java.time.Instant.now()));
		DatabaseConnection.loadCategories();
		System.out.println("Menu items loaded " + Date.from(java.time.Instant.now()));
		DollarRate.loadDollarRates();
		System.out.println("Dollar rates loaded " + Date.from(java.time.Instant.now()));

		System.out.println("Everything is loaded " + Date.from(java.time.Instant.now()));
		Application.launch();
	}
}