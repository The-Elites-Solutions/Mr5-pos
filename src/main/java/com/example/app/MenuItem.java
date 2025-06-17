package com.example.app;

import java.util.ArrayList;
import java.util.Date;

public class MenuItem {

	private String MenuItemId;
	private String name;
	private String description;
	private Category category;
	private double price;
	private ArrayList<String> ingredients;

	private static ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

	public MenuItem(String name, String description, Category category, double price, ArrayList<String> ingredients) {
		setMenuItemId();
		setName(name);
		setDescription(description);
		setCategory(category);
		setPrice(price);
		setIngredients(ingredients);
		addMenuItem(this);
	}

	public MenuItem(String MenuItemId, String name, String description, Category category, double price, ArrayList<String> ingredients) {
		setMenuItemId(MenuItemId);
		setName(name);
		setDescription(description);
		setCategory(category);
		setPrice(price);
		setIngredients(ingredients);
		addMenuItem(this);
	}

	private void setMenuItemId() {
		Date date = new Date();
		this.MenuItemId = "M" + date.getTime();
	}

	private void setMenuItemId(String MenuItemId) {this.MenuItemId = MenuItemId;}

	public static MenuItem[] getAllMenuItems() {
		MenuItem[] items = new MenuItem[menuItems.size()];
		for (int i = 0; i < menuItems.size(); i++) {
			items[i] = menuItems.get(i);
		}
		return items;
	}

	public static void removeMenuItem(MenuItem item) {
		menuItems.remove(item);
		System.out.println("Menu Item Removed");
	}

	public static String[] getItems(String category) {
		ArrayList<String> items = new ArrayList<String>();
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getCategory().getName().equals(category)) {
				items.add(menuItem.getName());
			}
		}
		String[] itemsArray = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			itemsArray[i] = items.get(i);
		}
		return itemsArray;
	}

	private void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private void setCategory(Category category) {
		this.category = category;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	private void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}

	public void editIngredients(ArrayList<String> ingredients) {
		ingredients.clear();
		this.ingredients = ingredients;
	}

	private void addMenuItem(MenuItem menuItem) {
		for (MenuItem item : menuItems) {
			if (item.getName().equals(menuItem.getName())
					&& item.getCategory().equals(menuItem.getCategory())) {
				return;
			}
		}
		menuItems.add(menuItem);
	}

	public static String getMenuItemId(String name) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getName().equals(name)) {
				return menuItem.MenuItemId;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public String getMenuItemId() {
		return MenuItemId;
	}

	public static double getPrice(String menuItemId) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.MenuItemId.equals(menuItemId)) {
				return menuItem.price;
			}
		}
		return 0;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public static ArrayList<MenuItem> getMenuItems() {
		return menuItems;
	}

	public static MenuItem getMenuItem(String name) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getName().equals(name)) {
				return menuItem;
			}
		}
		return null;
	}

	public static Boolean MenuItemExists(String name, Category category) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getName().equals(name) && menuItem.getCategory().equals(category)) {
				return true;
			}
		}
		return false;
	}

	public void setCategory(String category) {
		if (Category.CategoryNotAvailable(category)) {
			this.category = new Category(category);
		} else {
			this.category = Category.getCategory(category);
		}
	}

	public static ArrayList<String> getAllIngredients() {
		ArrayList<String> ingredients = new ArrayList<String>();
		for (MenuItem menuItem : menuItems) {
			for (String ingredient : menuItem.getIngredients()) {
				if (!ingredients.contains(ingredient)) {
					ingredients.add(ingredient);
				}
			}
		}
		return ingredients;
	}


}