package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import core.Main;
import core.Utils;

public class ChatClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Thread t3, t4;
    private Scanner sc;
    public ChatReceptionMessage rc;
    public static String[] s;
    
    public ChatClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);
            //Thread tEnvoiMessage = new Thread(new EnvoiMessage(out, login));
            //tEnvoiMessage.start();
            rc = new ChatReceptionMessage(in);
            Thread tReceptionMessage = new Thread(rc);
            tReceptionMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public void sendMessage(String text){
    	out.println("chat-add-" + Main.getChat().getServer() + "-" + Main.getPlayer().getLogin() + "-" + Utils.hash( Main.getChat().getServer() + "salt" + text + Main.getPlayer().getLogin() + "42$1a" ) + "-" + text);
		out.flush();
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

	public String[] getServers() {
		System.out.println("anegoaeg");
		out.println("servers-get");
		out.flush();
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for( int i = 0; i < ChatClient.s.length; i++ ){
			System.out.println(ChatClient.s[i]);
		}
		return ChatClient.s;
	}
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

class ChatReceptionMessage implements Runnable{
	BufferedReader in;
	public ChatReceptionMessage(BufferedReader in){
		this.in = in;
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		System.out.println("Prêt à la réception pour le chat !");
		while(true){
            try {
            	String message = in.readLine();
            	if( message.equals("server-beginning") && mode == 0 ){
            		mode = 1;
            	}else if( mode == 1 ){
            		ChatClient.s = new String[Integer.valueOf(message)];
            		mode = 2;
            	}else if( mode == 2 ){
            		ChatClient.s[n++] = message;
            	}else if( message.equals("server-end") && mode == 2 ){
            		mode = 0;
            		n = 0;
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}