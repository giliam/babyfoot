package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import core.Main;

public class GameClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private GameReceptionMessage grc;
    String[] rodPositions;
    
    public GameClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            grc = new GameReceptionMessage(in, this);
            Thread tReceptionMessage = new Thread(grc);
            tReceptionMessage.start();
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public void setRodPositions(String login, int[] rodPositions) {
		out.println("match-setrod-" + login + "-" + rodPositions[0] + "-" + rodPositions[1] + "-"  + rodPositions[2] + "-"  + rodPositions[3] );
    	out.flush();
	}
    
    public String[] getRodPositions(String login) {
		out.println("match-getrodpositions-" + login );
    	out.flush();
    	return rodPositions;
	}

	public String[] getRodPositions() {
		return rodPositions;
	}

	public void setRodPositions(String[] rodPositions) {
		this.rodPositions = rodPositions;
	}
}

class GameReceptionMessage implements Runnable{
	
	BufferedReader in;
	String message;
	GameClient gc;
	
	public GameReceptionMessage(BufferedReader in, GameClient gc ){
		this.in = in;
		this.gc = gc;
		message = "";
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		System.out.println("Prêt à la réception pour les joueurs !");
		try {
			while(true){
            	message = in.readLine();
            	if( message.equals("rod-beginning") && mode == 0 ){
            		mode = 1;
            		gc.setRodPositions(new String[8]);
            	}else if( message.equals("rod-end") && mode == 1 ){
            		mode = 0;
            		n = 0;
            	}else if( mode == 2 ){
            		gc.rodPositions[n++] = message;
            	}
			}
		} catch (IOException e) {
                e.printStackTrace();
        }
	}
}
