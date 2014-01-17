package core;

import java.util.Hashtable;

import network.Server;

public class Match {
	private int leftScore;
	private int rightScore;
	
	public static enum Types { ONEVSONE, TWOVSTWO, ONEVSTWO };
	public static enum States { WAITING, FULL, PLAYING, FINISHED };  
	private Types type;
	private States state;
	
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	
	public static enum RodPositions { GARDIEN , DEFENSE, MILIEU, ATTAQUE };
	@SuppressWarnings("unchecked")
	private Hashtable<RodPositions, Integer>[] rodPositions = new Hashtable[2] ;
	
	
	
	@SuppressWarnings("unchecked")
	public Match(String login, Types type) {
		this.player1 = Server.tplayer.getPlayer(login);
		this.player1.setMatch(this);
		this.type = type;
		this.rodPositions = new Hashtable[2];
		
		this.rodPositions[0] = new Hashtable<RodPositions, Integer>();
		this.rodPositions[1] = new Hashtable<RodPositions, Integer>();
		
		this.rodPositions[0].put( RodPositions.GARDIEN, 100 );
		this.rodPositions[0].put( RodPositions.DEFENSE, 150 );
		this.rodPositions[0].put( RodPositions.MILIEU, 100 );
		this.rodPositions[0].put( RodPositions.ATTAQUE, 100 );
		
		this.rodPositions[1].put( RodPositions.GARDIEN, 100 );
		this.rodPositions[1].put( RodPositions.DEFENSE, 150 );
		this.rodPositions[1].put( RodPositions.MILIEU, 100 );
		this.rodPositions[1].put( RodPositions.ATTAQUE, 100 );
	}

	public boolean isPlayer( String login ){
		return ( player1 != null && player1.getLogin().equals(login) )|| ( player2 != null && player2.getLogin().equals(login) ) || ( player3 != null && player3.getLogin().equals(login) ) || ( player4 != null && player4.getLogin().equals(login));
	}


	public int getLeftScore() {
		return leftScore;
	}

	public void setLeftScore(int leftScore) {
		this.leftScore = leftScore;
	}

	public int getRightScore() {
		return rightScore;
	}

	public void setRightScore(int rightScore) {
		this.rightScore = rightScore;
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getPlayer3() {
		return player3;
	}

	public void setPlayer3(Player player3) {
		this.player3 = player3;
	}

	public Player getPlayer4() {
		return player4;
	}

	public void setPlayer4(Player player4) {
		this.player4 = player4;
	}

	public Hashtable<RodPositions, Integer>[] getRodPositions() {
		return rodPositions;
	}
	
	/*public void setRodPositions(Hashtable<RodPositions, Integer>[] rodPositions) {
		this.rodPositions = rodPositions;
	}*/
	
	public void setRodPositions(Hashtable<RodPositions, Integer> rodPositions, int i) {
		this.rodPositions[i] = rodPositions;
	}

	public String addPlayer(Player p) {
		if( player1 == null ){
			player1 = p;
			p.updateSide(1);
			return "true";
		}else if( player2 == null ){
			player2 = p;
			p.updateSide(2);
			return "true";
		}else if( player3 == null ){
			player3 = p;
			p.updateSide(3);
			return "true";
		}else if( player4 == null ){
			player4 = p;
			p.updateSide(4);
			return "true";
		}
		return "false";
	}

	public void removePlayer(String login) {
		if( player1 != null ){
			if( player1.getLogin().equals(login) )
				player1 = null;
		}else if( player2 != null ){
			if( player2.getLogin().equals(login) )
				player2 = null;
		}else if( player3 != null ){
			if( player3.getLogin().equals(login) )
				player3 = null;
		}else if( player4 != null ){
			if( player4.getLogin().equals(login) )
				player4 = null;
		}
	}

	public int getRodPosition(boolean b, RodPositions i) {
		return rodPositions[b ? 1 : 0].get(i);
	}
	
}
