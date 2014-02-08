package serverCore;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import clientCore.Utils;
import clientGui.GameZone.RodPositions;

public class Collisions {
	
	private int ballX;
	private int ballY;
	private int ballSpeedX;
	private int ballSpeedY;
	
	
	@SuppressWarnings("unchecked")
	private Hashtable<RodPositions, Integer>[] rodPositions = new Hashtable[2] ;
	
	public Collisions() {
	}
	
	public RodPositions testCollisions(Integer position, RodPositions rod){
		int yTopHitBox = position + Utils.GAP_EDGE;
		int xLeftHitBox = 0;
		switch(rod){
			case GARDIEN:
				yTopHitBox = position + Utils.GAP_EDGE + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2;
				xLeftHitBox = Utils.GAP_EDGE+30-Utils.IMAGE_PLAYER_X/3;
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y ) ) 
					return rod;
				break;
			case DEFENSE:
				yTopHitBox = position + Utils.GAP_EDGE + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2;
				xLeftHitBox = Utils.GAP_EDGE+30+100-Utils.IMAGE_PLAYER_X/3;
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y ) ) 
					return rod;
				yTopHitBox = position + Utils.GAP_EDGE + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2;
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y ) ) 
					return rod;
				break;
			case MILIEU:
				yTopHitBox += Utils.HEIGHT/6;
				break;
			case ATTAQUE:
				yTopHitBox += Utils.HEIGHT/4;
				break;
		}
		return rod;
	}
	
	public boolean isBallInCollision(int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom ){
		return ( ( ballX + Utils.BALL_RADIUS ) >= xLeftTop && ( ballX - Utils.BALL_RADIUS ) <= xRightBottom ) 
				&& ( ( ballY + Utils.BALL_RADIUS ) >= yLeftTop && ( ballY - Utils.BALL_RADIUS ) <= yRightBottom );
	}

	public void setBallPosition(int ballX, int ballY, int ballSpeedX, int ballSpeedY) {
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballSpeedX = ballSpeedX;
		this.ballSpeedY = ballSpeedY;
	}
}
