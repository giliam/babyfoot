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
    private PlayerReceptionMessage prc;
    static boolean ok;
    
    public PlayerClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            prc = new PlayerReceptionMessage(in);
            Thread tReceptionMessage = new Thread(prc);
            tReceptionMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public boolean addPlayer(){
    	out.println("player-add-" + Main.getPlayer().getLogin() );
    	out.flush();
    	try {
			Thread.currentThread().sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ok;
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
	boolean ready;
	
	public PlayerReceptionMessage(BufferedReader in){
		this.in = in;
		message = "aba";
	}
	
	public void run() {
		System.out.println("Prêt à la réception pour les joueurs !");
		try {
			while(true){
	            
	            	message = in.readLine();
	            	PlayerClient.ok = message.equals("true");
			}
		} catch (IOException e) {
                e.printStackTrace();
        }
	}
}
