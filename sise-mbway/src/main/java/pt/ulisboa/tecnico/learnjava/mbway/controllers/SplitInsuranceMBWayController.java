package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import java.util.ArrayList;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.domain.Friend;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayClient;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AmountBiggerThanZeroException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AmountLessThanZeroException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoSourcePhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoTargetPhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SourceStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TargetStateException;

public class SplitInsuranceMBWayController {
	
	private int nMembers, amount;
	private MBWay MBWay;
	private String friend_phone;
	private int friend_amount;
	private int person_count = 1;
	
	//ForTransferOperations
	private MBWayClient src_client, trg_client;
	private Services svc = new Services();
	private int maxNr = 100;
	private Sibs sibs = new Sibs(maxNr, svc);
	ArrayList<Friend> friends;

	public SplitInsuranceMBWayController(String nMembers, String amount, MBWay MBWay) {
		this.nMembers = Integer.valueOf(nMembers);
		this.amount = Integer.valueOf(amount);
		this.MBWay = MBWay;
		this.friends = new ArrayList<Friend>(this.nMembers);
	}
	
	public void process() 
			throws SourceStateException, TargetStateException, NoSourcePhoneException, 
			NoTargetPhoneException, SibsException, AccountException, OperationException, 
			AmountBiggerThanZeroException, AmountLessThanZeroException {
		//GetPayers
		getPayers();
		//VerifyAmounts
		verifyAmounts();
	}

	private void getPayers() 
			throws TargetStateException, NoTargetPhoneException, 
			SourceStateException, NoSourcePhoneException {
		
		while (this.person_count <= this.nMembers) {
			//OpenScanner
			String[] input = openScanner();
			//Get the first friend (MainPerson)
			if (input[0].equals("friend") && this.person_count == 1) {
				transaction(input);
				this.person_count++;
			}
			//Get the rest of friends
			else if (input[0].equals("friend")) {
				transaction(input);
				this.person_count++;
			}
		}
	}
	
	private void verifyAmounts() 
			throws AmountBiggerThanZeroException, AmountLessThanZeroException, 
			SibsException, AccountException, OperationException {
		
		if (verifyFinalValue()) {
			transferAll();
		}
		else {
			if (this.amount > 0)
				throw new AmountBiggerThanZeroException();
			else
				throw new AmountLessThanZeroException();
		}	
	}

	private void transaction(String[] input) 
			throws TargetStateException, NoTargetPhoneException, 
			SourceStateException, NoSourcePhoneException {
		//Get inserted info
		this.friend_phone = input[1];
		this.friend_amount = Integer.valueOf(input[2]);
		//MainPerson
		if (this.person_count == 1) {
			mbwayVerificationTarget();
			this.amount -= this.friend_amount;
			System.out.println("Main person added.");
		}
		//Friend
		else {
			mbwayVerificationSource();
			this.friends.add(new Friend(this.src_client, this.friend_amount));
			System.out.println("Friend added.");
		}
	}
	

	public String[] openScanner() {
		Scanner command = new Scanner(System.in);
		System.out.println("Enter friend #" + this.person_count + ": ");
		String cmd = command.nextLine();
		String[] input = cmd.split(" ");
		return input;
	}

	public boolean verifyFinalValue() {
		for (Friend f : this.friends) {
			this.amount -= f.getAmount();
		}
		return this.amount == 0;
	}
	
	public void transferAll() throws SibsException, AccountException, OperationException {
		for (Friend f : this.friends) {
			transfer(f.getClient(), f.getAmount());
			this.amount -= f.getAmount();
		}
	}
		
	public void transfer(MBWayClient src_client, int amount) throws SibsException, AccountException, OperationException {
		//VerifySourceAccountBalance
		if (this.svc.getAccountByIban(src_client.getIban()).getBalance() >= amount) {
			this.sibs.transfer(src_client.getIban(), this.trg_client.getIban(), amount);
		}
		else {
			throw new OperationException();
		}
	}
	
	public void mbwayVerificationSource() throws SourceStateException, NoSourcePhoneException {
		//VerifyIfPhoneNumberExists
		if (MBWay.getMBWayClients().containsKey(this.friend_phone)) {
			//VerifyState
			if (MBWay.getMBWayClients().get(this.friend_phone).getState() == true) {
				this.src_client = MBWay.getMBWayClients().get(this.friend_phone);
			}
			else
				throw new SourceStateException();
		}
		else
			throw new NoSourcePhoneException();
		
	}
	
	public void mbwayVerificationTarget() throws TargetStateException, NoTargetPhoneException {
		//VerifyIfPhoneNumberExists
		if (MBWay.getMBWayClients().containsKey(this.friend_phone)) {
			//VerifyState
			if (MBWay.getMBWayClients().get(this.friend_phone).getState() == true) {
				this.trg_client = MBWay.getMBWayClients().get(this.friend_phone);
			}
			else
				throw new TargetStateException();
		}
		else
			throw new NoTargetPhoneException();
	}
}
