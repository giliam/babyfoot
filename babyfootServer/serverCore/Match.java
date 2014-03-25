package serverCore;

import java.util.Hashtable;

import clientCore.Utils;
import clientCore.Utils.Rod;
import clientCore.Utils.CollisionType;
import clientCore.Utils.RodStatus;
import clientCore.Utils.Sides;
import clientCore.Utils.States;
import clientCore.Utils.Types;

import serverNetwork.ServerBabyfoot;


public class Match {
	private int leftScore;
	private int rightScore;
	
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

	private boolean noSlow = true;
	
	private boolean pause = false;

	private final int STEP_X = 2;
	private final int STEP_Y = 2;
	
	private int status = 0;
	
	@SuppressWarnings("unchecked")
	private Hashtable<Rod, Integer>[] yRodPositions = new Hashtable[2];
	private RefreshBallPosition tRefresh;
	private Hashtable<Rod, RodStatus>[] rodStatus = new Hashtable[2];
	
	
	
	@SuppressWarnings("unchecked")
	public Match(String login, Types type) {
		this.type = type;
		
		this.player1 = ServerBabyfoot.tplayer.getPlayer(login);
		this.player1.setMatch(this);
		setStatus(this.player1.updateRods(0));
		
		this.yRodPositions = new Hashtable[2];
		
		this.yRodPositions[0] = new Hashtable<Rod, Integer>();
		this.yRodPositions[1] = new Hashtable<Rod, Integer>();
		
		this.yRodPositions[0].put( Rod.GARDIEN, 100 );
		this.yRodPositions[0].put( Rod.DEFENSE, 150 );
		this.yRodPositions[0].put( Rod.MILIEU, 100 );
		this.yRodPositions[0].put( Rod.ATTAQUE, 100 );
		
		this.yRodPositions[1].put( Rod.GARDIEN, 100 );
		this.yRodPositions[1].put( Rod.DEFENSE, 150 );
		this.yRodPositions[1].put( Rod.MILIEU, 100 );
		this.yRodPositions[1].put( Rod.ATTAQUE, 100 );
		
		this.rodStatus = new Hashtable[2];
		
		this.rodStatus[0] = new Hashtable<Rod, RodStatus>();
		this.rodStatus[1] = new Hashtable<Rod, RodStatus>();
		
		this.rodStatus[0].put( Rod.GARDIEN, RodStatus.NORMAL );
		this.rodStatus[0].put( Rod.DEFENSE, RodStatus.NORMAL );
		this.rodStatus[0].put( Rod.MILIEU, RodStatus.NORMAL );
		this.rodStatus[0].put( Rod.ATTAQUE, RodStatus.NORMAL );
		
		this.rodStatus[1].put( Rod.GARDIEN, RodStatus.NORMAL );
		this.rodStatus[1].put( Rod.DEFENSE, RodStatus.NORMAL );
		this.rodStatus[1].put( Rod.MILIEU, RodStatus.NORMAL );
		this.rodStatus[1].put( Rod.ATTAQUE, RodStatus.NORMAL );
		
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

	public Hashtable<Rod, Integer>[] getRodPositions() {
		return yRodPositions;
	}
	
	/*public void setRodPositions(Hashtable<RodPositions, Integer>[] yRodPositions) {
		this.yRodPositions = yRodPositions;
	}*/
	
	public void setRodPositions(Hashtable<Rod, Integer> yRodPositions, Hashtable<Rod, RodStatus> statusPositions, int i) {
		this.yRodPositions[i] = yRodPositions;
		this.rodStatus[i] = statusPositions;
	}

	public int addPlayer(Player p) {
		if( player1 == null ){
			player1 = p;
			p.updateSide(1);
			setStatus(p.updateRods(getStatus()));
		}else if( player2 == null ){
			player2 = p;
			p.updateSide(2);
			setStatus(p.updateRods(getStatus()));
		}else if( player3 == null ){
			player3 = p;
			p.updateSide(3);
			setStatus(p.updateRods(getStatus()));
		}else if( player4 == null ){
			player4 = p;
			p.updateSide(4);
			setStatus(p.updateRods(getStatus()));
		}
		return p.getStatus();
	}
	
	
	public int countPlayers() {
		int i = 0;
		if( player1 != null )
			i++;
		if( player2 != null )
			i++;
		if( player3 != null )
			i++;
		if( player4 != null )
			i++;
		
		return i;
	}

	public void removePlayer(String login) {
		if( player1 != null && player1.getLogin().equals(login) ){
			player1 = null;
		}else if( player2 != null && player2.getLogin().equals(login) ){
			player2 = null;
		}else if( player3 != null && player3.getLogin().equals(login) ){
			player3 = null;
		}else if( player4 != null && player4.getLogin().equals(login) ){
			player4 = null;
		}
	}

	public int getRodPosition(boolean b, Rod i) {
		return yRodPositions[b ? 1 : 0].get(i);
	}
	
	public RodStatus getRodStatus(boolean b, Rod i) {
		return rodStatus[b ? 1 : 0].get(i);
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
		if( verifPlayers() ){
			setBallX(450-Utils.BALL_RADIUS/2);
			setBallY(350-Utils.BALL_RADIUS/2);
			//setBallX(130);
			//setBallY(265);
			//setBallSpeedX(-5);
			//setBallSpeedY(0);
			setBallSpeedX(( Math.random() > 0.5 ? (int)(Math.random()*Utils.MAX_INITIAL_SPEED) + 2 : (-1)*(int)(Math.random()*Utils.MAX_INITIAL_SPEED) - 2 ));
			setBallSpeedY(( Math.random() > 0.5 ? (int)(Math.random()*Utils.MAX_INITIAL_SPEED) + 2 : (-1)*(int)(Math.random()*Utils.MAX_INITIAL_SPEED) - 2 ));
			tRefresh = new RefreshBallPosition(this);
			Thread t = new Thread(tRefresh);
			t.start();
		}
	}

	private boolean verifPlayers() {
		switch( type ){
			case ONEVSONE:
				return player1 != null && player2 != null;
			case ONEVSTWO:
				return player1 != null && player2 != null && player3 != null;
			case TWOVSTWO:
				return player1 != null && player2 != null && player3 != null && player4 != null;
		}
		return false;
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
		if( ballY >= Utils.HEIGHT/2-Utils.GOAL_SIZE/2+10 && ballY <= Utils.HEIGHT/2+Utils.GOAL_SIZE/2-10 ){
			//System.out.println(leftScore + " - " + rightScore);
			if( ballX >= Utils.WIDTH / 2 )
				leftScore++;
			else
				rightScore++;
			tRefresh.setRun(false);
			start();
		}
	}

	public CollisionType testCollisions() {
		collisions.setBallPosition(ballX, ballY, ballSpeedX, ballSpeedY);
		for( int i = 0; i < 2; i++ ){
			CollisionType gardien = collisions.testCollisions(yRodPositions[i].get(Rod.GARDIEN),Rod.GARDIEN);
			if( gardien != null ){
				return gardien;
			}
			
			CollisionType defense = collisions.testCollisions(yRodPositions[i].get(Rod.DEFENSE),Rod.DEFENSE);
			if( defense != null ){
				return defense;
			}
			
			CollisionType milieu = collisions.testCollisions(yRodPositions[i].get(Rod.MILIEU),Rod.MILIEU);
			if( milieu != null ){
				return milieu;
			}
			
			CollisionType attaque = collisions.testCollisions(yRodPositions[i].get(Rod.ATTAQUE),Rod.ATTAQUE); 
			if( attaque != null ){
				return attaque;
			}
		}
		return null;
	}

	public void shoot(String r, String s) {
		Sides side = Sides.valueOf(s);
		Rod rod = Rod.valueOf(r);
		if( side == Sides.DOWN ){
			if( rod == Rod.GARDIEN ){
				int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.GARDIEN, 1, 1, side );
				if( ballX >= Utils.GARDIEN_POSITION && ballX <= Utils.GARDIEN_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.DEFENSE ){
				boolean test = false;
				for( int i = 1; i <= 2; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.DEFENSE, i, 2, side );
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.DEFENSE_POSITION && ballX <= Utils.DEFENSE_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.MILIEU ){
				boolean test = false;
				for( int i = 1; i <= 5; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.MILIEU, i, 5, side );
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.MILIEU_POSITION && ballX <= Utils.MILIEU_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.ATTAQUE ){
				boolean test = false;
				for( int i = 1; i <= 3; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.ATTAQUE, i, 3, side );
					test |= ( ballY >= yPosition - Utils.BALL_RADIUS && ballY <= Utils.BALL_RADIUS + Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX >= Utils.ATTAQUE_POSITION && ballX <= Utils.ATTAQUE_POSITION + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}
		}else{
			if( rod == Rod.GARDIEN ){
				int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.GARDIEN, 1, 1, side);
				if( ballX + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS >= Utils.GARDIEN_POSITION  && ballX <= Utils.GARDIEN_POSITION 
						//&& ballSpeedX <= 0
						&& ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.DEFENSE ){
				boolean test = false;
				for( int i = 1; i <= 2; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.DEFENSE, i, 2, side);
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS >= Utils.DEFENSE_POSITION && ballX <= Utils.DEFENSE_POSITION 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.MILIEU ){
				boolean test = false;
				for( int i = 1; i <= 5; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.MILIEU, i, 5, side);
					test |= ( ballY >= yPosition && ballY <= Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS >= Utils.MILIEU_POSITION && ballX <= Utils.MILIEU_POSITION 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}else if( rod == Rod.ATTAQUE ){
				boolean test = false;
				for( int i = 1; i <= 3; i++ ){
					int yPosition = Utils.getYPositionPlayer(yRodPositions, Rod.ATTAQUE, i, 3, side);
					test |= ( ballY >= yPosition - Utils.BALL_RADIUS && ballY <= Utils.BALL_RADIUS + Utils.IMAGE_PLAYER_Y + yPosition );
					if( test ) break;
				}
				if( ballX + Utils.IMAGE_PLAYER_SHOOTING_X + Utils.BALL_RADIUS >= Utils.ATTAQUE_POSITION && ballX <= Utils.ATTAQUE_POSITION 
						//&& ballSpeedX <= 0
						&& test ){
					ballSpeedX = Utils.MAX_INITIAL_SPEED;
					ballSpeedY *= -0.1;
				}
			}
		}
		rodStatus[side == Sides.UP ? 1 : 0].put(rod, RodStatus.SHOOTING);
	}
	
	

	public boolean isSlow() {
		return noSlow;
	}

	public void setSlow(boolean slow) {
		this.noSlow = slow;
	}

	public String getBoss() {
		return player1.getLogin();
	}

	public boolean isPause() {
		return pause;
	}
	
	public void setPause(boolean s){
		pause = s;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

class RefreshBallPosition implements Runnable {
	private Match match;
	private boolean run;
	public RefreshBallPosition(Match m){
		match = m;
		setRun(true);
	}
	
	@Override
	public void run() {
		try{
			while(isRun()){
				if( !match.isPause() ){
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
						match.setBallSpeedX((-1)*match.getBallSpeedX()+( match.isSlow() ? 0 : 1*match.getBallSpeedX()/11));
					else if( resultatsCollisions == CollisionType.UPANDDOWN )
						match.setBallSpeedY((-1)*match.getBallSpeedY()+( match.isSlow() ? 0 : 1*match.getBallSpeedY()/11));
					
					match.addBallX(match.getBallSpeedX());
					match.addBallY(match.getBallSpeedY());
					Thread.sleep(25);
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
}
