package eaa.chapter16.pessimisticlock;

import java.util.List;

import eaa.support.ConcurrencyException;

import activemapper.MappedField;
import activemapper.MappedTable;

@MappedTable("accounts")
public class Account extends LockedActiveRecord {

	@MappedField("description")
	private String description;
	@MappedField("balance")
	private double balance;
	
	public Account() {
		super();
	}
	
	public Account( String description, float balance ) {
		super();
		this.description = description;
		this.balance = balance;
	}
	
	@SuppressWarnings("unchecked")
	public static Account find( long id ) throws ConcurrencyException {
		return LockedActiveRecord.find( Account.class, id );
	}
	
	public static List<Account> find( String conditions, Object...params) throws ConcurrencyException {
		return LockedActiveRecord.find( Account.class, conditions, params ); 
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
