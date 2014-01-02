package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import core.Database;


public class Server implements Runnable {

	ServerSocket socketserver = null;
	Socket socket = null;
	BufferedReader in;
	PrintWriter out;
	String login;
	ChatServer tchat;
	PlayerServer tplayer;
	public static Database db;
	
	public Server(ServerSocket s){
		socketserver = s;
		db = new Database();
		db.connect();
	}
	
	
	public void run() {
		try {
            while(true){
            	socket = socketserver.accept();
	            System.out.println("Requête reçue");
    			tchat = new ChatServer(socket);
    			tplayer = new PlayerServer();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur à la réception !");
        }
	}
	
	public static void main(String[] args){
		try{	
			System.out.println("Lancement du serveur en cours...");
			Server s = new Server(new ServerSocket(2010));
			System.out.println("Serveur prêt !");
			Thread serveur = new Thread(s);
			serveur.start();
		} catch (IOException e) {
	        System.err.println("Erreur serveur au lancement !");
	    }
	}
}