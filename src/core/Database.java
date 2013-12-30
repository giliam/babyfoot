package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	Connection conn;
	public void connect(){
		try {
	      Class.forName("org.postgresql.Driver");
	      System.out.println("Driver O.K.");

	      String url = "jdbc:postgresql://localhost:5432/Babyfoot";
	      String user = "babyfoot";
	      String passwd = "robert42";

	      conn = DriverManager.getConnection(url, user, passwd);
	      System.out.println("Connexion effective !");         
	         
	    } catch (Exception e) {
	      e.printStackTrace();
	    }      
	}
	
	public String getServers(){
		String s = "";
		try {
			//Création d'un objet Statement
			Statement state = conn.createStatement();
			//L'objet ResultSet contient le résultat de la requête SQL
			ResultSet result = state.executeQuery("SELECT * FROM salons");
			//On récupère les MetaData
			ResultSetMetaData resultMeta = result.getMetaData();
			 
			s += "\n**********************************";
			//On affiche le nom des colonnes
			for(int i = 1; i <= resultMeta.getColumnCount(); i++)
				s += "\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *";
			 
			s += "\n**********************************";
			 
			while(result.next()){         
				for(int i = 1; i <= resultMeta.getColumnCount(); i++)
					s += "\t" + result.getObject(i).toString() + "\t |";
				s += "\n---------------------------------";
			}

			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public void addServer(String nom){
		try {
			//Création d'un objet Statement
			Statement state = conn.createStatement();
			//L'objet ResultSet contient le résultat de la requête SQL
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
		d.addServer("Global");
		System.out.println(d.getServers());
	}
}
