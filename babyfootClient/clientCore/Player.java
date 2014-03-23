package clientCore;


import java.util.Hashtable;

import clientCore.Utils.RodStatus;
import clientCore.Utils.Sides;
import clientCore.Utils.Types;
import clientGui.GameZone;
import clientCore.Utils.Rod;



/** Cette classe est la version locale du joueur. Elle contient les informations principales sur celui-ci, à savoir : 
son login, à quelle barre il a le droit de toucher lors de la partie en cours, de quel côté du terrain il se situe et enfin
si il est le maitre de jeu (celui qui a créé la partie).  Elle contient aussi les méthodes gérant l'envoi au serveur des 
principales requêtes. */
public class Player {
	private String login;
	private Hashtable<Rod, Boolean> rodAvailables;
	private Utils.Sides side = Utils.Sides.DOWN;
	private boolean boss;
	private ClientBabyfoot main;
	private Types gameType;
	private int status;
	public Player(String l, ClientBabyfoot m){
		main = m;
		status = 1;
		rodAvailables = new Hashtable<Rod, Boolean>();
		//A priori on donne tous les accès, ce qui pourra être modifié par la suite.
		rodAvailables.put(Rod.GARDIEN, true);
		rodAvailables.put(Rod.DEFENSE, true);
		rodAvailables.put(Rod.MILIEU, true);
		rodAvailables.put(Rod.ATTAQUE, true);
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
	
	public boolean askForPause(boolean currentState){
		return main.getClient().getMc().askForPause(login, currentState);
	}

	/** Déconnecte le joueur. */
	public void removePlayer(String login) {
		if(main.getClient().getPc().removePlayer(login))
			this.setLogin("");
	}
	
	/** Ajoute un match au serveur. */
	public boolean addMatch(int type){
		gameType = ( type == 1 ? Types.ONEVSONE : ( type == 2 ? Types.TWOVSTWO : Types.ONEVSTWO ));
		return main.getClient().getPc().addMatch(type);
	}
	
	/** Envoie les modifications de positions des barres au serveur pour le joueur actuel. */
	public void setRod(Hashtable<Rod, Integer> rodPositionsHash, Hashtable<Rod, RodStatus> rodStatusHash){
		int[] rodPositions = { rodPositionsHash.get(Rod.GARDIEN), rodPositionsHash.get(Rod.DEFENSE),rodPositionsHash.get(Rod.MILIEU),rodPositionsHash.get(Rod.ATTAQUE) };
		RodStatus[] rodStatus = { rodStatusHash.get(Rod.GARDIEN), rodStatusHash.get(Rod.DEFENSE),rodStatusHash.get(Rod.MILIEU),rodStatusHash.get(Rod.ATTAQUE) };
		main.getClient().getMc().setRodPositions(login, rodPositions, rodStatus );
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public Hashtable<Rod, Boolean> getRodAvailables() {
		return rodAvailables;
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
		status = 1;
	}

	public void sendShoot(long duration, Rod rodPosition, Sides side) {
		main.getClient().getMc().sendShoot(login, duration, rodPosition, side);
	}

	public void quitMatch() {
		main.getClient().getMc().quitMatch(login);
	}

	public void setRodAvailablesByMatchType(String gameType, int status) {
		this.status = status;
		if( gameType.equals("1vs1") ){
			this.gameType = Types.ONEVSONE;
		}else if( gameType.equals("1vs2") ){
			this.gameType = Types.ONEVSTWO;
		}else if( gameType.equals("2vs2") ){
			this.gameType = Types.TWOVSTWO;
		}
	}
	
	
	public void initRodAvailables() {
		rodAvailables = new Hashtable<Rod, Boolean>();
		switch( gameType ){
			case ONEVSONE:
				rodAvailables.put(Rod.GARDIEN, true);
				rodAvailables.put(Rod.DEFENSE, true);
				rodAvailables.put(Rod.MILIEU, true);
				rodAvailables.put(Rod.ATTAQUE, true);
				break;
			case ONEVSTWO:
				if( side == Sides.DOWN ){
					rodAvailables.put(Rod.GARDIEN, true);
					rodAvailables.put(Rod.DEFENSE, true);
					rodAvailables.put(Rod.MILIEU, true);
					rodAvailables.put(Rod.ATTAQUE, true);
				}else if( (status & 4) == 0 ){
					rodAvailables.put(Rod.GARDIEN, true);
					rodAvailables.put(Rod.DEFENSE, true);
					rodAvailables.put(Rod.MILIEU, false);
					rodAvailables.put(Rod.ATTAQUE, false);
				}else{
					rodAvailables.put(Rod.GARDIEN, false);
					rodAvailables.put(Rod.DEFENSE, false);
					rodAvailables.put(Rod.MILIEU, true);
					rodAvailables.put(Rod.ATTAQUE, true);
				}
				break;
			case TWOVSTWO:
				if( ( (status & 4) == 0 && side == Sides.UP ) || ( (status & 1) == 0 && side == Sides.DOWN ) ){
					rodAvailables.put(Rod.GARDIEN, true);
					rodAvailables.put(Rod.DEFENSE, true);
					rodAvailables.put(Rod.MILIEU, false);
					rodAvailables.put(Rod.ATTAQUE, false);
				}else{
					rodAvailables.put(Rod.GARDIEN, false);
					rodAvailables.put(Rod.DEFENSE, false);
					rodAvailables.put(Rod.MILIEU, true);
					rodAvailables.put(Rod.ATTAQUE, true);
				}
				break;
		}
	}
}
