package core;

import network.ChatClient;

public class Player {
	private String login;
	private Team team;
	
	public Player(String l, Team t){
		setLogin(l);
		team = t;
	}
	
	public Player(){
		this("",new Team());
	}
	
	public boolean addPlayer(String login){
		this.setLogin(login);
		Main.getClient().getPc().addPlayer();
		return true;
	}

	public void removePlayer(String login) {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
