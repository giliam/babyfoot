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
		return Main.getClient().getPc().addPlayer();
	}

	public void removePlayer(String login) {
		if(Main.getClient().getPc().removePlayer(login))
			this.setLogin("");
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
