package src.controllers;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.database.DatabaseConnection;
import src.interfaces.RefreshMenuList;
import src.main.POSApplication;
import src.models.Category;
import src.models.MenuItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuItemController implements RefreshMenuList {

	@FXML
	private AnchorPane anchorpane;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Button btnadd, btnedit;

	@FXML
	private ImageView btnback;

	@FXML
	private TableView<MenuItem> menuitemstv;

	@FXML
	private TableColumn<MenuItem, String> nameclmn, descriptionclmn, categoryclmn, priceclmn;

	@FXML
	private TableColumn<MenuItem, ArrayList<String>> ingredientsclmn;

	@FXML
	private TableColumn<MenuItem, Void> removebtnclmn;

	@FXML
	public void initialize() {
		setupColumns();
		setupRemoveButton();
		menuitemstv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		refreshMenuList();
		menuitemstv.setSkin(new CustomTableViewSkin<>(menuitemstv));
	}

	private void setupColumns() {
		nameclmn.setCellValueFactory(new PropertyValueFactory<>("name"));
		descriptionclmn.setCellValueFactory(new PropertyValueFactory<>("description"));
		categoryclmn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
		priceclmn.setCellValueFactory(new PropertyValueFactory<>("price"));

		ingredientsclmn.setCellFactory(column -> new TableCell<>() {
			private final ComboBox<String> comboBox = new ComboBox<>();

			{
				comboBox.setEditable(false);
				comboBox.setStyle("-fx-background-color: #fcfcfc; -fx-padding: 0; " +
						"-fx-pref-width: 162; -fx-pref-height: -1;");
			}

			@Override
			protected void updateItem(ArrayList<String> ingredients, boolean empty) {
				super.updateItem(ingredients, empty);
				if (empty || ingredients == null || ingredients.isEmpty()) {
					setGraphic(null);
				} else {
					comboBox.setItems(FXCollections.observableArrayList(ingredients));
					comboBox.setValue(ingredients.get(0));
					setGraphic(comboBox);
				}
			}
		});
		ingredientsclmn.setCellValueFactory(cellData ->
				new SimpleObjectProperty<>(cellData.getValue().getIngredients()));
	}

	private void setupRemoveButton() {
		removebtnclmn.setCellFactory(column -> new TableCell<MenuItem, Void>() {
			private final Button removeButton = new Button("-");

			{
				removeButton.setOnAction(event -> {
					int index = getIndex();
					if (index >= 0 && index < getTableView().getItems().size()) {
						MenuItem item = getTableView().getItems().get(index);
						try {
							MenuItem.removeMenuItem(item);
							DatabaseConnection.deleteMenuItem(item.getName());
							System.out.println("Menu Item Removed " + LocalDateTime.now());
							refreshMenuList();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : removeButton);
			}
		});
	}

	public void refreshMenuList() {
		System.out.println("Refreshing Menu List " + LocalDateTime.now());
		ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(MenuItem.getMenuItems());
		menuitemstv.setItems(menuItems);
	}

	@Override
	public void setMenuItemRefresher(MenuItemController menuItemController) {
		refreshMenuList();
	}

	@FXML
	public void setBtnrefresh() {
		System.out.println("Refresh Button Pressed " + LocalDateTime.now());
		refreshMenuList();
		menuitemstv.refresh();
		System.out.println("Refreshed " + LocalDateTime.now());
	}

	@FXML
	public void setBtnback() {
		System.out.println("Back Button Pressed " + LocalDateTime.now());
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(POSApplication.class.getResource("HomeScreen.fxml"));
			Parent root = fxmlLoader.load();
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Home Screen");
			newStage.show();

			Stage stage = (Stage) btnback.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void setBtnadd() {
		System.out.println("Add Button Pressed " + LocalDateTime.now());
		openMenuItemWindow("AddMenuItem.fxml", "Add");
	}

	@FXML
	public void setBtnedit() {
		System.out.println("Edit Button Pressed " + LocalDateTime.now());
		openMenuItemWindow("EditMenuItem.fxml", "Edit");
	}

	private void openMenuItemWindow(String fxmlFile, String title) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
			Parent root = fxmlLoader.load();
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle(title);
			newStage.show();

			RefreshMenuList menuItemController = fxmlLoader.getController();
			menuItemController.setMenuItemRefresher(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBtnfilters() {
		System.out.println("Filter Button Pressed " + LocalDateTime.now());
		Pane filterPane = createFilterPane();
		anchorpane.getChildren().add(filterPane);
		System.out.println("Filter Pane Loaded " + LocalDateTime.now());
	}

	private Pane createFilterPane() {
		Pane pane = new Pane();
		pane.setPrefSize(400, 400);
		pane.setLayoutX(160);
		pane.setLayoutY(32);
		pane.setStyle("-fx-background-color: #252525;");

		Label label = new Label("Filter By");
		label.setLayoutX(25);
		label.setLayoutY(3);
		label.setStyle("-fx-text-fill: #fcfcfc; -fx-font-size: 18px;" +
				" -fx-font-weight: Bold Italic; -fx-font-family: 'Times New Roman';");
		pane.getChildren().add(label);

		TreeView<Object> treeView = createFilterTreeView();
		treeView.setLayoutX(25);
		treeView.setLayoutY(25);
		treeView.setPrefSize(342, 342);
		pane.getChildren().add(treeView);

		Button btnapply = new Button("Done");
		btnapply.setLayoutX(120);
		btnapply.setLayoutY(368);
		btnapply.setPrefSize(70, 15);
		btnapply.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; -fx-font-size: 16px;" +
				" -fx-font-weight: Bold Italic; -fx-font-family: 'Times New Roman'");
		btnapply.setOnAction(event -> {
			applyFilters(treeView);
			anchorpane.getChildren().remove(pane);
			System.out.println("Filter Applied " + LocalDateTime.now());
		});

		Button btnclear = new Button("Clear");
		btnclear.setLayoutX(200);
		btnclear.setLayoutY(368);
		btnclear.setPrefSize(70, 15);
		btnclear.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; -fx-font-size: 16px;" +
				" -fx-font-weight: Bold Italic; -fx-font-family: 'Times New Roman'");
		btnclear.setOnAction(event -> clearFilters(treeView));
		pane.getChildren().addAll(btnapply, btnclear);

		return pane;
	}

	private TreeView<Object> createFilterTreeView() {
		TreeView<Object> treeView = new TreeView<>();
		TreeItem<Object> root = new TreeItem<>("Filter");
		TreeItem<Object> category = new TreeItem<>("Category");
		TreeItem<Object> ingredients = new TreeItem<>("Ingredients");

		Slider slider = createPriceSlider();
		TreeItem<Object> pricetag = new TreeItem<>("Price Range");
		TreeItem<Object> price = new TreeItem<>(new StackPane(slider));

		root.getChildren().addAll(category, ingredients, pricetag);
		pricetag.getChildren().add(price);
		treeView.setRoot(root);
		treeView.setShowRoot(false);
		root.getChildren().forEach(item -> item.setExpanded(false));

		// Add categories to the tree view
		for (String categoryName : Category.getCategories()) {
			CheckBox checkBox = new CheckBox(categoryName);
			TreeItem<Object> treeItem = new TreeItem<>(checkBox);
			category.getChildren().add(treeItem);
		}

		// Add ingredients to the tree view
		for (String ingredient : MenuItem.getAllIngredients()) {
			CheckBox checkBox = new CheckBox(ingredient);
			TreeItem<Object> treeItem = new TreeItem<>(checkBox);
			ingredients.getChildren().add(treeItem);
		}

		treeView.setCellFactory(tv -> new TreeCell<>() {
			@Override
			protected void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else if (item instanceof StackPane) {
					setGraphic((StackPane) item);
					setText(null);
				} else if (item instanceof CheckBox) {
					setGraphic((CheckBox) item);
					setText(null);
				} else {
					setText(item.toString());
					setGraphic(null);
				}
			}
		});
		return treeView;
	}

	private Slider createPriceSlider() {
		Slider slider = new Slider(0, 100, 0);
		slider.setValue(100);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(10);
		slider.setMinorTickCount(1);
		slider.setBlockIncrement(10);
		slider.setSnapToTicks(true);
		slider.setPrefWidth(180);
		return slider;
	}

	private void applyFilters(TreeView<Object> treeView) {
		TreeItem<Object> root = treeView.getRoot();
		List<String> selectedCategories = new ArrayList<>();
		List<String> selectedIngredients = new ArrayList<>();
		double priceRange = 0;

		for (TreeItem<Object> item : root.getChildren()) {
			if (item.getValue().toString().equals("Category")) {
				for (TreeItem<Object> child : item.getChildren()) {
					CheckBox checkBox = (CheckBox) child.getValue();
					if (checkBox.isSelected()) {
						selectedCategories.add(checkBox.getText());
					}
				}
			} else if (item.getValue().toString().equals("Ingredients")) {
				for (TreeItem<Object> child : item.getChildren()) {
					CheckBox checkBox = (CheckBox) child.getValue();
					if (checkBox.isSelected()) {
						selectedIngredients.add(checkBox.getText());
					}
				}
			} else if (item.getValue().toString().equals("Price Range")) {
				for (TreeItem<Object> child : item.getChildren()) {
					StackPane stackPane = (StackPane) child.getValue();
					Slider slider = (Slider) stackPane.getChildren().get(0);
					priceRange = slider.getValue();
				}
			}
		}

		filterItems(selectedCategories, selectedIngredients, priceRange);
	}

	private void clearFilters(TreeView<Object> treeView) {
		TreeItem<Object> root = treeView.getRoot();
		for (TreeItem<Object> item : root.getChildren()) {
			for (TreeItem<Object> child : item.getChildren()) {
				if (child.getValue() instanceof CheckBox) {
					CheckBox checkBox = (CheckBox) child.getValue();
					checkBox.setSelected(false);
				} else if (child.getValue() instanceof StackPane) {
					StackPane stackPane = (StackPane) child.getValue();
					Slider slider = (Slider) stackPane.getChildren().get(0);
					slider.setValue(0);
				}
			}
		}
		System.out.println("Filter Cleared " + LocalDateTime.now());
	}

	private void filterItems(List<String> selectedCategories, List<String> selectedIngredients, double priceRange) {
		ObservableList<MenuItem> allMenuItems = FXCollections.observableArrayList(MenuItem.getMenuItems());
		ObservableList<MenuItem> filteredMenuItems = FXCollections.observableArrayList();

		for (MenuItem menuItem : allMenuItems) {
			boolean matchesCategory = selectedCategories.isEmpty() || selectedCategories.contains(menuItem.getCategory().getName());
			boolean matchesIngredients = selectedIngredients.isEmpty() || selectedIngredients.stream().anyMatch(ingredient -> menuItem.getIngredients().contains(ingredient));
			boolean matchesPrice = menuItem.getPrice() <= priceRange;

			if (matchesCategory && matchesIngredients && matchesPrice) {
				filteredMenuItems.add(menuItem);
			}
		}

		menuitemstv.setItems(filteredMenuItems);
	}

	// Custom TableViewSkin to disable scroll bars
	private static class CustomTableViewSkin<T> extends TableViewSkin<T> {
		public CustomTableViewSkin(TableView<T> tableView) {
			super(tableView);

			// Find and remove scroll bars
			getChildren().removeIf(node -> node instanceof ScrollBar);
		}
	}
}