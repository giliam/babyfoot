package core;

import java.util.Hashtable;

import network.Server;

public class Match {
	private int leftScore;
	private int rightScore;
	
	private int type;
	
	private int state;
	
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	
	public static enum RodPositions { GARDIEN , DEFENSE, MILIEU, ATTAQUE };
	@SuppressWarnings("unchecked")
	private Hashtable<RodPositions, Integer>[] rodPositions = new Hashtable[2] ;
	
	
	
	public Match(String login, int type) {
		this.player1 = Server.tplayer.getPlayer(login);
		this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
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
	
	public void setRodPositions(Hashtable<RodPositions, Integer>[] rodPositions) {
		this.rodPositions = rodPositions;
	}
	
}
