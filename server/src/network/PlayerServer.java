package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import core.Player;

public class PlayerServer extends AbstractServer {
	private HashMap<String, Player> liste = new HashMap<String, Player>();
	
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split("-");
		String task = datas[1];
    	String login = datas[2];
    	if( task.equals("add") ){
    		if( addPlayer(login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if(query.equals("remove")){
    		removePlayer(login);
    	}
	}

	private void removePlayer(String login) {
		if( liste.containsKey(login)){
			liste.remove(login);
		}
	}

	private boolean addPlayer(String login) {
		if( !liste.containsKey(login)){
			liste.put(login,new Player(login));
			return true;
		}
		return false;
	}

	public Player getPlayer(String login) {
		if( liste.containsKey(login)){
			return liste.get(login);
		}
		return null;
	}
}
