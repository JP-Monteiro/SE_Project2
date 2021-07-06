package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}
	

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		//Initialization & CheckForSourceAccountException
		Account sourceAccount = this.services.getAccountByIban(sourceIban);
		//Initialization & CheckForTargetAccountException
		Account targetAccount = this.services.getAccountByIban(targetIban);
		
		//AccountsVerification
		if (sourceAccount == null || targetAccount == null)
			throw new AccountException();
		//AddOperation
		int index = addOperation(sourceIban, targetIban, amount);
//			getOperation(index).setState(new Retry());
		//BalanceVerification
//		else {
//			if (sourceAccount.getBalance() < amount)
//				throw new OperationException();
//		}
	}

	public int addOperation(String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation = new Operation(sourceIban, targetIban, value);

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}
	
	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}
	
	public int getNumberOfOperations() throws SibsException{
		int nOperations=0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null)
				nOperations++;
		}
		return nOperations;
	}
	
	public int getTotalValueOfOperations() throws SibsException {
		int total = 0;
		for (Operation o: this.operations) {
			if (o != null)
				total += o.getValue();
		}
		return total;
	}
	
	public void processOperations() throws AccountException, SibsException {
		for (int i = 0; i < this.getNumberOfOperations(); i++) {
			if (this.getOperation(i).getClass().getName() != new Completed().getClass().getName())
				this.getOperation(i).process();
		}
	}
	
	public void cancelOperation(int index) throws OperationException, SibsException, AccountException {
		Operation op = this.getOperation(index);
		if (op == null)
			throw new OperationException();
		op.cancel();
	}

}
