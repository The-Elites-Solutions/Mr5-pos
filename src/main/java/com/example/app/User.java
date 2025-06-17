package com.example.app;

import java.util.ArrayList;
import java.util.Date;

public class User {
	private String EmployeeId;
	private String username;
	private String password;
	private String fonction;
	private static ArrayList<User> Userlist = new ArrayList<User>();

	public User() {

	}

	public User(String username, String password, String fonction) {
		setEmployeeId();
		setUsername(username);
		setPassword(password);
		setFonction(fonction);
		Userlist.add(this);
	}

	public User(String EmployeeId, String username, String password, String fonction) {
	setEmployeeId(EmployeeId);
	setUsername(username);
	setPassword(password);
	setFonction(fonction);
	Userlist.add(this);
	}

	public static void deleteUserbyFunction(String fonctionname) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getFonction().equals(fonctionname)) {
				Userlist.remove(i);
				DatabaseConnection.deleteUser(Userlist.get(i).getUsername());
			}
		}
	}

	private void setEmployeeId() {
		Date date = new Date();
		this.EmployeeId = "A" + date.getTime();
	}

	private void setEmployeeId(String EmployeeId) {
		this.EmployeeId = EmployeeId;
	}

	private void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	private void setPassword(String password) {
		this.password = password.toLowerCase();
	}

	private void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFonction() {
		return fonction;
	}

	public String getEmployeeId() {
		return EmployeeId;
	}

	public static String getUsername(String EmployeeId) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getEmployeeId().equals(EmployeeId)) {
				return Userlist.get(i).getUsername();
			}
		}
		return "";
	}

	public static ArrayList<User> getUserlist() {
		return Userlist;
	}

	public static ArrayList<String> getUsernamelist() {
		ArrayList<String> Usernamelist = new ArrayList<String>();
		for (int i = 0; i < Userlist.size(); i++) {
			Usernamelist.add(Userlist.get(i).getUsername());
		}
		return Usernamelist;
	}

	public static Boolean isAvailable(String username) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	public static String getPasswordbyuser(String username) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				return Userlist.get(i).getPassword();
			}
		}
		return "";
	}

	public static String getFonctionbyuser(String username) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				return Userlist.get(i).getFonction();
			}
		}
		return "";
	}

	public static String getEmployeeId(String username) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				return Userlist.get(i).getEmployeeId();
			}
		}
		return "";
	}

	public static void ChangePassword(String username, String password) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				Userlist.get(i).setPassword(password);
				DatabaseConnection.changePassword(username, password);
			}
		}
	}

	public static void ChangeFonction(String username, String fonction) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				Userlist.get(i).setFonction(fonction);
				DatabaseConnection.changeFonction(username, fonction);
			}
		}
	}

	public static void DeleteByFonction(String fonction) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getFonction().equals(fonction)) {
				User user = Userlist.get(i);
				Userlist.remove(i);
				DatabaseConnection.deleteUser(user.getUsername());
			}
		}
	}

	public static void deleteUser(String username) {
		for (int i = 0; i < Userlist.size(); i++) {
			if (Userlist.get(i).getUsername().equals(username)) {
				Userlist.remove(i);
				DatabaseConnection.deleteUser(username);
			}
		}
	}

	public static void printUserList() {
		for (int i = 0; i < Userlist.size(); i++) {
			System.out.println("{Username:" + Userlist.get(i).getUsername() +
					"; Password:" + Userlist.get(i).getPassword() +
					"; Fonction:" + Userlist.get(i).getFonction() + "}");
		}
	}
}