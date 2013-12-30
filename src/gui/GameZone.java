package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameZone extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Taille des buts en pixels et épaisseur des traits
	 */
	private static final int goalSize = 2*100;
	private static final int lineStrength = 4;
	private static final int gapEdge = 2*20;
	private static final int imagePlayerY = 38;
	private static final int imagePlayerX = 30;
	
	public GameZone(){
	    setPreferredSize(new Dimension(900,700));
	    setMinimumSize(new Dimension(900,700));
	}
	

	public void paint(Graphics g){
		g.setColor(new Color(116,152,29));
		g.fillRect(0,0,getWidth(),getHeight());
		
		drawLines(g);
		drawGoals(g);
		drawPlayers(g);
	}
	
	private void drawGoals(Graphics g){
		int h = getHeight();
		int w = getWidth();
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
		int h = getHeight();
		int w = getWidth();
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
		int h = getHeight();
		int w = getWidth();
		
		drawPlayer(g, gapEdge+30, 0, h, 1, Color.RED, false, 1);
		drawPlayer(g, gapEdge+30+100, 0, h, 2, Color.RED, false, 1);
		drawPlayer(g, w-lineStrength-gapEdge-230, 0, h, 3, Color.RED, false, 1);
		drawPlayer(g, (w-lineStrength)/2-70, 0, h, 5, Color.RED, false, 1);
		
		drawPlayer(g, w-lineStrength-(gapEdge+30), 0, h, 1, Color.RED, true, 1);
		drawPlayer(g, w-lineStrength-(gapEdge+30+100), 0, h, 2, Color.RED, true, 1);
		drawPlayer(g, w-lineStrength-(w-lineStrength-gapEdge-230), 0, h, 3, Color.RED, true, 1);
		drawPlayer(g, w-lineStrength-((w-lineStrength)/2-70), 0, h, 5, Color.RED, true, 1);
	}
	
	/**
	 * rightPlayer : true = orienté vers la gauche, false orienté vers la droite. 
	 */
	private void drawPlayer(Graphics g, int x, int y, int h, int nb, Color color, boolean rightPlayer, int position ){
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
	
}
