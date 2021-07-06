package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Registered extends State{

	private Services svc = new Services();
	
	@Override
	public void process(Operation op){
		try {
			svc.withdraw(op.getSourceIban(), op.getValue());
			op.setState(new Withdrawn());
		}catch (AccountException e) {
			op.setState(new Retry(this));
		}
    }
	
	@Override
	public void processRetry(Operation op) throws AccountException {
		svc.withdraw(op.getSourceIban(), op.getValue());
		op.setState(new Withdrawn());
	}
	
	@Override
	public void cancel(Operation op) {
		op.setState(new Cancelled());
	}

	
}
