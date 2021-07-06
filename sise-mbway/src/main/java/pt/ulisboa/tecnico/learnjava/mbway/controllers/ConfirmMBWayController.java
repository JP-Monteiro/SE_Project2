package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoClientsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoPhoneNumberException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.WrongCodeException;

public class ConfirmMBWayController {
	
	private String phone_number, code;
	private MBWay MBWay;
	private boolean phoneVerified, clientVerified;

	public ConfirmMBWayController(String phone_number, String code, MBWay MBWay) throws AccountException {
		//Initialize parameters
		this.phone_number = phone_number;
		this.code = code;
		this.MBWay = MBWay;
		this.clientVerified = false;
		this.phoneVerified = false;
	}
	
	public void process() throws NoClientsException, ClientException, NoPhoneNumberException, WrongCodeException {
		
		//Verify if there is MBWayClients
		if (MBWay.getMBWayClients().isEmpty())
			throw new NoClientsException();
		//Verification of phone number and code
		mbwayVerification();

	}
	
	public void mbwayVerification() throws NoPhoneNumberException, WrongCodeException {
		
		//VerifyIfPhoneNumberExists
		if (MBWay.getMBWayClients().containsKey(this.phone_number)) {
			//VerifyIfCodeIsCorrect
			if (MBWay.getMBWayClients().get(this.phone_number).getCode().equals(this.code)) {
				//SetStateToVerified
				MBWay.getMBWayClients().get(this.phone_number).setState(true);
				this.clientVerified = true;
			}
			this.phoneVerified = true;
		}
		
		//Verification for phone_number
		if (this.phoneVerified == false)
			throw new NoPhoneNumberException();	
		//Verification for code
		else if (this.clientVerified == false)
			throw new WrongCodeException();
	}
	
}
