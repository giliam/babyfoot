package gui;
import java.awt.*;
import java.awt.event.*;
import java.security.Timestamp;
import java.util.Map;

import javax.swing.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
	private MainFrame window;
	private JButton push = new JButton("Envoyer");
	private JTabbedPane onglet = new JTabbedPane();
	private JList<String> listServersLayout;
	private JTextField textfield;
	private int[] listServers;
	private long timeFirstClick;
	
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
		textfield = new  JTextField();
		textfield.setPreferredSize(new Dimension(180,20));
		push.setPreferredSize(new Dimension(90,25));
		
		//On s'occupe de la liste des serveurs
		listServersLayout = new JList<String>(format(window.chat.getServers()));
		listServersLayout.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listServersLayout.setLayoutOrientation(JList.VERTICAL);
		listServersLayout.setVisibleRowCount(-1);
		
		//On rajoute le gestionnaire d'événements du double-clic sur un élément de la liste
		JScrollPane listScroller = new JScrollPane(listServersLayout);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		//On entre tout ça
		add(onglet);
		tPan[0].add(displayZone);
		tPan[0].add(textfield);
		tPan[0].add(push);
		
		tPan[1].add(listScroller);
		
		//On ajoute les listener
		listServersLayout.addMouseListener(this);
		push.addActionListener(this);
		textfield.addKeyListener(this);
	}
	
	private void sendMessage(){
		if( !textfield.getText().equals("") ){
			window.chat.sendMessage(textfield.getText());
			textfield.setText("");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == push ){
			sendMessage();
		}
	}
	
	public String[] format(String[] list){
		listServers = new int[list.length];
		for(int i = 0; i<list.length; i++){
			String[] m = list[i].split(" - ",2);
			listServers[i] = Integer.valueOf(m[0]);
			list[i] = m[1];
		}
		return list;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && ( timeFirstClick - System.currentTimeMillis() ) < 1000 ) {
			int index = listServersLayout.locationToIndex(e.getPoint());
			window.chat.setChat(listServers[index]);
		}
		timeFirstClick = System.currentTimeMillis();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent event) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if( event.getKeyChar() == Event.ENTER ){
			sendMessage();
		}
	}
}
