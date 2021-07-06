package pt.ulisboa.tecnico.learnjava.mbway.domain;

public class Friend{
	
	private MBWayClient client;
	private int amount;

	public Friend(MBWayClient client, int amount) {
		this.client = client;
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public MBWayClient getClient() {
		return this.client;
	}

}
