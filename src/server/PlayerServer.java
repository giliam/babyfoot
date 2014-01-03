package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerServer extends AbstractServer {
	public void handle(BufferedReader in, PrintWriter out){
    	String query = datas[1];
    	String login = datas[2];
    	if( query.equals("add") ){
    		if( Server.db.addPlayer(login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if(query.equals("remove")){
    		Server.db.removePlayer(login);
    	}
	}
}