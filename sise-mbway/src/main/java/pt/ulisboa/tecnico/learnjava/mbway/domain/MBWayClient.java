package pt.ulisboa.tecnico.learnjava.mbway.domain;

public class MBWayClient {

	private String iban, phone_number, code;
	private boolean verified;
	
	
	public MBWayClient(String phone_number, String iban, String code) {
		this.phone_number = phone_number;
		this.iban = iban;
		this.code = code;
		this.verified = false;
	}
	
	public String getPhoneNumber(){
		return this.phone_number;
	}
	
	public String getIban(){
		return this.iban;
	}
	
	public String getCode(){
		return this.code;
	}

	public boolean getState() {
		return verified;
	}
	
	public void setState(boolean state) {
		this.verified = state;
	}
	
}
