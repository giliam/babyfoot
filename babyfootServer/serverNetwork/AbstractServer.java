package serverNetwork;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class AbstractServer {
	protected String query;
	
	public void setQuery(String d){
		query = d; 
	}
	
	public void handle(BufferedReader in, PrintWriter out){
	}
}