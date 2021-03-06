package clientGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import clientCore.ClientBabyfoot;
import clientCore.Player;
import clientCore.Utils;
import clientCore.Utils.CollisionType;
import clientCore.Utils.RodStatus;
import clientCore.Utils.Sides;
import clientCore.Utils.Rod;
import clientNetwork.GameClient;
import clientNetwork.MatchClient;


public class GameZone extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Taille des buts en pixels et épaisseur des traits
	 */
	private int oldY = 0;
	
	private Hashtable<Rod, Integer>[] yPosition;
	
	private Hashtable<Rod, Boolean> rodPositions;
	
	private Hashtable<Rod, RodStatus>[] rodStatus;
	
	Rod rodPosition;
	
	private int ballX;
	private int ballY;
	
	
	private GamePanel gamepanel;
	private InfoZone infoZone;
	
	private long shootBeginning;
	private boolean pause = false;
	private Sides side;
	private int lastKeyY;
	private Sides toNormalSide;
	private Rod toNormalRod;
	
	@SuppressWarnings("unchecked")
	public GameZone(GamePanel window, boolean testMode, int width){
		this.gamepanel = window;
		
		this.setSide(getGamePanel().getWindow().getPlayer().getSide());
		setSize(width,729);
		
		infoZone = new InfoZone(this);
		setRodStatus(new Hashtable[2]);
		rodStatus[0] = new Hashtable<Rod, RodStatus>();
		rodStatus[0].put(Rod.GARDIEN, RodStatus.NORMAL);
		rodStatus[0].put(Rod.DEFENSE, RodStatus.NORMAL);
		rodStatus[0].put(Rod.MILIEU, RodStatus.NORMAL);
		rodStatus[0].put(Rod.ATTAQUE, RodStatus.NORMAL);
		rodStatus[1] = new Hashtable<Rod, RodStatus>();
		rodStatus[1].put(Rod.GARDIEN, RodStatus.NORMAL);
		rodStatus[1].put(Rod.DEFENSE, RodStatus.NORMAL);
		rodStatus[1].put(Rod.MILIEU, RodStatus.NORMAL);
		rodStatus[1].put(Rod.ATTAQUE, RodStatus.NORMAL);
		
		yPosition = new Hashtable[2];
		yPosition[0] = new Hashtable<Rod, Integer>();
		yPosition[0].put(Rod.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN));
		yPosition[0].put(Rod.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE));
		yPosition[0].put(Rod.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU));
		yPosition[0].put(Rod.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE));
		yPosition[1] = new Hashtable<Rod, Integer>();
		yPosition[1].put(Rod.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN));
		yPosition[1].put(Rod.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE));
		yPosition[1].put(Rod.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU));
		yPosition[1].put(Rod.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE));
		
		rodPosition = ( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(Rod.MILIEU) ? Rod.MILIEU : Rod.GARDIEN );
	    setPreferredSize(new Dimension(width,729));
	    setMinimumSize(new Dimension(width,729));
	    
	    addKeyListener(this);
	    addMouseListener(this);
	    addMouseMotionListener(this);
	    
	    setFocusable(true);
	    requestFocusInWindow();
	    
	    if( !testMode ){
		    Thread tr = new Thread(new RefreshRods(this));
		    tr.start();
	    }
	}
	

	public void paint(Graphics g){
		g.setColor(new Color(116,152,29));
		//g.fillRect(0,0,getWidth(),getHeight());
		try {
	      Image img = ImageIO.read(new File("pictures/terrain.png"));
	      g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
	      e.printStackTrace();
	    }   
		
		initInfoZone();
		drawLines(g);
		drawBall(g);
		drawGoals(g);
		drawPlayers(g);
		drawRodPosition(g);
	}
	
	private void initInfoZone(){
		infoZone.getLeftScore().setBackground(Color.RED);
		infoZone.getRightScore().setBackground(Color.BLUE);
		gamepanel.repaint();
	}
	
	private void drawBall(Graphics g) {
		//Côté gauche
		g.setColor(Color.WHITE);
		g.fillOval(ballX-Utils.BALL_RADIUS, ballY-Utils.BALL_RADIUS, Utils.BALL_RADIUS*2, Utils.BALL_RADIUS*2);
	}


	private void drawGoals(Graphics g){
		//Côté gauche
		g.setColor(Color.RED);
		//Long côté
		g.fillRect(Utils.GAP_EDGE/2,Utils.HEIGHT/2-Utils.GOAL_SIZE/2,Utils.LINE_STRENGTH,Utils.GOAL_SIZE);
		//petits côtés
		g.fillRect(Utils.GAP_EDGE/2,Utils.HEIGHT/2-Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		g.fillRect(Utils.GAP_EDGE/2,Utils.HEIGHT/2+Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		//Côté droit
		g.setColor(Color.BLUE);
		//Long côté
		g.fillRect(Utils.WIDTH-Utils.GAP_EDGE/2-Utils.LINE_STRENGTH,Utils.HEIGHT/2-Utils.GOAL_SIZE/2,Utils.LINE_STRENGTH,Utils.GOAL_SIZE);
		//petits côtés
		g.fillRect(Utils.WIDTH-Utils.GAP_EDGE,Utils.HEIGHT/2-Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		g.fillRect(Utils.WIDTH-Utils.GAP_EDGE,Utils.HEIGHT/2+Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
	}
	
	private void drawLines(Graphics g){
		try {
	      Image img = ImageIO.read(new File("pictures/bordhaut.png"));
	      g.drawImage(img, 0, 0, this);
		} catch (IOException e) {
	      e.printStackTrace();
	    }
		try {
	      Image img = ImageIO.read(new File("pictures/bordbas.png"));
	      g.drawImage(img, 0, Utils.HEIGHT - 46, this);
		} catch (IOException e) {
	      e.printStackTrace();
	    }
		
		g.setColor(Color.WHITE);
		//Milieu de terrain
		g.fillRect((Utils.WIDTH-Utils.LINE_STRENGTH)/2,46,Utils.LINE_STRENGTH,Utils.HEIGHT-92);
		//gauche
		g.fillRect(Utils.GAP_EDGE,46,Utils.LINE_STRENGTH,Utils.HEIGHT-92);
		//haut
		//g.fillRect(0,Utils.GAP_EDGE,Utils.WIDTH,Utils.LINE_STRENGTH);
		//droite
		g.fillRect(Utils.WIDTH-Utils.GAP_EDGE-Utils.LINE_STRENGTH,46,Utils.LINE_STRENGTH,Utils.HEIGHT-92);
		//bas
		//g.fillRect(0,Utils.HEIGHT-Utils.GAP_EDGE-Utils.LINE_STRENGTH,Utils.WIDTH,Utils.LINE_STRENGTH);
	}
	
	private void drawPlayers(Graphics g){
		drawPlayer(g, Utils.GARDIEN_POSITION, 0, 1, Color.RED, Sides.DOWN, Rod.GARDIEN);
		drawPlayer(g, Utils.DEFENSE_POSITION, 0, 2, Color.RED, Sides.DOWN, Rod.DEFENSE);
		drawPlayer(g, Utils.MILIEU_POSITION, 0, 5, Color.RED, Sides.DOWN, Rod.MILIEU);
		drawPlayer(g, Utils.ATTAQUE_POSITION, 0, 3, Color.RED, Sides.DOWN, Rod.ATTAQUE);
		
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GARDIEN_POSITION, 0, 1, Color.RED, Sides.UP, Rod.GARDIEN);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.DEFENSE_POSITION, 0, 2, Color.RED, Sides.UP, Rod.DEFENSE);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.MILIEU_POSITION, 0, 5, Color.RED, Sides.UP, Rod.MILIEU);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.ATTAQUE_POSITION, 0, 3, Color.RED, Sides.UP, Rod.ATTAQUE);
	}
	
	/**
	 * side : true = orienté vers la gauche, false orienté vers la droite.
	 * position : quelle est la position de la barre ? tir, droit, etc. 
	 */
	
	private void drawPlayer(Graphics g, int x, int y, int nb, Color color, Sides side, Rod rod ){
		RodStatus status = rodStatus[side == Sides.UP ? 1 : 0].get(rod);
		int position = status == RodStatus.HOLDING ? 3 : (status == RodStatus.NORMAL ? 1 : 2);
		g.setColor(new Color(192, 192, 192));
		g.fillRect(x,20,3*Utils.LINE_STRENGTH/2,Utils.HEIGHT-92+52);
		g.setColor(color);
		for( int i=1; i<=nb;i++){
			try {
				Image img = null;
				if(side == Sides.UP)
					img = ImageIO.read(new File("pictures/joueurdroit" + String.valueOf(position) + ".png"));
				else
					img = ImageIO.read(new File("pictures/joueurgauche" + String.valueOf(position) + ".png"));
				if(position==1)
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
				else if(position==2 && side == Sides.DOWN )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-30, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
				else if(position==2 && side == Sides.UP )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-60, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
				else if(position==3 && side == Sides.DOWN )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-40, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
				else if(position==3 && side == Sides.UP )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-40, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }                
		}
	}
	
	
	/*private void testCollisionsBottom(Integer position, Rod rod, Graphics g){
		float yTopHitBox = position + Utils.GAP_EDGE;
		float xLeftHitBox = 0;
		switch(rod){
			case GARDIEN:
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 1 );
				xLeftHitBox = Utils.GARDIEN_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				g.fillRect( (int)xLeftHitBox, (int)yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
				break;
			case DEFENSE:
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 1, 2 );
				xLeftHitBox = Utils.DEFENSE_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				g.fillRect( (int)xLeftHitBox, (int)yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
				yTopHitBox = Utils.getYPositionPlayer( position, rod, 2, 2 );
				g.fillRect( (int)xLeftHitBox, (int)yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
				break;
			case MILIEU:
				xLeftHitBox = Utils.MILIEU_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 6; i++ ){
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 5 );
					g.fillRect( (int)xLeftHitBox, (int)yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
				}
				break;
			case ATTAQUE:
				xLeftHitBox = Utils.ATTAQUE_POSITION-(float)(Utils.IMAGE_PLAYER_X/3);
				for( int i = 1; i < 4; i++ ){
					yTopHitBox = Utils.getYPositionPlayer( position, rod, i, 3 );
					g.fillRect( (int)xLeftHitBox, (int)yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
				}
				break;
		}
	}*/
	
	
	private void drawRodPosition(Graphics g){
		/*g.setColor(Color.BLACK);
		testCollisionsBottom(yPosition[0].get(Rod.GARDIEN), Rod.GARDIEN, g);
		testCollisionsBottom(yPosition[0].get(Rod.DEFENSE), Rod.DEFENSE, g);
		testCollisionsBottom(yPosition[0].get(Rod.ATTAQUE), Rod.ATTAQUE, g);
		testCollisionsBottom(yPosition[0].get(Rod.MILIEU), Rod.MILIEU, g);//*/
		/*int position = 0;
		int yTopHitBox = 0;
		int xLeftHitBox = 0;
		position = yPosition[1].get(RodPositions.GARDIEN);
		yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN);
		xLeftHitBox = Utils.WIDTH - (int)(Utils.GARDIEN_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
		g.fillRect( xLeftHitBox, yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y );
		
		position = yPosition[1].get(RodPositions.DEFENSE);
		yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		xLeftHitBox = Utils.WIDTH - (int)(Utils.DEFENSE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
		g.fillRect( xLeftHitBox, yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y ); 
		yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		g.fillRect( xLeftHitBox, yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y ); 
		
		position = yPosition[1].get(RodPositions.MILIEU);
		xLeftHitBox = Utils.WIDTH - (int)(Utils.MILIEU_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
		for( int i = 1; i < 6; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU);
			g.fillRect( xLeftHitBox, yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y ); 
		}
		
		position = yPosition[1].get(RodPositions.ATTAQUE);
		xLeftHitBox = Utils.WIDTH - (int)(Utils.ATTAQUE_POSITION+(float)(Utils.IMAGE_PLAYER_X/3));
		for( int i = 1; i < 4; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE);
			g.fillRect( xLeftHitBox, yTopHitBox, Utils.IMAGE_PLAYER_X, Utils.IMAGE_PLAYER_Y ); 
		}
		
		
		
		
		position = yPosition[0].get(RodPositions.GARDIEN);
		yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN);
		xLeftHitBox = (Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, (int)(2*(float)(Utils.IMAGE_PLAYER_X/3)), (int)(2*(float)(Utils.IMAGE_PLAYER_Y/3)));
		position = yPosition[0].get(RodPositions.DEFENSE);
		yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		xLeftHitBox = (Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, (int)(2*(float)(Utils.IMAGE_PLAYER_X/3)), (int)(2*(float)(Utils.IMAGE_PLAYER_Y/3))); 
		yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		g.fillRect( xLeftHitBox, yTopHitBox, (int)(2*(float)(Utils.IMAGE_PLAYER_X/3)), (int)(2*(float)(Utils.IMAGE_PLAYER_Y/3))); 
		position = yPosition[0].get(RodPositions.MILIEU);
		xLeftHitBox = (Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 6; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU);
			g.fillRect( xLeftHitBox, yTopHitBox, (int)(2*(float)(Utils.IMAGE_PLAYER_X/3)), (int)(2*(float)(Utils.IMAGE_PLAYER_Y/3))); 
		}
		position = yPosition[0].get(RodPositions.ATTAQUE);
		xLeftHitBox = (Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 4; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE);
			g.fillRect( xLeftHitBox, yTopHitBox, (int)(2*(float)(Utils.IMAGE_PLAYER_X/3)), (int)(2*(float)(Utils.IMAGE_PLAYER_Y/3)) ); 
		}*/
		Image img = null;
		try {
			img = ImageIO.read(new File("pictures/barrechoisie.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int decal = 0;
		switch(rodPosition){
			case GARDIEN:
				decal = Utils.GAP_EDGE+30-15;
				g.drawImage(img, ( getSide() == Sides.UP ? Utils.WIDTH - decal - 35 : decal ), Utils.HEIGHT-64, this);
				break;
			case DEFENSE:
				decal = Utils.GAP_EDGE+30+100-15;
				g.drawImage(img, ( getSide() == Sides.UP ? Utils.WIDTH - decal - 35 : decal ), Utils.HEIGHT-64, this);
				break;
			case MILIEU:
				decal = (Utils.WIDTH-Utils.LINE_STRENGTH)/2-70-15;
				g.drawImage(img, ( getSide() == Sides.UP ? Utils.WIDTH - decal - 35 : decal ), Utils.HEIGHT-64, this);
				break;
			case ATTAQUE:
				decal = Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GAP_EDGE-230-15;
				g.drawImage(img, ( getSide() == Sides.UP ? Utils.WIDTH - decal - 35 : decal ), Utils.HEIGHT-64, this);
				break;
		}
	}


	public void keyPressed(KeyEvent e) {
		if( !pause ){
			if( e.getKeyCode() == 32 ){
				yPosition[0].put(Rod.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN));
				yPosition[0].put(Rod.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE));
				yPosition[0].put(Rod.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU));
				yPosition[0].put(Rod.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE));
				yPosition[1].put(Rod.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(Rod.GARDIEN));
				yPosition[1].put(Rod.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(Rod.DEFENSE));
				yPosition[1].put(Rod.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(Rod.MILIEU));
				yPosition[1].put(Rod.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(Rod.ATTAQUE));
			}
		}
		//Pas de gestion du clavier
		/*if( e.getKeyCode() == 38 ){
			moveUpAndDown(true,Utils.MOVE_STEP);
		}else if( e.getKeyCode() == 40 ){
			moveUpAndDown(false,Utils.MOVE_STEP);
		}*/
	}
	
	public void moveUpAndDown(int y, boolean up){
		int limitSup = 38;
		int limitInf = 83;
		switch(rodPosition){
			case GARDIEN:
				limitSup = -5;
				limitInf = 215;
				break;
			case DEFENSE:
				limitSup = -10;
				limitInf = 300;
				break;
			case MILIEU:
				limitSup = 20;
				limitInf = 180;
				break;
			case ATTAQUE:
				limitSup = -10;
				limitInf = 220;
				break;
		}
		int mov = 0;
		mov = (int)Math.ceil( ( Math.abs( lastKeyY-y )* Utils.getSensibility() / ( 1.+Utils.getSensibility() ) ) );
		lastKeyY = y;

		int upOrDown = getGamePanel().getWindow().getMain().getPlayer().getSide() == Utils.Sides.DOWN ? 0 : 1;
		if( up && yPosition[upOrDown].get(rodPosition) 
				> (limitSup+mov) ){
			yPosition[upOrDown].put(rodPosition, 
					yPosition[upOrDown].get(rodPosition)-mov);
			this.lastKeyY = y;
			getGamePanel().getWindow().getMain().getPlayer().setRod(yPosition[upOrDown], rodStatus[upOrDown]);
			repaint();
		}else if( !up && yPosition[upOrDown].get(rodPosition) 
				< (limitInf-mov) ){
			yPosition[upOrDown].put(rodPosition, 
					yPosition[upOrDown].get(rodPosition)+mov);
			lastKeyY = y;
			getGamePanel().getWindow().getMain().getPlayer().setRod(yPosition[upOrDown], rodStatus[upOrDown]);
			repaint();
		}
	}


	public void keyReleased(KeyEvent e) {
	}


	public void keyTyped(KeyEvent e) {
		if( !pause ){
			if( e.getKeyChar() == 'a' || e.getKeyChar() == 'A' ){
				Rod rod = ( getSide() == Sides.UP ? Rod.ATTAQUE : Rod.GARDIEN );
				if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
					rodPosition = rod;
					repaint();
				}
			}else if( e.getKeyChar() == 'z'|| e.getKeyChar() == 'Z' ){
				Rod rod = ( getSide() == Sides.UP ? Rod.MILIEU : Rod.DEFENSE );
				if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
					rodPosition = rod;
					repaint();
				}
			}else if( e.getKeyChar() == 'e'|| e.getKeyChar() == 'E' ){
				Rod rod = ( getSide() == Sides.UP ? Rod.DEFENSE : Rod.MILIEU );
				if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
					rodPosition = rod;
					repaint();
				}
			}else if( e.getKeyChar() == 'r'|| e.getKeyChar() == 'R' ){
				Rod rod = ( getSide() == Sides.UP ? Rod.GARDIEN : Rod.ATTAQUE );
				if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
					rodPosition = rod;
					repaint();
				}
			}
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		if( !pause ){
			int y = e.getYOnScreen();
			if( y > ( 20 + oldY ) ){
				moveUpAndDown(y, false);
			}else if( y < ( oldY - 17 )){
				moveUpAndDown(y, true);
			}
			if( ( y - oldY ) > 20 || ( oldY - y ) > 20 )
				oldY = y;
		}
	}


	public void refreshRodPositions(String[] rodPositions, Sides s) {
		Player p = getGamePanel().getWindow().getMain().getPlayer();
		if( rodPositions.length > 1 ){
			//On met à jour les autres barres
			if( ( !p.getRodAvailables().get(Rod.GARDIEN) || p.getSide() != Sides.DOWN ) && rodPositions[0] != null )
				yPosition[0].put(Rod.GARDIEN, Integer.valueOf(rodPositions[0]));
			if( ( !p.getRodAvailables().get(Rod.DEFENSE) || p.getSide() != Sides.DOWN ) && rodPositions[1] != null )
				yPosition[0].put(Rod.DEFENSE, Integer.valueOf(rodPositions[1]));
			if( ( !p.getRodAvailables().get(Rod.MILIEU) || p.getSide() != Sides.DOWN ) && rodPositions[2] != null )	
				yPosition[0].put(Rod.MILIEU, Integer.valueOf(rodPositions[2]));
			if( ( !p.getRodAvailables().get(Rod.ATTAQUE) || p.getSide() != Sides.DOWN ) && rodPositions[3] != null )
				yPosition[0].put(Rod.ATTAQUE, Integer.valueOf(rodPositions[3]));
			
			if( ( !p.getRodAvailables().get(Rod.GARDIEN) ||  p.getSide() != Sides.UP ) && rodPositions[4] != null )
				yPosition[1].put(Rod.GARDIEN, Integer.valueOf(rodPositions[4]));
			if( ( !p.getRodAvailables().get(Rod.DEFENSE) ||  p.getSide() != Sides.UP ) && rodPositions[5] != null )
				yPosition[1].put(Rod.DEFENSE, Integer.valueOf(rodPositions[5]));
			if( ( !p.getRodAvailables().get(Rod.MILIEU) || p.getSide() != Sides.UP ) && rodPositions[6] != null )
				yPosition[1].put(Rod.MILIEU, Integer.valueOf(rodPositions[6]));
			if( ( !p.getRodAvailables().get(Rod.ATTAQUE) || p.getSide() != Sides.UP ) && rodPositions[7] != null )
				yPosition[1].put(Rod.ATTAQUE, Integer.valueOf(rodPositions[7]));
			if( rodPositions.length > 8 ){
			if( ( !p.getRodAvailables().get(Rod.GARDIEN) || p.getSide() != Sides.DOWN ) && rodPositions[8] != null )
				rodStatus[0].put(Rod.GARDIEN, RodStatus.valueOf(rodPositions[8]));
			if( ( !p.getRodAvailables().get(Rod.DEFENSE) || p.getSide() != Sides.DOWN ) && rodPositions[9] != null )
				rodStatus[0].put(Rod.DEFENSE, RodStatus.valueOf(rodPositions[9]));
			if( ( !p.getRodAvailables().get(Rod.MILIEU) || p.getSide() != Sides.DOWN ) && rodPositions[10] != null )	
				rodStatus[0].put(Rod.MILIEU, RodStatus.valueOf(rodPositions[10]));
			if( ( !p.getRodAvailables().get(Rod.ATTAQUE) || p.getSide() != Sides.DOWN ) && rodPositions[11] != null )
				rodStatus[0].put(Rod.ATTAQUE, RodStatus.valueOf(rodPositions[11]));
			if( rodPositions.length > 12 ){
				if( ( !p.getRodAvailables().get(Rod.GARDIEN) || p.getSide() != Sides.UP ) && rodPositions[12] != null )
					rodStatus[1].put(Rod.GARDIEN, RodStatus.valueOf(rodPositions[12]));
				if( ( !p.getRodAvailables().get(Rod.DEFENSE) || p.getSide() != Sides.UP ) && rodPositions[13] != null )
					rodStatus[1].put(Rod.DEFENSE, RodStatus.valueOf(rodPositions[13]));
				if( ( !p.getRodAvailables().get(Rod.MILIEU) ||  p.getSide() != Sides.UP ) && rodPositions[14] != null )
					rodStatus[1].put(Rod.MILIEU, RodStatus.valueOf(rodPositions[14]));
				if( ( !p.getRodAvailables().get(Rod.ATTAQUE) || p.getSide() != Sides.UP ) && rodPositions[15] != null )
					rodStatus[1].put(Rod.ATTAQUE, RodStatus.valueOf(rodPositions[15]));
			}
			}
			repaint();
		}
	}


	public void refreshPositions(String[] positions, Sides side, int i, int j) {
		ballX = i;
		ballY = j;
		refreshRodPositions( positions, side );
	}


	public GamePanel getGamePanel() {
		return gamepanel;
	}


	public void setGamePanel(GamePanel window) {
		this.gamepanel = window;
	}
	
	public InfoZone getInfoPanel(){
		return infoZone;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if( !pause ){
			shootBeginning = System.currentTimeMillis();
			rodStatus[getSide() == Sides.UP ? 1 : 0].put(rodPosition,RodStatus.HOLDING);
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if( !pause ){
			long duration = System.currentTimeMillis() - shootBeginning;
			shootBeginning = 0;
			rodStatus[getSide() == Sides.UP ? 1 : 0].put(rodPosition,RodStatus.SHOOTING);
			getGamePanel().getWindow().getMain().getPlayer().sendShoot(duration, rodPosition, getGamePanel().getWindow().getMain().getPlayer().getSide() );
			setToNormalSide(getSide());
			setToNormalRod(rodPosition);
		}
	}


	public boolean isPause() {
		return pause;
	}


	public void setPause(boolean pause) {
		if( pause )
			infoZone.askForAPause.setText("Relancer");
		else
			infoZone.askForAPause.setText("Pause ?");
		this.pause = pause;
	}


	public JLabel getLeftScore() {
		return infoZone.getLeftScore();
	}


	public JLabel getRightScore() {
		return infoZone.getRightScore();
	}


	public Sides getToNormalSide() {
		return toNormalSide;
	}


	public void setToNormalSide(Sides toNormalSide) {
		this.toNormalSide = toNormalSide;
	}


	public Rod getToNormalRod() {
		return toNormalRod;
	}


	public void setToNormalRod(Rod toNormalRod) {
		this.toNormalRod = toNormalRod;
	}


	public Hashtable<Rod, RodStatus>[] getRodStatus() {
		return rodStatus;
	}


	public void setRodStatus(Hashtable<Rod, RodStatus>[] rodStatus) {
		this.rodStatus = rodStatus;
	}


	public Sides getSide() {
		return side;
	}


	public void setSide(Sides side) {
		this.side = side;
	}
}


