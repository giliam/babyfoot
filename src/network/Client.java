package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import core.Database;

public class Client {
	private static Socket socket;
	private ChatClient cc;
	private PlayerClient pc;
	private Thread tChat;
	private Thread tPlayer;
	
	public Client(){
		Scanner sc = new Scanner(System.in);
	    try {
	        System.out.println("Demande de connexion");
	        Client.socket = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur, authentification :");
	        cc = new ChatClient( Client.socket );
	        tChat = new Thread(cc);
	        tChat.start();
	        pc = new PlayerClient( Client.socket );
	        tPlayer = new Thread(pc);
	        tPlayer.start();
	    } catch (UnknownHostException e) {
	      System.err.println("Impossible de se connecter à l'adresse 127.0.0.1 ");
	    } catch (IOException e) {
	      System.err.println("Aucun serveur à l'écoute du port 2010");
	    }
	    sc.close();
	}

	public PlayerClient getPc() {
		return pc;
	}

	public void setPc(PlayerClient pc) {
		this.pc = pc;
	}
}
