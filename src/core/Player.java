package core;

import gui.GameZone;

import java.util.Hashtable;

public class Player {
	private String login;
	
	public Player(String l){
		setLogin(l);
	}
	
	public Player(){
		this("");
	}
	
	public boolean addPlayer(String login){
		this.setLogin(login);
		return Main.getClient().getPc().addPlayer();
	}

	public void removePlayer(String login) {
		if(Main.getClient().getPc().removePlayer(login))
			this.setLogin("");
	}
	
	public boolean addMatch(int type){
		return Main.getClient().getPc().addMatch(type);
	}
	
	public void setRod(Hashtable<GameZone.RodPositions, Integer> rodPositionsHash){
		int[] rodPositions = { rodPositionsHash.get(GameZone.RodPositions.GARDIEN), rodPositionsHash.get(GameZone.RodPositions.DEFENSE),rodPositionsHash.get(GameZone.RodPositions.MILIEU),rodPositionsHash.get(GameZone.RodPositions.ATTAQUE) };
		Main.getClient().getMc().setRodPositions(login, rodPositions);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
