package serverNetwork;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import serverCore.Match;
import serverCore.Utils;
import serverCore.Match.RodPositions;


public class MatchServer extends AbstractServer {
	
	private LinkedList<Match> liste = new LinkedList<Match>();
	
	public void handle(BufferedReader in, PrintWriter out){
		String[] datas = query.split("-");
		String task = datas[1];
    	if( task.equals("add") ){
    		int type = Integer.valueOf(datas[3]);
    		String login = datas[2];
    		if( addMatch(type, login) )
    			out.println("true");
    		else
    			out.println("false");
    		out.flush();
    	}else if(task.equals("setrod")){
    		String login = datas[2];
    		int[] positions = {Integer.valueOf(datas[3]),Integer.valueOf(datas[4]),Integer.valueOf(datas[5]),Integer.valueOf(datas[6])};
    		setRod(login, positions);
    	}else if(task.equals("getpositions")){
    		String login = datas[2];
    		sendPositions( login, out );
    	}else if(task.equals("getserverslist")){
    		getServersList( out );
    	}else if( task.equals( "getmatchinfo" ) ){
    		String login = datas[2];
    		getMatchInfo( login, out );
    	}else if( task.equals( "run" ) ){
    		String login = datas[2];
    		runMatch( login, out );
    	}else if( task.equals( "stop" ) ){
    		String login = datas[2];
    		stopMatch( login );
    	}
	}
	
	private void stopMatch(String login) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		m.stop();
		m = null;
	}

	private void runMatch(String login, PrintWriter out) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		m.setState(Match.States.PLAYING);
		m.start();
	}

	private void getMatchInfo(String login, PrintWriter out) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		if( m == null ){
			out.println("matchinfo-deleted");
			out.flush();
		}else{
			out.println("matchinfo-beginning");
			String display = String.valueOf( m.getState() == Match.States.PLAYING ? 1 : 0 ) + "-";
			display += String.valueOf(m.getType() == Match.Types.TWOVSTWO ? 2 : ( m.getType() == Match.Types.ONEVSONE ? 1 : 3 ));
			if( m.getPlayer1() != null ){
				display += "-" + m.getPlayer1().getLogin();
			}if( m.getPlayer2() != null ){
				display += "-" + ( m.getType() == Match.Types.TWOVSTWO ? "" : " -" ) + m.getPlayer2().getLogin();
			}if( m.getPlayer3() != null ){
				display += "-" + ( m.getType() == Match.Types.TWOVSTWO ? " -" : "" ) + m.getPlayer3().getLogin();
			}if( m.getPlayer4() != null ){
				display += "-" + m.getPlayer4().getLogin();
			}
			display += "- - - - ";
			out.println(display);
			out.println("matchinfo-end");
			out.flush();
		}
	}

	private void getServersList(PrintWriter out) {
		out.println("matchlist-beginning");
		out.println(liste.size());
		Iterator<Match> it = liste.iterator();
		while ( it.hasNext() ){
			Match m = ((Match) it.next());
			String display = ( m.getType() == Match.Types.ONEVSONE ? "1vs1" : ( m.getType() == Match.Types.TWOVSTWO ? "2vs2" : "1vs2" ) );
			int i = 0;
			if( m.getPlayer1() != null ){
				i++;
				display += " - " + m.getPlayer1().getLogin();
			}if( m.getPlayer2() != null ){
				i++;
				display += " - " + m.getPlayer2().getLogin();
			}if( m.getPlayer3() != null ){
				i++;
				display += " - " + m.getPlayer3().getLogin();
			}if( m.getPlayer4() != null ){
				i++;
				display += " - " + m.getPlayer4().getLogin();
			}
			display += " - " + i + " joueur(s) / " + ( m.getType() == Match.Types.ONEVSONE ? "2" : ( m.getType() == Match.Types.TWOVSTWO ? "4" : "3" ) );
			out.println(display);
		}
		out.println("matchlist-end");
		out.flush();
	}

	private int[][] getRodPositions(String login) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		int[][] r = new int[2][4];
		r[0][0] = Math.abs(m.getRodPosition(false,RodPositions.GARDIEN));
		r[0][1] = Math.abs(m.getRodPosition(false,RodPositions.DEFENSE));
		r[0][2] = Math.abs(m.getRodPosition(false,RodPositions.MILIEU));
		r[0][3] = Math.abs(m.getRodPosition(false,RodPositions.ATTAQUE));
		
		r[1][0] = Math.abs(m.getRodPosition(true,RodPositions.GARDIEN));
		r[1][1] = Math.abs(m.getRodPosition(true,RodPositions.DEFENSE));
		r[1][2] = Math.abs(m.getRodPosition(true,RodPositions.MILIEU));
		r[1][3] = Math.abs(m.getRodPosition(true,RodPositions.ATTAQUE));
		return r;
	}
	
	private String getBallPositions(String login) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		return Math.abs(m.getBallX()) + "-" + Math.abs(m.getBallY());
	}

	private void setRod(String login, int[] positions) {
		Match m = ServerBabyfoot.tplayer.getPlayer(login).getMatch();
		Hashtable<RodPositions, Integer> positionsToSend = new Hashtable<RodPositions, Integer>();
		
		positionsToSend.put(RodPositions.GARDIEN, positions[0]);
		positionsToSend.put(RodPositions.DEFENSE, positions[1]);
		positionsToSend.put(RodPositions.MILIEU, positions[2]);
		positionsToSend.put(RodPositions.ATTAQUE, positions[3]);
		
		m.setRodPositions( positionsToSend, ServerBabyfoot.tplayer.getPlayer(login).getSide() );
	}

	private boolean addMatch(int type, String login){
		Iterator<Match> it = liste.iterator();
		while ( it.hasNext() ){
			//On vérifie que le joueur n'est pas déjà dans un match.
		   if( ((Match) it.next()).isPlayer(login) ){
			   return false;
		   }
		}
		liste.add(new Match(login, type == 1 ? Match.Types.ONEVSONE : ( type == 2 ? Match.Types.TWOVSTWO : Match.Types.ONEVSTWO )));
		ServerBabyfoot.tplayer.getPlayer(login).setSide(Utils.Sides.BOTTOM);
		return true;
	}

	public void sendPositions( String login, PrintWriter out){
		int[][] datas = getRodPositions(login);
		String ballPositions = getBallPositions(login);
		
		String display = "positions";
		for( int i = 0; i < 2; i++ ){
			for( int j = 0; j < 4; j++ ){
				display += "-" + Math.abs( datas[i][j] );
			}
		}
		display += "-" + ballPositions;
		out.println(display);
		out.flush();
	}
	
	public LinkedList<Match> getListe() {
		return liste;
	}

	public void removeFromListe(Match match) {
		if( liste.contains(match) ){
			liste.remove(match);
		}
	}
}
