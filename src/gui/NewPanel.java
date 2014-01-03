package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Main;

@SuppressWarnings("serial")
public class NewPanel extends BPanel implements ActionListener {
	private JButton bQuit = new JButton("Quitter");
	private JButton bGo = new JButton("Ouvrir la partie à d'autres joueurs");
	private JButton bReturn = new JButton("Retour");
	private JRadioButton bNumber1 = new JRadioButton("1 contre 1");
	private JRadioButton bNumber2 = new JRadioButton("2 contre 2");
	private ButtonGroup bNumberPlayers = new ButtonGroup();

	public NewPanel(MainFrame f) {
		super(f);
		
	    ChatPanel chat = new ChatPanel();
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
		bNumber1.setPreferredSize(new Dimension(500,100));
		bNumber1.setBackground(Color.ORANGE);
		bNumber2.setPreferredSize(new Dimension(500,100));
		bNumber2.setBackground(Color.ORANGE);
		
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout());
		menu.setBackground(Color.ORANGE);
		add(menu,BorderLayout.CENTER);
		
		bNumber1.setSelected(true);
	    //On ajoute les boutons au groupe
		bNumberPlayers.add(bNumber1);
		bNumberPlayers.add(bNumber2);
	    
		Box buttonsPlayers = Box.createHorizontalBox();
		buttonsPlayers.add(new JLabel("Nombre de joueurs : "));
		buttonsPlayers.add(bNumber1);
		buttonsPlayers.add(bNumber2);
		Box buttonsBottom = Box.createHorizontalBox();
	    buttonsBottom.add(bReturn);
	    buttonsBottom.add(bGo);
	    buttonsBottom.add(bQuit);
	    //On crée un conteneur avec gestion verticale
	    Box menuContent = Box.createVerticalBox();
	    menuContent.add(buttonsPlayers);
	    menuContent.add(buttonsBottom);
	    menu.add(menuContent);
	    
		
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
