package pt.ulisboa.tecnico.learnjava.sibs.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class transferMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private String sourceIBAN;
	private String targetIBAN;

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		// SetService
		this.services = new Services();
		// SetSibs
		this.sibs = new Sibs(100, services);
		// SetBanks
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		// SetClients
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		// SetAccounts
		this.sourceIBAN = this.sourceBank.createAccount(AccountType.CHECKING, sourceClient, 10400, 0);
		this.targetIBAN = this.targetBank.createAccount(AccountType.CHECKING, targetClient, 0, 0);
	}

	@Test
	public void success() throws AccountException, SibsException, OperationException {
		int value = 10000;
//		DoTransactions
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value);
		// ProcessToWithdrawn
		this.sibs.getOperation(0).process();
		// ProcessToDeposited
		this.sibs.getOperation(0).process();
		// ProcessToCompleted
		this.sibs.getOperation(0).process();
//		VerifyValues
		assertEquals(new Completed().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(200, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(value, this.services.getAccountByIban(this.targetIBAN).getBalance());
		assertEquals(200, this.sibs.getOperation(0).getCommission());
	}

//	@Test
//	public void checkIfValueIsSuperiorThanSourceAccountBalance()
//			throws SibsException, AccountException, OperationException {
////		DoTransaction	
//		try {
//			this.sibs.transfer(this.sourceIBAN, this.targetIBAN, 15000);
//		} catch (OperationException e) {
//			assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
//			assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
//		}
//	}
//
	@Test
	public void checkIfAccountsDontExistWhenBanksDont() throws SibsException, AccountException, OperationException {
		// SOURCE_ACCOUNT
//		DoTransaction
		try {
			this.sibs.transfer("WKOEI4", this.targetIBAN, 10000);
		} catch (AccountException e) {
			assertEquals(null, this.services.getAccountByIban("WKOEI4"));
			assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
		}

		// TARGET_ACCOUNT
//		DoTransaction
		try {
			this.sibs.transfer(this.sourceIBAN, "WKOEI4", 10000);
		} catch (AccountException e) {
			assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
			assertEquals(null, this.services.getAccountByIban("WKOEI4"));
		}
	}

	@Test
	public void checkIfAccountsExistWhenBanksDo() throws SibsException, AccountException, OperationException {
		// SOURCE_ACCOUNT
//		DoTransaction
		try {
			this.sibs.transfer("BPIEI4", this.targetIBAN, 10000);
		} catch (AccountException e) {
			assertEquals(null, this.services.getAccountByIban("BPIEI4"));
			assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
		}

		// TARGET_ACCOUNT
//		DoTransaction
		try {
			this.sibs.transfer(this.sourceIBAN, "BPIEI4", 10000);
		} catch (AccountException e) {
			assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
			assertEquals(null, this.services.getAccountByIban("BPIEI4"));
		}
	}

//	//WRONG SOURCE_IBAN
//	@Test
//	public void verifyRetryStateForWrongIbans() throws SibsException, AccountException, OperationException {
//		// Init_
//		int value1 = 2000;
//		this.sibs.transfer("BPIEI4", this.targetIBAN, value1);
//		//VerificationOfRetry
//		assertEquals(new Retry(new Registered()).getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
//	}
//	
//	@Test
//	public void verifyErrorState() throws SibsException, AccountException, OperationException {
//		// Init_
//		int value1 = 2000;
//		this.sibs.transfer("BPIEI4", this.targetIBAN, value1);
//		
//		for (int i = 0; i < 4; i++) {
//			this.sibs.processOperations();
//		}
//		//VerificationOfError
//		assertEquals(new Error().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
//	}
//	
	@Test
	public void verifyErrorState() throws SibsException, AccountException, OperationException {
		// Init_
		int value1 = 20000;
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value1);
		//GetErrorState
		for (int i = 0; i <= 4; i++) {
			this.sibs.processOperations();
		}
		//VerificationOfErrorState
		assertEquals(new Error().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
	}
	@Test
	public void verifyRetryStateForDifferentStates() throws SibsException, AccountException, OperationException {
		// Init_
		int value1 = 20000;
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value1);
		//VerificationOfRetryAfterProcess:Registered
		this.sibs.processOperations();
		assertEquals(new Retry(new Registered()).getClass().getName(), 
					this.sibs.getOperation(0).getState().getClass().getName());
		//DepositValueToSourceAccountForWithdrawn
		this.services.deposit(sourceIBAN, 9600);
		//VerificationOfWithdrawnAfterProcess:Retry_from_Registered
		this.sibs.processOperations();
		assertEquals(new Withdrawn().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		//VerificationOfRetryAfterProcess:Retry_from_Deposited
		this.sibs.processOperations();
		this.sibs.processOperations();
		assertEquals(new Retry(new Registered()).getClass().getName(), 
					this.sibs.getOperation(0).getState().getClass().getName());
		//DepositValueToSourceAccountForDepositedCommission
		this.services.deposit(sourceIBAN, 400);
		this.sibs.processOperations();
		assertEquals(new Completed().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
	}
	
	
	@Test
	public void verifyProcessOperations() throws AccountException, SibsException, OperationException {
		// Init_
		int value1 = 2000, value2 = 3000;
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value1);
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value2);
		// ProcessOperations
		this.sibs.processOperations();
		// VerifyValues
		assertEquals(new Withdrawn().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(new Withdrawn().getClass().getName(), this.sibs.getOperation(1).getState().getClass().getName());
		assertEquals(5400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}

	@Test
	public void verifyCancelOperationOnRegistered() throws OperationException, SibsException, AccountException {
		// Init_
		int value = 2000;
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value);
		// ProcessOperations
		this.sibs.processOperations();
		// VerifyValues_PROCESS
		assertEquals(new Withdrawn().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(8400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
		// CancelOperation
		this.sibs.cancelOperation(0);
		// VerifyValues_CANCEL
		assertEquals(new Cancelled().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}

	@Test
	public void verifyCancelOperationOnWithdrawn() throws OperationException, SibsException, AccountException {
		// Init_
		int value = 2000;
		this.sibs.transfer(this.sourceIBAN, this.targetIBAN, value);
		// ProcessOperations
		this.sibs.processOperations();
		this.sibs.processOperations();
		// VerifyValues_PROCESS
		assertEquals(new Deposited().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(8400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(2000, this.services.getAccountByIban(this.targetIBAN).getBalance());
		// CancelOperation
		this.sibs.cancelOperation(0);
		// VerifyValues_CANCEL
		assertEquals(new Cancelled().getClass().getName(), this.sibs.getOperation(0).getState().getClass().getName());
		assertEquals(10400, this.services.getAccountByIban(this.sourceIBAN).getBalance());
		assertEquals(0, this.services.getAccountByIban(this.targetIBAN).getBalance());
	}

	@Test(expected = OperationException.class)
	public void verifyCancelOperationOnNull() throws OperationException, SibsException, AccountException {
		// TryToCancelNullOperation
		this.sibs.cancelOperation(0);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
