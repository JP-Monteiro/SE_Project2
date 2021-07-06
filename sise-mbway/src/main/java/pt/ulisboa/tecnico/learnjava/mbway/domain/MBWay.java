package pt.ulisboa.tecnico.learnjava.mbway.domain;

import java.util.HashMap;

public class MBWay {
	
	private int nrClient;
	private HashMap<String, MBWayClient> MBWayClients;
	

	public MBWay() {
		this.MBWayClients = new HashMap<String, MBWayClient>();
		this.nrClient = 0;
	}

	public void addClient(MBWayClient client) {
		this.MBWayClients.put(client.getPhoneNumber(), client);
		this.nrClient++;
	}
	
	public HashMap<String, MBWayClient> getMBWayClients(){
		return this.MBWayClients;
	}
	
	public int getNrClient() {
		return this.nrClient;
	}
}
