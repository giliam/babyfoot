package clientNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import clientCore.ClientBabyfoot;
import clientCore.Utils;

public class GameClient implements Runnable {
	private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private GameReceptionMessage grc;
    public String[] rodPositionsAndStatus;
    private int ballX;
    private int ballY;
	public boolean go;
	private ClientBabyfoot main;
	private Thread tReceptionMessage;
    
    public GameClient(Socket s, ClientBabyfoot m){
    	setMain(m);
        socket = s;
        go = false;
        rodPositionsAndStatus = new String[8];
        System.out.println("Update");
        rodPositionsAndStatus[0] = "100";
        rodPositionsAndStatus[1] = "150";
        rodPositionsAndStatus[2] = "100";
        rodPositionsAndStatus[3] = "100";
        rodPositionsAndStatus[4] = "100";
        rodPositionsAndStatus[5] = "150";
        rodPositionsAndStatus[6] = "100";
        rodPositionsAndStatus[7] = "100";
        
        ballX = 450;
        ballY = 350;
    }
    
    public void run() {
        try {
        	out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            grc = new GameReceptionMessage(in, this);
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }
    
    public void setRodPositions(String login, int[] rodPositions) {
		out.println("match" + Utils.SEPARATOR + "setrod" + Utils.SEPARATOR + login + Utils.SEPARATOR + rodPositions[0] + Utils.SEPARATOR + rodPositions[1] + Utils.SEPARATOR  + rodPositions[2] + Utils.SEPARATOR  + rodPositions[3] );
    	out.flush();
	}
    
    public String[] getPositions(String login, boolean request ) {
    	if( !request ){
			out.println("match" + Utils.SEPARATOR + "getpositions" + Utils.SEPARATOR + login );
	    	out.flush();
    	}
    	return rodPositionsAndStatus;
	}
    
    public int getBallX() {
		return ballX;
	}

	public void setBallX(int ballX) {
		this.ballX = ballX;
	}

	public int getBallY() {
		return ballY;
	}

	public void setBallY(int ballY) {
		this.ballY = ballY;
	}

	public String[] getRodPositionsAndStatus() {
		return rodPositionsAndStatus;
	}

	public void setRodPositionsAndStatus(String[] rodPositions) {
		this.rodPositionsAndStatus = rodPositions;
	}

	public Thread gettReceptionMessage() {
		return tReceptionMessage;
	}

	public void settReceptionMessage(Thread tReceptionMessage) {
		this.tReceptionMessage = tReceptionMessage;
	}

	public void startThread() {
		tReceptionMessage = new Thread(grc);
		tReceptionMessage.start();
	}

	public ClientBabyfoot getMain() {
		return main;
	}

	public void setMain(ClientBabyfoot main) {
		this.main = main;
	}
}

class GameReceptionMessage implements Runnable{
	
	BufferedReader in;
	String message;
	GameClient gc;
	
	public GameReceptionMessage(BufferedReader in, GameClient gc ){
		this.in = in;
		this.gc = gc;
		message = "";
	}
	
	public void run() {
		int mode = 0;
		int n = 0;
		System.out.println("Prêt à la réception pour les jeux !");
		try {
			while(true){
				gc.getPositions( gc.getMain().getPlayer().getLogin(), true );
            	message = in.readLine();
            	if( message != null ){
	            	String[] datas = message.split(Utils.SEPARATOR);
	            	if( datas.length < 9 ){
	            		datas = null;
	            		if( message != null ){
	            			datas = message.split(Utils.SEPARATOR);
		            		if( datas[0].equals("matchscores") ){
		                		gc.getMain().getClient().getMc().setMatchInfos(message);
		                	}
	            		}
	            	}else if( datas != null && datas[0].equals("positions") ){
	            		gc.setRodPositionsAndStatus(new String[16]);
	            		for( int i = 0; i<8; i++ ){
	            			gc.rodPositionsAndStatus[i] = datas[i+1];
	            		}
            			gc.setBallX(Integer.valueOf(datas[9]));
            			gc.setBallY(Integer.valueOf(datas[10]));
            			for( int i = 0; i<8; i++ ){
	            			gc.rodPositionsAndStatus[8+i] = datas[11+i];
	            		}
					}
	            	message = null;
	            	datas = null;
            	}
			}
		} catch (IOException e) {
            e.printStackTrace();
		}
	}
}
