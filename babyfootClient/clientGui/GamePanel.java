package clientGui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {
	
	MainFrame window;
	GameZone gameZone;
	/**
	 * GamePanel Constructor : 
	 * @param f
	 */
	public GamePanel(MainFrame f, boolean testMode) {
		window = f;
		//Nécessaire de mettre 729 car décalage inhérent à l'affichage
		f.setSize(1300,729);
		setLayout(new BorderLayout());
		JPanel chat = null;
		if( !testMode ){
			chat = new ChatPanel(window);
		}else{
			chat = new JPanel();
		}
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,729));
		chat.setMinimumSize(new Dimension(300,729));
		add(chat,BorderLayout.EAST);
		
		gameZone = new GameZone(this, testMode);
    add(gameZone,BorderLayout.CENTER);
    add(gameZone.getInfoPanel(),BorderLayout.WEST);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

	public MainFrame getWindow() {
		return window;
	}
}