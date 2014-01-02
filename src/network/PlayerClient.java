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
    private PlayerReceptionMessage prc;
    
    public PlayerClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);
            prc = new PlayerReceptionMessage(in);
            Thread tEnvoiMessage = new Thread(prc);
            tEnvoiMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public boolean addPlayer(){
    	out.println("player-add-" + Main.getPlayer().getLogin() );
    	out.flush();
		return prc.message.equals("true");
    }

	public boolean removePlayer(String login) {
		out.println("player-remove-" + Main.getPlayer().getLogin() );
		out.flush();
		return true;
	}
}

class PlayerReceptionMessage implements Runnable{
	
	BufferedReader in;
	String message;
	
	public PlayerReceptionMessage(BufferedReader in){
		this.in = in;
		message = "aba";
	}
	
	public void run() {
		System.out.println("Prêt à la réception pour les joueurs !");
		while(true){
            try {
            	message = in.readLine();
            	System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	
}
