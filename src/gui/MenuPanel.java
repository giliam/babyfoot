package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuPanel extends BPanel implements ActionListener {
	JButton bQuit = new JButton("Quitter");
	JButton bNewGame = new JButton("Nouvelle partie");
	JButton bJoinGame = new JButton("Rejoindre une partie");
	JButton bSettings = new JButton("Options");
	
	public MenuPanel(MainFrame f) {
		super(f);
		f.setSize(800,800);
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
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			System.exit(0);
		}else if( e.getSource() == bNewGame ){
			window.setContentPane(new NewPanel(window));
		    window.setVisible(true);
		}else if( e.getSource() == bJoinGame ){
			window.setContentPane(new ServersPanel(window));
		    window.setVisible(true);
		}else if( e.getSource() == bSettings ){
		}
	}
}
