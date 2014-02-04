package serverNetwork;

import clientCore.Utils;
import java.io.BufferedReader;
import java.io.PrintWriter;

import serverCore.Database;



public class ChatServer extends AbstractServer {
	private Database db;
	
	public ChatServer(){
		db = new Database();
		db.connect();
	}
	
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split(Utils.SEPARATOR, 6);
		String domain = datas[0];
		String task = datas[1];
		if( domain.equals("chat") ){
			if( task.equals("add")){
				String message = datas[5];
		    	String login = datas[3];
		    	String serveur = datas[2];
		    	String hashTag = datas[4];
		    	if( hashTag.equals(Utils.hash(serveur + "salt" + message + login + "42$1a" ) ) ){
		    		addMessage(serveur, login, message );
		    		System.out.println("ENVOI REUSSI");
		    	}else{
		    		System.out.println("Requête non valide par le type !");
		    	}
			}else if( task.equals("get")){
				String serveur = datas[2];
				out.println("chat" + Utils.SEPARATOR + "beginning");
    			out.flush();
	    		String[] messages = getMessages( serveur );
	    		out.println(messages.length);
	    		out.flush();
	    		for( int i = 0; i<messages.length; i++ ){
	    			out.println(messages[i]);
	    			out.flush();
	    		}
	    		out.println("chat" + Utils.SEPARATOR + "end");
    			out.flush();
				
			}
		}else if( domain.equals("servers") ){
			if( task.equals("get")){
				out.println("server" + Utils.SEPARATOR + "beginning");
    			out.flush();
	    		String[] servers = getServers();
	    		out.println(servers.length);
	    		out.flush();
	    		for( int i = 0; i<servers.length; i++ ){
	    			out.println(servers[i]);
	    			out.flush();
	    		}
	    		out.println("server" + Utils.SEPARATOR + "end");
    			out.flush();
			}
		}
	}

	private String[] getServers() {
		return db.getServers();
	}

	private String[] getMessages(String serveur) {
		return db.getMessages(serveur);
	}

	private void addMessage(String serveur, String login, String message) {
		db.addMessage(serveur, login, message);
	}
}