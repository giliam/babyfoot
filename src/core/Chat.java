package core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import network.ChatClient;

public class Chat {
	private Thread tChat = null;
	private Socket socket = null;
	private Database db;
	private ChatClient cc;
	
	public Chat(){
	}
	
	public void setChat(int id){
		
	}

	public void sendMessage(String text){
		System.out.println("Requête pour envoi de message...");
		cc.sendMessage(text);
	}
	
	public String[] getServers(){
		return cc.getServers();
	}
	
	public void logIn(){
		
	}
}
