package clientNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import clientCore.Utils;
import clientCore.Utils.Sides;
import clientCore.Utils.Rod;


public class MatchClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    public PrintWriter getOut() {
		return out;
	}

	private BufferedReader in = null;
    private MatchReceptionMessage prc;
    private boolean ok;
    private String[] serverList;
    private String[] matchDatas;
    private int leftScore = 0;
    private int rightScore = 0;
    private boolean pause = false;
    private boolean toDelete = false;
    private int statusRod = 0; 

	public MatchClient(Socket s){
        socket = s;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            prc = new MatchReceptionMessage(in, this);
            Thread tReceptionMessage = new Thread(prc);
            tReceptionMessage.start();
            
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
	public void setRodPositions(String login, int[] rodPositions) {
		out.println("match" + Utils.SEPARATOR + "setrod" + Utils.SEPARATOR + login + Utils.SEPARATOR 
				+ rodPositions[0] + Utils.SEPARATOR + rodPositions[1] + Utils.SEPARATOR + rodPositions[2] + Utils.SEPARATOR + rodPositions[3] );
    	out.flush();
	}

	public String[] getServers() {
		out.println("match" + Utils.SEPARATOR + "getserverslist" );
    	out.flush();
		try {
			Thread.currentThread();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getServerList();
	}

	public boolean setServerFromHost(String login, String loginHost) {
		out.println("player" + Utils.SEPARATOR + "joinmatch" + Utils.SEPARATOR + login + Utils.SEPARATOR + loginHost );
    	out.flush();
    	try {
			Thread.currentThread();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	if( statusRod != 0 ){
    		setOk(false);
    		return true;
    	}else{
    		return false;
    	}
	}
	
	public String[] getMatchInfo(String login) {
		out.println("match" + Utils.SEPARATOR + "getmatchinfo" + Utils.SEPARATOR + login );
    	out.flush();
		try {
			Thread.currentThread();
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getMatchDatas();
	}

	public String[] getServerList() {
		return serverList;
	}

	public void setServerList(String[] serverList) {
		this.serverList = serverList;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public void setMatchDatas(String[] md) {
		switch( Integer.valueOf( md[1] ) ){
			case 1:
				matchDatas = new String[4];
				matchDatas[1] = "1";
				if( md[2].equals(" ") ){
					matchDatas[2] = "";
					matchDatas[3] = md[3];
				}else if( md[3].equals(" ") ){
					matchDatas[2] = md[2];
					matchDatas[3] = md[4];
				}
				break;
			case 2:
				matchDatas = new String[6];
				matchDatas[1] = "2";
				if( md[2].equals(" ") ){
					matchDatas[2] = "";
					matchDatas[3] = "";
					matchDatas[4] = md[3];
					matchDatas[5] = md[4];
				}else if( md[3].equals(" ") ){
					matchDatas[2] = md[2];
					matchDatas[3] = "";
					matchDatas[4] = md[4];
					matchDatas[5] = md[5];
				}else if( md[4].equals(" ") ){
					matchDatas[2] = md[2];
					matchDatas[3] = md[3];
					matchDatas[4] = md[5];
					matchDatas[5] = md[6];
				}else if( md[5].equals(" ") ){
					matchDatas[2] = md[2];
					matchDatas[3] = md[3];
					matchDatas[4] = md[4];
					matchDatas[5] = "";
				}else{
					matchDatas[2] = md[2];
					matchDatas[3] = md[3];
					matchDatas[4] = md[4];
					matchDatas[5] = md[5];
				}
				break;
			case 3:
				matchDatas = new String[5];
				matchDatas[1] = "3";
				if( md[2].equals(" ") ){
					matchDatas[2] = "";
					matchDatas[3] = md[3];
					matchDatas[4] = md[4];
				}else if( md[3].equals(" ") ){
					matchDatas[2] = md[2];
					matchDatas[3] = md[4];
					matchDatas[4] = md[5];
				}
				break;
		}
		matchDatas[0] = md[0];
	}

	public String[] getMatchDatas() {
		return matchDatas;
	}

	public void initMatchDatas(int i) {
		matchDatas = new String[i];
	}

	public void runMatch(String login) {
		out.println("match" + Utils.SEPARATOR + "run" + Utils.SEPARATOR + login );
    	out.flush();
		try {
			Thread.currentThread();
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stopMatch(String login) {
		out.println("match" + Utils.SEPARATOR + "stop" + Utils.SEPARATOR + login );
    	out.flush();
    	statusRod = 0;
	}

	public void deleteMatch() {
		toDelete = true;
	}
	

    public boolean isToDelete() {
		return toDelete;
	}

	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
	}

	public void sendShoot(String login, long duration, Rod rodPosition, Sides side) {
		out.println("match" + Utils.SEPARATOR + "shoot" + Utils.SEPARATOR + login + Utils.SEPARATOR + rodPosition + Utils.SEPARATOR + side );
    	out.flush();
	}

	public void quitMatch(String login) {
		out.println("match" + Utils.SEPARATOR + "quit" + Utils.SEPARATOR + login );
    	out.flush();
	}

	public void setMatchInfos(String message) {
		String[] s = message.split(Utils.SEPARATOR);
		setLeftScore(Integer.valueOf(s[1]));
		setRightScore(Integer.valueOf(s[2]));
		setPause(Boolean.valueOf(s[3]));
	}
	
	public void setMatchCarac(String message) {
		try{
			statusRod = Integer.valueOf(message);
		}catch(NumberFormatException e){
		}
	}

	public int getLeftScore() {
		return leftScore;
	}

	public void setLeftScore(int leftScore) {
		this.leftScore = leftScore;
	}

	public int getRightScore() {
		return rightScore;
	}

	public void setRightScore(int rightScore) {
		this.rightScore = rightScore;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getStatusRod() {
		return statusRod;
	}

	public void setStatusRod(int statusRod) {
		this.statusRod = statusRod;
	}
}

class MatchReceptionMessage implements Runnable{
	
	BufferedReader in;
	boolean ready;
	MatchClient matchClient;
	
	public MatchReceptionMessage(BufferedReader in, MatchClient matchClient){
		this.in = in;
		this.matchClient = matchClient;
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		int type = 0;
		System.out.println("Prêt à la réception pour les match !");
		try {
			while(true){
            	String message = in.readLine();
            	if(message.equals("matchinfo" + Utils.SEPARATOR + "deleted")){
            		matchClient.deleteMatch();
            	}
            	if( ( message.equals("matchlist" + Utils.SEPARATOR + "beginning") || message.equals("matchdata" + Utils.SEPARATOR + "beginning") ) && mode == 0 ){
            		if( message.equals("matchdata" + Utils.SEPARATOR + "beginning") ){
            			type = 1;
            			matchClient.initMatchDatas(5);
            			mode = 2;
            		}else
            			mode = 1;
            	}else if( mode == 1 ){
            		if( type == 0){
            			matchClient.setServerList(new String[Integer.valueOf(message)]);
            		}
            		mode = 2;
            	}else if( ( message.equals( "matchlist" + Utils.SEPARATOR + "end") || message.equals("matchdata" + Utils.SEPARATOR + "end") )  && mode == 2 ){
            		mode = 0;
            		type = 0;
            		n = 0;
            	}else if( mode == 2 ){
            		if( type == 0)
            			matchClient.getServerList()[n++] = message;
            		else if( type == 1){
            			matchClient.setMatchDatas(message.split(Utils.SEPARATOR));
            		}
            	}else{
            		matchClient.setOk(message.equals("true"));
            		matchClient.setMatchCarac(message);
            	}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
 