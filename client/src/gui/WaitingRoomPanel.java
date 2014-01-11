package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Main;
import core.Utils;

@SuppressWarnings("serial")
public class WaitingRoomPanel extends BPanel implements ActionListener {
	private JButton bQuit;
	private JButton bGo;
	private JButton bReturn;
	private String[] playersTeamOne;
	private String[] playersTeamTwo;
	private int type;
	public WaitingRoomPanel(MainFrame f) {
		super(f);

		bReturn = new JButton("Retour");
		bGo = new JButton("Lancer la partie");
	    bQuit = new JButton("Quitter");
	    /*
	     * Case 0 contient le type
	     * Case 1 contient le joueur1
	     * Case 2 contient le joueur2 etc.
	     */
	    handleMatchInfo(Main.getClient().getMc().getMatchInfo(Main.getPlayer().getLogin()));
	    
	    ChatPanel chat = new ChatPanel();
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);

		
		//On s'occupe de la liste des serveurs
		JList<String> listMembersTeamOne = new JList<String>(playersTeamOne);
		listMembersTeamOne.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listMembersTeamOne.setLayoutOrientation(JList.VERTICAL);
		listMembersTeamOne.setVisibleRowCount(-1);
		//On s'occupe de la liste des serveurs
		JList<String> listMembersTeamTwo = new JList<String>(playersTeamTwo);
		listMembersTeamTwo.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listMembersTeamTwo.setLayoutOrientation(JList.VERTICAL);
		listMembersTeamTwo.setVisibleRowCount(-1);
		
		JPanel teamOnePanel = new JPanel();
		teamOnePanel.setMinimumSize(new Dimension(100,100));
		teamOnePanel.add(new JScrollPane(listMembersTeamOne));
	    JPanel teamTwoPanel = new JPanel();
	    teamTwoPanel.add(new JScrollPane(listMembersTeamTwo));
	    JSplitPane s = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, teamOnePanel, teamTwoPanel);
	    s.setDividerLocation(250);
	    
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(3,1));
		menu.setBackground(Color.ORANGE);
		add(menu,BorderLayout.CENTER);
		
		menu.add(s);
		
		JPanel buttonsList = new JPanel();
		buttonsList.setBackground(Color.ORANGE);
		buttonsList.add(bReturn);
		buttonsList.add(bGo);
		buttonsList.add(bQuit);
		menu.add(buttonsList);
		
		bReturn.addActionListener(this);
		bGo.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	private void handleMatchInfo(String[] datas) {
		type = Integer.valueOf(datas[0]);
	    switch(type){
	    	case 1:
	    		playersTeamOne = new String[1];
	    		playersTeamOne[0] = datas[1];
	    		playersTeamTwo = new String[1];
    			playersTeamTwo[0] = datas[2];
	    		break;
	    	case 2:
	    		playersTeamOne = new String[2];
	    		playersTeamOne[0] = datas[1];
    			playersTeamOne[1] = datas[2];
	    		playersTeamTwo = new String[2];
    			playersTeamTwo[0] = datas[3];
    			playersTeamTwo[1] = datas[4];
	    		break;
	    	case 3:
	    		playersTeamOne = new String[1];
	    		playersTeamOne[0] = datas[1];
	    		playersTeamTwo = new String[2];
    			playersTeamTwo[0] = datas[2];
    			playersTeamTwo[0] = datas[3];
	    		break;
	    }
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			Main.closeWindow();
		}else if( e.getSource() == bReturn ){
			window.setContentPane(new MenuPanel(window));
		    window.setVisible(true);
		}else if( e.getSource() == bGo ){
			window.setContentPane(new GamePanel(window));
		    window.setVisible(true);
		}
	}
}
