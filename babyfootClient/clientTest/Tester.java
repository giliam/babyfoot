package clientTest;

import clientCore.*;
import clientGui.*;
import clientNetwork.*;

public class Tester {
	public static void main(String[] args){
		ClientBabyfoot m1 = new ClientBabyfoot();
		Thread t1 = new Thread(new TesterThread(m1));
		t1.start();
		
		ClientBabyfoot m2 = new ClientBabyfoot();
		Thread t2 = new Thread(new TesterThread(m2));
		t2.start();
	}
}

class TesterThread implements Runnable {
	private ClientBabyfoot main;
	public TesterThread(ClientBabyfoot m){
		main = m;
	}
	public void run(){
		main.init();
		((ConnexionPanel) main.actualPanel).logIn("aogjaeignoaeg");
	}
}
