package serverCore;

import java.util.Hashtable;

import clientCore.Utils;
import clientCore.Utils.Rod;

public class Player {
	private Match match;
	private String login;
	private Hashtable<Rod, Boolean> rodAvailables;
	private Utils.Sides side;
	private int status;
	
	public Player(String login){
		this.login = login;
		this.status = 1;
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
	public Hashtable<Rod, Boolean> getRodAvailables() {
		return rodAvailables;
	}
	public void setRodAvailables(
			Hashtable<Rod, Boolean> rodAvailables) {
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
					side = Utils.Sides.DOWN;
				else
					side = Utils.Sides.UP;
				break;
			case TWOVSTWO:
				if( i < 3 )
					side = Utils.Sides.DOWN;
				else
					side = Utils.Sides.UP;
				break;
			default:
				break;
		
		}
	}


	public int updateRods(int status) {
		int adding = 0;
		switch( match.getType() ){
			case ONEVSONE:
				if( (status & 1) == 0 ){
					adding = 3;
				}else{
					adding = 12;
				}
				break;
			case ONEVSTWO:
				if( (status & 1) == 0 ){
					adding = 3;
				}else if( (status & 4) == 0 ){
					adding = 4;
				}else{
					adding = 8;
				}
				break;
			case TWOVSTWO:
				if( (status & 1) == 0 ){
					adding = 1;
				}else if( (status & 2) == 0 ){
					adding = 2;
				}else if( (status & 4) == 0 ){
					adding = 4;
				}else{
					adding = 8;
				}
				break;
			default:
				break;
		}
		this.status = adding;
		return status + adding;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
}
