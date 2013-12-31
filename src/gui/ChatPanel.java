package gui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements ActionListener {
	private MainFrame window;
	private JButton push = new JButton("Envoyer");
	private JTabbedPane onglet = new JTabbedPane();
	private JComboBox listServers;
	
	
	public ChatPanel(MainFrame f) {
		window = f;
		
		//Création de plusieurs Panneau
		JPanel[] tPan = { new JPanel(), new JPanel() };
		tPan[0].setBackground(Color.BLACK);
	    //Création de notre conteneur d'onglets
	    onglet.add("Chat", tPan[0]);
	    onglet.add("Salons", tPan[1]);
	    onglet.setPreferredSize(new Dimension(300,800));
	    
	    //On s'occupe de la partie tchat en lui-même
		JTextPane text = new JTextPane();
		text.setText("");
		
		JScrollPane displayZone = new JScrollPane(text);
		displayZone.setPreferredSize(new Dimension(280,500));
		
		JTextField textfield = new  JTextField();
		textfield.setPreferredSize(new Dimension(180,20));
		push.setPreferredSize(new Dimension(90,25));
		
		//Liste des serveurs		
		listServers = new JComboBox<String>(window.chat.getServers());
		add(onglet);
		tPan[0].add(displayZone);
		tPan[0].add(textfield);
		tPan[0].add(push);
		
		tPan[1].add(listServers);
	}
	
	public void actionPerformed(ActionEvent e) {
	}
}
