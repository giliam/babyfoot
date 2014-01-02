package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import core.Main;
import core.Utils;

public class PlayerClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Thread t3, t4;
    private Scanner sc;
    
    public PlayerClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);
            //Thread tEnvoiMessage = new Thread(new EnvoiMessage(out, login));
            //tEnvoiMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public void addPlayer(){
    	System.out.println("Tentative de connexion...");
    	out.println("player-" + Main.getPlayer().getLogin() );
		out.flush();
		System.out.println("Connexion réussie...");
    }
}
