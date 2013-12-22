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
		//Gestion du header
	    Header header = new Header();
	    header.setPreferredSize(new Dimension(800,150));
	    header.setMaximumSize(new Dimension(800,150));
	    add(header,BorderLayout.NORTH);
	    
	    //Gauche-droite
	    JPanel left = new JPanel();
	    left.setPreferredSize(new Dimension(300,200));
	    add(left,BorderLayout.WEST);
	    
	    JPanel bottom = new JPanel();
	    bottom.setPreferredSize(new Dimension(800,300));
	    add(bottom,BorderLayout.SOUTH);
	    
	    JPanel right = new JPanel();
	    right.setPreferredSize(new Dimension(300,200));
	    add(right,BorderLayout.EAST);
	    //Gestion du menu central
	    JPanel centralMenu = new JPanel();
	    centralMenu.setLayout(new GridLayout(4,1));
	    centralMenu.setPreferredSize(new Dimension(400,100));
	    centralMenu.setMaximumSize(new Dimension(400,100));
	    add(centralMenu, BorderLayout.CENTER);
	    
	    //Gestion des boutons
	    bNewGame = new JButton("Nouvelle partie");
	    bJoinGame = new JButton("Rejoindre une partie");
	    bSettings = new JButton("Options");
	    bQuit = new JButton("Quitter");
		
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
