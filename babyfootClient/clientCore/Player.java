package clientCore;


import java.util.Hashtable;

import clientCore.Utils.Sides;
import clientGui.GameZone;
import clientGui.GameZone.RodPositions;



/** Cette classe est la version locale du joueur. Elle contient les informations principales sur celui-ci, à savoir : 
son login, à quelle barre il a le droit de toucher lors de la partie en cours, de quel côté du terrain il se situe et enfin
si il est le maitre de jeu (celui qui a créé la partie).  Elle contient aussi les méthodes gérant l'envoi au serveur des 
principales requêtes. */
public class Player {
	private String login;
	private Hashtable<GameZone.RodPositions, Boolean> rodAvailables;
	private Utils.Sides side = Utils.Sides.BOTTOM;
	private boolean boss;
	private ClientBabyfoot main;
	public Player(String l, ClientBabyfoot m){
		main = m;
		
		rodAvailables = new Hashtable<GameZone.RodPositions, Boolean>();
		//A priori on donne tous les accès, ce qui pourra être modifié par la suite.
		rodAvailables.put(RodPositions.GARDIEN, true);
		rodAvailables.put(RodPositions.DEFENSE, true);
		rodAvailables.put(RodPositions.MILIEU, true);
		rodAvailables.put(RodPositions.ATTAQUE, true);
		setLogin(l);
	}
	
	public Player(){
		this("", null);
	}
	
	public Player(ClientBabyfoot m) {
		this("", m);
	}

	/** Connecte le joueur. */
	public boolean addPlayer(String login){
		this.login = login;
		return main.getClient().getPc().addPlayer();
	}

	/** Déconnecte le joueur. */
	public void removePlayer(String login) {
		if(main.getClient().getPc().removePlayer(login))
			this.setLogin("");
	}
	
	/** Ajoute un match au serveur. */
	public boolean addMatch(int type){
		return main.getClient().getPc().addMatch(type);
	}
	
	/** Envoie les modifications de positions des barres au serveur pour le joueur actuel. */
	public void setRod(Hashtable<GameZone.RodPositions, Integer> rodPositionsHash){
		int[] rodPositions = { rodPositionsHash.get(GameZone.RodPositions.GARDIEN), rodPositionsHash.get(GameZone.RodPositions.DEFENSE),rodPositionsHash.get(GameZone.RodPositions.MILIEU),rodPositionsHash.get(GameZone.RodPositions.ATTAQUE) };
		main.getClient().getMc().setRodPositions(login, rodPositions);
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
	
	
	/** Retourne la liste des parties actuellement disponibles ou en cours en lançant une requête. */
	public String[] getServers() {
		return main.getClient().getMc().getServers();
	}
	
	/** Crée une nouvelle partie à partir de la liste des parties actuelles et l'index de celle choisie. 
	@param selectedGame donne l'index de la partie sélectionnée dans la liste des parties disponibles
	@param selectedStrings est la liste des parties accessibles */
	public boolean setServer(int selectedGame, String[] selectedStrings) {
		String[] datas = selectedStrings[selectedGame].split("-");
		//Récupère le nom du maitre de jeu : celui affiché dans la liste des parties disponibles
		String loginHost = datas[1].trim();
		//Si on change la partie, c'est-à-dire qu'on ne la crée pas donc qu'on n'est pas maitre de jeu
		boss = false;
		return main.getClient().getMc().setServerFromHost(login,loginHost);
	}

	public Utils.Sides getSide() {
		return side;
	}
	
	/** Lance le match du joueur, c'est-à-dire met à jour le statut du match au serveur. */
	public void runMatch() {
		main.getClient().getMc().runMatch(login);
	}

	public void setBoss(boolean b) {
		boss = b;
	}

	public boolean isBoss() {
		return boss;
	}

	public void setSide(Sides s) {
		side = s;
	}

	public void stopMatch() {
		main.getClient().getMc().stopMatch(login);
	}
}
