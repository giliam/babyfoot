package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	Connection link;
	public void connect(){
		try {
	      Class.forName("org.postgresql.Driver");
	      System.out.println("Driver O.K.");

	      link = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Babyfoot", "babyfoot", "robert42");
	      System.out.println("Connexion effective !");         
	         
	    } catch (Exception e) {
	      e.printStackTrace();
	    }      
	}
	
	public String[] getServers(){
		String[] s = null;
		try {
			Statement state = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet result = state.executeQuery("SELECT * FROM salons");
			ResultSetMetaData resultMeta = result.getMetaData();
			result.last();
			//On crée le tableau
			s = new String[result.getRow()];
			result.beforeFirst();
			int j = 0;
			//On récupère l'ensemble des noms de serveurs que l'on stocke dans un tableau.
			while(result.next())
				s[j++] = result.getString("salon_id") + " - " + result.getString("nom");
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public void addServer(String nom){
		try {
			Statement state = link.createStatement();
			ResultSet result = state.executeQuery("INSERT INTO salons(nom) VALUES('" + nom + "')");
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		Database d = new Database();
		d.connect();
		String[] s = d.getServers();
		for(int i = 0; i < 1; i++ ){
			System.out.println(s[i]);
		}
	}
}
