package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Main;

@SuppressWarnings("serial")
public class ConnexionPanel extends BPanel implements ActionListener, KeyListener {
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
		fpseudo.addKeyListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			Main.closeWindow();
		}else if( e.getSource() == bConnect ){
			logIn();
		}
	}
	
	public void logIn(){
		if( !fpseudo.getText().equals("") ){
			if( Main.getPlayer().addPlayer(fpseudo.getText()) ) {
				
				window.setContentPane(new MenuPanel(window));
		    	window.setVisible(true);
			}else{
				error.setForeground(Color.RED);
				error.setText("Login déjà utilisé ! ");
			}
		}
	}
	
	public void keyPressed(KeyEvent event) {
		
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
		if( event.getKeyChar() == Event.ENTER ){
			logIn();
		}
	}
}
