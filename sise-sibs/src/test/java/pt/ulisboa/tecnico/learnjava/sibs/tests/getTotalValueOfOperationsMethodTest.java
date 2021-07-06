package pt.ulisboa.tecnico.learnjava.sibs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class getTotalValueOfOperationsMethodTest {

	//FIRST
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";
	private static final int VALUE = 100;
	//SECOND
	private static final String TARGET_IBAN2 = "TargetIban";
	private static final String SOURCE_IBAN2 = "SourceIban";
	private static final int VALUE2 = 200;

	private Sibs sibs;
	
	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(4, new Services());
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(SOURCE_IBAN2, TARGET_IBAN2, VALUE2);
	}

	@Test
	public void success() throws SibsException {
		
		assertEquals(300, this.sibs.getTotalValueOfOperations());
	}
}
