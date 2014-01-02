package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Main;

@SuppressWarnings("serial")
public class ConnexionPanel extends BPanel implements ActionListener {
	JButton bQuit = new JButton("Quitter");
	JTextField fpseudo = new JTextField("giliam");
	JLabel error = new JLabel("", (int) Component.CENTER_ALIGNMENT);
	JButton bConnect = new JButton("Rejoindre une partie");
	
	public ConnexionPanel(MainFrame f) {
		super(f);
		f.setSize(800,300);
		setLayout(new FlowLayout());
	    //Gauche-droite
	    fpseudo.setPreferredSize(new Dimension(200,30));
	    error.setPreferredSize(new Dimension(700,30));
	    //Gestion des boutons
	    add(error);
	    add(fpseudo);
	    add(bConnect);
	    add(bQuit);
		
	    bConnect.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			Main.closeWindow();
		}else if( e.getSource() == bConnect ){
			if( !fpseudo.getText().equals("") ){
				if( Main.getDb().addPlayer(fpseudo.getText()) ) {
					window.chat.setLogin(fpseudo.getText());
					window.setContentPane(new MenuPanel(window));
			    	window.setVisible(true);
				}else{
					error.setForeground(Color.RED);
					error.setText("Login déjà utilisé ! ");
				}
			}
		}
	}
}
