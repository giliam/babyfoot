package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class AbstractServer {
	protected String[] datas;
	
	public void setDatas(String[] d){
		datas = d; 
	}
	
	public void handle(BufferedReader in, PrintWriter out){
	}
}