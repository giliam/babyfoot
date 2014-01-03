package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


import core.Utils;

public class ChatServer extends AbstractServer {
	public void handle(BufferedReader in, PrintWriter out){
		String task = datas[0];
		String query = datas[1];
		if( task.equals("chat") ){
			if( query.equals("add")){
				String message = datas[5];
		    	String login = datas[3];
		    	String serveur = datas[2];
		    	String hashTag = datas[4];
		    	if( hashTag.equals(Utils.hash(serveur + "salt" + message + login + "42$1a" ) ) ){
		    		Server.db.addMessage(serveur, login, message );
		    		System.out.println("ENVOI REUSSI");
		    	}else{
		    		System.out.println("Requête non valide par le type !");
		    	}
			}
		}else if( task.equals("servers") ){
			if( query.equals("get")){
				out.println("server-beginning");
    			out.flush();
	    		String[] servers = Server.db.getServers();
	    		out.println(servers.length);
	    		out.flush();
	    		for( int i = 0; i<servers.length; i++ ){
	    			out.println(servers[i]);
	    			out.flush();
	    		}
	    		out.println("server-end");
    			out.flush();
			}
		}
	}
}