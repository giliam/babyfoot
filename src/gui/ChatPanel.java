package gui;
import core.Chat;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatPanel extends JPanel implements ActionListener {
	MainFrame window;
	JButton push;
	JTabbedPane onglet;
	public ChatPanel(MainFrame f) {
		Chat c = new Chat();
		
		window = f;
		
		//Création de plusieurs Panneau
		JPanel[] tPan = { new JPanel(), new JPanel() };
		tPan[0].setBackground(Color.BLACK);
	    //Création de notre conteneur d'onglets
		onglet = new JTabbedPane();
	    onglet.add("Chat", tPan[0]);
	    onglet.add("Salons", tPan[1]);
	    onglet.setPreferredSize(new Dimension(300,800));
	    
		JTextPane text = new JTextPane();
		text.setText(c.getText() + c.getText());
		
		JScrollPane displayZone = new JScrollPane(text);
		displayZone.setPreferredSize(new Dimension(280,500));
		
		JTextField textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(180,20));
		push = new JButton("Envoyer");
		push.setPreferredSize(new Dimension(90,25));
		
		add(onglet);
		tPan[0].add(displayZone);
		tPan[0].add(textfield);
		tPan[0].add(push);
	}
	
	public void actionPerformed(ActionEvent e) {
	}
}
