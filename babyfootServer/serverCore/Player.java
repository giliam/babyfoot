package serverCore;

import java.util.Hashtable;

import clientCore.Utils;

public class Player {
	private Match match;
	private String login;
	private Hashtable<Match.RodPositions, Boolean> rodAvailables;
	private Utils.Sides side;
	
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
		return side == Utils.Sides.UP ? 1 : 0;
	}
	
	public void setSide(Utils.Sides b) {
		side = b;
	}


	public void updateSide(int i) {
		switch( match.getType() ){
			case ONEVSONE:
			case ONEVSTWO:
				if( i == 1 )
					side = Utils.Sides.BOTTOM;
				else
					side = Utils.Sides.UP;
				break;
			case TWOVSTWO:
				if( i < 3 )
					side = Utils.Sides.BOTTOM;
				else
					side = Utils.Sides.UP;
				break;
			default:
				break;
		
		}
	}
}
