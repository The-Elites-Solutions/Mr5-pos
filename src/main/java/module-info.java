module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
	requires java.desktop;
	requires org.controlsfx.controls;
	requires com.fasterxml.jackson.databind;
	requires layout;
	requires kernel;


	opens com.example.app to javafx.fxml;
    exports com.example.app;
}