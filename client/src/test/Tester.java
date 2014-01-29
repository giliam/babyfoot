package test;

import core.Main;

public class Tester {
	public static void main(String[] args){
		Main m1 = new Main();
		Thread t1 = new Thread(new TesterThread(m1));
		t1.start();
		
		Main m2 = new Main();
		Thread t2 = new Thread(new TesterThread(m2));
		t2.start();
	}
}

class TesterThread implements Runnable {
	private Main main;
	public TesterThread(Main m){
		main = m;
	}
	public void run(){
		main.init();
	}
}
