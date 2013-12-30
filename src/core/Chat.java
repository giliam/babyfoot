package core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import network.ChatClient;

public class Chat {
	public String login;
	private Thread tChat;
	private Socket socket = null;
	public Chat(){
		Scanner sc = new Scanner(System.in);
	    try {
	        System.out.println("Demande de connexion");
	        socket = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur, authentification :");
	        tChat = new Thread(new ChatClient(socket, ""));
	        tChat.start();
	    } catch (UnknownHostException e) {
	      System.err.println("Impossible de se connecter à l'adresse 127.0.0.1 ");
	    } catch (IOException e) {
	      System.err.println("Aucun serveur à l'écoute du port 2010");
	    }
	    sc.close();
	}
	
	public void setLogin(String login) {
		this.login = new String(login);
		tChat.interrupt();
		tChat = new Thread(new ChatClient(socket, login));
        tChat.start();
	}
}
