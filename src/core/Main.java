package core;

import gui.MainFrame;


public class Main {
	private static Chat chat;
	private static Database db;
	public static void main(String[] args){
		Main.db = new Database();
		Main.db.connect();
		Main.chat = new Chat(Main.db);
		new MainFrame("Babyfoot en réseau trololol", Main.chat);
	}
}


