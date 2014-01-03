package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import core.Database;

public class Client {
	private static Socket socketChat;
	private static Socket socketPlayer;
	private ChatClient cc;
	private PlayerClient pc;
	private Thread tChat;
	private Thread tPlayer;
	
	public Client(){
		Scanner sc = new Scanner(System.in);
	    try {
	        System.out.println("Demande de connexion pour le chat");
	        Client.socketChat = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur pour le chat");
	        setCc(new ChatClient( Client.socketChat ));
	        tChat = new Thread(getCc());
	        tChat.start();
	        System.out.println("Demande de connexion pour les joueurs");
	        Client.socketPlayer = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur pour les joueurs");
	        pc = new PlayerClient( Client.socketPlayer );
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

	public ChatClient getCc() {
		return cc;
	}

	public void setCc(ChatClient cc) {
		this.cc = cc;
	}
}
