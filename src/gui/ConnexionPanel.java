package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class ConnexionPanel extends BPanel implements ActionListener {
	JButton bQuit = new JButton("Quitter");
	JTextField fpseudo = new JTextField("Joueur " + String.valueOf(1));
	JButton bConnect = new JButton("Rejoindre une partie");
	
	public ConnexionPanel(MainFrame f) {
		super(f);
		f.setSize(800,300);
		setLayout(new FlowLayout());
	    //Gauche-droite
	    fpseudo.setPreferredSize(new Dimension(200,30));
	    //Gestion des boutons
	    add(fpseudo,new Dimension(0,0));
	    add(bConnect,new Dimension(1,0));
	    add(bQuit,new Dimension(3,0));
		
	    bConnect.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			System.exit(0);
		}else if( e.getSource() == bConnect ){
			if( !fpseudo.getText().equals("") ){
				window.chat.setLogin(fpseudo.getText());
				window.setContentPane(new MenuPanel(window));
		    	window.setVisible(true);
			}
		}
	}
}
