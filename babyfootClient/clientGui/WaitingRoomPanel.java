package clientGui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import clientCore.ClientBabyfoot;
import clientCore.Utils;
import clientCore.Utils.Types;


@SuppressWarnings("serial")
public class WaitingRoomPanel extends BPanel implements ActionListener {
	private JButton bQuit;
	private JButton bGo;
	private JButton bReturn;
	private String[] playersTeamOne;
	private String[] playersTeamTwo;
	private Utils.Types type;
	
	private RefreshRoom refreshRoom;
	
	private JList<String> listMembersTeamOne = new JList<String>();
	private JList<String> listMembersTeamTwo = new JList<String>();
	public WaitingRoomPanel(MainFrame f) {
		super(f);

		bReturn = new JButton("Retour");
		bGo = new JButton("Lancer la partie");
		bGo.setEnabled(false);
	    bQuit = new JButton("Quitter");
	    /*
	     * Case 0 contient le type
	     * Case 1 contient le joueur1
	     * Case 2 contient le joueur2 etc.
	     */
	    handleMatchInfo(getWindow().getMain().getClient().getMc().getMatchInfo(getWindow().getMain().getPlayer().getLogin()));
	    
	    ChatPanel chat = new ChatPanel(getWindow());
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(300,700));
		chat.setMinimumSize(new Dimension(300,700));
		add(chat,BorderLayout.EAST);
		
		
		//On s'occupe de la liste des serveurs
		listMembersTeamOne = new JList<String>(playersTeamOne);
		listMembersTeamOne.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listMembersTeamOne.setLayoutOrientation(JList.VERTICAL);
		listMembersTeamOne.setVisibleRowCount(-1);
		//On s'occupe de la liste des serveurs
		listMembersTeamTwo = new JList<String>(playersTeamTwo);
		listMembersTeamTwo.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listMembersTeamTwo.setLayoutOrientation(JList.VERTICAL);
		listMembersTeamTwo.setVisibleRowCount(-1);
		JScrollPane s1 = new JScrollPane(listMembersTeamOne);
		s1.setPreferredSize(new Dimension(200,100));
		JScrollPane s2 = new JScrollPane(listMembersTeamTwo);
		s2.setPreferredSize(new Dimension(200,100));
		JPanel teamOnePanel = new JPanel();
		teamOnePanel.add(s1);
	    JPanel teamTwoPanel = new JPanel();
	    teamTwoPanel.add(s2);
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
		refreshRoom = new RefreshRoom(this);
		Thread tRefreshRoom = new Thread(refreshRoom);
		tRefreshRoom.start();
		
		bReturn.addActionListener(this);
		bGo.addActionListener(this);
		bQuit.addActionListener(this);
	}
	
	

	private void handleMatchInfo(String[] datas) {
		int go = Integer.valueOf(datas[0]);
		if( go == 1 ){
			startGame();
		}else{
			int type = Integer.valueOf(datas[1]);
			if( type == 1 )
				this.type = Utils.Types.ONEVSONE;
			else if( type == 2 )
				this.type = Utils.Types.TWOVSTWO;
			else if( type == 3 )
				this.type = Utils.Types.ONEVSTWO;
		    switch(type){
		    	case 1:
		    		playersTeamOne = new String[1];
		    		playersTeamOne[0] = datas[2];
		    		playersTeamTwo = new String[1];
	    			playersTeamTwo[0] = datas[3];
		    		break;
		    	case 2:
		    		playersTeamOne = new String[2];
		    		playersTeamOne[0] = datas[2];
	    			playersTeamOne[1] = datas[3];
		    		playersTeamTwo = new String[2];
	    			playersTeamTwo[0] = datas[4];
	    			playersTeamTwo[1] = datas[5];
		    		break;
		    	case 3:
		    		playersTeamOne = new String[1];
		    		playersTeamOne[0] = datas[2];
		    		playersTeamTwo = new String[2];
	    			playersTeamTwo[0] = datas[3];
	    			playersTeamTwo[0] = datas[4];
		    		break;
		    }
		}
	}
	
	private boolean testIsReady() {
	    switch(type){
	    	case ONEVSONE:
	    		return !playersTeamOne[0].equals(" ") && !playersTeamTwo[0].equals(" "); 
	    	case TWOVSTWO:
	    		return !playersTeamOne[0].equals(" ") && !playersTeamOne[1].equals(" ") && !playersTeamTwo[1].equals(" ") && !playersTeamTwo[0].equals(" ");
	    	case ONEVSTWO:
	    		return !playersTeamOne[0].equals(" ") && !playersTeamTwo[0].equals(" ") && !playersTeamTwo[1].equals(" ");
	    }
		return false;
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			if( getWindow().getMain().getPlayer().isBoss() ){
				getWindow().getMain().getPlayer().stopMatch();
			}
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bReturn ){
			if( getWindow().getMain().getPlayer().isBoss() ){
				getWindow().getMain().getPlayer().stopMatch();
			}
			getWindow().setContentPane(new MenuPanel(getWindow()));
		    getWindow().setVisible(true);
		}else if( e.getSource() == bGo && getWindow().getMain().getPlayer().isBoss() && testIsReady() ){
			getWindow().getMain().getPlayer().runMatch();
			startGame();
		}
	}
	
	
	private void startGame(){
		boolean sideOk = false;
		for( int i = 0; i < playersTeamOne.length; i++ ){
			if( playersTeamOne[i].equals(getWindow().getMain().getPlayer().getLogin())){
				getWindow().getMain().getPlayer().setSide(Utils.Sides.BOTTOM);
				sideOk = true;
				break;
			}
		}
		if( !sideOk ){
			getWindow().getMain().getPlayer().setSide(Utils.Sides.UP);
		}
		refreshRoom.run = false;
		getWindow().getMain().getClient().getGc().startThread();
		getWindow().setContentPane(new GamePanel(getWindow(),false));
	    getWindow().setVisible(true);
	}


	public void refresh() {
		handleMatchInfo(getWindow().getMain().getClient().getMc().getMatchInfo(getWindow().getMain().getPlayer().getLogin()));
		if( getWindow().getMain().getClient().getMc().isToDelete() ){
			getWindow().setContentPane(new MenuPanel(getWindow()));
			getWindow().getMain().getClient().getMc().setToDelete(false);
		    getWindow().setVisible(true);
		}else{
			listMembersTeamOne.setListData(playersTeamOne);
			listMembersTeamTwo.setListData(playersTeamTwo);
			if( testIsReady() && getWindow().getMain().getPlayer().isBoss() )
				bGo.setEnabled(true);
			else
				bGo.setEnabled(false);
		}
	}
}

class RefreshRoom implements Runnable{
	private WaitingRoomPanel waitingroom;
	public boolean run;
	public RefreshRoom(WaitingRoomPanel wp){
		waitingroom = wp;
		run = true;
	}
	
	public void run(){
		while(run){
			waitingroom.refresh();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

