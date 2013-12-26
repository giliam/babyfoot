package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Header extends JPanel {
	public Header(){
		super();
		setPreferredSize(new Dimension(800,150));
		setMaximumSize(new Dimension(800,150));
		setBackground(Color.WHITE);
	}
	public void paintComponent(Graphics g){
		setSize(800,200);
		try {
    		Image img = ImageIO.read(new File("pictures/header.jpg"));
			g.drawImage(img, (getWidth()-540)/2, 0, this);
			//Pour une image de fond
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
}
