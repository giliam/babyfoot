package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GameZone extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Taille des buts en pixels et épaisseur des traits
	 */
	private static final int goalSize = 2*60;
	private static final int lineStrength = 4;
	private static final int gapEdge = 2*20;
	
	public GameZone(){
	    setPreferredSize(new Dimension(900,700));
	    setMinimumSize(new Dimension(900,700));
	}
	

	public void paint(Graphics g){
		g.setColor(new Color(116,152,29));
		g.fillRect(0,0,getWidth(),getHeight());
		
		drawLines(g);
		drawGoals(g);
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
		g.fillRect((w-lineStrength)/2,0,lineStrength,h);
		g.fillRect(gapEdge,0,lineStrength,h);
		g.fillRect(0,gapEdge,w,lineStrength);
		g.fillRect(w-gapEdge-lineStrength,0,lineStrength,h);
		g.fillRect(0,h-gapEdge-lineStrength,w,lineStrength);
	}
	
}
