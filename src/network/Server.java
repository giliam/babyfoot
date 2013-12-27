package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {

	ServerSocket socketserver = null;
	Socket socket = null;
	BufferedReader in;
	PrintWriter out;
	String login;
	Thread tchat, tplayer;
	
	public Server(Socket s, String log){
		socket = s;
		login = log;
	}
	
	
	public void run() {
		try {
			try {
	            while(true){
					
	            	socket = socketserver.accept();
		            System.out.println("Requête reçue");
		            
	            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    			out = new PrintWriter(socket.getOutputStream());
	    			tchat = new Thread(new ChatServer(out, in));
	    			tchat.start();
	    			tplayer = new Thread(new PlayerServer());
	    			tplayer.start();
	            }
	        } catch (IOException e) {
	            System.err.println("Erreur serveur");
	        }
		} catch (IOException e) {
			System.err.println(login +"s'est déconnecté ");
		}
}
}