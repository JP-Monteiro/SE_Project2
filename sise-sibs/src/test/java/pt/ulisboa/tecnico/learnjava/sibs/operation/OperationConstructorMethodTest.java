package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class OperationConstructorMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	@Test
	public void success() throws OperationException {
		Operation operation = new Operation(SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertEquals(VALUE, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new Operation(null, TARGET_IBAN, VALUE);
		
//		try {
//			new Operation(null, TARGET_IBAN, VALUE);
//			fail();
//		}catch (OperationException e) {
//			
//		}
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new Operation(SOURCE_IBAN, "", VALUE);
		
	}

}
