package clientTest;

import java.io.IOException;
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
		Thread t1 = new Thread(new TesterThread(m1,true));
		t1.start();
		
		ClientBabyfoot m2 = new ClientBabyfoot();
		Thread t2 = new Thread(new TesterThread(m2,false));
		t2.start();
	}
}

class TesterThread implements Runnable {
	private ClientBabyfoot main;
	private boolean isHoster;
	public TesterThread(ClientBabyfoot m, boolean b){
		main = m;
		isHoster = b;
	}
	public void run(){
		main.init();
		((ConnexionPanel) main.actualPanel).logIn( String.valueOf( (int)(Math.random()*10000000) ) );
		if( isHoster ){
			((MenuPanel) main.actualPanel).getWindow().setContentPane(new NewPanel(((MenuPanel) main.actualPanel).getWindow()));
			((NewPanel) main.actualPanel).getWindow().setVisible(true);
		}else{
			((MenuPanel) main.actualPanel).getWindow().setContentPane(new ServersPanel(((MenuPanel) main.actualPanel).getWindow()));
			((MenuPanel) main.actualPanel).getWindow().setVisible(true);
		}
	}
}
