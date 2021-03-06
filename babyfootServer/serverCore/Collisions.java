package serverCore;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import clientCore.Utils;
import clientCore.Utils.CollisionType;
import clientCore.Utils.Rod;
import clientCore.Utils.Sides;
import clientCore.Utils.*;

public class Collisions {
	
	private float ballX;
	private float ballY;
	private float ballSpeedX;
	private float ballSpeedY;
	
	private Hashtable<Sides, Hashtable<Rod, Long>> lastCollision;
	
	@SuppressWarnings("unchecked")
	private Hashtable<Rod, Integer>[] rodPositions = new Hashtable[2] ;
	
	public Collisions() {
		lastCollision = new Hashtable<Sides, Hashtable<Rod, Long>>();
		Hashtable<Rod, Long> tUp = new Hashtable<Rod, Long>();
		Hashtable<Rod, Long> tDown = new Hashtable<Rod, Long>();
		
		lastCollision.put(Sides.UP, tUp);
		lastCollision.put(Sides.DOWN, tDown);
		zeroLast();
	}
	
	private void zeroLast(){
		lastCollision.get(Sides.UP).put(Rod.GARDIEN, 0L);
		lastCollision.get(Sides.UP).put(Rod.DEFENSE, 0L);
		lastCollision.get(Sides.UP).put(Rod.MILIEU, 0L);
		lastCollision.get(Sides.UP).put(Rod.ATTAQUE, 0L);
		lastCollision.get(Sides.DOWN).put(Rod.GARDIEN, 0L);
		lastCollision.get(Sides.DOWN).put(Rod.DEFENSE, 0L);
		lastCollision.get(Sides.DOWN).put(Rod.MILIEU, 0L);
		lastCollision.get(Sides.DOWN).put(Rod.ATTAQUE, 0L);
	}
	
	public CollisionType testCollisions(Integer position, Rod rod){
		CollisionType rodBottom = null;
		CollisionType rodTop = null;
		rodTop = testCollisionsTop(position, rod);
		rodBottom = testCollisionsBottom(position, rod);
		if( rodTop != null || rodBottom != null ){
			Sides side = ( rodTop != null ? Sides.UP : Sides.DOWN );
			Sides otherSide = ( rodTop != null ? Sides.DOWN : Sides.UP );
			if( (lastCollision.get(side)).get(rod) > 0L && System.currentTimeMillis() - (lastCollision.get(side)).get(rod) < 500 ){
				if( (lastCollision.get(otherSide)).get(rod) > System.currentTimeMillis() - 500 ) {
					(lastCollision.get(otherSide)).put(rod,System.currentTimeMillis());
				}else{
					//System.out.println("Avortée !" + side + " - " + rod);
					return null;
				}
				//System.out.println("Avortée - " + side + " - " + rod);
				return null;
			}
			else{
				//zeroLast();
				(lastCollision.get(side)).put(rod,System.currentTimeMillis());
			}//*/
		}
		return ( rodTop == null ? rodBottom : rodTop );
	}
	
