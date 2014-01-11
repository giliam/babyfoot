package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private static Socket socketChat;
	private static Socket socketPlayer;
	private static Socket socketMatch;
	private ChatClient cc;
	private PlayerClient pc;
	private MatchClient mc;
	private Thread tChat;
	private Thread tPlayer;
	private Thread tMatch;
	
	public Client(){
		Scanner sc = new Scanner(System.in);
	    try {
	        Client.socketChat = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur pour le chat");
	        setCc(new ChatClient( Client.socketChat ));
	        tChat = new Thread(getCc());
	        tChat.start();
	        Client.socketPlayer = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur pour les joueurs");
	        pc = new PlayerClient( Client.socketPlayer );
	        tPlayer = new Thread(pc);
	        tPlayer.start();
	        Client.socketMatch = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur pour les matchs");
	        mc = new MatchClient( Client.socketMatch );
	        tMatch = new Thread(mc);
	        tMatch.start();
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
	
	public MatchClient getMc() {
		return mc;
	}

	public void setMc(MatchClient mc) {
		this.mc = mc;
	}
}
