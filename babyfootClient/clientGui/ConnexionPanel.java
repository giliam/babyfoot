package clientGui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import clientCore.ClientBabyfoot;
import clientCore.Utils;


/** Cette classe s'occupe du premier écran affiché lors du lancement du jeu, c'est-à-dire un écran de connexion qui permet à l'utilisateur de choisir un pseudonyme.
Ce pseudonyme sera ensuite utilisé sur les tchats ou lors de la création de parties. Il pourrait permettre de stocker des highscores ou encore certaines statistiques. Par défaut, les 
pseudonymes générés sont des nombres entre 0 et 10 000 000 afin de permettre des tests faciles. */
@SuppressWarnings("serial")
public class ConnexionPanel extends BPanel implements ActionListener, KeyListener {
	JButton bQuit = new JButton("Quitter");
	JTextField fpseudo = new JTextField( String.valueOf( (int)(Math.random()*10000000) ) );
	JLabel error = new JLabel("", (int) Component.CENTER_ALIGNMENT);
	JButton bConnect = new JButton("Rejoindre une partie");
	
	/**
	 * Ce constructeur met la fenêtre à la bonne taille puis ajoute le champ affichant les messages d'erreur puis le champ de texte et les boutons.
	 * @param f
	 */
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
	
	/**
	 * Gère les actions faisables en appuyant sur les deux boutons, méthode très sommaire en somme. 
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bConnect ){
			logIn();
		}
	}
	
	/**
	 * Permet de connecter l'utilisateur à partir du login entré dans le champ de texte. Applique certaines règles : unicité du login puisque c'est ce qui identifie le joueur
	 * à défaut de mettre en place un système d'UID (Unique Identity). De plus, comme il sera présent dans les requêtes envoyées entre client et serveur, il doit respecter une certaine
	 * nomenclature, à savoir ne pas comprendre de tiret. Au cas où l'un de ces deux tests ne seraient pas validés, un message d'erreur s'affiche. 
	 */
	public void logIn(String s){
		if( !s.equals("") || !fpseudo.getText().equals("") ){
			if( s.equals("") )
				s = fpseudo.getText();
			if( s.split(Utils.SEPARATOR).length > 1){
				error.setForeground(Color.RED);
				error.setText("Login contenant un " + Utils.SEPARATOR + " ce qui n'est pas permis ! ");
			}else if( getWindow().getMain().getPlayer().addPlayer(s) ) {
				getWindow().setContentPane(new MenuPanel(getWindow()));
		    	getWindow().setVisible(true);
			}else{
				error.setForeground(Color.RED);
				error.setText("Login déjà utilisé ! ");
			}
		}
	}
	
	public void logIn(){
		logIn("");
	}
	
	public void keyPressed(KeyEvent event) {
		
	}

	public void keyReleased(KeyEvent event) {
	}
	
	/**
	 * Permet de valider le formulaire avec la touche Entrée, ce qui est bien pratique lors des tests.
	 */
	public void keyTyped(KeyEvent event) {
		if( event.getKeyChar() == Event.ENTER ){
			logIn();
		}
	}
}
