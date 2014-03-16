package clientGui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import clientCore.ClientBabyfoot;
import clientCore.Utils;


@SuppressWarnings("serial")
public class MenuPanel extends BPanel implements ActionListener {
	JButton bQuit = new JButton("Quitter");
	JButton bNewGame = new JButton("Nouvelle partie");
	JButton bJoinGame = new JButton("Rejoindre une partie");
	JButton bSettings = new JButton("Options");
	JPanel centralMenu;
	
	public MenuPanel(MainFrame f) {
		super(f, false, 200);
		f.setSize(800,800);
	    
		Image image = null;
		try {
			image = ImageIO.read(new File("pictures/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    JLabel viewer = new JLabel(new ImageIcon(image));
	    viewer.setSize(800,800);
	    add(viewer);
		
		//Gauche-droite
	    JPanel left = new JPanel();
	    left.setBackground(Color.WHITE);
	    left.setPreferredSize(new Dimension(250,200));
	    add(left,BorderLayout.WEST);
	    JPanel bottom = new JPanel();
	    bottom.setBackground(Color.WHITE);
	    bottom.setPreferredSize(new Dimension(800,300));
	    add(bottom,BorderLayout.SOUTH);
	    JPanel right = new JPanel();
	    right.setBackground(Color.WHITE);
	    right.setPreferredSize(new Dimension(250,200));
	    add(right,BorderLayout.EAST);
	    //Gestion du menu central
	    centralMenu = new JPanel();
	    centralMenu.setBackground(Color.WHITE);
	    centralMenu.setPreferredSize(new Dimension(400,100));
	    centralMenu.setMaximumSize(new Dimension(400,100));
	    add(centralMenu, BorderLayout.CENTER);
	    
	    //Gestion des boutons
	    bNewGame.setPreferredSize(new Dimension(200,50));
	    bJoinGame.setPreferredSize(new Dimension(200,50));
	    bSettings.setPreferredSize(new Dimension(200,50));
	    bQuit.setPreferredSize(new Dimension(200,50));
		
	    centralMenu.add(bNewGame,new Dimension(0,0));
	    centralMenu.add(bJoinGame,new Dimension(1,0));
	    centralMenu.add(bSettings,new Dimension(2,0));
	    centralMenu.add(bQuit,new Dimension(3,0));
		
		bNewGame.addActionListener(this);
		bJoinGame.addActionListener(this);
		bSettings.addActionListener(this);
		bQuit.addActionListener(this);
		
	}
	
	/*public void paint(Graphics g) {
		try {
			g.drawImage(ImageIO.read(new File("pictures/background.png")), 0, 0, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		centralMenu.add(bNewGame,new Dimension(0,0));
	    centralMenu.add(bJoinGame,new Dimension(1,0));
	    centralMenu.add(bSettings,new Dimension(2,0));
	    centralMenu.add(bQuit,new Dimension(3,0));
	    add(centralMenu, BorderLayout.CENTER);
	    setVisible(true);
    }*/
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bNewGame ){
			getWindow().setContentPane(new NewPanel(getWindow()));
		    getWindow().setVisible(true);
		}else if( e.getSource() == bJoinGame ){
			getWindow().setContentPane(new ServersPanel(getWindow()));
		    getWindow().setVisible(true);
		}else if( e.getSource() == bSettings ){
		}
	}
}
