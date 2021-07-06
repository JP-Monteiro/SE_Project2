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
		
		int i = 1;
		while (i <= this.nMembers) {
			//GetFriends
			Scanner command = new Scanner(System.in);
			System.out.println("Enter friend #" + i + ": ");
			String cmd = command.nextLine();
			String[] input = cmd.split(" ");
			if (input[0].equals("friend") && i == 1) {
				this.friend_phone = input[1];
				this.friend_amount = Integer.valueOf(input[2]);
				mbwayVerificationTarget();
				this.amount -= this.friend_amount;
				i++;
			}
			else if (input[0].equals("friend")) {
				this.friend_phone = input[1];
				this.friend_amount = Integer.valueOf(input[2]);
				mbwayVerificationSource();
				this.friends.add(new Friend(this.src_client, friend_amount));
				System.out.println("Friend added.");
				i++;
			}
		}
		//VerifyAmounts
		if (verifyAmounts()) {
			transferAll();
		}
		else {
			if (this.amount > 0)
				throw new AmountBiggerThanZeroException();
			else
				throw new AmountLessThanZeroException();
		}	
	}
	
	//VerifyAmounts
	public boolean verifyAmounts() {
		for (Friend f : this.friends) {
			this.amount -= f.getAmount();
		}
		return this.amount == 0;
	}
	
	//TransferAll
	public void transferAll() throws SibsException, AccountException, OperationException {
		for (Friend f : this.friends) {
			transfer(f.getClient(), f.getAmount());
			this.amount -= f.getAmount();
		}
	}
		
	//Transfer
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
