package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import core.Utils;

public class ChatClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Thread t3, t4;
    private Scanner sc;
    private String login;
    
    public ChatClient(Socket s, String login){
        socket = s;
        this.login = login;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);
            //Thread tEnvoiMessage = new Thread(new EnvoiMessage(out, login));
            //tEnvoiMessage.start();
            Thread tReceptionMessage = new Thread(new ReceptionMessage(in));
            tReceptionMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public void sendMessage(String text){
    	System.out.println("Envoi de message...");
    	out.println("tchat-Global-giliam-" + Utils.hash("Globalsalt" + text + "giliam" + "42$1a" ) + "-" + text);
		out.flush();
		System.out.println("Envoi réussi...");
    }
    /*
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
    	Socket socket = null;
	    try {
	        System.out.println("Demande de connexion");
	        socket = new Socket("127.0.0.1",2010);
	        System.out.println("Connexion établie avec le serveur, authentification :");
	        String log = sc.nextLine();
	        System.out.println("Vous êtes connecté !");
	        Thread t1 = new Thread(new ChatClient(socket, log));
	        t1.start();
	    } catch (UnknownHostException e) {
	      System.err.println("Impossible de se connecter à l'adresse "+ socket.getLocalAddress());
	    } catch (IOException e) {
	      System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
	    }
    }*/
}
/*
class EnvoiMessage implements Runnable {
	PrintWriter out;
	String login;
	public EnvoiMessage(PrintWriter out, String log){
		this.out = out;
		login = log;
	}
	public void run() {
		Scanner sc = new Scanner(System.in);
		while(true){
			try {
				String message = sc.nextLine();
				out.println("tchat-principal-" + login + "-" + Utils.hash("principalsalt" + message + login + "42$1a" ) + "-" + message);
				out.flush();
			}catch(NoSuchElementException e){
				
			}
		}
	}
}*/

class ReceptionMessage implements Runnable{
	
	BufferedReader in;
	
	public ReceptionMessage(BufferedReader in){
		this.in = in;
	}
	
	public void run() {
		System.out.println("Prêt à la réception !");
		while(true){
            try {
            	String message = in.readLine();
            	System.out.println("Le serveur vous dit :" +message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
}