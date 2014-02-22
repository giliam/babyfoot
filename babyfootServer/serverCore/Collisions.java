package serverCore;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import clientCore.Utils;
import clientCore.Utils.CollisionType;
import clientCore.Utils.RodPositions;

public class Collisions {
	
	private float ballX;
	private float ballY;
	private float ballSpeedX;
	private float ballSpeedY;
	
	private Hashtable<RodPositions, Integer> yDecalDefault;
	
	private long lastCollision;
	
	@SuppressWarnings("unchecked")
	private Hashtable<RodPositions, Integer>[] rodPositions = new Hashtable[2] ;
	
	public Collisions() {
	}
	
	public CollisionType testCollisions(Integer position, RodPositions rod){
		yDecalDefault = new Hashtable<RodPositions, Integer>();
		yDecalDefault.put(RodPositions.GARDIEN, 100);
		yDecalDefault.put(RodPositions.DEFENSE, 150);
		yDecalDefault.put(RodPositions.MILIEU, 100);
		yDecalDefault.put(RodPositions.ATTAQUE, 100);
		CollisionType rodBottom = null;
		CollisionType rodTop = null;
		rodTop = testCollisionsTop(position, rod);
		//rodBottom = testCollisionsBottom(position, rod);
		if( rodTop != null ) System.out.println("TOP" + rod);
		if( rodBottom != null ) System.out.println("BOTTOM" + rod);//*/
		if( rodTop != null || rodBottom != null ){
			if( lastCollision > System.currentTimeMillis() - 1000 ) return null;
			else lastCollision = System.currentTimeMillis();
		}
		return ( rodTop == null ? rodBottom : rodTop );
	}
	
	public CollisionType testCollisionsTop(Integer position, RodPositions rod){
		//y final : y + i*h/(1+nb)-Utils.IMAGE_PLAYER_Y/2 + yDecal[rightPlayer ? 1 : 0].get(rod)-Utils.Y_STAGGERING_DEFAULT.get(rod)
		// i va de 1 à nb où nb est le nombre de joueurs sur une barre
		float yTopHitBox = position + Utils.GAP_EDGE;
		float xLeftHitBox = 0;
		CollisionType ballPosition = null;
		switch(rod){
		//Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3
			case GARDIEN:
				yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.GARDIEN);
				xLeftHitBox = Utils.WIDTH - (Utils.GARDIEN_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case DEFENSE:
				yTopHitBox = position + Utils.GAP_EDGE + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				xLeftHitBox = Utils.WIDTH - (Utils.DEFENSE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				yTopHitBox = position + 2*Utils.HEIGHT/3-(float)(Utils.IMAGE_PLAYER_Y/2) - yDecalDefault.get(RodPositions.DEFENSE);
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case MILIEU:
				xLeftHitBox = Utils.WIDTH - (Utils.MILIEU_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				for( int i = 1; i < 6; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/6-(float)(Utils.IMAGE_PLAYER_Y/2) - yDecalDefault.get(RodPositions.MILIEU);
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.WIDTH - (Utils.ATTAQUE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				for( int i = 1; i < 4; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/4-(float)(Utils.IMAGE_PLAYER_Y/2) - yDecalDefault.get(RodPositions.ATTAQUE);
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
		}
		return null;
	}
	
	public CollisionType testCollisionsBottom(Integer position, RodPositions rod){
		int yTopHitBox = position + Utils.GAP_EDGE;
		int xLeftHitBox = 0;
		CollisionType ballPosition = null;
		switch(rod){
			case GARDIEN:
				yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.GARDIEN);
				xLeftHitBox = Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3;
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*(float)(Utils.IMAGE_PLAYER_X/3), yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case DEFENSE:
				yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				xLeftHitBox = Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3;
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*(float)(Utils.IMAGE_PLAYER_X/3), yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*(float)(Utils.IMAGE_PLAYER_X/3), yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case MILIEU:
				xLeftHitBox = Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3;
				for( int i = 1; i < 6; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.MILIEU);
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*(float)(Utils.IMAGE_PLAYER_X/3), yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3;
				for( int i = 1; i < 4; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.ATTAQUE);
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*(float)(Utils.IMAGE_PLAYER_X/3), yTopHitBox + Utils.IMAGE_PLAYER_Y/2 );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
		}
		return null;
	}
	
	public CollisionType isBallInCollision(float xLeftTop, float yLeftTop, float xRightBottom, float yRightBottom ){
		if( ( Math.pow( ballX - xLeftTop, 2) + Math.pow( ballY - yLeftTop, 2 ) ) <= Math.pow( Utils.BALL_RADIUS, 2 )
				|| ( Math.pow( ballX - xRightBottom, 2) + Math.pow( ballY - yRightBottom, 2 ) ) <= Math.pow( Utils.BALL_RADIUS, 2 ) ){
			if( Math.abs(ballX - Utils.BALL_RADIUS - xLeftTop) / Utils.IMAGE_PLAYER_X <= Math.abs(ballY - Utils.BALL_RADIUS  - yLeftTop) / Utils.IMAGE_PLAYER_Y ) return CollisionType.UPANDDOWN;
			else return CollisionType.SIDES;
		}
		return null;
	}

	public void setBallPosition(float ballX, float ballY, float ballSpeedX, float ballSpeedY) {
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballSpeedX = ballSpeedX;
		this.ballSpeedY = ballSpeedY;
	}
}
