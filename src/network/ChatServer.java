package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import core.Utils;

public class ChatServer implements Runnable {
	
	PrintWriter out;
	BufferedReader in;
	
	public ChatServer(PrintWriter out, BufferedReader in){
		this.out = out;
		this.in = in;
	}
	
	public void run() {
		while(true){
            try {
            	String[] datas = in.readLine().split("-", 5);
            	String date = datas[2];
            	String message = datas[4];
            	String login = datas[1];
            	String serveur = datas[0];
            	String hashTag = datas[3];
            	if( hashTag == Utils.hash(serveur + "salt" + message + login + "42$1a" + hashTag ) ){
            		System.out.println("Serveur " + serveur + " - " + date + " - " + login+" : "+message);
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}
