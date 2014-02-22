package clientGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Image;
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
import clientCore.Utils.Sides;
import clientNetwork.GameClient;


public class GameZone extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Taille des buts en pixels et épaisseur des traits
	 */
	private int h;
	private int w;
	
	private int oldY = 0;
	
	private Hashtable<RodPositions, Integer>[] yDecal;
	
	
	public static enum RodPositions { GARDIEN , DEFENSE, MILIEU, ATTAQUE };
	RodPositions rodPosition;
	
	private int ballX;
	private int ballY;
	
	private boolean testMode;
	
	private MainFrame window;
	private JPanel infoZone;
	private long shootBeginning;
	
	@SuppressWarnings("unchecked")
	public GameZone(MainFrame window, boolean testMode){
		this.window = window;
		this.testMode = testMode;
		
		infoZone = new JPanel();
		infoZone.add(new JLabel("Coucou"));
		infoZone.setBackground(Color.BLACK);
		infoZone.setPreferredSize(new Dimension(100,700));
		infoZone.setMinimumSize(new Dimension(100,700));
		
		yDecal = new Hashtable[2];
		yDecal[0] = new Hashtable<RodPositions, Integer>();
		yDecal[0].put(RodPositions.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN));
		yDecal[0].put(RodPositions.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE));
		yDecal[0].put(RodPositions.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU));
		yDecal[0].put(RodPositions.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE));
		yDecal[1] = new Hashtable<RodPositions, Integer>();
		yDecal[1].put(RodPositions.GARDIEN, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN));
		yDecal[1].put(RodPositions.DEFENSE, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE));
		yDecal[1].put(RodPositions.MILIEU, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU));
		yDecal[1].put(RodPositions.ATTAQUE, Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE));
		
		rodPosition = RodPositions.MILIEU;
	    setPreferredSize(new Dimension(900,700));
	    setMinimumSize(new Dimension(900,700));
	    
	    addKeyListener(this);
	    addMouseListener(this);
	    addMouseMotionListener(this);
	    
	    setFocusable(true);
	    requestFocus();
	    
	    if( !testMode ){
		    Thread tr = new Thread(new RefreshRods(this));
		    tr.start();
	    }
	}
	

	public void paint(Graphics g){
		h = getHeight();
		w = getWidth();
		g.setColor(new Color(116,152,29));
		g.fillRect(0,0,getWidth(),getHeight());
		
		drawLines(g);
		drawBall(g);
		drawGoals(g);
		drawPlayers(g);
		drawRodPosition(g);
		
	}
	
	private void drawBall(Graphics g) {
		//Côté gauche
		g.setColor(Color.WHITE);
		g.fillOval(ballX, ballY, Utils.BALL_RADIUS, Utils.BALL_RADIUS);
	}


	private void drawGoals(Graphics g){
		//Côté gauche
		g.setColor(Color.RED);
		//Long côté
		g.fillRect(Utils.GAP_EDGE/2,h/2-Utils.GOAL_SIZE/2,Utils.LINE_STRENGTH,Utils.GOAL_SIZE);
		//petits côtés
		g.fillRect(Utils.GAP_EDGE/2,h/2-Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		g.fillRect(Utils.GAP_EDGE/2,h/2+Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		//Côté droit
		g.setColor(Color.BLUE);
		//Long côté
		g.fillRect(w-Utils.GAP_EDGE/2-Utils.LINE_STRENGTH,h/2-Utils.GOAL_SIZE/2,Utils.LINE_STRENGTH,Utils.GOAL_SIZE);
		//petits côtés
		g.fillRect(w-Utils.GAP_EDGE,h/2-Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
		g.fillRect(w-Utils.GAP_EDGE,h/2+Utils.GOAL_SIZE/2,Utils.GAP_EDGE/2,Utils.LINE_STRENGTH);
	}
	
	private void drawLines(Graphics g){
		g.setColor(Color.WHITE);
		//Milieu de terrain
		g.fillRect((w-Utils.LINE_STRENGTH)/2,0,Utils.LINE_STRENGTH,h);
		//gauche
		g.fillRect(Utils.GAP_EDGE,0,Utils.LINE_STRENGTH,h);
		//haut
		g.fillRect(0,Utils.GAP_EDGE,w,Utils.LINE_STRENGTH);
		//droite
		g.fillRect(w-Utils.GAP_EDGE-Utils.LINE_STRENGTH,0,Utils.LINE_STRENGTH,h);
		//bas
		g.fillRect(0,h-Utils.GAP_EDGE-Utils.LINE_STRENGTH,w,Utils.LINE_STRENGTH);
	}
	
	private void drawPlayers(Graphics g){
		drawPlayer(g, Utils.GARDIEN_POSITION, 0, h, 1, Color.RED, false, 1, RodPositions.GARDIEN);
		drawPlayer(g, Utils.DEFENSE_POSITION, 0, h, 2, Color.RED, false, 1, RodPositions.DEFENSE);
		drawPlayer(g, Utils.MILIEU_POSITION, 0, h, 5, Color.RED, false, 1, RodPositions.MILIEU);
		drawPlayer(g, Utils.ATTAQUE_POSITION, 0, h, 3, Color.RED, false, 1, RodPositions.ATTAQUE);
		
		drawPlayer(g, w-Utils.LINE_STRENGTH-Utils.GARDIEN_POSITION, 0, h, 1, Color.RED, true, 1, RodPositions.GARDIEN);
		drawPlayer(g, w-Utils.LINE_STRENGTH-Utils.DEFENSE_POSITION, 0, h, 2, Color.RED, true, 1, RodPositions.DEFENSE);
		drawPlayer(g, w-Utils.LINE_STRENGTH-Utils.MILIEU_POSITION, 0, h, 5, Color.RED, true, 1, RodPositions.MILIEU);
		drawPlayer(g, w-Utils.LINE_STRENGTH-Utils.ATTAQUE_POSITION, 0, h, 3, Color.RED, true, 1, RodPositions.ATTAQUE);
	}
	
	/**
	 * rightPlayer : true = orienté vers la gauche, false orienté vers la droite.
	 * position : quelle est la position de la barre ? tir, droit, etc. 
	 */
	
	private void drawPlayer(Graphics g, int x, int y, int h, int nb, Color color, boolean rightPlayer, int position, RodPositions rod ){
		y += yDecal[rightPlayer ? 1 : 0].get(rod)-Utils.Y_STAGGERING_DEFAULT.get(rod);
		g.setColor(new Color(192, 192, 192));
		g.fillRect(x,0,3*Utils.LINE_STRENGTH/2,h);
		g.setColor(color);
		for( int i=1; i<=nb;i++){
			try {
				Image img = null;
				if(rightPlayer)
					img = ImageIO.read(new File("pictures/joueurdroit" + String.valueOf(position) + ".png"));
				else
					img = ImageIO.read(new File("pictures/joueurgauche" + String.valueOf(position) + ".png"));
				if(position==1)
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3, y+i*h/(1+nb)-Utils.IMAGE_PLAYER_Y/2, this);
				else if(position==2 && !rightPlayer )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-13, y+i*h/(1+nb)-Utils.IMAGE_PLAYER_Y/2, this);
				else if(position==2 && rightPlayer )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-48, y+i*h/(1+nb)-Utils.IMAGE_PLAYER_Y/2, this);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }                
		}
	}
	
	private void drawRodPosition(Graphics g){
		g.setColor(Color.BLACK);
		int position = 0;
		int yTopHitBox = 0;
		int xLeftHitBox = 0;
		position = yDecal[1].get(RodPositions.GARDIEN);
		yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN);
		xLeftHitBox = Utils.WIDTH - (Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 );
		position = yDecal[1].get(RodPositions.DEFENSE);
		yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		xLeftHitBox = Utils.WIDTH - (Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		position = yDecal[1].get(RodPositions.MILIEU);
		xLeftHitBox = Utils.WIDTH - (Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 6; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU);
			g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		}
		position = yDecal[1].get(RodPositions.ATTAQUE);
		xLeftHitBox = Utils.WIDTH - (Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 4; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE);
			g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		}
		
		
		
		
		position = yDecal[0].get(RodPositions.GARDIEN);
		yTopHitBox = position + Utils.HEIGHT/2-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.GARDIEN);
		xLeftHitBox = (Utils.GARDIEN_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 );
		position = yDecal[0].get(RodPositions.DEFENSE);
		yTopHitBox = position + Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		xLeftHitBox = (Utils.DEFENSE_POSITION-Utils.IMAGE_PLAYER_X/3);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		yTopHitBox = position + 2*Utils.HEIGHT/3-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.DEFENSE);
		g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		position = yDecal[0].get(RodPositions.MILIEU);
		xLeftHitBox = (Utils.MILIEU_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 6; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/6-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.MILIEU);
			g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		}
		position = yDecal[0].get(RodPositions.ATTAQUE);
		xLeftHitBox = (Utils.ATTAQUE_POSITION-Utils.IMAGE_PLAYER_X/3);
		for( int i = 1; i < 4; i++ ){
			yTopHitBox = position + i*Utils.HEIGHT/4-Utils.IMAGE_PLAYER_Y/2 - Utils.Y_STAGGERING_DEFAULT.get(RodPositions.ATTAQUE);
			g.fillRect( xLeftHitBox, yTopHitBox, 2*Utils.IMAGE_PLAYER_X/3, 2*Utils.IMAGE_PLAYER_Y/3 ); 
		}
		/*switch(rodPosition){
			case GARDIEN:
				g.fillRect(Utils.GAP_EDGE+30,600,50,50);
				break;
			case DEFENSE:
				g.fillRect(Utils.GAP_EDGE+30+100,600,50,50);
				break;
			case MILIEU:
				g.fillRect((w-Utils.LINE_STRENGTH)/2-70,600,50,50);
				break;
			case ATTAQUE:
				g.fillRect(w-Utils.LINE_STRENGTH-Utils.GAP_EDGE-230,600,50,50);
				break;
		}//*/
		//g.fillRect(1,1,50,50);
	}


	public void keyPressed(KeyEvent e) {
		if( e.getKeyCode() == 38 ){
			moveUpAndDown(true,Utils.MOVE_STEP);
		}else if( e.getKeyCode() == 40 ){
			moveUpAndDown(false,Utils.MOVE_STEP);
		}
	}
	
	public void moveUpAndDown(boolean up, int path){
		int limitSup = 38;
		int limiInf = 83;
		switch(rodPosition){
			case GARDIEN:
				limitSup = -5;
				limiInf = 215;
				break;
			case DEFENSE:
				limitSup = -10;
				limiInf = 300;
				break;
			case MILIEU:
				limitSup = 20;
				limiInf = 180;
				break;
			case ATTAQUE:
				limitSup = -10;
				limiInf = 220;
				break;
		}
		int upOrDown = getWindow().getMain().getPlayer().getSide() == Utils.Sides.BOTTOM ? 0 : 1;
		if( up && yDecal[upOrDown].get(rodPosition) > (limitSup+2*path) ){
			/*yDecal[upOrDown].put(rodPosition, yDecal[upOrDown].get(rodPosition)-path);
			Main.getPlayer().setRod(yDecal[upOrDown]);
			repaint();*/
			yDecal[upOrDown].put(rodPosition, yDecal[upOrDown].get(rodPosition)-path);
			getWindow().getMain().getPlayer().setRod(yDecal[upOrDown]);
			repaint();
		}else if( !up && yDecal[upOrDown].get(rodPosition) < (limiInf-2*path) ){
			/*
			yDecal[upOrDown].put(rodPosition, yDecal[upOrDown].get(rodPosition)+path);
			Main.getPlayer().setRod(yDecal[upOrDown]);
			repaint();*/
			yDecal[upOrDown].put(rodPosition, yDecal[upOrDown].get(rodPosition)+path);
			getWindow().getMain().getPlayer().setRod(yDecal[upOrDown]);
			repaint();
		}
	}


	public void keyReleased(KeyEvent e) {
	}


	public void keyTyped(KeyEvent e) {
		if( e.getKeyChar() == 'a' || e.getKeyChar() == 'A' ){
			if( getWindow().getMain().getPlayer().getRodAvailables().get(RodPositions.GARDIEN) ){
				rodPosition = RodPositions.GARDIEN;
				repaint();
			}
		}else if( e.getKeyChar() == 'z'|| e.getKeyChar() == 'Z' ){
			if( getWindow().getMain().getPlayer().getRodAvailables().get(RodPositions.DEFENSE) ){
				rodPosition = RodPositions.DEFENSE;
				repaint();
			}
		}else if( e.getKeyChar() == 'e'|| e.getKeyChar() == 'E' ){
			if( getWindow().getMain().getPlayer().getRodAvailables().get(RodPositions.MILIEU) ){
				rodPosition = RodPositions.MILIEU;
				repaint();
			}
		}else if( e.getKeyChar() == 'r'|| e.getKeyChar() == 'R' ){
			if( getWindow().getMain().getPlayer().getRodAvailables().get(RodPositions.ATTAQUE) ){
				rodPosition = RodPositions.ATTAQUE;
				repaint();
			}
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		int y = e.getYOnScreen();
		if( y > ( 20 + oldY ) ){
			moveUpAndDown(false, Utils.MOVE_STEP);
		}else if( y < ( oldY - 17 )){
			moveUpAndDown(true, Utils.MOVE_STEP);
		}
		if( ( y - oldY ) > 20 || ( oldY - y ) > 20 )
			oldY = y;
	}


	public void refreshRodPositions(String[] rodPositions, Utils.Sides s) {
		
		if( rodPositions.length > 1 ){
			//On met à jour les autres barres
			if( s == Utils.Sides.UP ){
				yDecal[0].put(RodPositions.GARDIEN, Integer.valueOf(rodPositions[0]));
				yDecal[0].put(RodPositions.DEFENSE, Integer.valueOf(rodPositions[1]));
				yDecal[0].put(RodPositions.MILIEU, Integer.valueOf(rodPositions[2]));
				yDecal[0].put(RodPositions.ATTAQUE, Integer.valueOf(rodPositions[3]));
			}else{
				yDecal[1].put(RodPositions.GARDIEN, Integer.valueOf(rodPositions[4]));
				yDecal[1].put(RodPositions.DEFENSE, Integer.valueOf(rodPositions[5]));
				yDecal[1].put(RodPositions.MILIEU, Integer.valueOf(rodPositions[6]));
				yDecal[1].put(RodPositions.ATTAQUE, Integer.valueOf(rodPositions[7]));
			}
			repaint();
		}
	}


	public void refreshPositions(String[] positions, Sides side, int i, int j) {
		ballX = i;
		ballY = j;
		refreshRodPositions( positions, side );
	}


	public MainFrame getWindow() {
		return window;
	}


	public void setWindow(MainFrame window) {
		this.window = window;
	}
	
	public JPanel getInfoPanel(){
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
		shootBeginning = System.currentTimeMillis();
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		long duration = System.currentTimeMillis() - shootBeginning;
		shootBeginning = 0;
		getWindow().getMain().getPlayer().sendShoot(duration, rodPosition, getWindow().getMain().getPlayer().getSide() );
	}
}


class RefreshRods implements Runnable {
	private GameZone gamezone;
	
	public RefreshRods(GameZone g ){
		gamezone = g;
	}
	
	public void run() {
		while(true){
			//Alias pour faciliter la lecture
			GameClient gc = gamezone.getWindow().getMain().getClient().getGc();
			Player p = gamezone.getWindow().getMain().getPlayer();
			gamezone.refreshPositions( gc.getPositions( p.getLogin(), false ) , p.getSide(), gc.getBallX(), gc.getBallY() );
			try{
				Thread.sleep(20);
			}catch( InterruptedException e ){
				
			}
		}
	}
	
}
