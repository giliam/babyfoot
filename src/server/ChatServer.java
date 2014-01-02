package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


import core.Utils;

public class ChatServer extends AbstractServer {
	public void handle(BufferedReader in, PrintWriter out){
		String message = datas[4];
    	String login = datas[2];
    	String serveur = datas[1];
    	String hashTag = datas[3];
    	if( hashTag.equals(Utils.hash(serveur + "salt" + message + login + "42$1a" ) ) ){
    		Server.db.addMessage(serveur, login, message );
    		System.out.println("ENVOI REUSSI");
    	}else{
    		System.out.println("Requête non valide par le type !");
    	}
	}
}