package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import core.Utils;

public class ChatServer {
	
	BufferedReader in;
	PrintWriter out;
	
	public ChatServer(Socket socket){
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			Thread tReceptionMessageServeur = new Thread(new ReceptionMessageServeur(in));
			tReceptionMessageServeur.start();
			Thread tEmissionMessageServeur = new Thread(new EmissionMessageServeur(out));
			tEmissionMessageServeur.start();
		}catch(IOException e){
			System.err.println("Erreur serveur à la création d'un serveur fils !");
		}
	}
}

class ReceptionMessageServeur implements Runnable{
	private BufferedReader in;
	public ReceptionMessageServeur(BufferedReader in){
		this.in = in;
	}
	
	public void run(){
		String m;
		while(true){
			try {
				m = in.readLine();
				if( !m.equals("") ) {
	        		String[] datas = m.split("-", 6);
	        		String date = datas[3];
	            	String message = datas[5];
	            	String login = datas[2];
	            	String serveur = datas[1];
	            	String typeRequete = datas[0];
	            	String hashTag = datas[4];
	            	if( typeRequete.equals("tchat") && hashTag.equals(Utils.hash(serveur + "salt" + message + login + "42$1a" ) ) ){
	            		System.out.println("Serveur " + serveur + " - " + date + " - " + login+" : "+message);
	            	}else if( typeRequete.equals("tchat")){
	            		System.out.println("Requête non valide par le hash!");
	            	}else{
	            		System.out.println("Requête non valide par le type !");
	            	}
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        	
	}
}

class EmissionMessageServeur implements Runnable{
	private PrintWriter out;
	private Scanner sc;
	public EmissionMessageServeur(PrintWriter out){
		this.out = out;
	}
	
	public void run(){
		sc = new Scanner(System.in);
	}
}