class RefreshRods implements Runnable {
	private GameZone gamezone;
	
	public RefreshRods(GameZone g ){
		gamezone = g;
	}
	
	public void run() {
		int i = 0;
		while(true){
			//Alias pour faciliter la lecture
			GameClient gc = gamezone.getGamePanel().getWindow().getMain().getClient().getGc();
			if( gamezone.getGamePanel().getWindow().getMain().getClient().getMc().isToDelete() ){
				gamezone.getGamePanel().getWindow().setContentPane(new MenuPanel(gamezone.getGamePanel().getWindow()));
				gamezone.getGamePanel().getWindow().setVisible(true);
				break;
			}
			Player p = gamezone.getGamePanel().getWindow().getMain().getPlayer();
			gamezone.refreshPositions( gc.getPositions( p.getLogin(), false ) , p.getSide(), gc.getBallX(), gc.getBallY() );
			MatchClient mc = gamezone.getGamePanel().getWindow().getMain().getClient().getMc();
			if( mc.getLeftScore() < 20 && mc.getLeftScore() >= 0 )
				gamezone.getLeftScore().setText( " " + mc.getLeftScore() );
			if( mc.getRightScore() < 20 && mc.getRightScore() >= 0 )
				gamezone.getRightScore().setText( " " + mc.getRightScore() );
			gamezone.setPause(mc.isPause());
			try{
				Thread.sleep(10);
			}catch( InterruptedException e ){
				
			}
			if( gamezone.getToNormalSide() != null && gamezone.getToNormalRod() != null ){
				if( i > 50 ){
					gamezone.getRodStatus()[gamezone.getSide() == Sides.UP ? 1 : 0].put(gamezone.rodPosition,RodStatus.NORMAL);
					i = 0;
					gamezone.setToNormalRod(null);
					gamezone.setToNormalSide(null);
				}else
					i++;
			}
		}
	}
	
}


