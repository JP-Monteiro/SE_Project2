package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Withdrawn extends State{

	private Services svc = new Services();
	
	@Override
	public void process(Operation op) throws AccountException{
		svc.deposit(op.getTargetIban(), op.getValue());
        op.setState(new Deposited());
    }
	
	@Override
	public void cancel(Operation op) throws AccountException {
		svc.deposit(op.getSourceIban(), op.getValue());
		op.setState(new Cancelled());
	}
	
}
