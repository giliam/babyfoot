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
		String[] datas = query.split("-", 6);
		String domain = datas[0];
		String task = datas[1];
		if( domain.equals("chat") ){
			if( task.equals("add")){
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
			}else if( task.equals("get")){
				String serveur = datas[2];
				out.println("chat-beginning");
    			out.flush();
	    		String[] messages = Server.db.getMessages( serveur );
	    		out.println(messages.length);
	    		out.flush();
	    		for( int i = 0; i<messages.length; i++ ){
	    			out.println(messages[i]);
	    			out.flush();
	    		}
	    		out.println("chat-end");
    			out.flush();
				
			}
		}else if( domain.equals("servers") ){
			if( task.equals("get")){
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