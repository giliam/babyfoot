package clientTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

import serverNetwork.ServerBabyfoot;
import clientCore.*;
import clientGui.*;
import clientNetwork.*;

public class Tester {
	public static void main(String[] args) throws IOException{
		ServerBabyfoot s = new ServerBabyfoot(new ServerSocket(2010));
		Thread serveur = new Thread(s);
		serveur.start();
		
		ClientBabyfoot m1 = new ClientBabyfoot();
		Thread t1 = new Thread(new TesterThread(m1));
		t1.start();
	}
}

class TesterThread implements Runnable {
	private ClientBabyfoot main;
	public TesterThread(ClientBabyfoot m){
		main = m;
	}
	public void run(){
		main.init();
		String loginHost = "bob";
		((ConnexionPanel) main.actualPanel).logIn( loginHost );
		((MenuPanel) main.actualPanel).getWindow().setContentPane(new NewPanel(((MenuPanel) main.actualPanel).getWindow()));
		((NewPanel) main.actualPanel).getWindow().setVisible(true);
		((NewPanel) main.actualPanel).getWindow().getMain().getPlayer().addMatch(1);
		((NewPanel) main.actualPanel).getWindow().getMain().getPlayer().setBoss(true);
		((NewPanel) main.actualPanel).getWindow().setContentPane(new WaitingRoomPanel(((NewPanel) main.actualPanel).getWindow()));
		((WaitingRoomPanel) main.actualPanel).getWindow().setVisible(true);
		PrintWriter out = ((WaitingRoomPanel) main.actualPanel).getWindow().getMain().getClient().getMc().getOut();
		String login = "coucou";
		out.println("player" + Utils.SEPARATOR + "add" + Utils.SEPARATOR + login);
		out.flush();
		out.println("player" + Utils.SEPARATOR + "joinmatch" + Utils.SEPARATOR + login + Utils.SEPARATOR + loginHost );
		out.flush();
		out.println("match" + Utils.SEPARATOR + "run" + Utils.SEPARATOR + loginHost );
    	out.flush();
		((WaitingRoomPanel) main.actualPanel).getWindow().getMain().getClient().getGc().startThread();
		((WaitingRoomPanel) main.actualPanel).getWindow().setContentPane(new GamePanel(((WaitingRoomPanel) main.actualPanel).getWindow(),false));
		((WaitingRoomPanel) main.actualPanel).getWindow().setVisible(true);
	}
}
