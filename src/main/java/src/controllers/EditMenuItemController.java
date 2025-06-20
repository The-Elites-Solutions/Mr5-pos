package src.controllers;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import src.database.DatabaseConnection;
import src.interfaces.RefreshMenuList;
import src.models.Category;
import src.models.MenuItem;

import java.util.ArrayList;
import java.util.Date;

public class EditMenuItemController implements RefreshMenuList {
	RefreshMenuList refreshMenulist;

	@FXML
	private ComboBox<String> namecb, categorycb;

	@FXML
	private TextField pricetf, ingredientstf;

	@FXML
	private TextArea descriptiontf;

	@FXML
	private CheckBox ingredientsrmcheckbox;

	@FXML
	private TableColumn<String, String> ingredientsclmn;

	@FXML
	private TableView<String> ingredientstv;

	@FXML
	private Button btnapply, btncancel, btnaddrmingredient;

	@FXML
	private Pane mainpane;

	ComboBox<String> rmingredientscb = new ComboBox<>();

	private ArrayList<String> ingredients;

	private MenuItemController menuItemController;

	@FXML
	public void initialize() {
		ingredientsclmn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		ingredients = new ArrayList<String>();
		namecb.getItems().addAll(getNames());
		categorycb.getItems().addAll(Category.getCategories());
		categorycb.setValue("Select Category");
		btnaddrmingredient.setOnAction(e -> {
			btnaddrmingredient.setText("+");
			btnaddrmingredient.setOnAction(event -> {
				ingredients.add(ingredientstf.getText());
				ingredientstv.getItems().clear();
				ingredientstv.getItems().addAll(ingredients);
				ingredientstv.setItems(FXCollections.observableArrayList(ingredients));
			});
		});

	}

	@FXML
	public void setBtnapply() {
		System.out.println("Apply Button Pressed " + Date.from(java.time.Instant.now()));
		if (namecb.getValue() == null || pricetf.getText().isEmpty()
				|| categorycb.getValue().equals("Select Category") || ingredients.isEmpty()) {
			System.out.println("Please fill in all fields " + Date.from(java.time.Instant.now()));
		} else {
			if (descriptiontf.getText().isEmpty()) {
				descriptiontf.setText("No description");
			}
			MenuItem item = MenuItem.getMenuItem(namecb.getValue());
			item.setPrice(Double.parseDouble(pricetf.getText()));
			item.setDescription(descriptiontf.getText());
			item.setCategory(categorycb.getValue());
			item.editIngredients(ingredients);
			DatabaseConnection.changeMenuItem(item.getName(), item.getDescription(), item.getCategory().getName(),
					item.getPrice(), item.getIngredients());
			System.out.println("MenuItem edited " + Date.from(java.time.Instant.now()));
			if (refreshMenulist != null) {
				refreshMenulist.setMenuItemRefresher(menuItemController);
			}
			Stage stage = (Stage) btnapply.getScene().getWindow();
			stage.close();
		}
	}

	@FXML
	public void OnNameSelected() throws NullPointerException {
		try {
			MenuItem item = MenuItem.getMenuItem(namecb.getValue());
			pricetf.setText(String.valueOf(item.getPrice()));
			descriptiontf.setText(item.getDescription());
			categorycb.setValue(item.getCategory().getName());
			ingredients.clear();
			ingredientstv.getItems().clear();
			ingredients.addAll(item.getIngredients());
			ingredientstv.getItems().addAll(ingredients);
			ingredientstv.setItems(FXCollections.observableArrayList(ingredients));
			rmingredientscb.getItems().clear();
			rmingredientscb.getItems().addAll(ingredients);
			System.out.println("Item selected " + Date.from(java.time.Instant.now()));
		} catch (NullPointerException e) {
			System.out.println("No item selected " + Date.from(java.time.Instant.now()));
		}
	}

	@FXML
	public void CheckBoxAction() {
		if (ingredientsrmcheckbox.isSelected()) {
			//change ingredientstf to a combobox and add the ingredients to the combobox
			ingredientstf.setVisible(false);
			rmingredientscb.setVisible(true);
			rmingredientscb.getItems().clear();
			rmingredientscb.getItems().addAll(ingredients);
			rmingredientscb.setPromptText("Select an ingredient");
			rmingredientscb.setStyle("-fx-background-color: #fcfcfc; -fx-padding: 0; " +
					"-fx-pref-width: 200; -fx-pref-height: 25; -fx-translate-y: 42; -fx-translate-x: 310;");
			btnaddrmingredient.setText("-");
			btnaddrmingredient.setOnAction(e -> {
				ingredients.remove(rmingredientscb.getValue());
				ingredientstv.getItems().clear();
				ingredientstv.getItems().addAll(ingredients);
				ingredientstv.setItems(FXCollections.observableArrayList(ingredients));
				rmingredientscb.getItems().clear();
				rmingredientscb.getItems().addAll(ingredients);
				rmingredientscb.setPromptText("Select an ingredient");
			});
			mainpane.getChildren().add(rmingredientscb);
			System.out.println("CheckBox is selected " + Date.from(java.time.Instant.now()));
		} else {
			//change combobox back to textfield
			mainpane.getChildren().remove(rmingredientscb);
			ingredientstf.setVisible(true);
			rmingredientscb.setVisible(false);
			btnaddrmingredient.setText("+");
			btnaddrmingredient.setOnAction(event -> {
				ingredients.add(ingredientstf.getText());
				ingredientstv.getItems().clear();
				ingredientstv.getItems().addAll(ingredients);
				ingredientstv.setItems(FXCollections.observableArrayList(ingredients));
				ingredientstf.clear();
			});
			System.out.println("CheckBox is not selected " + Date.from(java.time.Instant.now()));
		}
	}

	@FXML
	public void setBtncancel() {
		System.out.println("Cancel Button Pressed " + Date.from(java.time.Instant.now()));
		try {
			Stage homestage = (Stage) btncancel.getScene().getWindow();
			homestage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (MenuItem item : MenuItem.getAllMenuItems()) {
			names.add(item.getName());
		}
		return names;
	}

	@Override
	public void setMenuItemRefresher(MenuItemController menuItemController) {
		this.menuItemController = menuItemController;
	}
}
