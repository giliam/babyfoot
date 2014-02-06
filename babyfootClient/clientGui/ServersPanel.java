package clientGui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import clientCore.ClientBabyfoot;


@SuppressWarnings("serial")
public class ServersPanel extends BPanel implements ActionListener, MouseListener {
	private JButton bQuit = new JButton("Quitter");
	private JButton bGo = new JButton("Lancer la partie");
	private JButton bReturn = new JButton("Retour");
	private JButton bRefresh = new JButton("Rafraichir la liste");
	private JList<String> listServersLayout;
	private int selectedGame;
	private long timeFirstClick;
	private String[] listServers;
	
	public ServersPanel(MainFrame f) {
		super(f);
	    
	    ChatPanel chat = new ChatPanel(getWindow());
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
		JPanel menu = new JPanel();
		BorderLayout g = new BorderLayout();
		//g.setVgap(15);
		menu.setLayout(g);
		menu.setBackground(Color.ORANGE);
		add(menu,BorderLayout.CENTER);
		
		//On s'occupe de la liste des serveurs
		listServers = getWindow().getMain().getPlayer().getServers();
		listServersLayout = new JList<String>(listServers);
		listServersLayout.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listServersLayout.setLayoutOrientation(JList.VERTICAL);
		listServersLayout.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(listServersLayout);
		menu.add((new JPanel()).add(listScroller),BorderLayout.CENTER);
		
		JPanel buttonsList = new JPanel();
		buttonsList.setBackground(Color.ORANGE);
		buttonsList.add(bReturn);
		buttonsList.add(bRefresh);
		buttonsList.add(bGo);
		buttonsList.add(bQuit);
		menu.add(buttonsList,BorderLayout.SOUTH);
		
		bGo.setEnabled(false);
		
		//On rajoute le gestionnaire d'événements du double-clic sur un élément de la liste
		listServersLayout.addMouseListener(this);
		
		bReturn.addActionListener(this);
		bRefresh.addActionListener(this);
		bGo.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bReturn ){
			getWindow().setContentPane(new MenuPanel(getWindow()));
		    getWindow().setVisible(true);
		}else if( e.getSource() == bRefresh ){
			listServers = getWindow().getMain().getPlayer().getServers();
			listServersLayout.setListData(listServers);
			bGo.setEnabled(false);
		}else if( e.getSource() == bGo ){
			if( getWindow().getMain().getPlayer().setServer(selectedGame, listServers) ){
				getWindow().setContentPane(new WaitingRoomPanel(getWindow()));
			    getWindow().setVisible(true);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		selectedGame = listServersLayout.locationToIndex(e.getPoint());
		if( selectedGame >= 0 )
			bGo.setEnabled(true);
		if (e.getClickCount() == 2 && ( timeFirstClick - System.currentTimeMillis() ) < 1000 ) {
			if( getWindow().getMain().getPlayer().setServer(selectedGame, listServers) ){
				getWindow().setContentPane(new WaitingRoomPanel(getWindow()));
			    getWindow().setVisible(true);
			}
		}
		timeFirstClick = System.currentTimeMillis();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
