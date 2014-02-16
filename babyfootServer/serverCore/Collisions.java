package serverCore;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import clientCore.Utils;
import clientGui.GameZone.RodPositions;

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
	
	public RodPositions testCollisions(Integer position, RodPositions rod){
		yDecalDefault = new Hashtable<RodPositions, Integer>();
		yDecalDefault.put(RodPositions.GARDIEN, 100);
		yDecalDefault.put(RodPositions.DEFENSE, 150);
		yDecalDefault.put(RodPositions.MILIEU, 100);
		yDecalDefault.put(RodPositions.ATTAQUE, 100);
		RodPositions rodBottom = null;
		RodPositions rodTop = testCollisionsTop(position, rod);
		//rodBottom = testCollisionsBottom(position, rod);
		if( rodTop != null ) System.out.println("TOP" + rod);
		if( rodBottom != null ) System.out.println("BOTTOM");
		if( rodTop != null ||rodBottom != null ){
			if( lastCollision > System.currentTimeMillis() - 1000 ) return null;
			else lastCollision = System.currentTimeMillis();
		}
		return ( rodTop == null ? rodBottom : rodTop );
	}
	
	public RodPositions testCollisionsTop(Integer position, RodPositions rod){
		int yTopHitBox = position + Utils.GAP_EDGE;
		int xLeftHitBox = 0;
		switch(rod){
			case GARDIEN:
				yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.GARDIEN);
				xLeftHitBox = Utils.WIDTH - (Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3);
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				break;
			case DEFENSE:
				yTopHitBox = position + Utils.GAP_EDGE + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				xLeftHitBox = Utils.WIDTH - (Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3);
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				break;
			case MILIEU:
				xLeftHitBox = Utils.WIDTH - (Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 6; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.MILIEU);
					if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
						return rod;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.WIDTH - (Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 4; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.ATTAQUE);
					if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
						return rod;
				}
				break;
		}
		return null;
	}
	
	public RodPositions testCollisionsBottom(Integer position, RodPositions rod){
		int yTopHitBox = position + Utils.GAP_EDGE;
		int xLeftHitBox = 0;
		switch(rod){
			case GARDIEN:
				yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.GARDIEN);
				xLeftHitBox = Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3;
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				break;
			case DEFENSE:
				yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				xLeftHitBox = Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3;
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.DEFENSE);
				if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
					return rod;
				break;
			case MILIEU:
				xLeftHitBox = Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3;
				for( int i = 1; i < 6; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.MILIEU);
					if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + 2*Utils.IMAGE_PLAYER_Y/3 ) ) 
						return rod;
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3;
				for( int i = 1; i < 4; i++ ){
					yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - yDecalDefault.get(RodPositions.ATTAQUE);
					if( isBallInCollision( xLeftHitBox, yTopHitBox, xLeftHitBox + 2*Utils.IMAGE_PLAYER_X/3, yTopHitBox + Utils.IMAGE_PLAYER_Y ) ) 
						return rod;
				}
				break;
		}
		return null;
	}
	
	public boolean isBallInCollision(float xLeftTop, float yLeftTop, float xRightBottom, float yRightBottom ){
		return ( ( ballX + Utils.BALL_RADIUS ) >= xLeftTop && ( ballX - Utils.BALL_RADIUS ) <= xRightBottom ) 
				&& ( ( ballY + Utils.BALL_RADIUS ) >= yLeftTop && ( ballY - Utils.BALL_RADIUS ) <= yRightBottom );
	}

	public void setBallPosition(float ballX, float ballY, float ballSpeedX, float ballSpeedY) {
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballSpeedX = ballSpeedX;
		this.ballSpeedY = ballSpeedY;
	}
}
