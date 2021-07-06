package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Operation {

	private final int value;
	private int commission;
	private State state;
	private final String targetIban;
	private final String sourceIban;

	public Operation(String sourceIban, String targetIban, int value) throws OperationException {
		checkParameters(value);
		this.value = value;

		if (invalidString(targetIban)) {
			throw new OperationException();
		}
		if (invalidString(sourceIban)) {
			throw new OperationException();
		}

		this.targetIban = targetIban;
		this.sourceIban = sourceIban;
		this.commission = (int)(this.value*0.02);
		state = new Registered();
	}

	private void checkParameters(int value) throws OperationException {
		if (value <= 0) {
			throw new OperationException(value);
		}
	}

	public int getValue() {
		return this.value;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public int getCommission() {
		return this.commission;
	}

	public String getTargetIban() {
		return this.targetIban;
	}
	public String getSourceIban() {
		return this.sourceIban;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setState(State state) {
        this.state = state;
    }
	
	public void process() throws AccountException {
		state.process(this);
	}
	
	public void cancel() throws AccountException {
		state.cancel(this);
	}
}
