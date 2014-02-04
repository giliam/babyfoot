package serverCore;

import java.util.Hashtable;

import serverNetwork.Server;


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
	
	private int ballX;
	private int ballY;
	private int ballSpeedX;
	private int ballSpeedY;
	
	private final int STEP_X = 2;
	private final int STEP_Y = 2;
	
	final int LINE_STRENGTH = 0;
	final int GAP_EDGE = 2*20;	
	final int BALL_RADIUS = 25;
	
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

	public void stop() {
		if( player1 != null )
			player1.setMatch(null);
		if( player2 != null )
			player2.setMatch(null);
		if( player3 != null )
			player3.setMatch(null);
		if( player4 != null )
			player4.setMatch(null);
	}

	public void start() {
		setBallX(450);
		setBallY(350);
		setBallSpeedX(( Math.random() > 0.5 ? 15 : -15 ));
		setBallSpeedY(( Math.random() > 0.5 ? 15 : -15 ));
		Thread t = new Thread(new RefreshBallPosition(this));
		t.start();
	}

	public int getBallY() {
		return ballY;
	}

	public void setBallY(int ballY) {
		this.ballY = ballY;
	}

	public int getBallSpeedY() {
		return ballSpeedY;
	}

	public void setBallSpeedY(int ballSpeedY) {
		this.ballSpeedY = ballSpeedY;
	}

	public int getBallX() {
		return ballX;
	}

	public void setBallX(int ballX) {
		this.ballX = ballX;
	}

	public int getBallSpeedX() {
		return ballSpeedX;
	}

	public void setBallSpeedX(int ballSpeedX) {
		this.ballSpeedX = ballSpeedX;
	}

	public void addBallX(int ballX2) {
		ballX += ballX2;
	}

	public void addBallY(int ballY2) {
		ballY += ballY2;
	}
	
}

class RefreshBallPosition implements Runnable {
	private Match match;
	public RefreshBallPosition(Match m){
		match = m;
	}
	
	@Override
	public void run() {
		try{
			while(true){
				
				if( ( match.getBallX() + match.getBallSpeedX() ) >=  ( match.BALL_RADIUS + 15 ) ) {
					if( ( match.getBallX() + match.getBallSpeedX() ) >=  ( 900 - match.BALL_RADIUS ) ){
						match.setBallSpeedX((-1)*match.getBallSpeedX());
					}
				}else if( match.getBallSpeedX() < 0 ){
					match.setBallSpeedX((-1)*match.getBallSpeedX());
				}
				
				if( ( match.getBallY() + match.getBallSpeedY() ) >=  ( match.BALL_RADIUS + 15 ) ){
					if( ( match.getBallY() + match.getBallSpeedY() ) >=  ( 700 - 2*match.BALL_RADIUS - match.GAP_EDGE ) ){
						match.setBallSpeedY((-1)*match.getBallSpeedY());
					}
				}else if( match.getBallSpeedY() < 0 ){
					match.setBallSpeedY((-1)*match.getBallSpeedY());
				}
				match.addBallX(match.getBallSpeedX());
				match.addBallY(match.getBallSpeedY());
				
				Thread.sleep(100);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}