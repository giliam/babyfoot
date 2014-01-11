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
	public GamePanel(MainFrame f) {
		window = f;
		f.setSize(1200,700);
		setLayout(new BorderLayout());

		ChatPanel chat = new ChatPanel();
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
	    gameZone = new GameZone();
	    add(gameZone,BorderLayout.CENTER);
	    
	    repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
}