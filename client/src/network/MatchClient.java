package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MatchClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private MatchReceptionMessage prc;
    static boolean ok;
    static String[] serverList;
    
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

	public String[] getServers() {
		out.println("match-getserverslist" );
    	out.flush();
		try {
			Thread.currentThread();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return serverList;
	}

	public boolean setServerFromHost(String login, String loginHost) {
		out.println("player-joinmatch-" + login + "-" + loginHost );
    	out.flush();
    	try {
			Thread.currentThread();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	if( ok ){
    		ok = false;
    		return true;
    	}else{
    		return false;
    	}
	}
}

class MatchReceptionMessage implements Runnable{
	
	BufferedReader in;
	boolean ready;
	
	public MatchReceptionMessage(BufferedReader in){
		this.in = in;
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		System.out.println("Prêt à la réception pour les match !");
		try {
			while(true){
            	String message = in.readLine();
            	if( message.equals("matchlist-beginning") && mode == 0 ){
            		mode = 1;
            	}else if( mode == 1 ){
            		MatchClient.serverList = new String[Integer.valueOf(message)];
            		mode = 2;
            	}else if( message.equals("matchlist-end")  && mode == 2 ){
            		mode = 0;
            		n = 0;
            	}else if( mode == 2 ){
            		MatchClient.serverList[n++] = message;
            	}else{
            		MatchClient.ok = message.equals("true");
            	}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
 