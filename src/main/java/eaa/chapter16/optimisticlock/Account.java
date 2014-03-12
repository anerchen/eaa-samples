package eaa.chapter16.optimisticlock;

import java.util.List;

import activemapper.MappedField;
import activemapper.MappedTable;

@MappedTable("accounts")
public class Account extends VersionedActiveRecord {

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
	public static Account find( long id ) {
		return VersionedActiveRecord.find( Account.class, id );
	}
	
	public static List<Account> find( String conditions, Object...params) {
		return VersionedActiveRecord.find( Account.class, conditions, params ); 
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
