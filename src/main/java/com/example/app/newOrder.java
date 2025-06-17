package com.example.app;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.ButtonType;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class newOrder {

	@FXML
	private Label Restaurant_Adress, Restaurant_PhoneNumber, Restaurant_WebLink, CustomerName, orderdetails,
			serverFullName, SubTotal, TaxCalc, Total, Points, RemainigAmount, customerphonenumber;

	@FXML
	private FlowPane orderItemsList;

	@FXML
	private TableView<String> clientpaymentsTv;

	@FXML
	private TableColumn<String, String> currenciesclmn, amountpaidclmn;

	@FXML
	ComboBox<String> CurrenciesCb;

	@FXML
	private Pane pane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	TextField paymenttxt;

	private Order order;
	private ArrayList<String> orderItemslist;
	private String clientid;
	private String addressid;
	private String ordertype;
	private int TableNumber;
	private Double amountpaid = 0.0;
	private Double amountremaining = 0.0;
	private GaussianBlur blur = new GaussianBlur(10);

	private static int buttonwidth = 150;
	private static int buttonheight = 90;

	@FXML
	protected void initialize() {
		System.out.println("New Order Screen Loaded " + Date.from(Instant.now()));
		order = new Order();
		serverFullName.setText(HomeScreen.getUserLoggedIn());
		SubTotal.setText("0.0");
		TaxCalc.setText(Order.getTax() + "");
		Total.setText("0.0");
		Points.setText("0.0");
		RemainigAmount.setText("0.0");
		orderdetails.setText("");
		CustomerName.setText("");
		clientid = null;
		addressid = "";
		ordertype = "";
		orderItemsList.getChildren().clear();
		orderItemslist = new ArrayList<>();
		amountpaid = 0.0;
		amountremaining = 0.0;

		CurrenciesCb.getItems().clear();
		CurrenciesCb.getItems().addAll(DollarRate.getCurrencyList());
		Scene scene = CurrenciesCb.getScene();
		initializeclientpaymentsTv();
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			stage.setOnCloseRequest(event -> {
				event.consume(); // Consume the event to prevent automatic closing
				confirmClose();
			});
		} else {
			System.out.println("Scene is null in initialize method");
		}
		Loadordertype();
	}

	private void initializeclientpaymentsTv() {
		currenciesclmn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
		amountpaidclmn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

		// Optionally, you can set column names (though you may have already done this in your FXML):
		currenciesclmn.setText("Currency");
		amountpaidclmn.setText("Amount Paid");

	}

	private void confirmClose() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Unsaved Changes");
		alert.setHeaderText("Do you want to save changes before closing?");
		alert.setContentText("Choose your option.");

		ButtonType saveButton = new ButtonType("Save");
		ButtonType discardButton = new ButtonType("Discard");
		ButtonType cancelButton = ButtonType.CANCEL;

		alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

		alert.showAndWait().ifPresent(buttonType -> {
			if (buttonType == saveButton) {
				System.exit(0);
			} else if (buttonType == discardButton) {
				try {
					String orderId = order.getOrderId();
					Order.DeleteOrder(orderId);
					System.exit(0);
				} catch (Exception e) {
					System.exit(0);
				}

			} else {
				// Cancel logic here
			}
		});
	}

	public void LoadClientSearch() {
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPrefSize(950, 750);
		anchorPane.setStyle("-fx-background-color: #fcfcfc;");

		Label label = new Label("Choose Client");
		label.setLayoutX(378);
		label.setLayoutY(14);
		label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 36px; -fx-font-weight: Bold Italic;");

		TextField searchField = new TextField();
		searchField.setLayoutX(253);
		searchField.setLayoutY(65);
		searchField.setPrefSize(459, 26);
		searchField.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		searchField.setPromptText("Search by name or phone number");

		Button searchButton = new Button("Search");
		searchButton.setLayoutX(371);
		searchButton.setLayoutY(108);
		searchButton.setPrefSize(106, 40);
		searchButton.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
				"-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");

		Button addClientButton = new Button("Add Client");
		addClientButton.setLayoutX(487);
		addClientButton.setLayoutY(108);
		addClientButton.setPrefSize(106, 40);
		addClientButton.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
				"-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");

		ImageView back = new ImageView("file:src/main/resources/com/example/app/Logo/blackback.png");
		back.setLayoutX(820);
		back.setLayoutY(640);
		back.setFitHeight(92);
		back.setFitWidth(124);
		back.onMouseClickedProperty().set(e -> {
			System.out.println("Button Pressed: Back " + Date.from(Instant.now()));
			orderdetails.setText("");
			pane.getChildren().clear();
			Loadordertype();
		});

		TableView<Customer> resultsListView = new TableView<>();
		resultsListView.setLayoutX(163);
		resultsListView.setLayoutY(157);
		resultsListView.setPrefSize(640, 540);

		TableColumn<Customer, Void> chooseBtnColumn = new TableColumn<>("");
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Client Name");
		TableColumn<Customer, String> phoneColumn = new TableColumn<>("Phone Number");

		chooseBtnColumn.setPrefWidth(40);
		nameColumn.setPrefWidth(300);
		phoneColumn.setPrefWidth(300);

		chooseBtnColumn.setResizable(false);
		nameColumn.setResizable(false);
		phoneColumn.setResizable(false);

		resultsListView.getColumns().addAll(chooseBtnColumn, nameColumn, phoneColumn);

		anchorPane.getChildren().addAll(label, searchField, searchButton, addClientButton, resultsListView, back);
		pane.getChildren().add(anchorPane);

		if (ordertype.equals("Delivery")) {
		} else {
			Button skipButton = new Button("Skip");
			skipButton.setLayoutX(163);
			skipButton.setLayoutY(702);
			skipButton.setPrefSize(85, 28);
			skipButton.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
					"-fx-font-family: 'Times New Roman'; -fx-font-size: 18px; -fx-font-weight: Bold Italic;");
			// Set actions for buttons
			skipButton.setOnAction(e -> {
				System.out.println("Button Pressed: Skip " + new Date());
				if (ordertype.equals("Take Out")) {
					order = new Order(User.getEmployeeId(HomeScreen.getUserLoggedIn()));
				} else if (ordertype.equals("Dine In")) {
					order = new Order(User.getEmployeeId(HomeScreen.getUserLoggedIn()), TableNumber);
				}
				pane.getChildren().clear();
				loadButtons(Category.getCategories());
			});
			anchorPane.getChildren().add(skipButton);
		}
		searchButton.setOnAction(e -> {
			System.out.println("Button Pressed: Search " + new Date());
			resultsListView.getItems().clear();
			ArrayList<Customer> customers = Customer.getCustomerFromDb();
			SearchforClient(searchField.getText(), resultsListView, nameColumn, phoneColumn, customers);
		});

		addClientButton.setOnAction(e -> {
			System.out.println("Button Pressed: Add Client " + new Date());
			AddCustomer();
		});


		//add a click listenner on the enter key
		searchField.setOnKeyPressed(e -> {
			if (e.getCode().toString().equals("ENTER")) {
				System.out.println("Button Pressed: Search " + new Date());
				resultsListView.getItems().clear();
				ArrayList<Customer> customers = Customer.getCustomerFromDb();
				SearchforClient(searchField.getText(), resultsListView, nameColumn, phoneColumn, customers);
			}
		});

		initializeTableColumns(chooseBtnColumn);

	}

	private void initializeTableColumns(TableColumn<Customer, Void> chooseBtnColumn) {

		chooseBtnColumn.setCellFactory(param -> new TableCell<>() {
			private final Button chooseButton = new Button("+");
			private ComboBox<String> comboBox;
			String selectedAddress;

			{
				chooseButton.setOnAction(event -> {
					Customer customer = getTableView().getItems().get(getIndex());
					clientid = customer.getCustomerId();
					Pane blockingpane = new Pane();
					blockingpane.setPrefSize(1376, 810);
					blockingpane.setStyle("-fx-background-color: transparent;");
					blockingpane.setEffect(blur);
					blockingpane.setLayoutX(0);
					blockingpane.setLayoutY(0);
					anchorPane.getChildren().add(blockingpane);
					AnchorPane addressPane = new AnchorPane();
					addressPane.setPrefSize(400, 300);
					addressPane.setLayoutX(500);
					addressPane.setLayoutY(250);
					addressPane.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; -fx-border-width: 5px;");
					Label label = new Label("Choose Address");
					label.setLayoutX(75);
					label.setLayoutY(14);
					label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 36px; -fx-font-weight: Bold Italic;");
					ComboBox<String> addressComboBox = new ComboBox<>();
					addressComboBox.setLayoutX(100);
					addressComboBox.setLayoutY(100);
					addressComboBox.setPrefSize(200, 26);
					addressComboBox.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px;" +
							" -fx-font-weight: Bold Italic;");
					addressComboBox.setPromptText("Choose Address");
					ArrayList<String> addresses = new ArrayList<>();
					for (String addressid : Customer.getAddress(clientid)) {
						addresses.add(Address.getAddresstxt(addressid));
					}
					addressComboBox.getItems().addAll(addresses);
					addressComboBox.setOnAction(e -> {
						selectedAddress = addressComboBox.getValue();
					});
					Button chooseAddressButton = new Button("Choose Address");
					chooseAddressButton.setLayoutX(210);
					chooseAddressButton.setLayoutY(200);
					chooseAddressButton.setPrefSize(100, 40);
					chooseAddressButton.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
							"-fx-font-family: 'Times New Roman'; -fx-font-size: 18px; -fx-font-weight: Bold Italic;");
					chooseAddressButton.setOnAction(e -> {
						System.out.println("Button Pressed: Choose Address " + new Date());
						addressid = Address.getAddressId(selectedAddress);
						System.out.println("Address ID: " + addressid);
						pane.getChildren().clear();
						anchorPane.getChildren().remove(blockingpane);
						anchorPane.getChildren().remove(addressPane);
						CreateOrder();
						loadButtons(Category.getCategories());
					});

					Button cancel = new Button("Cancel");
					cancel.setLayoutX(90);
					cancel.setLayoutY(200);
					cancel.setPrefSize(100, 40);
					cancel.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
							"-fx-font-family: 'Times New Roman'; -fx-font-size: 18px; -fx-font-weight: Bold Italic;");
					cancel.setOnAction(e -> {
						System.out.println("Button Pressed: Cancel " + new Date());
						pane.getChildren().clear();
						anchorPane.getChildren().remove(blockingpane);
						anchorPane.getChildren().remove(addressPane);
						LoadClientSearch();
					});
					addressPane.getChildren().addAll(label, addressComboBox, chooseAddressButton, cancel);
					anchorPane.getChildren().add(addressPane);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(chooseButton);
				}
			}
		});
	}


	private void SearchforClient(String text, TableView<Customer> resultsListView,
	                             TableColumn<Customer, String> nameColumn, TableColumn<Customer,
			String> phoneColumn, ArrayList<Customer> customers) {
		int count = 0;
		resultsListView.getItems().clear();

		// Set cell factories
		nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
		phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));

		// Filter and add customers to the list
		for (Customer customer : customers) {
			String fullName = customer.getFullName();
			String phone = customer.getPhone();

			// Include customer if search is empty or matches name/phone
			if (text.isEmpty() || fullName.toLowerCase().contains(text.toLowerCase()) || phone.contains(text)) {
				resultsListView.getItems().add(customer);
			}
		}
	}

	private void CreateOrder() {
		if (ordertype.equals("Delivery")) {
			order = new Order(clientid, User.getEmployeeId(HomeScreen.getUserLoggedIn()), addressid);
		} else if (ordertype.equals("Take Out")) {
			order = new Order(clientid, User.getEmployeeId(HomeScreen.getUserLoggedIn()));
			orderdetails.setText("Take Away");
		} else {
			order = new Order(clientid, User.getEmployeeId(HomeScreen.getUserLoggedIn()), TableNumber);
		}
	}

	private void Loadordertype() {
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPrefSize(950, 750);
		anchorPane.setStyle("-fx-background-color: #252525;");
		Label label = new Label("Choose Order Type:");
		label.setLayoutX(333);
		label.setLayoutY(14);
		label.setStyle("-fx-font-family: 'Times New Roman' ; -fx-font-size: 36px;" +
				" -fx-font-weight: Bold Italic; -fx-text-fill: #fcfcfc;");
		Button dinein = new Button("Dine In");
		dinein.setLayoutX(306);
		dinein.setLayoutY(108);
		dinein.setPrefSize(106, 40);
		dinein.setStyle("-fx-background-color: #fcfcfc; " +
				" -fx-font-family: 'Times New Roman' ; -fx-font-size: 18px; -fx-font-weight: Bold Italic; -fx-text-fill: #252525;");
		Button takeout = new Button("Take Out");
		takeout.setLayoutX(422);
		takeout.setLayoutY(108);
		takeout.setPrefSize(106, 40);
		takeout.setStyle("-fx-background-color: #fcfcfc; " +
				" -fx-font-family: 'Times New Roman' ; -fx-font-size: 18px; -fx-font-weight: Bold Italic; -fx-text-fill: #252525;");
		Button delivery = new Button("Delivery");
		delivery.setLayoutX(538);
		delivery.setLayoutY(108);
		delivery.setPrefSize(106, 40);
		delivery.setStyle("-fx-background-color: #fcfcfc; " +
				" -fx-font-family: 'Times New Roman' ; -fx-font-size: 18px; -fx-font-weight: Bold Italic; -fx-text-fill: #252525;");
		ImageView imageView = new ImageView("file:src/main/resources/com/example/app/Logo/whiteback.png");
		imageView.setLayoutX(820);
		imageView.setLayoutY(640);
		imageView.setFitHeight(92);
		imageView.setFitWidth(124);
		imageView.onMouseClickedProperty().set(e -> {
			System.out.println("Button Pressed: Back " + Date.from(Instant.now()));
			btnBackOnAction();
		});
		anchorPane.getChildren().addAll(label, dinein, takeout, delivery, imageView);
		pane.getChildren().add(anchorPane);
		dinein.setOnAction(e -> {
			System.out.println("Button Pressed: Dine In " + Date.from(Instant.now()));
			ordertype = "Dine In";
			pane.getChildren().clear();
			loadAvailableTables();
		});
		takeout.setOnAction(e -> {
			System.out.println("Button Pressed: Take Out " + Date.from(Instant.now()));
			ordertype = "Take Out";
			pane.getChildren().clear();
			LoadClientSearch();
		});

		delivery.setOnAction(e -> {
			Date date = new Date();
			/*
			if (date.getDay() != 3) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Today is Not a Delivery Day");
				alert.setContentText("Come back on Thursday.");
				alert.showAndWait();
				return;
				}
				*/
			System.out.println("Button Pressed: Delivery " + Date.from(Instant.now()));
			ordertype = "Delivery";
			pane.getChildren().clear();
			LoadClientSearch();

		});
	}

	private void loadAvailableTables() {
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPrefSize(950, 750);
		anchorPane.setStyle("-fx-background-color: #fcfcfc;");
		Label label = new Label("Choose Table");
		label.setLayoutX(378);
		label.setLayoutY(14);
		label.setStyle("-fx-font-family: 'Times New Roman' ; -fx-font-size: 36px; -fx-font-weight: Bold Italic;");
		FlowPane flowPane = new FlowPane();
		flowPane.setLayoutX(0);
		flowPane.setLayoutY(108);
		flowPane.setPrefSize(950, 642);
		ArrayList<Integer> tables = Table.getTables();
		for (int table : tables) {
			if (Table.isAvailable(table)) {
				Button button = new Button("Table: " + table);
				button.setPrefSize(buttonwidth, buttonheight);
				button.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
						"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
				button.setOnAction(e -> {
					int tableNumber = table;
					System.out.println("Button Pressed: Table " + tableNumber + " " + Date.from(Instant.now()));
					orderdetails.setText("DINE IN: " + "Table: " + tableNumber);
					TableNumber = tableNumber;
					pane.getChildren().clear();
					LoadClientSearch();
				});
				flowPane.getChildren().add(button);
				System.out.println("Table: " + table + "Loaded " + Date.from(Instant.now()));
			}
		}
		anchorPane.getChildren().addAll(label, flowPane);
		pane.getChildren().add(anchorPane);
	}

	@FXML
	protected void btnBackOnAction() {

		// When the "Cancel" button is pressed
		System.out.println("Back Button Pressed " + Date.from(Instant.now()));
		try {
			// Load the PowerOff.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("HomeScreen.fxml"));
			Parent root = (Parent) fxmlLoader.load();

			// Create a new scene and stage for the "Power Off" window
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();

			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setResizable(false);
			newStage.setScene(newScene);
			newStage.setTitle("Home Screen");

			// Show the "Power Off" window
			newStage.show();

			Stage homeStage = (Stage) pane.getScene().getWindow();
			homeStage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadButtons(ArrayList<String> categories) {
		refreshReceiptHeader();
		refreshReceiptFooter();
		for (String category : categories) {
			Button button = new Button(category);
			button.setOnAction(event -> {
				pane.getChildren().clear();
				System.out.println("Button Pressed: " + category + " " + Date.from(Instant.now()));
				for (String item : MenuItem.getItems(category)) {
					Button itemButton = new Button(item);
					itemButton.setPrefSize(buttonwidth, buttonheight);
					itemButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
							"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
					itemButton.setOnAction(e -> {
						System.out.println("Button Pressed: " + item + " " + Date.from(Instant.now()));
						pane.getChildren().clear();
						ArrayList<String> ingredientsRemoved = new ArrayList<>();
						// Load the items ingredients
						for (String ingredient : DatabaseConnection.LoadIngredients(item)) {
							Button ingredientButton = new Button(ingredient);
							ingredientButton.setPrefSize(buttonwidth, buttonheight);
							ingredientButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
									"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
							ingredientButton.setOnAction(e2 -> {
								System.out.println("Button Pressed: " + ingredient + " " + Date.from(Instant.now()));
								if (ingredientsRemoved.contains(ingredient)) {
									System.out.println("Ingredient: " + ingredient + " Undo " + Date.from(Instant.now()));
									ingredientButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
											"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
									ingredientsRemoved.remove(ingredient);
								} else {
									System.out.println("Ingredient: " + ingredient + "Added " + Date.from(Instant.now()));
									ingredientButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: red; " +
											"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
									ingredientsRemoved.add(ingredient);
								}
							});
							pane.getChildren().add(ingredientButton);
							System.out.println("Ingredient: " + ingredient + "Loaded " + Date.from(Instant.now()));
						}
						Button backButton = new Button("Back");
						backButton.setOnAction(e2 -> {
							pane.getChildren().clear();
							loadButtons(Category.getCategories());
						});
						backButton.setPrefSize(buttonwidth, buttonheight);
						backButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
								"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
						pane.getChildren().add(backButton);
						Button Done = new Button("Done");
						Done.setOnAction(e2 -> {
							System.out.println("Button Pressed: Done " + Date.from(Instant.now()));
							String menuitemid = MenuItem.getMenuItemId(item);
							OrderItem orderItem = new OrderItem(item, menuitemid, ingredientsRemoved, order.getOrderId(), 1);
							orderItemslist.add(orderItem.getOrderItemId());
							order.AddOrderItem(orderItem.getOrderItemId());
							addToReceipt(orderItem);
							pane.getChildren().clear();
							order.setTotal();
							order.setLoyaltyPoints();
							refreshReceiptFooter();
							loadButtons(Category.getCategories());
						});
						Done.setPrefSize(buttonwidth, buttonheight);
						Done.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
								"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
						pane.getChildren().add(Done);
					});
					pane.getChildren().add(itemButton);
					System.out.println("Item: " + item + "Loaded " + Date.from(Instant.now()));
				}
				Button backButton = new Button("Back");
				backButton.setOnAction(e -> {
					pane.getChildren().clear();
					loadButtons(Category.getCategories());
				});
				backButton.setPrefSize(buttonwidth, buttonheight);
				backButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
						"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
				pane.getChildren().add(backButton);
			});
			button.setPrefSize(buttonwidth, buttonheight);
			button.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
					"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
			pane.getChildren().add(button);
			System.out.println("Category: " + category + "Loaded " + Date.from(Instant.now()));
		}
		Button RefreshButton = new Button("Refresh");
		RefreshButton.setOnAction(e -> {
			pane.getChildren().clear();
			loadButtons(Category.getCategories());
			refreshReceiptFooter();
		});
		RefreshButton.setPrefSize(buttonwidth, buttonheight);
		RefreshButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
				"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
		pane.getChildren().add(RefreshButton);
		Button closeButton = new Button("Close Order");
		closeButton.setOnAction(e -> {
			System.out.println("Button Pressed: Close Order " + Date.from(Instant.now()));
			Order.DeleteOrder(order.getOrderId());
			pane.getChildren().clear();
			initialize();
		});
		closeButton.setPrefSize(buttonwidth, buttonheight);
		closeButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
				"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
		Button printButton = new Button("Print");
		printButton.setOnAction(e -> {
			System.out.println("Button Pressed: Print " + Date.from(Instant.now()));
		});
		printButton.setPrefSize(buttonwidth, buttonheight);
		printButton.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
				"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
		pane.getChildren().add(printButton);
		Button CompleteOrder = new Button("Complete\n Order");
		CompleteOrder.setOnAction(e -> {
			System.out.println("Button Pressed: Complete Order " + Date.from(Instant.now()));
			if (ConfirmOrder()) {
				pane.getChildren().clear();
				order = null;
				initialize();
			}

		});
		CompleteOrder.setPrefSize(buttonwidth, buttonheight);
		CompleteOrder.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; " +
				"-fx-border-width: 5px; -fx-font-family: 'Times New Roman' ; -fx-font-size: 20px; -fx-font-weight: bold;");
		pane.getChildren().add(CompleteOrder);
		pane.getChildren().add(closeButton);
	}

	private void addToReceipt(OrderItem orderItem) {
		HBox hbox = new HBox();
		hbox.setSpacing(0);
		hbox.setPrefSize(300, 30);
		hbox.setId(orderItem.getOrderItemId());

		Label name = new Label(orderItem.getItemName());
		name.setPrefSize(100, 30);
		name.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");


		Label price = new Label(String.valueOf(orderItem.getPrice()) + " $");
		price.setPrefSize(68, 30);
		price.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");

		TreeView<String> treeView = new TreeView<>();
		TreeItem<String> root = new TreeItem<>("Removed Ingredients");
		treeView.setRoot(root);
		treeView.setShowRoot(false);

		List<String> ingredients = orderItem.getRemovedIngredients();
		if (ingredients != null) {
			for (String ingredient : ingredients) {
				TreeItem<String> item = new TreeItem<>(ingredient);
				root.getChildren().add(item);
			}
		}

		treeView.setFixedCellSize(24); // Set a fixed cell size for each item
		int maxVisibleItems = 5; // Maximum number of items visible before scrolling
		treeView.setPrefHeight(Math.min(root.getChildren().size(), maxVisibleItems) * treeView.getFixedCellSize());


		TitledPane titledPane = new TitledPane("Removed Ingredients", treeView);
		titledPane.setCollapsible(true);
		titledPane.setExpanded(false);  // Start in a collapsed state

		VBox container = new VBox(hbox, titledPane);  // Using VBox to stack HBox and TitledPane
		Pane quantityPane = new Pane();
		quantityPane.setPrefSize(132, 30);
		TextField quantitytf = new TextField();
		quantitytf.setPrefSize(30, 30);
		quantitytf.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		quantitytf.setText(String.valueOf(orderItem.getQuantity()));
		quantitytf.setEditable(false);
		quantitytf.setLayoutX(47);
		quantitytf.setLayoutY(0);
		//set quantity when textfield is edited
		quantitytf.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				quantitytf.setText(newValue.replaceAll("[^\\d]", ""));
			}
			orderItem.setQuantity(Integer.parseInt(newValue));
			OrderItem.setQuantityDb(orderItem.getOrderItemId(), orderItem.getQuantity());
			OrderItem.setTotalPriceDb(orderItem.getOrderItemId());
			Order.updateTotal(order.getOrderId());
			refreshReceiptFooter();
			price.setText(String.valueOf(orderItem.getTotalPrice(orderItem.getOrderItemId())) + " $");
		});

		Button minus = new Button("-");
		minus.setPrefSize(30, 30);
		minus.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc;" +
				" -fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		minus.setLayoutX(0);
		minus.setLayoutY(0);
		minus.setOnAction(e -> {
			int quantity = Integer.parseInt(quantitytf.getText());
			if (quantity > 1) {
				quantity--;
				quantitytf.setText(String.valueOf(quantity));
				orderItem.setQuantity(quantity);
				OrderItem.setQuantityDb(orderItem.getOrderItemId(), quantity);
				OrderItem.setTotalPriceDb(orderItem.getOrderItemId());
				Order.updateTotal(order.getOrderId());
				price.setText(String.valueOf(orderItem.getTotalPrice(orderItem.getOrderItemId())) + " $");
			} else {
				// Make sure the right element is being targeted for removal
				orderItemsList.getChildren().remove(container);
				order.DeleteOrderItem(orderItem.getOrderItemId());
			}
		});

		Button plus = new Button("+");
		plus.setPrefSize(30, 30);
		plus.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc;" +
				" -fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		plus.setLayoutX(86);
		plus.setLayoutY(0);
		plus.setOnAction(e -> {
			System.out.println("Button Pressed: + " + Date.from(Instant.now()));
			int quantity = Integer.parseInt(quantitytf.getText());
			quantity++;
			quantitytf.setText(String.valueOf(quantity));
			orderItem.setQuantity(quantity);
			OrderItem.setQuantityDb(orderItem.getOrderItemId(), quantity);
			OrderItem.setTotalPriceDb(orderItem.getOrderItemId());
			Order.updateTotal(order.getOrderId());
			refreshReceiptFooter();
			price.setText(String.valueOf(orderItem.getTotalPrice(orderItem.getOrderItemId())) + " $");
		});

		hbox.getChildren().addAll(name, quantityPane, price);
		quantityPane.getChildren().addAll(minus, quantitytf, plus);
		orderItemsList.getChildren().add(container);
	}


	private void refreshReceiptHeader() {
		// Refresh the receipt header
		if (clientid != null) {
			CustomerName.setText(Customer.getCustomerName(clientid));
			customerphonenumber.setText(Customer.getCustomerPhone(clientid));
		}
		if (ordertype.equals("Delivery")) {
			orderdetails.setText("DELIVERY: " + Address.getAddresstxt(addressid));
		} else if (ordertype.equals("Take Out")) {
			orderdetails.setText("TAKE AWAY");
		} else {
			orderdetails.setText("DINE IN: " + "Table: " + TableNumber);
		}
	}

	public Boolean ConfirmOrder() {
		if (amountpaid < order.getTotal()) {
			return true;
		} else {
			Order.setIsPaid(order.getOrderId());
			return true;
		}
	}

	@FXML
	public void btnAddPaymentOnAction() {
		String payedamount = paymenttxt.getText();
		Double payed = Double.parseDouble(payedamount);
		String currency = CurrenciesCb.getValue();

		if (currency == null || payedamount.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Please fill in all fields");
			alert.showAndWait();
		} else {
			// Calculate remaining amount and update TableView
			amountpaid += payed / DatabaseConnection.getCurrencyValue(currency);
			amountremaining = ((order.getTotal()) * (order.getTax() / 100)) + order.getTotal() - (amountpaid / DatabaseConnection.getCurrencyValue(currency));

			// Create a string representing the payment with currency and amount
			String paymentDetails = currency + " " + payedamount;

			// Update TableView
			clientpaymentsTv.getItems().add(paymentDetails);

			// Clear payment text field and update order
			paymenttxt.clear();
			order.addCurency(DatabaseConnection.getCurrencyId(currency));
			refreshReceiptFooter();
		}
	}

	@FXML
	public void btnClearRemainingOnAction() {
		if (amountremaining > 0) {
			amountpaid = (order.getTotal() * (order.getTax() / 100)) + (order.getTotal());
			amountremaining = 0.0;
			refreshReceiptFooter();
		}
	}

	public void refreshReceiptFooter() {
		SubTotal.setText("");
		TaxCalc.setText("");
		Total.setText("");
		Points.setText("");
		RemainigAmount.setText("");
		amountremaining = ((order.getTotal()) * (order.getTax() / 100)) + (order.getTotal()) - amountpaid;
		RemainigAmount.setText(String.valueOf(-amountremaining));
		SubTotal.setText(String.valueOf(order.getTotal()));
		TaxCalc.setText(String.valueOf(order.getTax()));
		Total.setText(String.valueOf(((order.getTotal()) * (order.getTax() / 100)) + (order.getTotal())));
		Points.setText(String.valueOf(order.getLoyaltyPoints()));
	}

	private void AddCustomer() {
		Pane blockingpane = new Pane();
		blockingpane.setPrefSize(1376, 810);
		blockingpane.setEffect(blur);
		blockingpane.setLayoutX(0);
		blockingpane.setLayoutY(0);
		anchorPane.getChildren().add(blockingpane);

		AnchorPane CustomerPane = new AnchorPane();
		CustomerPane.setPrefSize(630, 350);
		CustomerPane.setLayoutX(200);
		CustomerPane.setLayoutY(190);
		CustomerPane.setStyle("-fx-background-color: #fcfcfc;-fx-border-color: #252525; -fx-border-width: 5px;");

		Label label = new Label("New Customer");
		label.setPrefSize(140, 46);
		label.setLayoutX(100);
		label.setLayoutY(51);
		label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 22px; -fx-font-weight: Bold Italic;");

		Button AddCustomer = new Button("Add Customer");
		AddCustomer.setPrefSize(120, 50);
		AddCustomer.setLayoutX(200);
		AddCustomer.setLayoutY(260);
		AddCustomer.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
				"-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");

		Button cancel = new Button("Cancel");
		cancel.setPrefSize(120, 50);
		cancel.setLayoutX(335);
		cancel.setLayoutY(260);
		cancel.setStyle("-fx-background-color: #252525; -fx-text-fill: #fcfcfc; " +
				"-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		cancel.setOnAction(e -> {
			System.out.println("Button Pressed: Cancel " + Date.from(Instant.now()));
			anchorPane.getChildren().remove(blockingpane);
			anchorPane.getChildren().remove(CustomerPane);
		});

		TextField FirstName = new TextField();
		FirstName.setPrefSize(114, 26);
		FirstName.setLayoutX(45);
		FirstName.setLayoutY(127);
		FirstName.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		FirstName.setPromptText("First Name");

		TextField LastName = new TextField();
		LastName.setPrefSize(114, 26);
		LastName.setLayoutX(181);
		LastName.setLayoutY(127);
		LastName.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		LastName.setPromptText("Last Name");

		TextField PhoneNumber = new TextField();
		PhoneNumber.setPrefSize(250, 26);
		PhoneNumber.setLayoutX(45);
		PhoneNumber.setLayoutY(161);
		PhoneNumber.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		PhoneNumber.setPromptText("Phone Number");

		TextField Email = new TextField();
		Email.setPrefSize(250, 26);
		Email.setLayoutX(45);
		Email.setLayoutY(196);
		Email.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		Email.setPromptText("Email");

		Label AddressLabel = new Label("Address");
		label.setPrefSize(140, 46);
		label.setLayoutX(354);
		label.setLayoutY(53);
		label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 24px; -fx-font-weight: Bold Italic;");

		TextField Street = new TextField();
		Street.setPrefSize(250, 26);
		Street.setLayoutX(354);
		Street.setLayoutY(91);
		Street.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		Street.setPromptText("Street");

		TextField City = new TextField();
		City.setPrefSize(250, 26);
		City.setLayoutX(354);
		City.setLayoutY(127);
		City.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		City.setPromptText("City");

		TextField Postalcode = new TextField();
		Postalcode.setPrefSize(250, 26);
		Postalcode.setLayoutX(354);
		Postalcode.setLayoutY(161);
		Postalcode.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		Postalcode.setPromptText("Postal code");

		ComboBox<String> Country = new ComboBox<>();
		Country.setPrefSize(250, 26);
		Country.setLayoutX(354);
		Country.setLayoutY(196);
		Country.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-font-weight: Bold Italic;");
		Country.setPromptText("Country");
		Country.getItems().addAll(
				"Afghanistan", "Albania", "Algeria", "Andorra", "Angola",
				"Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria",
				"Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
				"Belarus", "Belgium", "Belize", "Benin", "Bhutan",
				"Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei",
				"Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia",
				"Cameroon", "Canada", "Central African Republic", "Chad", "Chile",
				"China", "Colombia", "Comoros", "Congo, Democratic Republic of the", "Congo, Republic of the",
				"Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
				"Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor (Timor-Leste)",
				"Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
				"Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland",
				"France", "Gabon", "Gambia, The", "Georgia", "Germany",
				"Ghana", "Greece", "Grenada", "Guatemala", "Guinea",
				"Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary",
				"Iceland", "India", "Indonesia", "Iran", "Iraq",
				"Ireland", "Israel", "Italy", "Jamaica", "Japan",
				"Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North",
				"Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos",
				"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
				"Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi",
				"Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
				"Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova",
				"Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique",
				"Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands",
				"New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia",
				"Norway", "Oman", "Pakistan", "Palau", "Panama",
				"Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland",
				"Portugal", "Qatar", "Romania", "Russia", "Rwanda",
				"Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
				"Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
				"Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
				"Somalia", "South Africa", "Spain", "Sri Lanka", "Sudan",
				"Sudan, South", "Suriname", "Sweden", "Switzerland", "Syria",
				"Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo",
				"Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
				"Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
				"United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City",
				"Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
		);
		Country.setValue(LocationFinder.checkLocation());

		AddCustomer.setOnAction(e -> {
			System.out.println("Button Pressed: Add Customer " + Date.from(Instant.now()));
			if (FirstName.getText().isEmpty() || LastName.getText().isEmpty() || PhoneNumber.getText().isEmpty() ||
					Email.getText().isEmpty() || Street.getText().isEmpty() || City.getText().isEmpty() || Postalcode.getText().isEmpty()) {
				if (FirstName.getText().isEmpty() || PhoneNumber.getText().isEmpty() || Street.getText().isEmpty() ||
						City.getText().isEmpty() || Postalcode.getText().isEmpty()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error: Missing Information");
					alert.setContentText("Please fill in all required fields.");
					alert.showAndWait();
				} else {
					Email.setText("N/A");
					LastName.setText("N/A");
					Address address = new Address(Street.getText(), City.getText(), Postalcode.getText(), Country.getValue());
					String addressid = address.getAddressId();
					Customer customer = new Customer(FirstName.getText(), LastName.getText(), PhoneNumber.getText(), Email.getText(), addressid);
					anchorPane.getChildren().remove(blockingpane);
					anchorPane.getChildren().remove(CustomerPane);
				}
			} else {
				Address address = new Address(Street.getText(), City.getText(), Postalcode.getText(), Country.getValue());
				String addressid = address.getAddressId();
				Customer customer = new Customer(FirstName.getText(), LastName.getText(), PhoneNumber.getText(), Email.getText(), addressid);
				anchorPane.getChildren().remove(blockingpane);
				anchorPane.getChildren().remove(CustomerPane);
			}
		});

		CustomerPane.getChildren().addAll(label, FirstName, LastName, PhoneNumber, Email, Street, City, Postalcode, Country, AddCustomer, cancel);
		anchorPane.getChildren().add(CustomerPane);
	}
}