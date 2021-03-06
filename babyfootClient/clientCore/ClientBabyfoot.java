package clientCore;

import javax.swing.JPanel;

import clientCore.*;
import clientGui.MainFrame;
import clientNetwork.Client;



/** Cette classe contient le Main du client du projet. Elle initialise les différents composants nécessaires à l'exécution
 * du programme, c'est-à-dire le client, pour la connexion avec le serveur, le système de chat et le joueur. Elle lance
 * ensuite la classe MainFrame du package gui.
 */
public class ClientBabyfoot {
	private Player player;
	private Chat chat;
	private Client client;
	private MainFrame mainFrame;
	
	private int port;
	
	public JPanel actualPanel;
	
	public ClientBabyfoot(int port) {
		this.port = port;
	}

	public static void main(String[] args){
		int port = 2010;
		if( args.length >= 1 ){
			if( args[0].equals("-port") && args[1].length() > 0 ){
				port = Integer.valueOf(args[1]);
			}
		}
		ClientBabyfoot m = new ClientBabyfoot(port);
		m.init();
	}
	
	/** Lors de la fermeture de la fenêtre, supprime le joueur du serveur puisqu'il s'est déconnecté. */
	public void closeWindow() {
		if( !getPlayer().getLogin().equals("") ){
			try{
				getPlayer().removePlayer(getPlayer().getLogin());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	public void init(){
		setClient(new Client(this));
		setChat(new Chat(this));
		setPlayer(new Player(this));
		mainFrame = new MainFrame("Babyfoot en réseau trololol", this);
	}
	
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}


