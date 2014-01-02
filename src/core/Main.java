package core;

import gui.MainFrame;


public class Main {
	private static Chat chat;
	private static Database db;
	public static void main(String[] args){
		Main.setDb(new Database());
		Main.getDb().connect();
		Main.chat = new Chat(Main.getDb());
		new MainFrame("Babyfoot en réseau trololol", Main.chat);
	}
	public static Database getDb() {
		return db;
	}
	public static void setDb(Database db) {
		Main.db = db;
	}
	public static void closeWindow() {
		if( !Main.chat.getLogin().equals("") ){
			Main.getDb().removePlayer(Main.chat.getLogin());
		}
		System.exit(0);
	}
}


