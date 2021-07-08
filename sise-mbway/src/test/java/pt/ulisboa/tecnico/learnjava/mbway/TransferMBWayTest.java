package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.AssociateMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.ConfirmMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.TransferMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AccountExistsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoClientsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoPhoneNumberException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoSourcePhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoTargetPhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SourceStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TargetStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.WrongCodeException;


public class TransferMBWayTest {
	
	//Data_Client1 - Carolina Marques
	private static final String ADDRESS1 = "Praca da republica";
	private static final String PHONE_NUMBER1 = "914208271";
	private static final String NIF1 = "297109681";
	private static final String LAST_NAME1 = "Marques";
	private static final String FIRST_NAME1 = "Carolina";
	private String firstIban;
	//Data_Client2 - Vera Pires
	private static final String ADDRESS2 = "Rua das couves";
	private static final String PHONE_NUMBER2 = "928039152";
	private static final String NIF2 = "279019271";
	private static final String LAST_NAME2 = "Pires";
	private static final String FIRST_NAME2 = "Vera";
	private String secondIban;
	//Banks
	private Bank firstBank;
	private Bank secondBank;
	//Clients
	private Client firstClient;
	private Client secondClient;
	//MBWay
	private MBWay MBWay;
	//Services
	private Services svc;

	@Before
	public void setUp() throws BankException, ClientException, AccountException, AccountExistsException, NoClientsException, NoPhoneNumberException, WrongCodeException {
		//SET-UP
		//----------
		//SetServices
		this.svc = new Services();
		//SetBanks
		this.firstBank = new Bank("CGD");
		this.secondBank = new Bank("BPI");
		//SetClients
		this.firstClient = new Client(this.firstBank, FIRST_NAME1, LAST_NAME1, NIF1, PHONE_NUMBER1, ADDRESS1, 24);
		this.secondClient = new Client(this.secondBank, FIRST_NAME2, LAST_NAME2, NIF2, PHONE_NUMBER2, ADDRESS2, 25);
		//SetAccounts
		this.firstIban = this.firstBank.createAccount(AccountType.CHECKING, firstClient, 20000, 0);
		this.secondIban = this.secondBank.createAccount(AccountType.CHECKING, secondClient, 15000, 0);
		//SetMBWay
		this.MBWay = new MBWay();
		//----------
		
		//FirstClient - Associate-Confirm
		AssociateMBWayController c1 = new AssociateMBWayController(
										  this.firstIban, 
										  this.firstClient.getPhoneNumber(), 
										  this.MBWay);
		c1.process();
		ConfirmMBWayController c2 = new ConfirmMBWayController(
										this.firstClient.getPhoneNumber(), 
										this.MBWay.getMBWayClients().get(firstClient.getPhoneNumber()).getCode(), 
										this.MBWay);
		c2.process();
		
		//SecondClient - Associate-Confirm
		c1 = new AssociateMBWayController(this.secondIban, this.secondClient.getPhoneNumber(), this.MBWay);
		c1.process();
		c2 = new ConfirmMBWayController(
				 this.secondClient.getPhoneNumber(), 
				 this.MBWay.getMBWayClients().get(secondClient.getPhoneNumber()).getCode(), 
				 this.MBWay);
		c2.process();
	}

	@Test
	public void success() 
			throws BankException, ClientException, 
			AccountException, AccountExistsException, 
			NoClientsException, NoPhoneNumberException, 
			WrongCodeException, SourceStateException, 
			TargetStateException, NoSourcePhoneException, 
			NoTargetPhoneException, SibsException, OperationException {
		
		TransferMBWayController c3 = new TransferMBWayController(firstClient.getPhoneNumber(), 
									secondClient.getPhoneNumber(), 
									String.valueOf(5000), MBWay);
		c3.getClients();
		c3.transfer();
		assertEquals(15000, this.svc.getAccountByIban(firstIban).getBalance());
		assertEquals(20000, this.svc.getAccountByIban(secondIban).getBalance());
	}
	
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}



}
