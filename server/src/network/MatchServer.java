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
    	}
	}
	
	private int[][] getRodPositions(String login) {
		return null;
	}

	private void setRod(String login, int[] positions) {
	}

	private boolean addMatch(int type, String login){
		Iterator it = liste.iterator();
		while ( it.hasNext() ){
		   if( ((Match) it.next()).isPlayer(login) )
			   return false;
		}
		liste.add(new Match(login, type));
		return false;
	}

	public void sendRodPositions(int[][] datas, PrintWriter out){
		out.println("rodpositions");
	}
}
