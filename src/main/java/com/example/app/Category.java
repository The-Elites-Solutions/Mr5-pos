package com.example.app;

import java.util.ArrayList;

public class Category {

	private String category;

	private static ArrayList<Category> categories = new ArrayList<Category>();

	public Category(String category) {
		setCategory(category);
		AddCategory(category);
	}

	public static boolean CategoryNotAvailable(String newCategory) {
		for (Category category : categories) {
			if (category.getCategory().equals(newCategory)) {
				return false;
			}
		}
		return true;
	}

	public static Category getCategory(String category) {
		for (Category cat : categories) {
			if (cat.getCategory().equals(category)) {
				return cat;
			}
		}
		return null;
	}

	private void AddCategory(String category) {
		for (Category cat : categories) {
			if (cat.getCategory().equals(category)) {
				return;
			}
		}
		categories.add(this);
	}

	public static ArrayList<String> getCategories() {
		ArrayList<String> categoryList = new ArrayList<String>();
		for (Category category : categories) {
			categoryList.add(category.getCategory());
		}
		return categoryList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return category;
	}
}
