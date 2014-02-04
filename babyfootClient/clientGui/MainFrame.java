package clientGui;

import java.awt.event.*;

import javax.swing.*;

import clientCore.Chat;
import clientCore.Main;


@SuppressWarnings("serial")
/** Cette classe est la fenêtre principale (comme son nom l'indique). Elle s'occupe du conteneur général du projet. Il ne s'agit ensuite
que de modifier le panel intérieur pour mettre à jour la fenêtre. Elle a un WindowListener qui lui permet de gérer plusieurs actions
lorsque le joueur quitte la fenêtre : par exemple, le déconnecter du serveur, ce qui se fait par une requête. Elle sera passée en paramètre
à tous ses Panel enfants car nécessaires pour pouvoir modifier certaines informations (taille). */
public class MainFrame extends JFrame implements WindowListener {
	private Main main;
	
	/** Constructeur qui initialise la fenêtre avec quelques paramètres (Resizable à false) fixés, dont la taille de 800 par 800 par défaut.
	Cela est souvent modifié par les panels fils. */
	public MainFrame(String title, Main m) {
		main = m;
		
		setTitle(title);
	    setSize(800, 800);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    addWindowListener(this);
	    //Quand on veut tester le programme complet
	    setContentPane(new ConnexionPanel(this));
	    //Quand on veut tester l'interface du jeu
	    //setContentPane(new GamePanel(this, true));
	    setResizable(false);
	    setVisible(true);
	}
	
	/** Alias sans paramètres */
	public MainFrame(){
		this("Babyfoot en réseau", null);
	}
	
	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		if( !main.getPlayer().getLogin().equals("") ){
			System.out.println("SUPPRESSION EN COURS");
			main.getPlayer().removePlayer(main.getPlayer().getLogin());
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

	public Main getMain() {
		return main;
	}
}
