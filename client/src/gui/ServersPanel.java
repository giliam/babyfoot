package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Main;

@SuppressWarnings("serial")
public class ServersPanel extends BPanel implements ActionListener {
	JButton bQuit;
	JButton bGo;
	JButton bReturn;
	public ServersPanel(MainFrame f) {
		super(f);
		
		bReturn = new JButton("Retour");
		bGo = new JButton("Lancer la partie");
	    bQuit = new JButton("Quitter");
	    
	    ChatPanel chat = new ChatPanel();
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
		JPanel menu = new JPanel();
		menu.setBackground(Color.ORANGE);
		add(menu,BorderLayout.CENTER);
		
		menu.add(bReturn);
		menu.add(bGo);
		menu.add(bQuit);
		
		bReturn.addActionListener(this);
		bGo.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			Main.closeWindow();
		}else if( e.getSource() == bReturn ){
			window.setContentPane(new MenuPanel(window));
		    window.setVisible(true);
		}else if( e.getSource() == bGo ){
			window.setContentPane(new WaitingRoomPanel(window));
		    window.setVisible(true);
		}
	}
}
