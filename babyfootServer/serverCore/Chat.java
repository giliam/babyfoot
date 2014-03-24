package serverCore;

import java.util.LinkedList;

import clientCore.Utils;

public class Chat {
	private LinkedList<Server> servers;
	private LinkedList<String> serversNames;
	
	public static String[] NO_MESSAGES;
	
	public Chat(){
		
		Chat.NO_MESSAGES = new String[1];
		Chat.NO_MESSAGES[0] = "Pas de messages";
		
		servers = new LinkedList<Server>();
		serversNames = new LinkedList<String>();
		
		addServer("Global");
		addServer("Maintenance");
	}
	
	public void addServer(String name){
		servers.add(new Server(name));
		serversNames.add(name);
	}
	
	public void addMessage(String serverName, String login, String text){
		Server server = getServerByItsName(serverName);
		if( server != null )
			server.addMessage(login, text);
		else
			System.out.println("Le message ne trouve pas de serveur !");
	}
	
	public String[] getMessages(String serverName){
		Server server = getServerByItsName(serverName);
		return ( server != null ? server.getMessages() : Chat.NO_MESSAGES );
	}
	
	public Server getServerByItsName(String name){
		int i = serversNames.indexOf(name);
		return ( i == -1 || i >= servers.size() ? null : servers.get( serversNames.indexOf(name) ) );
	}

	public String[] getServers() {
		String[] s = new String[servers.size()];
		int n = servers.size();
		for (int i = 0; i < n; i++)
			if( i < servers.size() )
				s[i] = i + " - " + servers.get(i).toString();
		return s;
	}

	public void deleteServerFromMatch(String name) {
		int i = serversNames.indexOf( "Partie de " + name );
		if( i > -1 && servers.get(i) != null ){
			servers.remove(i);
		}
	}
}

class Server{
	private String name;
	private Match game;
	private LinkedList<Message> messages;

	public Server(String name) {
		messages = new LinkedList<Message>();
		this.name = name;
		game = null;
	}

	public String toString(){
		return name;
	}
	
	public void addMessage(String login, String text ){
		messages.add(new Message(login, text, (int) System.currentTimeMillis()));
	}
	
	public String[] getMessages() {
		if( messages.size() == 0 ){
			return Chat.NO_MESSAGES;
		}else{
			String[] m = new String[messages.size()];
			int n = messages.size();
			for (int i = 0; i < n; i++)
			    m[i] = messages.get(i).toString();
			return m;
		}
	}
}

class Message{
	private String login;
	private String text;
	private int date;
	private Server server;
	
	public Message(String login2, String text2, int date2) {
		login = login2;
		text = text2;
		date = date2;
	}
	
	public String toString(){
		return Utils.formatDate( date ) + " - " + login + " : " + text;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	
}