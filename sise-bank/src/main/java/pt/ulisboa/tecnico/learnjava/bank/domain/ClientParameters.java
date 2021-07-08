package pt.ulisboa.tecnico.learnjava.bank.domain;

public class ClientParameters {
	private Bank bank;
	private String nif, phoneNumber;
	private int age;

	public ClientParameters(Bank bank, String nif, String phoneNumber, int age) {
		this.bank = bank;
		this.nif = nif;
		this.phoneNumber = phoneNumber;
		this.age = age;
	}

	public Bank getBank() {
		return this.bank;
	}

	public String getNif() {
		return this.nif;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public int getAge() {
		return this.age;
	}
}
