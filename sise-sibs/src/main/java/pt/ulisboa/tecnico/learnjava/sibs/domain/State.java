package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public abstract class State {

	public void process(Operation op) throws AccountException {}
	
	public void processRetry(Operation op) throws AccountException {}
	
	public void cancel(Operation op) throws AccountException {}
	
}
