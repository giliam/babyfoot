package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class WaitingRoomPanel extends JPanel implements ActionListener {
	JButton bQuit;
	JButton bGo;
	JButton bReturn;
	MainFrame window;
	public WaitingRoomPanel(MainFrame f) {
		window = f;
		
		bReturn = new JButton("Retour");
		bGo = new JButton("Lancer la partie");
	    bQuit = new JButton("Quitter");
		
		add(bReturn);
		add(bGo);
		add(bQuit);
		
		bReturn.addActionListener(this);
		bGo.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			System.exit(0);
		}else if( e.getSource() == bReturn ){
			window.setContentPane(new MenuPanel(window));
		    window.setVisible(true);
		}
	}
}
