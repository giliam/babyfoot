package core;

import network.Client;
import gui.MainFrame;


public class Main {
	private static Player player;
	private static Chat chat;
	private static Client client;
	
	public static void main(String[] args){
		Main.client = new Client();
		Main.setChat(new Chat());
		Main.setPlayer(new Player());
		new MainFrame("Babyfoot en réseau trololol", Main.getChat());
	}

	public static void closeWindow() {
		if( !Main.getPlayer().getLogin().equals("") ){
			Main.getPlayer().removePlayer(Main.getPlayer().getLogin());
		}
		System.exit(0);
	}
	public static Player getPlayer() {
		return player;
	}
	public static void setPlayer(Player player) {
		Main.player = player;
	}

	public static Chat getChat() {
		return chat;
	}

	public static void setChat(Chat chat) {
		Main.chat = chat;
	}
	
	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		Main.client = client;
	}
}


