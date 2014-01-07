package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MatchServer extends AbstractServer {
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split("-");
		String task = datas[1];
		System.out.println(query);
    	if( task.equals("add") ){
    		int type = Integer.valueOf(datas[3]);
    		String login = datas[2];
    		if( Server.db.addMatch(type, login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if(task.equals("setrod")){
    		String login = datas[2];
    		int[] positions = {Integer.valueOf(datas[3]),Integer.valueOf(datas[4]),Integer.valueOf(datas[5]),Integer.valueOf(datas[6])};
    		Server.db.setRod(login, positions);
    	}else if(task.equals("getrod")){
    		String login = datas[2];
    		sendRodPositions( Server.db.getRodPositions(login), out );
    	}
	}
	
	public void sendRodPositions(int[][] datas, PrintWriter out){
		out.println("rodpositions");
	}
}
