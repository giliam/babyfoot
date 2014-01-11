package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;

import javax.swing.JPanel;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import core.Main;

public class GameZone extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Taille des buts en pixels et épaisseur des traits
	 */
	private static final int goalSize = 2*100;
	private static final int lineStrength = 4;
	private static final int gapEdge = 2*20;
	private static final int imagePlayerY = 38;
	private static final int imagePlayerX = 30;
	private static final int movePath = 20;
	
	private int h;
	private int w;
	
	private Hashtable<RodPositions, Integer>[] yDecal;
	private Hashtable<RodPositions, Integer> yDecalDefault;
	
	public static enum RodPositions { GARDIEN , DEFENSE, MILIEU, ATTAQUE };
	RodPositions rodPosition;
	
	public GameZone(){
		Main.getPlayer().setLogin("giliam");
		yDecalDefault = new Hashtable<RodPositions, Integer>();
		yDecalDefault.put(RodPositions.GARDIEN, 100);
		yDecalDefault.put(RodPositions.DEFENSE, 150);
		yDecalDefault.put(RodPositions.MILIEU, 100);
		yDecalDefault.put(RodPositions.ATTAQUE, 100);
		yDecal = new Hashtable[2];
		yDecal[0] = new Hashtable<RodPositions, Integer>();
		yDecal[0].put(RodPositions.GARDIEN, yDecalDefault.get(RodPositions.GARDIEN));
		yDecal[0].put(RodPositions.DEFENSE, yDecalDefault.get(RodPositions.DEFENSE));
		yDecal[0].put(RodPositions.MILIEU, yDecalDefault.get(RodPositions.MILIEU));
		yDecal[0].put(RodPositions.ATTAQUE, yDecalDefault.get(RodPositions.ATTAQUE));
		yDecal[1] = new Hashtable<RodPositions, Integer>();
		yDecal[1].put(RodPositions.GARDIEN, yDecalDefault.get(RodPositions.GARDIEN));
		yDecal[1].put(RodPositions.DEFENSE, yDecalDefault.get(RodPositions.DEFENSE));
		yDecal[1].put(RodPositions.MILIEU, yDecalDefault.get(RodPositions.MILIEU));
		yDecal[1].put(RodPositions.ATTAQUE, yDecalDefault.get(RodPositions.ATTAQUE));
		
		rodPosition = RodPositions.MILIEU;
	    setPreferredSize(new Dimension(900,700));
	    setMinimumSize(new Dimension(900,700));
	    addKeyListener(this);
	    setFocusable(true);
	    requestFocus();
	}
	

	public void paint(Graphics g){
		h = getHeight();
		w = getWidth();
		g.setColor(new Color(116,152,29));
		g.fillRect(0,0,getWidth(),getHeight());
		
		drawLines(g);
		drawGoals(g);
		drawPlayers(g);
		drawRodPosition(g);
	}
	
	private void drawGoals(Graphics g){
		//Côté gauche
		g.setColor(Color.RED);
		//Long côté
		g.fillRect(gapEdge/2,h/2-goalSize/2,lineStrength,goalSize);
		//petits côtés
		g.fillRect(gapEdge/2,h/2-goalSize/2,gapEdge/2,lineStrength);
		g.fillRect(gapEdge/2,h/2+goalSize/2,gapEdge/2,lineStrength);
		//Côté droit
		g.setColor(Color.BLUE);
		//Long côté
		g.fillRect(w-gapEdge/2-lineStrength,h/2-goalSize/2,lineStrength,goalSize);
		//petits côtés
		g.fillRect(w-gapEdge,h/2-goalSize/2,gapEdge/2,lineStrength);
		g.fillRect(w-gapEdge,h/2+goalSize/2,gapEdge/2,lineStrength);
	}
	
	private void drawLines(Graphics g){
		g.setColor(Color.WHITE);
		//Milieu de terrain
		g.fillRect((w-lineStrength)/2,0,lineStrength,h);
		//gauche
		g.fillRect(gapEdge,0,lineStrength,h);
		//haut
		g.fillRect(0,gapEdge,w,lineStrength);
		//droite
		g.fillRect(w-gapEdge-lineStrength,0,lineStrength,h);
		//bas
		g.fillRect(0,h-gapEdge-lineStrength,w,lineStrength);
	}
	
	private void drawPlayers(Graphics g){
		drawPlayer(g, gapEdge+30, 0, h, 1, Color.RED, false, 1, RodPositions.GARDIEN);
		drawPlayer(g, gapEdge+30+100, 0, h, 2, Color.RED, false, 1, RodPositions.DEFENSE);
		drawPlayer(g, (w-lineStrength)/2-70, 0, h, 5, Color.RED, false, 1, RodPositions.MILIEU);
		drawPlayer(g, w-lineStrength-gapEdge-230, 0, h, 3, Color.RED, false, 1, RodPositions.ATTAQUE);
		
		drawPlayer(g, w-lineStrength-(gapEdge+30), 0, h, 1, Color.RED, true, 1, RodPositions.GARDIEN);
		drawPlayer(g, w-lineStrength-(gapEdge+30+100), 0, h, 2, Color.RED, true, 1, RodPositions.DEFENSE);
		drawPlayer(g, w-lineStrength-((w-lineStrength)/2-70), 0, h, 5, Color.RED, true, 1, RodPositions.MILIEU);
		drawPlayer(g, w-lineStrength-(w-lineStrength-gapEdge-230), 0, h, 3, Color.RED, true, 1, RodPositions.ATTAQUE);
	}
	
	/**
	 * rightPlayer : true = orienté vers la gauche, false orienté vers la droite. 
	 */
	private void drawPlayer(Graphics g, int x, int y, int h, int nb, Color color, boolean rightPlayer, int position, RodPositions rod ){
		y += yDecal[rightPlayer ? 1 : 0].get(rod)-yDecalDefault.get(rod);
		g.setColor(new Color(192, 192, 192));
		g.fillRect(x,0,3*lineStrength/2,h);
		g.setColor(color);
		for( int i=1; i<=nb;i++){
			try {
				Image img = null;
				if(rightPlayer)
					img = ImageIO.read(new File("pictures/joueurdroit" + String.valueOf(position) + ".png"));
				else
					img = ImageIO.read(new File("pictures/joueurgauche" + String.valueOf(position) + ".png"));
				if(position==1)
					g.drawImage(img, x-imagePlayerX/3, y+i*h/(1+nb)-imagePlayerY/2, this);
				else if(position==2 && !rightPlayer )
					g.drawImage(img, x-imagePlayerX/3-13, y+i*h/(1+nb)-imagePlayerY/2, this);
				else if(position==2 && rightPlayer )
					g.drawImage(img, x-imagePlayerX/3-48, y+i*h/(1+nb)-imagePlayerY/2, this);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }                
		}
	}
	
	private void drawRodPosition(Graphics g){
		g.setColor(Color.BLACK);
		switch(rodPosition){
			case GARDIEN:
				g.fillRect(gapEdge+30,600,50,50);
				break;
			case DEFENSE:
				g.fillRect(gapEdge+30+100,600,50,50);
				break;
			case MILIEU:
				g.fillRect((w-lineStrength)/2-70,600,50,50);
				break;
			case ATTAQUE:
				g.fillRect(w-lineStrength-gapEdge-230,600,50,50);
				break;
		}
	}


	public void keyPressed(KeyEvent e) {
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
		
		if( e.getKeyCode() == 38 ){
			if( yDecal[0].get(rodPosition) > (limitSup+movePath) ){
				yDecal[0].put(rodPosition, yDecal[0].get(rodPosition)-movePath);
				Main.getPlayer().setRod(yDecal[0]);
				repaint();
			}
		}else if( e.getKeyCode() == 40 ){
			if( yDecal[0].get(rodPosition) < (limiInf-movePath) ){
				yDecal[0].put(rodPosition, yDecal[0].get(rodPosition)+movePath);
				Main.getPlayer().setRod(yDecal[0]);
				repaint();
			}
		}
	}


	public void keyReleased(KeyEvent e) {
	}


	public void keyTyped(KeyEvent e) {
		if( e.getKeyChar() == 'a' || e.getKeyChar() == 'A' ){
			if( Main.getPlayer().getRodAvailables().get(RodPositions.GARDIEN) ){
				rodPosition = RodPositions.GARDIEN;
				repaint();
			}
		}else if( e.getKeyChar() == 'z'|| e.getKeyChar() == 'Z' ){
			if( Main.getPlayer().getRodAvailables().get(RodPositions.DEFENSE) ){
				rodPosition = RodPositions.DEFENSE;
				repaint();
			}
		}else if( e.getKeyChar() == 'e'|| e.getKeyChar() == 'E' ){
			if( Main.getPlayer().getRodAvailables().get(RodPositions.MILIEU) ){
				rodPosition = RodPositions.MILIEU;
				repaint();
			}
		}else if( e.getKeyChar() == 'r'|| e.getKeyChar() == 'R' ){
			if( Main.getPlayer().getRodAvailables().get(RodPositions.ATTAQUE) ){
				rodPosition = RodPositions.ATTAQUE;
				repaint();
			}
		}
	}

	
}