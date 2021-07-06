package pt.ulisboa.tecnico.learnjava.mbway.cli;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.AssociateMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.ConfirmMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.SplitInsuranceMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.TransferMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AccountExistsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AmountBiggerThanZeroException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AmountLessThanZeroException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoClientsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoPhoneNumberException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoSourcePhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.NoTargetPhoneException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SourceStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TargetStateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.WrongCodeException;

public class MBWayViews {

	//Data_Client1 - Carolina Marques
	private static final String ADDRESS1 = "Praca da republica";
	private static final String PHONE_NUMBER1 = "914208271";
	private static final String NIF1 = "297109681";
	private static final String LAST_NAME1 = "Marques";
	private static final String FIRST_NAME1 = "Carolina";
	private static String firstIban;
	//Data_Client2 - Vera Pires
	private static final String ADDRESS2 = "Rua das couves";
	private static final String PHONE_NUMBER2 = "928039152";
	private static final String NIF2 = "279019271";
	private static final String LAST_NAME2 = "Pires";
	private static final String FIRST_NAME2 = "Vera";
	private static String secondIban;
	//Data_Client3 - Joao Monteiro
	private static final String ADDRESS3 = "Praceta Timor";
	private static final String PHONE_NUMBER3 = "914103291";
	private static final String NIF3 = "273918231";
	private static final String LAST_NAME3 = "Monteiro";
	private static final String FIRST_NAME3 = "Joao";
	private static String thirdIban;
	//Banks
	private static Bank firstBank;
	private static Bank secondBank;
	private static Bank thirdBank;
	//Clients
	private static Client firstClient;
	private static Client secondClient;
	private static Client thirdClient;
	//MBWay
	private static MBWay MBWay = new MBWay();
	//CommandForLoop
	private static boolean ON = true;

	public static void main(String [] args) throws BankException, AccountException, ClientException {
		
		//SET-UP
		//----------
		//SetBanks
		firstBank = new Bank("CGD");
		secondBank = new Bank("BPI");
		thirdBank = new Bank("STD");
		//SetClients
		firstClient = new Client(firstBank, FIRST_NAME1, LAST_NAME1, NIF1, PHONE_NUMBER1, ADDRESS1, 24);
		secondClient = new Client(secondBank, FIRST_NAME2, LAST_NAME2, NIF2, PHONE_NUMBER2, ADDRESS2, 25);
		thirdClient = new Client(thirdBank, FIRST_NAME3, LAST_NAME3, NIF3, PHONE_NUMBER3, ADDRESS3, 22);
		//SetAccounts
		firstIban = firstBank.createAccount(AccountType.CHECKING, firstClient, 20000, 0);
		secondIban = secondBank.createAccount(AccountType.CHECKING, secondClient, 15000, 0);
		thirdIban = thirdBank.createAccount(AccountType.CHECKING, thirdClient, 10000, 0);
		//----------
		
		//API
		while (ON == true) {
			//GetCommands
			Scanner command = new Scanner(System.in);
			System.out.println("Enter the desired command: ");
			String cmd = command.nextLine();
			String[] input = cmd.split(" ");
			//CheckCommand
			switch (input[0]) {
				case "exit":{
					command.close();
					ON = false;
					for (int i = 0; i < 3; ++i) System.out.println(".");
					System.out.println("-	Finished	-\n");
					break;
				}
				case "associate-mbway":{
					try {
						//InitializateController
						AssociateMBWayController c1 = new AssociateMBWayController(input[1], input[2], MBWay);
						//Process
						c1.process();
						//Print generated code
						System.out.println("Generated code: " + MBWay.getMBWayClients().get(input[2]).getCode() + "\n");
						break;
						
					} catch (AccountException e) {
						System.out.println("Error while trying to create the MBWay association. Please try again.\n");
						break;
					} catch (AccountExistsException e) {
						System.out.println("MBWay association already exists.\n");
						break;
					}
				}
				case "confirm-mbway":{
					try {
						//InitializateController
						ConfirmMBWayController c2 = new ConfirmMBWayController(input[1], input[2], MBWay);
						//Process
						c2.process();
						System.out.println("MBWay association confirmed successfully!\n");
						break;
					} catch (NoClientsException e) {
						System.out.println("There are no registered clients in our database.\n");
						break;
					} catch (NoPhoneNumberException e) {
						System.out.println("The inserted phone number is not in our database.\n");
						break;
					} catch (WrongCodeException e) {
						System.out.println("The inserted code is wrong. Please try again.\n");
						break;
					}
				}
				case "mbway-transfer":{
					try {
						TransferMBWayController c3 = new TransferMBWayController(input[1], input[2], input[3], MBWay);
						c3.getClients();
						c3.transfer();
						System.out.println("The MBWay transfer was successful!\n");
						break;
					} catch (SourceStateException e) {
						System.out.println("Source account is not verified. Verify the account first!\n");
						break;
					} catch (TargetStateException e) {
						System.out.println("Target account is not verified. Verify the account first!\n");
						break;
					} catch (NoSourcePhoneException e) {
						System.out.println("The source account with the inserted phone number does not exist.\n");
						break;
					} catch (NoTargetPhoneException e) {
						System.out.println("The target account with the inserted phone number does not exist.\n");
						break;
					} catch (OperationException e) {
						System.out.println("Insufficient funds in source account.\n");
						break;
					} catch (SibsException e) {
						System.out.println("There was an error adding the operation (Sibs error).\n");
						break;
					}
				}
				case "mbway-split-insurance":{
					try {
						SplitInsuranceMBWayController c4 = new SplitInsuranceMBWayController(input[1], input[2], MBWay);
						c4.process();
						System.out.println("Insurance paid successfully!\n");
						break;
					} catch (TargetStateException e) {
						System.out.println("Error. Target account is not verified.\n");
						break;
					} catch (NoTargetPhoneException e) {
						System.out.println("The target account with the inserted phone number does not exist.\n");
						break;
					} catch (SourceStateException e) {
						System.out.println("Error. Source account is not verified.\n");
						break;
					} catch (NoSourcePhoneException e) {
						System.out.println("The source account with the inserted phone number does not exist.\n");
						break;
					} catch (SibsException e) {
						System.out.println("There was an error adding the operation (Sibs error).\n");
						break;
					} catch (AccountException e) {
						System.out.println("There was an error while performing transactions (Account error).\n");
						break;
					} catch (OperationException e) {
						System.out.println("Insufficient funds in friends source account.\n");
						break;
					} catch (AmountBiggerThanZeroException e) {
						System.out.println("Not enough to pay insurance amount. Please, try again.\n");
						break;
					} catch (AmountLessThanZeroException e) {
						System.out.println("More than enough to pay insurance amount. Please try again.\n");
						break;
					}
				}
				
			//SWITCH ENDING
			}
		//WHILE ENDING
		}
	}
}
