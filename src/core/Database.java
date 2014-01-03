package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

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
			state.executeUpdate("INSERT INTO salons(nom) VALUES('" + nom + "')");
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage(String serveur, String login, String message) {
		try {
			//On vérifie que le salon existe
			Statement stateVerifRoomExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet resultVerifRoomExist = stateVerifRoomExist.executeQuery("SELECT salon_id FROM salons WHERE nom = '" + serveur + "'" );
			resultVerifRoomExist.last();
			if( resultVerifRoomExist.getRow() >= 1 ){
				//On obtient donc l'ID du salon
				resultVerifRoomExist.first();
				int salon_id = resultVerifRoomExist.getInt("salon_id");
				
				Statement stateVerifPlayerExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet resultVerifPlayerExist = stateVerifPlayerExist.executeQuery("SELECT joueur_id FROM joueurs WHERE online = 1 AND login = '" + login + "'" );
				resultVerifPlayerExist.last();
				if( resultVerifPlayerExist.getRow() == 1 ){
					//On obtient donc l'ID du joueur
					resultVerifRoomExist.first();
					int joueur_id = resultVerifPlayerExist.getInt("joueur_id");
					Statement state = link.createStatement();
				    PreparedStatement prepare = link.prepareStatement("INSERT INTO chat(salon_id, message, date, joueur_id) VALUES(?, ?, ?, ? )");
				    prepare.setInt(1,salon_id);
				    prepare.setString(2,message);
				    prepare.setInt(3,(int) System.currentTimeMillis());
				    prepare.setInt(4,joueur_id);
				    prepare.executeUpdate();
				    prepare.close();
					state.close();
				}else{
					System.out.println("Ce joueur n'existe pas ! " + resultVerifPlayerExist.getRow());
				}
			}else{
				System.out.println("Ce salon n'existe pas ! " + resultVerifRoomExist.getRow());
			}
			resultVerifRoomExist.close();
			stateVerifRoomExist.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean addPlayer(String login){
		try {
			Statement stateVerifPlayerExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet resultVerifPlayerExist = stateVerifPlayerExist.executeQuery("SELECT joueur_id FROM joueurs WHERE online = 1 AND login = '" + login + "'" );
			resultVerifPlayerExist.last();
			if( resultVerifPlayerExist.getRow() >= 1 ){
				return false;
			}else{
				Statement state = link.createStatement();
				state.executeUpdate("INSERT INTO joueurs(login, online) VALUES('" + login + "', 1)");
				state.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public void removePlayer(String login) {
		try {
			Statement stateVerifPlayerExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet resultVerifPlayerExist = stateVerifPlayerExist.executeQuery("SELECT joueur_id FROM joueurs WHERE online = 1 AND login = '" + login + "'" );
			resultVerifPlayerExist.last();
			if( resultVerifPlayerExist.getRow() == 1 ){
				Statement state = link.createStatement();
				state.executeUpdate("UPDATE joueurs SET online = 0 WHERE online = 1 AND login = '" + login + "'");
				state.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args){
		Database d = new Database();
		d.connect();
		d.addMessage("Global","giliam", "salut les poteaux ! ''''");
		String[] s = d.getServers();
		for(int i = 0; i < 2; i++ ){
			System.out.println(s[i]);
		}
	}

	public String[] getMessages(String serveur) {
		try {
			//On vérifie que le salon existe
			Statement stateVerifRoomExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet resultVerifRoomExist = stateVerifRoomExist.executeQuery("SELECT salon_id FROM salons WHERE nom = '" + serveur + "'" );
			resultVerifRoomExist.last();
			if( resultVerifRoomExist.getRow() >= 1 ){
				//On récupère l'ID
				resultVerifRoomExist.first();
				int salon_id = resultVerifRoomExist.getInt("salon_id");
				
				//On crée la requête
				Statement stateGetMessages = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet resultGetMessages = stateGetMessages.executeQuery("SELECT message, date, login FROM chat as c LEFT JOIN joueurs ON joueurs.joueur_id = c.joueur_id WHERE salon_id = " + salon_id );
				resultGetMessages.last();
				
				//On crée le tableau
				String[] s = new String[resultGetMessages.getRow()];
				resultGetMessages.beforeFirst();
				int j = 0;
				
				//On récupère l'ensemble des messages que l'on stocke dans un tableau.
				while(resultGetMessages.next()){
					s[j++] = Utils.formatDate(resultGetMessages.getString("date")) + " - " + resultGetMessages.getString("login") + " : " + resultGetMessages.getString("message");
				}
				
				resultGetMessages.close();
				stateGetMessages.close();
				return s;
			}else{
				System.out.println("Ce salon n'existe pas ! " + resultVerifRoomExist.getRow());
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	

	
}
