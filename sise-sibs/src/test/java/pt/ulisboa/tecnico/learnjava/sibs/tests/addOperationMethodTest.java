package pt.ulisboa.tecnico.learnjava.sibs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.*;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class addOperationMethodTest {
	
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private String sourceIBAN;
	private String targetIBAN;

	//Declaration of domain entities.
	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		//SetService
		this.services = new Services();
		//SetSibs
		this.sibs = new Sibs(3, services);
		//SetBanks
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		//SetClients
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		//SetAccounts
		this.sourceIBAN = this.sourceBank.createAccount(AccountType.CHECKING, sourceClient, 10400, 0);
		this.targetIBAN = this.targetBank.createAccount(AccountType.CHECKING, targetClient, 0, 0);
	}

	@Test
	public void success() throws SibsException, OperationException {
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		
		assertEquals(this.sibs.getOperation(0).getSourceIban(), sourceIBAN);
		assertEquals(this.sibs.getOperation(0).getTargetIban(), targetIBAN);
		assertEquals(this.sibs.getOperation(0).getValue(), value);
		assertEquals(new Registered().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
	}
	
	@Test
	public void checkIfStateChangesToWithdrawnCorrectly() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		//CheckParameters
		assertEquals(new Withdrawn().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}
	
	@Test
	public void checkIfStateChangesToDepositedCorrectly() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		//ProcessToDeposited
		this.sibs.getOperation(0).process();
		//CheckParameters
		assertEquals(new Deposited().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(value, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}
	
	@Test
	public void checkIfStateChangesToCompletedCorrectly() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		//ProcessToDeposited
		this.sibs.getOperation(0).process();
		//ProcessToCompleted
		this.sibs.getOperation(0).process();
		//CheckParameters
		assertEquals(new Completed().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(200, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(value, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}
	
	@Test
	public void checkIfStateChangesFromRegisteredToCancelled() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//Cancel
		this.sibs.getOperation(0).cancel();
		//CheckParameters
		assertEquals(new Cancelled().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}
	
	@Test
	public void checkIfStateChangesFromWithdrawnToCancelled() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		//Cancel
		this.sibs.getOperation(0).cancel();
		//CheckParameters
		assertEquals(new Cancelled().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}
	
	@Test
	public void checkIfStateChangesFromDepositedToCancelled() throws OperationException, SibsException, AccountException {
		//Initialization
		int value = 10000;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
		//ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		//ProcessToDeposited
		this.sibs.getOperation(0).process();
		//Cancel
		this.sibs.getOperation(0).cancel();
		//CheckParameters
		assertEquals(new Cancelled().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}

	//SibsWithNoSpaceCantAdd
	@Test(expected = SibsException.class)
	public void negativePosition() throws SibsException, OperationException {
		this.sibs = new Sibs(0, new Services());
		this.sibs.addOperation(sourceIBAN, targetIBAN, 0);
	}

	//Position5DoesntExist
	@Test(expected = SibsException.class)
	public void positionAboveLength() throws SibsException {
		this.sibs.removeOperation(5);
	}

	//Position5DoesntExist
	@Test(expected = OperationException.class)
	public void checkOperationParametersTest() throws SibsException, OperationException {
		//Initialization
		int value = -1;
		this.sibs.addOperation(sourceIBAN, targetIBAN, value);
	}
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}
	
}
