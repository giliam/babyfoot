package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import core.Match;

public class MatchServer extends AbstractServer {
	
	private LinkedList<Match> liste = new LinkedList<Match>();
	
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split("-");
		String task = datas[1];
		System.out.println(query);
    	if( task.equals("add") ){
    		int type = Integer.valueOf(datas[3]);
    		String login = datas[2];
    		if( addMatch(type, login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if(task.equals("setrod")){
    		String login = datas[2];
    		int[] positions = {Integer.valueOf(datas[3]),Integer.valueOf(datas[4]),Integer.valueOf(datas[5]),Integer.valueOf(datas[6])};
    		setRod(login, positions);
    	}else if(task.equals("getrod")){
    		String login = datas[2];
    		sendRodPositions( getRodPositions(login), out );
    	}else if(task.equals("getserverslist")){
    		getServersList( out );
    	}
	}
	
	private void getServersList(PrintWriter out) {
		out.println("matchlist-beginning");
		out.println(liste.size());
		Iterator<Match> it = liste.iterator();
		while ( it.hasNext() ){
			Match m = ((Match) it.next());
			String display = ( m.getType() == 1 ? "1vs1" : ( m.getType() == 2 ? "2vs2" : "1vs2" ) );
			if( m.getPlayer1() != null )
				display += " - " + m.getPlayer1().getLogin();
			if( m.getPlayer2() != null )
				display += " - " + m.getPlayer2().getLogin();
			if( m.getPlayer3() != null )
				display += " - " + m.getPlayer3().getLogin();
			if( m.getPlayer4() != null )
				display += " - " + m.getPlayer4().getLogin();
			out.println(display);
		}
		out.println("matchlist-end");
		out.flush();
	}

	private int[][] getRodPositions(String login) {
		return null;
	}

	private void setRod(String login, int[] positions) {
	}

	private boolean addMatch(int type, String login){
		Iterator<Match> it = liste.iterator();
		while ( it.hasNext() ){
			//On vérifie que le joueur n'est pas déjà dans un match.
		   if( ((Match) it.next()).isPlayer(login) ){
			   return false;
		   }
		}
		liste.add(new Match(login, type));
		return true;
	}

	public void sendRodPositions(int[][] datas, PrintWriter out){
		out.println("rodpositions");
	}
}
