package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import core.Main;
import core.Utils;

public class MatchClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private MatchReceptionMessage prc;
    static boolean ok;
    
    public MatchClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            prc = new MatchReceptionMessage(in);
            Thread tReceptionMessage = new Thread(prc);
            tReceptionMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
	public void setRodPositions(String login, int[] rodPositions) {
		out.println("match-setrod-" + login + "-" + rodPositions[0] + "-" + rodPositions[1] + "-"  + rodPositions[2] + "-"  + rodPositions[3] );
    	out.flush();
	}
}

class MatchReceptionMessage implements Runnable{
	
	BufferedReader in;
	String message;
	boolean ready;
	
	public MatchReceptionMessage(BufferedReader in){
		this.in = in;
		message = "aba";
	}
	
	public void run() {
		System.out.println("Prêt à la réception pour les match !");
		try {
			while(true){
            	message = in.readLine();
            	MatchClient.ok = message.equals("true");
			}
		} catch (IOException e) {
                e.printStackTrace();
        }
	}
}
 