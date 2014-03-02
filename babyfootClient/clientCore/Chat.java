package clientCore;

/** Gère de manière propre les actions possibles sur le système de chat : recevoir la liste des serveurs, envoyer
un message, changer de serveur et récupérer les messages d'un serveur. */
public class Chat {
	private String server = "Global";
	private ClientBabyfoot main;
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Chat(ClientBabyfoot m){
		main = m;
	}
	
	/** Change de serveur. Récupère la liste des serveurs puis les informations qui l'intéressent sur celui sélectionné par le joueur. 
	@param id est le numéro du serveur dans la liste générée par la requête. */
	public void setServer(int id){
		String[] servers = Utils.formatStringArray(main.getClient().getCc().getServers());
		server = servers[id];
	}
	
	/** Envoie un message au serveur à partir du texte du message */
	public void sendMessage(String text){
		main.getClient().getCc().sendMessage(text);
	}
	
	/** Obtient la liste des serveurs */
	public String[] getServers(){
		return main.getClient().getCc().getServers();
	}
	
	/** Récupère les messages d'un serveur */
	public String[] getMessages(){
		return main.getClient().getCc().getMessages(server);
	}
	
	public void logIn(){
		
	}

	public void setServerByItsName(String serverName) {
		String[] servers = Utils.formatStringArray(main.getClient().getCc().getServers());
		int n = servers.length;
		for( int i = 0; i < n; i++ ){
			if( servers[i].equals(serverName))
				server = servers[i];
		}
	}
}
