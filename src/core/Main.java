package core;

import gui.MainFrame;


public class Main {
	private static Chat chat;
	public static void main(String[] args){
		Main.chat = new Chat();
		new MainFrame("Babyfoot en réseau trololol", Main.chat);
	}
}


