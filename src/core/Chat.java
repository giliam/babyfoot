package core;

public class Chat {
	private String server = "Global";
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Chat(){
		
	}
	
	public void setChat(int id){
		
	}

	public void sendMessage(String text){
		System.out.println("Requête pour envoi de message...");
		Main.getClient().getCc().sendMessage(text);
	}
	
	public String[] getServers(){
		return Main.getClient().getCc().getServers();
	}
	
	public void logIn(){
		
	}
}
