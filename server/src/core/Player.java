package core;

import java.util.Hashtable;

public class Player {
	private Match match;
	private String login;
	private Hashtable<Match.RodPositions, Boolean> rodAvailables;
	private boolean side;
	
	public Player(String login){
		this.login = login;
	}
	
	
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Hashtable<Match.RodPositions, Boolean> getRodAvailables() {
		return rodAvailables;
	}
	public void setRodAvailables(
			Hashtable<Match.RodPositions, Boolean> rodAvailables) {
		this.rodAvailables = rodAvailables;
	}


	public int getSide() {
		return side ? 1 : 0;
	}
	
	public void setSide(boolean b) {
		side = b;
	}
}
