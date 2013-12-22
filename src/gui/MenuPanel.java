package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MenuPanel extends JPanel implements ActionListener {
	JButton bQuit;
	JButton bNewGame;
	JButton bJoinGame;
	JButton bSettings;
	MainFrame window;
	
	public MenuPanel(MainFrame f) {
		window = f;
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		//Gestion du header
	    Header header = new Header();
	    header.setPreferredSize(new Dimension(800,150));
	    header.setMaximumSize(new Dimension(800,150));
	    add(header,BorderLayout.NORTH);
	    
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
	    JPanel centralMenu = new JPanel();
	    centralMenu.setBackground(Color.WHITE);
	    centralMenu.setPreferredSize(new Dimension(400,100));
	    centralMenu.setMaximumSize(new Dimension(400,100));
	    add(centralMenu, BorderLayout.CENTER);
	    
	    //Gestion des boutons
	    bNewGame = new JButton("Nouvelle partie");
	    bNewGame.setPreferredSize(new Dimension(200,50));
	    bJoinGame = new JButton("Rejoindre une partie");
	    bJoinGame.setPreferredSize(new Dimension(200,50));
	    bSettings = new JButton("Options");
	    bSettings.setPreferredSize(new Dimension(200,50));
	    bQuit = new JButton("Quitter");
	    bQuit.setPreferredSize(new Dimension(200,50));
		
	    centralMenu.add(bNewGame,new Dimension(0,0));
	    centralMenu.add(bJoinGame,new Dimension(1,0));
	    centralMenu.add(bSettings,new Dimension(2,0));
	    centralMenu.add(bQuit,new Dimension(3,0));
		
		bNewGame.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			System.exit(0);
		}else if( e.getSource() == bNewGame ){
			window.setContentPane(new WaitingRoomPanel(window));
		    window.setVisible(true);
		}else if( e.getSource() == bJoinGame ){
			
		}else if( e.getSource() == bSettings ){
			
		}
	}
}
