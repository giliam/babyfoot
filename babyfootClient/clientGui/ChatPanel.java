package clientGui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import clientCore.ClientBabyfoot;
import clientCore.Utils;



@SuppressWarnings("serial")
/** Cette classe gère le panel du chat qui peut être affiché à n'importe quel moment. Cette classe s'occupe des événements liés
au chat et à la gestion, notamment, des salons. Pour cela, elle implémente ActionListener, pour l'envoi de message, MouseListener pour
le choix de l'onglet et KeyListener pour la touche Entrée lorsque la zone de texte est sélectionnée. Met en place un système de deux onglets pour
les deux possibilités suivantes : la liste des salons ou les messages du salon actuel. Utilise un Thread pour mettre à jour les messages. Ce
Thread appelle la classe RefreshChat. */
public class ChatPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
	private JButton push = new JButton("Envoyer");
	private JTabbedPane onglet = new JTabbedPane();
	private JList<String> listServersLayout;
	private JTextField textfield;
	private long timeFirstClick;
	private JTextPane text = new JTextPane();
	private JLabel serverName;
	private MainFrame window;
	
	public ChatPanel(MainFrame window) {
		this.window = window;
		//Création de plusieurs Panneau
		JPanel[] tPan = { new JPanel(), new JPanel() };
		tPan[0].setBackground(Color.BLACK);
	    //Création de notre conteneur d'onglets
	    onglet.add("Chat", tPan[0]);
	    onglet.add("Salons", tPan[1]);
	    onglet.setPreferredSize(new Dimension(300,800));
	    
	    //Titre = nom du serveur actuel
	    serverName = new JLabel(window.getMain().getChat().getServer());
	    serverName.setForeground(Color.WHITE);
	    
	    //On s'occupe de la partie tchat en lui-même
	    updateMessages();
		JScrollPane displayZone = new JScrollPane(text);
		text.setEditable(false);
		displayZone.setPreferredSize(new Dimension(280,500));
		textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(180,20));
		push.setPreferredSize(new Dimension(90,25));
		
		//On s'occupe de la liste des serveurs
		listServersLayout = new JList<String>(Utils.formatStringArray(window.getMain().getChat().getServers()));
		listServersLayout.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listServersLayout.setLayoutOrientation(JList.VERTICAL);
		listServersLayout.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(listServersLayout);
		listScroller.setPreferredSize(new Dimension(250, 80));

		Thread tEnvoiMessage = new Thread(new RefreshChat(this));
        tEnvoiMessage.start();
		
		//On entre tout ça
		add(onglet);
		tPan[0].add(serverName);
		tPan[0].add(displayZone);
		tPan[0].add(textfield);
		tPan[0].add(push);
		
		tPan[1].add(listScroller);
		
		//On rajoute le gestionnaire d'événements du double-clic sur un élément de la liste
		listServersLayout.addMouseListener(this);
		//On ajoute les listener
		push.addActionListener(this);
		textfield.addKeyListener(this);
	}
	
	/** Est chargée d'envoyer le contenu de la zone de texte. Cette action est lancée par les événements. */
	private void sendMessage(){
		if( !textfield.getText().equals("") ){
			window.getMain().getChat().sendMessage(textfield.getText());
			updateMessages();
			textfield.setText("");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == push ){
			sendMessage();
		}
	}
	
	/** Met à jour les messages affichées en les récupérant depuis core.Main.getChat().getMessage() */
	public void updateMessages(){
		serverName.setText(window.getMain().getChat().getServer());
		String[] datas = window.getMain().getChat().getMessages();
		String s = "";
		for( int i = 0; i < datas.length; i++ ){
			s += datas[i] + "\n";
		}
		text.setText(s);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && ( timeFirstClick - System.currentTimeMillis() ) < 1000 ) {
			window.getMain().getChat().setServer(listServersLayout.locationToIndex(e.getPoint()));
			serverName.setText(window.getMain().getChat().getServer());
			updateMessages();
			onglet.setSelectedIndex(0);
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

/** Cette classe s'occupe de rafraichir le tchat à intervalles réguliers pour obtenir un suivi en direct des messages. Fait du coup 
appel à la méthode updateMessages() de la classe ChatPanel. Implémente Runnable et est lancée par le constructeur du ChatPanel. */
class RefreshChat implements Runnable{
	private ChatPanel cp;
	public RefreshChat(ChatPanel chatpanel ){
		cp = chatpanel;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cp.updateMessages();
		}
	}
}
