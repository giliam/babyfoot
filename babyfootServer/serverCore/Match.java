package serverCore;

import java.util.Hashtable;

import clientCore.Utils;
import clientCore.Utils.RodPositions;
import clientCore.Utils.CollisionType;
import clientCore.Utils.Sides;

import serverNetwork.ServerBabyfoot;


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
	
	private float ballX;
	private float ballY;
	private float ballSpeedX;
	private float ballSpeedY;
	
	private Collisions collisions;

	private boolean noSlow = false;

	private final int STEP_X = 2;
	private final int STEP_Y = 2;
	
	@SuppressWarnings("unchecked")
	private Hashtable<RodPositions, Integer>[] rodPositions = new Hashtable[2] ;
	
	
	
	@SuppressWarnings("unchecked")
	public Match(String login, Types type) {
		this.player1 = ServerBabyfoot.tplayer.getPlayer(login);
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
		
		this.collisions = new Collisions();
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
		setBallX(450-Utils.BALL_RADIUS/2);
		setBallY(350-Utils.BALL_RADIUS/2);
		//setBallSpeedX(5);
		//setBallSpeedY(0);
		setBallSpeedX(( Math.random() > 0.5 ? (int)(Math.random()*Utils.MAX_INITIAL_SPEED) + 2 : (-1)*(int)(Math.random()*Utils.MAX_INITIAL_SPEED) - 2 ));
		setBallSpeedY(( Math.random() > 0.5 ? (int)(Math.random()*Utils.MAX_INITIAL_SPEED) + 2 : (-1)*(int)(Math.random()*Utils.MAX_INITIAL_SPEED) - 2 ));
		Thread t = new Thread(new RefreshBallPosition(this));
		t.start();
	}

	public float getBallY() {
		return ballY;
	}

	public void setBallY(float ballY) {
		this.ballY = ballY;
	}

	public float getBallSpeedY() {
		return ballSpeedY;
	}

	public void setBallSpeedY(float ballSpeedY) {
		this.ballSpeedY = ballSpeedY;
	}

	public float getBallX() {
		return ballX;
	}

	public void setBallX(float ballX) {
		this.ballX = ballX;
	}

	public float getBallSpeedX() {
		return ballSpeedX;
	}

	public void setBallSpeedX(float ballSpeedX) {
		this.ballSpeedX = ballSpeedX;
	}

	public void addBallX(float ballX2) {
		ballX += ballX2;
	}

	public void addBallY(float ballY2) {
		ballY += ballY2;
	}

	public void verifGoal() {
		if( ballY >= Utils.HEIGHT/2-Utils.GOAL_SIZE/2 && ballY <= Utils.HEIGHT/2+Utils.GOAL_SIZE/2 ){
			if( ballX >= Utils.WIDTH / 2 )
				leftScore++;
			else
				rightScore++;
		}
	}

	public CollisionType testCollisions() {
		collisions.setBallPosition(ballX, ballY, ballSpeedX, ballSpeedY);
		for( int i = 0; i < 2; i++ ){
			CollisionType gardien = collisions.testCollisions(rodPositions[i].get(RodPositions.GARDIEN),RodPositions.GARDIEN);
			if( gardien != null ){
				return gardien;
			}
			
			CollisionType defense = collisions.testCollisions(rodPositions[i].get(RodPositions.DEFENSE),RodPositions.DEFENSE);
			if( defense != null ){
				return defense;
			}
			
			CollisionType milieu = collisions.testCollisions(rodPositions[i].get(RodPositions.MILIEU),RodPositions.MILIEU);
			if( milieu != null ){
				return milieu;
			}
			
			CollisionType attaque = collisions.testCollisions(rodPositions[i].get(RodPositions.ATTAQUE),RodPositions.ATTAQUE); 
			if( attaque != null ){
				return attaque;
			}
		}
		return null;
	}

	public void shoot(String rod, String side) {
		if( side.equals("DOWN") ){
			if( rod.equals("GARDIEN") ){
				int yPosition = Utils.getYPositionPlayer(rodPositions, RodPositions.GARDIEN, 1, 1, Sides.DOWN);
				if( ballX >= Utils.GARDIEN_POSITION && ballX <= Utils.GARDIEN_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition ){
					ballSpeedX = 15;
					ballSpeedY *= -1;
					System.out.println("GARDIEN !");
				}
			}else if( rod.equals("DEFENSE") ){
				boolean test = false;
				for( int i = 1; i <= 2; i++ ){
					int yPosition = Utils.getYPositionPlayer(rodPositions, RodPositions.DEFENSE, i, 2, Sides.DOWN);
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.DEFENSE_POSITION && ballX <= Utils.DEFENSE_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = 15;
					ballSpeedY *= -1;
					System.out.println("DEFENSE !");
				}
			}else if( rod.equals("MILIEU") ){
				boolean test = false;
				for( int i = 1; i <= 5; i++ ){
					int yPosition = Utils.getYPositionPlayer(rodPositions, RodPositions.MILIEU, i, 5, Sides.DOWN);
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.MILIEU_POSITION && ballX <= Utils.MILIEU_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = 15;
					ballSpeedY *= -1;
					System.out.println("MILIEU !");
				}
			}else if( rod.equals("ATTAQUE") ){
				boolean test = false;
				for( int i = 1; i <= 3; i++ ){
					int yPosition = Utils.getYPositionPlayer(rodPositions, RodPositions.ATTAQUE, i, 3, Sides.DOWN);
					test |= ( ballY >= yPosition - Utils.BALL_RADIUS && ballY <= Utils.BALL_RADIUS + Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.ATTAQUE_POSITION && ballX <= Utils.ATTAQUE_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = 15;
					ballSpeedY *= -1;
					System.out.println("Attaque !");
				}
			}
		}else{
			
		}
	}
	
	

	public boolean isSlow() {
		return noSlow;
	}

	public void setSlow(boolean slow) {
		this.noSlow = slow;
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
				//Si on a atteint le bord extérieur gauche et que la vitesse est bien négative (donc vers la gauche), on change de vitesse.
				if( ( match.getBallX() -5 - Utils.BALL_RADIUS ) <=  ( Utils.GAP_EDGE + Utils.LINE_STRENGTH )  ) {
					if( match.getBallSpeedX() < 0 )
						match.setBallSpeedX((-1)*match.getBallSpeedX()+( match.isSlow() ? 0 : match.getBallSpeedX()/Math.abs(match.getBallSpeedX())));
					match.verifGoal();
				//Si on a atteint le bord extérieur droit et que la vitesse est bien positive (donc vers la droite), on change de vitesse
				}else if( ( match.getBallX()+5 ) >=  ( Utils.WIDTH - Utils.GAP_EDGE - Utils.LINE_STRENGTH - Utils.BALL_RADIUS ) ){
					if( match.getBallSpeedX() > 0 )
						match.setBallSpeedX((-1)*match.getBallSpeedX()+( match.isSlow() ? 0 : match.getBallSpeedX()/Math.abs(match.getBallSpeedX())));
					match.verifGoal();
				}
				//Si on a atteint le bord extérieur haut et que la vitesse est bien négative (donc vers le haut), on change de vitesse.
				if( ( match.getBallY()-5 - Utils.BALL_RADIUS  ) <=  ( Utils.GAP_EDGE + Utils.LINE_STRENGTH ) ){
					if( match.getBallSpeedY() < 0 )
						match.setBallSpeedY((-1)*match.getBallSpeedY()+( match.isSlow() ? 0 : match.getBallSpeedY()/Math.abs(match.getBallSpeedY())));
				//Si on a atteint le bord extérieur bas et que la vitesse est bien positive (donc vers le bas), on change de vitesse
				}else if( ( match.getBallY() +5+ Utils.BALL_RADIUS  ) >=  ( Utils.HEIGHT - Utils.GAP_EDGE - Utils.LINE_STRENGTH ) ) {
					if( match.getBallSpeedY() > 0 )
						match.setBallSpeedY((-1)*match.getBallSpeedY()+( match.isSlow() ? 0 : match.getBallSpeedY()/Math.abs(match.getBallSpeedY())));
				}
				
				CollisionType resultatsCollisions = match.testCollisions();
				if( resultatsCollisions == CollisionType.SIDES )
					match.setBallSpeedX((-1)*match.getBallSpeedX()+( match.isSlow() ? 0 : 2*match.getBallSpeedX()/7));
				else if( resultatsCollisions == CollisionType.UPANDDOWN )
					match.setBallSpeedY((-1)*match.getBallSpeedY()+( match.isSlow() ? 0 : 2*match.getBallSpeedY()/7));
				
				match.addBallX(match.getBallSpeedX());
				match.addBallY(match.getBallSpeedY());
				Thread.sleep(50);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
