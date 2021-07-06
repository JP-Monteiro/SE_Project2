package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayClient;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoSourcePhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoTargetPhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SourceStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TargetStateException;



public class TransferMBWayController {

	private String src_phone, trg_phone;
	private int amount, maxNr = 100;
	private MBWayClient src_client, trg_client;
	private MBWay MBWay;
	private Services svc = new Services();
	private Sibs sibs = new Sibs(maxNr, svc);
	
	public TransferMBWayController(String src_phone, String trg_phone, String amount, MBWay MBWay) throws AccountException {
		//InitializeParameters
		this.src_phone = src_phone;
		this.trg_phone = trg_phone;
		this.amount = Integer.valueOf(amount);
		this.MBWay = MBWay;
		this.src_client = null;
		this.trg_client = null;
	}
	
	//GET_CLIENTS
	public void getClients() throws SourceStateException, TargetStateException, 
									NoSourcePhoneException, NoTargetPhoneException {
		
		//GetSourceClient
		//VerifyIfPhoneNumberExists
		if (MBWay.getMBWayClients().containsKey(this.src_phone)) {
			//VerifyState
			if (MBWay.getMBWayClients().get(this.src_phone).getState() == true) {
				this.src_client = MBWay.getMBWayClients().get(this.src_phone);
			}
			else
				throw new SourceStateException();
		}
		else
			throw new NoSourcePhoneException();
	
		//GetTargetClient
		//VerifyIfPhoneNumberExists
		if (MBWay.getMBWayClients().containsKey(this.trg_phone)) {
			//VerifyState
			if (MBWay.getMBWayClients().get(this.trg_phone).getState() == true) {
				this.trg_client = MBWay.getMBWayClients().get(this.trg_phone);
			}
			else
				throw new TargetStateException();
		}
		else
			throw new NoTargetPhoneException();
	}
	
	//TRANSFER
	public void transfer() throws SibsException, AccountException, OperationException {
		//VerifySourceAccountBalance
		if (this.svc.getAccountByIban(this.src_client.getIban()).getBalance() >= this.amount) {
			this.sibs.transfer(this.src_client.getIban(), this.trg_client.getIban(), this.amount);
		}
		else {
			throw new OperationException();
		}
	}
}
