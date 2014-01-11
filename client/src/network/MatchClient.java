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
    private boolean ok;
    private String[] serverList;
    private String[] matchDatas;
    
    public MatchClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            prc = new MatchReceptionMessage(in, this);
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
		return getServerList();
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
    	if( isOk() ){
    		setOk(false);
    		return true;
    	}else{
    		return false;
    	}
	}
	
	public String[] getMatchInfo(String login) {
		out.println("match-getmatchinfo-" + login );
    	out.flush();
		try {
			Thread.currentThread();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getMatchDatas();
	}

	public String[] getServerList() {
		return serverList;
	}

	public void setServerList(String[] serverList) {
		this.serverList = serverList;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public void setMatchDatas(String[] matchDatas) {
		this.matchDatas = matchDatas;
	}

	public String[] getMatchDatas() {
		return matchDatas;
	}
}

class MatchReceptionMessage implements Runnable{
	
	BufferedReader in;
	boolean ready;
	MatchClient matchClient;
	
	public MatchReceptionMessage(BufferedReader in, MatchClient matchClient){
		this.in = in;
		this.matchClient = matchClient;
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		int type = 0;
		System.out.println("Prêt à la réception pour les match !");
		try {
			while(true){
            	String message = in.readLine();
            	if( ( message.equals("matchlist-beginning") || message.equals("matchinfo-beginning") ) && mode == 0 ){
            		if( message.equals("matchinfo-beginning") ){
            			type = 1;
            			matchClient.setMatchDatas(new String[5]);
            			mode = 2;
            		}else
            			mode = 1;
            	}else if( mode == 1 ){
            		if( type == 0)
            			matchClient.setServerList(new String[Integer.valueOf(message)]);
            		mode = 2;
            	}else if( ( message.equals("matchlist-end") || message.equals("matchinfo-end") )  && mode == 2 ){
            		mode = 0;
            		type = 0;
            		n = 0;
            	}else if( mode == 2 ){
            		if( type == 0)
            			matchClient.getServerList()[n++] = message;
            		else if( type == 1)
            			matchClient.setMatchDatas(message.split("-"));
            	}else{
            		matchClient.setOk(message.equals("true"));
            	}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
 