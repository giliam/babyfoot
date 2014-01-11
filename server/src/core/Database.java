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
				
				Statement state = link.createStatement();
			    PreparedStatement prepare = link.prepareStatement("INSERT INTO chat(salon_id, message, date, joueur) VALUES(?, ?, ?, ? )");
			    prepare.setInt(1,salon_id);
			    prepare.setString(2,message);
			    prepare.setInt(3,(int) System.currentTimeMillis());
			    prepare.setString(4,login);
			    prepare.executeUpdate();
			    prepare.close();
				state.close();
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
			System.out.println(findPlayer(login, 1));
			if( findPlayer(login, 1) <= 0 ){
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
			int joueur_id = findPlayer(login, 1);
			if( joueur_id >= 0 ){
				Statement state = link.createStatement();
				state.executeUpdate("UPDATE joueurs SET online = 0 WHERE online = 1 AND login = '" + login + "'");
				state.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
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
				ResultSet resultGetMessages = stateGetMessages.executeQuery("SELECT message, date, joueur AS login FROM chat WHERE salon_id = " + salon_id );
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

	public boolean addMatch(int type, String login) {
		try {
			int joueur_id = findPlayer(login, 1);
			if( joueur_id >= 0 ){
				Statement state = link.createStatement();
				state.executeUpdate("INSERT INTO parties(joueur1, type) VALUES(" + joueur_id + ", " + type + ")");
				state.close();
				return true;
			}else{
				System.out.println("Pas de joueur trouvé !");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("DOMMAGE BUG");
		return false;
	}

	
	public int findPlayer(String login, int online) throws SQLException {
		Statement stateVerifPlayerExist = link.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet resultVerifPlayerExist = stateVerifPlayerExist.executeQuery("SELECT joueur_id FROM joueurs WHERE online = " + online + " AND login = '" + login + "'" );
		resultVerifPlayerExist.last();
		if( resultVerifPlayerExist.getRow() != 1 ){
			return -1;
		}else{
			resultVerifPlayerExist.first();
			return resultVerifPlayerExist.getInt("joueur_id");
		}
	}
	
	

	public void setRod(String login, int[] positions) {
		try {
			int joueur_id = findPlayer(login, 1);
			if( joueur_id >= 0 ){
				Statement state = link.createStatement();
				state.executeUpdate("UPDATE barreposition SET gardien = " + positions[0] + ", defense = " + positions[1] + ", milieu = " + positions[2] + ", attaque = " + positions[3] + " WHERE player_id = " + joueur_id );
				state.close();
			}else{
				System.out.println("Pas de joueur trouvé !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int[][] getRodPositions(String login) {
		try {
			int joueur_id = findPlayer(login, 1);
			if( joueur_id >= 0 ){
				Statement state = link.createStatement();
				ResultSet result = state.executeQuery("SELECT partie_id, joueur1, joueur2, joueur3, joueur4 FROM parties WHERE etat < " + Utils.MATCH_END + " AND ( joueur1 = " + joueur_id + " OR joueur2 = " + joueur_id + " OR joueur3 = " + joueur_id + " OR joueur4 = " + joueur_id + ")" );
				result.first();
				int[] playerPos = {result.getInt("joueur1") == joueur_id ? 1 : ( result.getInt("joueur2") == joueur_id ? 2 : ( result.getInt("joueur3") == joueur_id ? 3 : 4 ) )};
				int[][] returnT = {playerPos,getRodPositionsFromOnePlayer(result.getInt("joueur1")),getRodPositionsFromOnePlayer(result.getInt("joueur2")),getRodPositionsFromOnePlayer(result.getInt("joueur3")),getRodPositionsFromOnePlayer(result.getInt("joueur4"))};
				state.close();
				result.close();
				return returnT;
			}else{
				System.out.println("Pas de joueur trouvé !");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getRodPositionsFromOnePlayer(int joueur_id) {
		try {
			Statement state = link.createStatement();
			ResultSet result = state.executeQuery("SELECT * FROM barreposition WHERE player_id = " + joueur_id );
			result.first();
			int[] d = { result.getInt("gardien"), result.getInt("defense"), result.getInt("milieu"), result.getInt("attaque") };
			state.close();
			result.close();
			return d;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	public static void main(String[] args){
		Database d = new Database();
		d.connect();
		String[] s = d.getServers();
		for(int i = 0; i < 2; i++ ){
			System.out.println(s[i]);
		}
	}*/
}
