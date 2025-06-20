package src.models;

import src.database.DatabaseConnection;

import java.util.ArrayList;

public class Role {
	private static ArrayList<String> Fonctionlist = new ArrayList<String>();
	private String fonctionname;
	private Boolean vieworderPanel;
	private Boolean neworderPanel;
	private Boolean expensesPanel;
	private Boolean settingsPanel;
	private Boolean reportsPanel;
	private Boolean menuitemsPanel;

	public Role() {

	}

	public Role(String fonctionname, Boolean vieworderPanel, Boolean neworderPanel, Boolean expensesPanel, Boolean settingsPanel, Boolean reportsPanel, Boolean menuitemsPanel) {
		setFonction(fonctionname);
		setPanels(vieworderPanel, neworderPanel, expensesPanel, settingsPanel, reportsPanel, menuitemsPanel);
	}

	private void setPanels(Boolean vieworderPanel, Boolean neworderPanel, Boolean expensesPanel, Boolean settingsPanel, Boolean reportsPanel, Boolean menuitemsPanel) {
		this.vieworderPanel = vieworderPanel;
		this.neworderPanel = neworderPanel;
		this.expensesPanel = expensesPanel;
		this.settingsPanel = settingsPanel;
		this.reportsPanel = reportsPanel;
		this.menuitemsPanel = menuitemsPanel;
	}

	public static void loadFonction() {
		Fonctionlist = DatabaseConnection.getFonction();
	}

	private void setFonction(String fonctionname) {
		setFonctionname(fonctionname);
		Fonctionlist.add(fonctionname);
	}

	private void setFonctionname(String fonctionname) {
		this.fonctionname = fonctionname;
	}


	public static ArrayList<String> getFonctionlist() {
		return Fonctionlist;
	}

	public static int getFonctionlistlenght() {
		return Fonctionlist.size();
	}

	public static void deleteFonction(String fonctionname) {
		Fonctionlist.remove(fonctionname);
		User.DeleteByFonction(fonctionname);
		DatabaseConnection.deleteFonction(fonctionname);
	}

	public Boolean isEqual(String fonctionname) {
		for (int i = 0; i < Fonctionlist.size(); i++) {
			if (Fonctionlist.get(i).equals(fonctionname)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static void clearFonctionlist() {
		Fonctionlist.clear();
	}

}