class InfoZone extends JPanel implements ActionListener{
	
	private JLabel leftScore = new JLabel(" 0 ");
	private JLabel rightScore = new JLabel(" 0 ");
	public JButton askForAPause;
	private JButton bQuit;
	
	private GameZone gamezone;
	
	public InfoZone(GameZone gz){
		this.gamezone = gz;
		
		JPanel padding = new JPanel();
		padding.setPreferredSize(new Dimension(100,200));
		padding.setLayout(new BorderLayout());
		JLabel title = new JLabel("Match");
		title.setForeground(Color.ORANGE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serial", Font.BOLD, 20));
		padding.setBackground(Color.black);
		padding.add(title);
		add(padding);
		add(new JLabel("<html>Bleu :<br /></html>"));
		add(leftScore);
		add(new JLabel("<html>Rouge :<br /></html>"));
		add(rightScore);
		askForAPause = new JButton("Pause ?");
		add(askForAPause);
		askForAPause.setFocusable(false);
		askForAPause.addActionListener(this);
		bQuit = new JButton("Quitter");
		add(bQuit);
		bQuit.setFocusable(false);
		bQuit.addActionListener(this);
		setFocusable(false);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(100,729));
		setMinimumSize(new Dimension(100,729));
	}
	
	public JLabel getLeftScore() {
		return leftScore;
	}


	public void setLeftScore(JLabel jLabel) {
		this.leftScore = jLabel;
	}


	public JLabel getRightScore() {
		return rightScore;
	}


	public void setRightScore(JLabel rightScore) {
		this.rightScore = rightScore;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if( arg0.getSource() == askForAPause ){
			gamezone.setPause(gamezone.getGamePanel().getWindow().getPlayer().askForPause(gamezone.isPause()));
			if( gamezone.isPause() ){
				askForAPause.setText("Relancer");
			}
		}else if( arg0.getSource() == bQuit ){
			gamezone.getGamePanel().getWindow().getMain().getPlayer().stopMatch();
			gamezone.getGamePanel().getWindow().setContentPane(new MenuPanel(gamezone.getGamePanel().getWindow()));
			gamezone.getGamePanel().getWindow().setVisible(true);
		}
	}
}