package core;

import network.Client;
import gui.MainFrame;

/** Cette classe contient le Main du client du projet. Elle initialise les différents composants nécessaires à l'exécution
 * du programme, c'est-à-dire le client, pour la connexion avec le serveur, le système de chat et le joueur. Elle lance
 * ensuite la classe MainFrame du package gui.
 */
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
	/** Lors de la fermeture de la fenêtre, supprime le joueur du serveur puisqu'il s'est déconnecté. */
	public static void closeWindow() {
		if( !Main.getPlayer().getLogin().equals("") ){
			try{
				Main.getPlayer().removePlayer(Main.getPlayer().getLogin());
			}catch(Exception e){
				e.printStackTrace();
			}
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


