package com.example.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;


public class AddMenuItem implements RefreshMenulist {
	RefreshMenulist refreshMenulist;

	@FXML
	private TextField nametf, pricetf, Ingredientstf;

	@FXML
	private ComboBox<String> categorycb;

	@FXML
	private TextArea descriptiontf;

	@FXML
	private Label ErrorLabel;

	@FXML
	private Button btncancel, btnapply;

	@FXML
	private TableColumn<String, String> ingredientsClmn;

	@FXML
	private TableView<String> Ingredientstv;

	public ArrayList<String> ingredients;

	private MenuItemPanel menuItemPanel;

	@FXML
	public void initialize() {
		ingredientsClmn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		ingredients = new ArrayList<String>();
		categorycb.getItems().addAll(Category.getCategories());
		categorycb.getItems().addFirst("Add New Category");
		categorycb.setValue("Select Category");
	}


	@FXML
	public void btnaddMenuItem() {
		String name = nametf.getText();
		String pricetxt = pricetf.getText();
		String description = descriptiontf.getText();
		String cattxt = categorycb.getValue();
		Category category = new Category(cattxt);
		Double price;
		if (pricetxt.isEmpty()) {
			price = 0.0;
		} else {
			price = Double.parseDouble(pricetf.getText());
		}
		if (name.isEmpty() || pricetxt.isEmpty() || cattxt.isEmpty() || ingredients.isEmpty()) {
			System.out.println("Please fill in all fields " + Date.from(java.time.Instant.now()));
			ErrorLabel.setText("Please fill in all fields");
		} else if (MenuItem.MenuItemExists(name, category)) {
			System.out.println("Menu Item already exists");
			ErrorLabel.setText("Menu Item already exists");
		} else {
			if (descriptiontf.getText().isEmpty()) {
				description = "No Description";
			}
			System.out.println("Add Menu Item Button Pressed " + Date.from(java.time.Instant.now()));
			System.out.println("{ Name: " + name + ", Description: " +
					description + ", Category: " + category + ", " +
					"Price: " + price + ", Ingredients: " + ingredients + " }");

			MenuItem menuItem = new MenuItem(name, description, category, price, ingredients);
			DatabaseConnection.addMenuItem(menuItem);
			if (refreshMenulist != null) {
				refreshMenulist.setMenuItemRefresher(menuItemPanel);
			}
			System.out.println("Menu Item Added " + Date.from(java.time.Instant.now()));
			try {
				Stage homestage = (Stage) btnapply.getScene().getWindow();
				homestage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	@FXML
	public void setBtnaddingredient() throws Exception {
		System.out.println("Add Ingredient Button Pressed " + Date.from(java.time.Instant.now()));
		String ingredient = Ingredientstf.getText();
		if (ingredient.isEmpty()) {
			System.out.println("Please enter an ingredient " + Date.from(java.time.Instant.now()));
			ErrorLabel.setText("Please enter an ingredient");
		} else if (IngredientNotAvailable()) {
			try {
				ingredients.add(ingredient);
				Ingredientstv.getItems().add(ingredient);
				Ingredientstf.clear();
				Ingredientstf.setStyle("-fx-border-color: green");
				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								Ingredientstf.setStyle("-fx-border-color: black");
							}
						},
						3000
				);
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	@FXML
	public void setCategorycb() {
		System.out.println("Category ComboBox Pressed " + Date.from(java.time.Instant.now()));
		String category = categorycb.getValue();
		if (category.equals("Add New Category")) {
			System.out.println("Add New Category");
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Add New Category");
			dialog.setHeaderText("Enter New Category");
			dialog.setContentText("Category:");
			dialog.showAndWait();
			String newCategory = dialog.getResult();
			if (Category.CategoryNotAvailable(newCategory)) {
				Category newCat = new Category(newCategory);
				categorycb.getItems().add(newCategory);
				categorycb.setValue(newCategory);
			} else {
				// show a message dialog to say that the category already exists
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Category already exists");
				alert.showAndWait();
				System.out.println("Category already exists");
			}
		}
	}

	public void setMenuItemRefresher(RefreshMenulist refreshMenulist) {
		this.refreshMenulist = refreshMenulist;
	}


	private Boolean IngredientNotAvailable() {
		for (String ingredient : ingredients) {
			if (ingredient.equals(Ingredientstf.getText())) {
				System.out.println("Ingredient already exists " + Date.from(java.time.Instant.now()));
				ErrorLabel.setText("Ingredient already exists");
				return false;
			}
		}
		return true;
	}

	@Override
	public void setMenuItemRefresher(MenuItemPanel menuItemPanel) {
		this.menuItemPanel = menuItemPanel;
	}
}