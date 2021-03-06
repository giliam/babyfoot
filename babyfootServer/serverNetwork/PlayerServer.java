package serverNetwork;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;

import clientCore.Utils;

import serverCore.Match;
import serverCore.Player;


public class PlayerServer extends AbstractServer {
	private HashMap<String, Player> liste = new HashMap<String, Player>();
	
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split(Utils.SEPARATOR);
		String task = datas[1];
    	String login = datas[2];
    	if( task.equals("add") ){
    		if( addPlayer(login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if( task.equals("remove")){
    		removePlayer(login);
    	}else if( task.equals("joinmatch")){
    		String loginHost = datas[3];
    		if( ((Player) liste.get(loginHost)) == null || ((Player) liste.get(login)) == null ){
    			out.println("false");
    		}else{
	    		Match m = ((Player) liste.get(loginHost)).getMatch();
	    		if( m == null )
	    			out.println("false");
	    		else{
		    		((Player) liste.get(login)).setMatch(m);
		    		out.println(m.addPlayer(((Player) liste.get(login))));
	    		}
    		}
	    	out.flush();
    	}
	}

	private void removePlayer(String login) {
		if( liste.containsKey(login) && liste.get(login) != null){
			if( ((Player)liste.get(login)).getMatch() != null )
			((Player)liste.get(login)).getMatch().removePlayer(login);
			ServerBabyfoot.tmatch.removeFromListe(((Player)liste.get(login)).getMatch());
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
