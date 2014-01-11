package gui;

import java.awt.event.*;

import javax.swing.*;
import core.Chat;
import core.Main;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, WindowListener {
	public JPanel pan;
	
	public MainFrame(String title, Chat chat) {
		setTitle(title);
	    setSize(800, 800);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    addWindowListener(this);
	    //Quand on veut tester le programme complet
	    //setContentPane(new ConnexionPanel(this));
	    //Quand on veut tester l'interface du jeu
	    setContentPane(new GamePanel(this));
	    setResizable(false);
	    setVisible(true);
	}
	
	public MainFrame(){
		this("Babyfoot en réseau", null);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		if( !Main.getPlayer().getLogin().equals("") ){
			System.out.println("SUPPRESSION EN COURS");
			Main.getPlayer().removePlayer(Main.getPlayer().getLogin());
		}
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}
