package clientGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
/** Cette classe gére le bandeau titre de la fenêtre avec l'affichage du logo (très moche certes). Elle sera appelée par le BPanel qui
est la classe mère de toutes les autres classes et qui initiliase l'objet afin de conserver le cadre partout. */
public class Header extends JPanel {
	private boolean displayLogo = true;
	public Header(boolean display, int height ){
		super();
		displayLogo = display;
		setPreferredSize(new Dimension(800,height));
		setMaximumSize(new Dimension(800,height));
		setBackground(Color.WHITE);
	}
	public void paintComponent(Graphics g){
		setSize(800,200);
		if( displayLogo ) {
			try {
	    		Image img = ImageIO.read(new File("pictures/header.png"));
				g.drawImage(img, 50, 0, this);
				//Pour une image de fond
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
}
