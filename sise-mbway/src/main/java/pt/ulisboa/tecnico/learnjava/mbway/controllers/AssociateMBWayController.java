package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayClient;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AccountExistsException;

public class AssociateMBWayController {
	
	private String iban, phone_number;
	private int code;
	private Services svc = new Services();
	private MBWay MBWay;
	
	//Constructor
	public AssociateMBWayController(String iban, String phone_number, MBWay MBWay){
		//Initialize parameters
		this.iban = iban;
		this.phone_number = phone_number;
		this.MBWay = MBWay;
	}
	
	//Teste
	//Process
	public void process() throws AccountException, AccountExistsException {
		//Verify if bank_account exists
		if (this.svc.getAccountByIban(this.iban) == null || this.phone_number.length() != 9)
			throw new AccountException();
		//Verify if mbway association exists
		if (MBWay.getMBWayClients().containsKey(this.phone_number))
			throw new AccountExistsException();
		//Generate confirmation code
		this.code = 100000 + new Random().nextInt(899999);
		//AddC client to MBWayClients
		this.MBWay.addClient(new MBWayClient(getPhoneNumber(), 
											 getIban(), 
											 String.valueOf(getCode())));
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getIban() {
		return this.iban;
	}
	
	public String getPhoneNumber() {
		return this.phone_number;
	}
}
