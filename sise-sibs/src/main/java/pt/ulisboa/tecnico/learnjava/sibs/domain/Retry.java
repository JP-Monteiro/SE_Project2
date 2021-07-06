package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Retry extends State{

	private int retries;
	private State prevState;
	
	public Retry(State state) {
		prevState = state;
		this.retries = 3;
		
	}
	
	@Override
	public void process(Operation op) {
		if (this.retries > 0) {
			try {
				prevState.processRetry(op);
			} catch (AccountException e) {
				this.retries--;
			}
		}
		else
			op.setState(new Error());
	}
	
}
