package pt.ulisboa.tecnico.learnjava.bank.services;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Services {
	public void deposit(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.deposit(amount);
	}

	public void withdraw(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.withdraw(amount);
	}

	public Account getAccountByIban(String iban) /*throws AccountException*/{
		String code = iban.substring(0, 3);
		String accountId = iban.substring(3);
		//GetBank
		Bank bank = Bank.getBankByCode(code);
		//BankVerification
		if (bank == null)
			return null;
		//GetAccount
		Account account = bank.getAccountByAccountId(accountId); //BUG FOR BANK NULL
		//AccountVerification
		if (account == null)
			return null;

		return account;
	}

}