	public CollisionType testCollisionsTop(Integer position, Rod rod){
		//y final : y + i*h/(1+nb)-Utils.IMAGE_PLAYER_Y/2 + yDecal[rightPlayer ? 1 : 0].get(rod)-Utils.Y_STAGGERING_DEFAULT.get(rod)
		// i va de 1 à nb où nb est le nombre de joueurs sur une barre
		float yTopHitBox = position + Utils.GAP_EDGE;
		float xLeftHitBox = 0;
		CollisionType ballPosition = null;
		switch(rod){
		//Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3
			case GARDIEN:
				//yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN);
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 1 );
				xLeftHitBox = Utils.WIDTH - (Utils.GARDIEN_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case DEFENSE:
				//yTopHitBox = position + Utils.GAP_EDGE + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE);
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 2 );
				xLeftHitBox = Utils.WIDTH - (Utils.DEFENSE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				//yTopHitBox = position + 2*Utils.HEIGHT/3-(float)(Utils.IMAGE_PLAYER_Y/2) - Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE);
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case MILIEU:
				xLeftHitBox = Utils.WIDTH - (Utils.MILIEU_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				for( int i = 1; i < 6; i++ ){
					//yTopHitBox = position + i*Utils.HEIGHT/6-(float)(Utils.IMAGE_PLAYER_Y/2) - Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU);
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 5 );
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.WIDTH - (Utils.ATTAQUE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
				for( int i = 1; i < 4; i++ ){
					//yTopHitBox = position + i*Utils.HEIGHT/4-(float)(Utils.IMAGE_PLAYER_Y/2) - Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE);
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 3 );
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
		}
		return null;
	}
	
	public CollisionType testCollisionsBottom(Integer position, Rod rod){
		float yTopHitBox = position + Utils.GAP_EDGE;
		float xLeftHitBox = 0;
		CollisionType ballPosition = null;
		switch(rod){
			case GARDIEN:
				//yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN);
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 1 );
				xLeftHitBox = Utils.GARDIEN_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case DEFENSE:
				//yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE);
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 2 );
				xLeftHitBox = Utils.DEFENSE_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				//yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE);
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 2, 2 );
				ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
				if( ballPosition != null ) 
					return ballPosition;
				break;
			case MILIEU:
				xLeftHitBox = Utils.MILIEU_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 6; i++ ){
					//yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU);
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 5 );
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.ATTAQUE_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 4; i++ ){
					//yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE);
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 3 );
					ballPosition = isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + Utils.IMAGE_PLAYER_X, yTopHitBox + Utils.IMAGE_PLAYER_Y );
					if( ballPosition != null ) 
						return ballPosition;
				}
				break;
		}
		return null;
	}
	
	public CollisionType isBallInCollision(float xLeftTop, float yLeftTop, float xRightBottom, float yRightBottom ){
		/*if( ( Math.pow( ballX - xLeftTop, 2) + Math.pow( ballY - yLeftTop, 2 ) ) <= Math.pow( Utils.BALL_RADIUS, 2 )
				|| ( Math.pow( ballX - xRightBottom, 2) + Math.pow( ballY - yRightBottom, 2 ) ) <= Math.pow( Utils.BALL_RADIUS, 2 ) ){
			if( Math.abs(ballX - Utils.BALL_RADIUS - xLeftTop) / Utils.IMAGE_PLAYER_X <= Math.abs(ballY - Utils.BALL_RADIUS  - yLeftTop) / Utils.IMAGE_PLAYER_Y ) return CollisionType.UPANDDOWN;
			else return CollisionType.SIDES;
		}*/
		
		//On se trouve dans le coin de droite en bas.
		if( ballX - Utils.BALL_RADIUS <= xRightBottom && ballX - Utils.BALL_RADIUS >= xLeftTop 
				&& ballY - Utils.BALL_RADIUS <= yRightBottom && ballY - Utils.BALL_RADIUS >= yLeftTop )
			return verifWhichSide( ballX, ballY, xRightBottom, yRightBottom ) ? CollisionType.UPANDDOWN : CollisionType.SIDES;
		//On se trouve dans le coin de droite en haut.
		else if( ballX - Utils.BALL_RADIUS <= xRightBottom && ballX - Utils.BALL_RADIUS >= xLeftTop 
					&& ballY + Utils.BALL_RADIUS <= yRightBottom && ballY + Utils.BALL_RADIUS >= yLeftTop )
			return verifWhichSide( ballX, ballY, xRightBottom, yLeftTop ) ? CollisionType.UPANDDOWN : CollisionType.SIDES;
		//On se trouve dans le coin de gauche en haut.
		else if ( ballX + Utils.BALL_RADIUS <= xRightBottom && ballX + Utils.BALL_RADIUS >= xLeftTop 
					&& ballY + Utils.BALL_RADIUS <= yRightBottom && ballY + Utils.BALL_RADIUS >= yLeftTop )
			return verifWhichSide( ballX, ballY, xLeftTop, yLeftTop ) ? CollisionType.UPANDDOWN : CollisionType.SIDES;
		//On se trouve dans le coin de gauche en bas.
		else if ( ballX + Utils.BALL_RADIUS <= xRightBottom && ballX + Utils.BALL_RADIUS >= xLeftTop 
					&& ballY - Utils.BALL_RADIUS <= yRightBottom && ballY - Utils.BALL_RADIUS >= yLeftTop )
			return verifWhichSide( ballX, ballY, xLeftTop, yRightBottom ) ? CollisionType.UPANDDOWN : CollisionType.SIDES;
		return null;
	}
	
	public boolean verifWhichSide( float xb, float yb, float xc, float yc )
	{
		return ( Math.abs(( xb - xc ) / ( yb - yc )) <= 1 );
	}

	public void setBallPosition(float ballX, float ballY, float ballSpeedX, float ballSpeedY) {
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballSpeedX = ballSpeedX;
		this.ballSpeedY = ballSpeedY;
	}
}

