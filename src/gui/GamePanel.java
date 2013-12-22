package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
	
	MainFrame window;
	
	/**
	 * GamePanel Constructor : 
	 * @param f
	 */
	public GamePanel(MainFrame f) {
		window = f;
		
		setLayout(new BorderLayout());
		
		ChatPanel chat = new ChatPanel(f);
		chat.setPreferredSize(new Dimension(300,200));
	    add(chat,BorderLayout.EAST);
	    
	    
		
	}
	
	public void actionPerformed(ActionEvent e) {
	}
}
