package clientGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	
	Rod rodPosition;
	
	private int ballX;
	private int ballY;
	
	
	private GamePanel gamepanel;
	private JPanel infoZone;
	private JLabel leftScore = new JLabel(" Rouge : ");
	private JLabel rightScore = new JLabel(" Bleue : ");
	private JButton askForAPause = new JButton("Pause ?");
	private long shootBeginning;
	private boolean pause;
	private Sides side;
	private int lastKeyY;
	
	@SuppressWarnings("unchecked")
	public GameZone(GamePanel window, boolean testMode){
		this.gamepanel = window;
		
		this.side = getGamePanel().getWindow().getPlayer().getSide();
		
		infoZone = new JPanel();
		infoZone.add(new JLabel("Le Match"));
		infoZone.add(leftScore);
		infoZone.add(rightScore);
		//infoZone.add(askForAPause);
		infoZone.setFocusable(false);
		infoZone.setBackground(Color.BLACK);
		infoZone.setPreferredSize(new Dimension(100,729));
		infoZone.setMinimumSize(new Dimension(100,729));
		
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
	    setPreferredSize(new Dimension(900,729));
	    setMinimumSize(new Dimension(900,729));
	    
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
		g.fillRect(0,0,getWidth(),getHeight());
		
		initInfoZone();
		
		drawLines(g);
		drawBall(g);
		drawGoals(g);
		drawPlayers(g);
		drawRodPosition(g);
		
	}
	
	private void initInfoZone(){
		leftScore.setBackground(Color.RED);
		rightScore.setBackground(Color.BLUE);
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
		g.setColor(Color.WHITE);
		//Milieu de terrain
		g.fillRect((Utils.WIDTH-Utils.LINE_STRENGTH)/2,0,Utils.LINE_STRENGTH,Utils.HEIGHT);
		//gauche
		g.fillRect(Utils.GAP_EDGE,0,Utils.LINE_STRENGTH,Utils.HEIGHT);
		//haut
		g.fillRect(0,Utils.GAP_EDGE,Utils.WIDTH,Utils.LINE_STRENGTH);
		//droite
		g.fillRect(Utils.WIDTH-Utils.GAP_EDGE-Utils.LINE_STRENGTH,0,Utils.LINE_STRENGTH,Utils.HEIGHT);
		//bas
		g.fillRect(0,Utils.HEIGHT-Utils.GAP_EDGE-Utils.LINE_STRENGTH,Utils.WIDTH,Utils.LINE_STRENGTH);
	}
	
	private void drawPlayers(Graphics g){
		drawPlayer(g, Utils.GARDIEN_POSITION, 0, 1, Color.RED, Sides.DOWN, 1, Rod.GARDIEN);
		drawPlayer(g, Utils.DEFENSE_POSITION, 0, 2, Color.RED, Sides.DOWN, 1, Rod.DEFENSE);
		drawPlayer(g, Utils.MILIEU_POSITION, 0, 5, Color.RED, Sides.DOWN, 1, Rod.MILIEU);
		drawPlayer(g, Utils.ATTAQUE_POSITION, 0, 3, Color.RED, Sides.DOWN, 1, Rod.ATTAQUE);
		
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GARDIEN_POSITION, 0, 1, Color.RED, Sides.UP, 1, Rod.GARDIEN);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.DEFENSE_POSITION, 0, 2, Color.RED, Sides.UP, 1, Rod.DEFENSE);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.MILIEU_POSITION, 0, 5, Color.RED, Sides.UP, 1, Rod.MILIEU);
		drawPlayer(g, Utils.WIDTH-Utils.LINE_STRENGTH-Utils.ATTAQUE_POSITION, 0, 3, Color.RED, Sides.UP, 1, Rod.ATTAQUE);
	}
	
	/**
	 * side : true = orienté vers la gauche, false orienté vers la droite.
	 * position : quelle est la position de la barre ? tir, droit, etc. 
	 */
	
	private void drawPlayer(Graphics g, int x, int y, int nb, Color color, Sides side, int position, Rod rod ){
		g.setColor(new Color(192, 192, 192));
		g.fillRect(x,0,3*Utils.LINE_STRENGTH/2,Utils.HEIGHT);
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
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-13, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
				else if(position==2 && side == Sides.UP )
					g.drawImage(img, x-Utils.IMAGE_PLAYER_X/3-48, Utils.getYPositionPlayer(yPosition, rod, i, nb, side), this);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }                
		}
	}
	
	private void drawRodPosition(Graphics g){
		g.setColor(Color.BLACK);
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
				g.drawImage(img, ( side == Sides.UP ? Utils.WIDTH - decal : decal ), Utils.HEIGHT-64, this);
				break;
			case DEFENSE:
				decal = Utils.GAP_EDGE+30+100-15;
				g.drawImage(img, ( side == Sides.UP ? Utils.WIDTH - decal : decal ), Utils.HEIGHT-64, this);
				break;
			case MILIEU:
				decal = (Utils.WIDTH-Utils.LINE_STRENGTH)/2-70-15;
				g.drawImage(img, ( side == Sides.UP ? Utils.WIDTH - decal : decal ), Utils.HEIGHT-64, this);
				break;
			case ATTAQUE:
				decal = Utils.WIDTH-Utils.LINE_STRENGTH-Utils.GAP_EDGE-230-15;
				g.drawImage(img, ( side == Sides.UP ? Utils.WIDTH - decal : decal ), Utils.HEIGHT-64, this);
				break;
		}
	}


	public void keyPressed(KeyEvent e) {
		//Pas de gestion du clavier
		/*if( e.getKeyCode() == 38 ){
			moveUpAndDown(true,Utils.MOVE_STEP);
		}else if( e.getKeyCode() == 40 ){
			moveUpAndDown(false,Utils.MOVE_STEP);
		}*/
	}
	
	public void moveUpAndDown(int y, boolean up, int path){
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
		int upOrDown = getGamePanel().getWindow().getMain().getPlayer().getSide() == Utils.Sides.DOWN ? 0 : 1;
		if( up && yPosition[upOrDown].get(rodPosition) > (limitSup+2*path) ){
			yPosition[upOrDown].put(rodPosition, yPosition[upOrDown].get(rodPosition)-(int)Math.floor(Math.abs(lastKeyY-y)/4.));
			this.lastKeyY = y;
			getGamePanel().getWindow().getMain().getPlayer().setRod(yPosition[upOrDown]);
			repaint();
		}else if( !up && yPosition[upOrDown].get(rodPosition) < (limitInf-2*path) ){
			yPosition[upOrDown].put(rodPosition, yPosition[upOrDown].get(rodPosition)+(int)Math.round(Math.abs(lastKeyY-y)/4.));
			lastKeyY = y;
			getGamePanel().getWindow().getMain().getPlayer().setRod(yPosition[upOrDown]);
			repaint();
		}
	}


	public void keyReleased(KeyEvent e) {
	}


	public void keyTyped(KeyEvent e) {
		if( e.getKeyChar() == 'a' || e.getKeyChar() == 'A' ){
			Rod rod = ( side == Sides.UP ? Rod.ATTAQUE : Rod.GARDIEN );
			if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
				rodPosition = rod;
				repaint();
			}
		}else if( e.getKeyChar() == 'z'|| e.getKeyChar() == 'Z' ){
			Rod rod = ( side == Sides.UP ? Rod.MILIEU : Rod.DEFENSE );
			if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
				rodPosition = rod;
				repaint();
			}
		}else if( e.getKeyChar() == 'e'|| e.getKeyChar() == 'E' ){
			Rod rod = ( side == Sides.UP ? Rod.DEFENSE : Rod.MILIEU );
			if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
				rodPosition = rod;
				repaint();
			}
		}else if( e.getKeyChar() == 'r'|| e.getKeyChar() == 'R' ){
			Rod rod = ( side == Sides.UP ? Rod.GARDIEN : Rod.ATTAQUE );
			if( getGamePanel().getWindow().getMain().getPlayer().getRodAvailables().get(rod) ){
				rodPosition = rod;
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
			moveUpAndDown(y, false, Utils.MOVE_STEP);
		}else if( y < ( oldY - 17 )){
			moveUpAndDown(y, true, Utils.MOVE_STEP);
		}
		if( ( y - oldY ) > 20 || ( oldY - y ) > 20 )
			oldY = y;
	}


	public void refreshRodPositions(String[] rodPositions, Utils.Sides s) {
		
		if( rodPositions.length > 1 ){
			//On met à jour les autres barres
			if( s == Utils.Sides.UP ){
				yPosition[0].put(Rod.GARDIEN, Integer.valueOf(rodPositions[0]));
				yPosition[0].put(Rod.DEFENSE, Integer.valueOf(rodPositions[1]));
				yPosition[0].put(Rod.MILIEU, Integer.valueOf(rodPositions[2]));
				yPosition[0].put(Rod.ATTAQUE, Integer.valueOf(rodPositions[3]));
			}else{
				if( rodPositions.length == 8 && rodPositions[4] != null ){
					yPosition[1].put(Rod.GARDIEN, Integer.valueOf(rodPositions[4]));
					yPosition[1].put(Rod.DEFENSE, Integer.valueOf(rodPositions[5]));
					yPosition[1].put(Rod.MILIEU, Integer.valueOf(rodPositions[6]));
					yPosition[1].put(Rod.ATTAQUE, Integer.valueOf(rodPositions[7]));
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
		getGamePanel().getWindow().getMain().getPlayer().sendShoot(duration, rodPosition, getGamePanel().getWindow().getMain().getPlayer().getSide() );
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


	public boolean isPause() {
		return pause;
	}


	public void setPause(boolean pause) {
		this.pause = pause;
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
			i++;
			//Alias pour faciliter la lecture
			GameClient gc = gamezone.getGamePanel().getWindow().getMain().getClient().getGc();
			Player p = gamezone.getGamePanel().getWindow().getMain().getPlayer();
			gamezone.refreshPositions( gc.getPositions( p.getLogin(), false ) , p.getSide(), gc.getBallX(), gc.getBallY() );
			MatchClient mc = gamezone.getGamePanel().getWindow().getMain().getClient().getMc();
			gamezone.getLeftScore().setText( "Rouge : " + mc.getLeftScore() );
			gamezone.getRightScore().setText( "Bleue : " + mc.getRightScore() );
			gamezone.setPause(mc.isPause());
			try{
				Thread.sleep(20);
			}catch( InterruptedException e ){
				
			}
		}
	}
	
}