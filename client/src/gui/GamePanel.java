package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {
	
	MainFrame window;
	JPanel gameZone;
	/**
	 * GamePanel Constructor : 
	 * @param f
	 */
	public GamePanel(MainFrame f, boolean testMode) {
		window = f;
		f.setSize(1200,700);
		setLayout(new BorderLayout());
		JPanel chat = null;
		if( !testMode ){
			chat = new ChatPanel(window);
		}else{
			chat = new JPanel();
		}
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
	    gameZone = new GameZone(window, testMode);
	    add(gameZone,BorderLayout.CENTER);
	    
	    repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
}
