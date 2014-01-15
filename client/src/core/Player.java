package core;

import gui.GameZone;
import gui.GameZone.RodPositions;

import java.util.Hashtable;

public class Player {
	private String login;
	private Hashtable<GameZone.RodPositions, Boolean> rodAvailables;
	private Utils.Sides side;
	private boolean boss;
	
	public Player(String l){
		rodAvailables = new Hashtable<GameZone.RodPositions, Boolean>();
		rodAvailables.put(RodPositions.GARDIEN, true);
		rodAvailables.put(RodPositions.DEFENSE, true);
		rodAvailables.put(RodPositions.MILIEU, true);
		rodAvailables.put(RodPositions.ATTAQUE, true);
		setLogin(l);
	}
	
	public Player(){
		this("");
	}
	
	public boolean addPlayer(String login){
		this.setLogin(login);
		return Main.getClient().getPc().addPlayer();
	}

	public void removePlayer(String login) {
		if(Main.getClient().getPc().removePlayer(login))
			this.setLogin("");
	}
	
	public boolean addMatch(int type){
		return Main.getClient().getPc().addMatch(type);
	}
	
	public void setRod(Hashtable<GameZone.RodPositions, Integer> rodPositionsHash){
		int[] rodPositions = { rodPositionsHash.get(GameZone.RodPositions.GARDIEN), rodPositionsHash.get(GameZone.RodPositions.DEFENSE),rodPositionsHash.get(GameZone.RodPositions.MILIEU),rodPositionsHash.get(GameZone.RodPositions.ATTAQUE) };
		Main.getClient().getMc().setRodPositions(login, rodPositions);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Hashtable<GameZone.RodPositions, Boolean> getRodAvailables() {
		return rodAvailables;
	}

	public void setRodAvailables(Hashtable<GameZone.RodPositions, Boolean> rodAvailables) {
		this.rodAvailables = rodAvailables;
	}

	public String[] getServers() {
		return Main.getClient().getMc().getServers();
	}

	public boolean setServer(int selectedGame, String[] selectedStrings) {
		String[] datas = selectedStrings[selectedGame].split("-");
		String loginHost = datas[1].trim();
		return Main.getClient().getMc().setServerFromHost(login,loginHost);
	}

	public Utils.Sides getSide() {
		return side;
	}

	public void runMatch() {
		Main.getClient().getMc().runMatch(login);
	}

	public void setBoss(boolean b) {
		boss = b;
	}

	public boolean isBoss() {
		return boss;
	}
}
