package gui;
import java.awt.*;
import java.awt.event.*;
import java.security.Timestamp;
import java.util.Map;

import javax.swing.*;

import core.Main;
import core.Utils;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
	private MainFrame window;
	private JButton push = new JButton("Envoyer");
	private JTabbedPane onglet = new JTabbedPane();
	private JList<String> listServersLayout;
	private JTextField textfield;
	private long timeFirstClick;
	private JTextPane text = new JTextPane();
	
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
	    updateMessages();
		JScrollPane displayZone = new JScrollPane(text);
		displayZone.setPreferredSize(new Dimension(280,500));
		textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(180,20));
		push.setPreferredSize(new Dimension(90,25));
		
		//On s'occupe de la liste des serveurs
		listServersLayout = new JList<String>(Utils.format(Main.getChat().getServers()));
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
			Main.getChat().sendMessage(textfield.getText());
			updateMessages();
			textfield.setText("");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == push ){
			sendMessage();
		}
	}
	
	public void updateMessages(){
		String[] datas = Main.getChat().getMessages();
		String s = "";
		for( int i = 0; i < datas.length; i++ ){
			s += datas[i] + "\n";
		}
		text.setText(s);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && ( timeFirstClick - System.currentTimeMillis() ) < 1000 ) {
			Main.getChat().setServer(listServersLayout.locationToIndex(e.getPoint()));
			updateMessages();
			
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
