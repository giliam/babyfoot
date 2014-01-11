package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	public static ChatServer tchat;
	public static PlayerServer tplayer;
	public static MatchServer tmatch;
	
	public Server(ServerSocket s){
		tmatch = new MatchServer();
		tplayer = new PlayerServer();
		tchat = new ChatServer();
		socketserver = s;
	}
	
	
	public void run() {
		try {
            while(true){
            	socket = socketserver.accept();
            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			out = new PrintWriter(socket.getOutputStream());
    			Thread allocator = new Thread(new Allocator(in, out));
    			allocator.start();
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


class Allocator implements Runnable{
	BufferedReader in;
	PrintWriter out;
	public Allocator( BufferedReader in, PrintWriter out ){
		this.in = in;
		this.out = out;
	}
	
	public void run(){
		String m;
		try {
			while(true){
				m = in.readLine();
				if( m != null ) {
					String[] datas = m.split("-");
					String typeRequete = datas[0];
					if( typeRequete.equals("player") ){
						Server.tplayer.setQuery(m);
						Server.tplayer.handle(in, out);
					}else if( typeRequete.equals("match") ){
						Server.tmatch.setQuery(m);
						Server.tmatch.handle(in, out);
					}else if( typeRequete.equals("servers") ){
						Server.tchat.setQuery(m);
						Server.tchat.handle(in, out);
					}else if( typeRequete.equals("chat") ){
						Server.tchat.setQuery(m);
						Server.tchat.handle(in, out);
					}
	        	}else{
	        		break;
	        	}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}