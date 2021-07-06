package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;

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
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AccountExistsException;


public class AssociateMBWayTest {
	
	//Data_Client1 - Carolina Marques
	private static final String ADDRESS1 = "Praca da republica";
	private static final String PHONE_NUMBER1 = "914208271";
	private static final String NIF1 = "297109681";
	private static final String LAST_NAME1 = "Marques";
	private static final String FIRST_NAME1 = "Carolina";
	private static String firstIban;
	//Data_Client2 - Vera Pires
	private static final String ADDRESS2 = "Rua das couves";
	private static final String PHONE_NUMBER2 = "928039152";
	private static final String NIF2 = "279019271";
	private static final String LAST_NAME2 = "Pires";
	private static final String FIRST_NAME2 = "Vera";
	private static String secondIban;
	//Data_Client3 - Joao Monteiro
	private static final String ADDRESS3 = "Praceta Timor";
	private static final String PHONE_NUMBER3 = "914103291";
	private static final String NIF3 = "273918231";
	private static final String LAST_NAME3 = "Monteiro";
	private static final String FIRST_NAME3 = "Joao";
	private static String thirdIban;
	//Banks
	private static Bank firstBank;
	private static Bank secondBank;
	private static Bank thirdBank;
	//Clients
	private static Client firstClient;
	private static Client secondClient;
	private static Client thirdClient;
	//MBWay
	private static MBWay MBWay;
	
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		//SET-UP
		//----------
		//SetBanks
		firstBank = new Bank("CGD");
		secondBank = new Bank("BPI");
		thirdBank = new Bank("STD");
		//SetClients
		firstClient = new Client(firstBank, FIRST_NAME1, LAST_NAME1, NIF1, PHONE_NUMBER1, ADDRESS1, 24);
		secondClient = new Client(secondBank, FIRST_NAME2, LAST_NAME2, NIF2, PHONE_NUMBER2, ADDRESS2, 25);
		thirdClient = new Client(thirdBank, FIRST_NAME3, LAST_NAME3, NIF3, PHONE_NUMBER3, ADDRESS3, 22);
		//SetAccounts
		firstIban = firstBank.createAccount(AccountType.CHECKING, firstClient, 20000, 0);
		secondIban = secondBank.createAccount(AccountType.CHECKING, secondClient, 15000, 0);
		thirdIban = thirdBank.createAccount(AccountType.CHECKING, thirdClient, 10000, 0);
		//SetMBWay
		MBWay = new MBWay();
		//----------
	}

	@Test
	public void success() throws BankException, ClientException, AccountException, AccountExistsException {
		//VerifyAccountCreation
		assertEquals("CGDCK1", firstIban);
		assertEquals("BPICK2", secondIban);
		assertEquals("STDCK3", thirdIban);
		
		//FirstClient - Verify IBAN, Phone number and Generated Code from process
		AssociateMBWayController c1 = new AssociateMBWayController(firstIban, firstClient.getPhoneNumber(), MBWay);
		assertEquals(c1.getIban(), firstIban);
		assertEquals(c1.getPhoneNumber(), firstClient.getPhoneNumber());
		c1.process();
		assertEquals(String.valueOf(c1.getCode()), 
				MBWay.getMBWayClients().get(firstClient.getPhoneNumber()).getCode());
		//SecondClient - Verify IBAN, Phone number and Generated Code from process
		c1 = new AssociateMBWayController(secondIban, secondClient.getPhoneNumber(), MBWay);
		assertEquals(c1.getIban(), secondIban);
		assertEquals(c1.getPhoneNumber(), secondClient.getPhoneNumber());
		c1.process();
		assertEquals(String.valueOf(c1.getCode()), 
				MBWay.getMBWayClients().get(secondClient.getPhoneNumber()).getCode());
		//ThirdClient - Verify IBAN, Phone number and Generated Code from process
		c1 = new AssociateMBWayController(thirdIban, thirdClient.getPhoneNumber(), MBWay);
		assertEquals(c1.getIban(), thirdIban);
		assertEquals(c1.getPhoneNumber(), thirdClient.getPhoneNumber());
		c1.process();
		assertEquals(String.valueOf(c1.getCode()), 
				MBWay.getMBWayClients().get(thirdClient.getPhoneNumber()).getCode());
		
		//Client 1 added but state not verified
		assertEquals(MBWay.getMBWayClients().containsKey(firstClient.getPhoneNumber()), true);
		assertEquals(MBWay.getMBWayClients().get(firstClient.getPhoneNumber()).getState(), false);
		
		//Client 2 added but state not verified
		assertEquals(MBWay.getMBWayClients().containsKey(secondClient.getPhoneNumber()), true);
		assertEquals(MBWay.getMBWayClients().get(secondClient.getPhoneNumber()).getState(), false);
		
		//Client 3 added but state not verified
		assertEquals(MBWay.getMBWayClients().containsKey(thirdClient.getPhoneNumber()), true);
		assertEquals(MBWay.getMBWayClients().get(thirdClient.getPhoneNumber()).getState(), false);
	}


}
