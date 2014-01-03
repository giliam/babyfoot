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
	
	public void setServer(int id){
		String[] servers = Utils.formatStringArray(Main.getClient().getCc().getServers());
		server = servers[id];
	}

	public void sendMessage(String text){
		Main.getClient().getCc().sendMessage(text);
	}
	
	public String[] getServers(){
		return Main.getClient().getCc().getServers();
	}
	
	public String[] getMessages(){
		return Main.getClient().getCc().getMessages(server);
	}
	
	public void logIn(){
		
	}
}
