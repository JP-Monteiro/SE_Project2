package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Deposited extends State{

	private Services svc = new Services();
	
	@Override
	public void process(Operation op) throws AccountException {
		try {
			svc.withdraw(op.getSourceIban(), op.getCommission());
	        op.setState(new Completed());
		} catch(AccountException e) {
			op.setState(new Retry(this));
		}
    }
	
	@Override
	public void processRetry(Operation op) throws AccountException {
		svc.withdraw(op.getSourceIban(), op.getCommission());
		op.setState(new Completed());
	}
	
	@Override
	public void cancel(Operation op) throws AccountException {
		svc.withdraw(op.getTargetIban(), op.getValue());
		svc.deposit(op.getSourceIban(), op.getValue());
		op.setState(new Cancelled());
	}

}
