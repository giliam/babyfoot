package gui;

import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
	JPanel pan;
	
	public MainFrame(String title) {
		setTitle(title);
	    setSize(800, 800);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //Quand on veut tester le programme complet
	    //setContentPane(new ConnexionPanel(this));
	    //Quand on veut tester l'interface du jeu
	    setContentPane(new GamePanel(this));
	    setResizable(false);
	    setVisible(true);
	}
	
	public MainFrame(){
		this("Babyfoot en réseau");
	}
	
	
	
	static public void main(String[] args) {
	    new MainFrame();
	}

	public void actionPerformed(ActionEvent arg0) {
		
	}
}
