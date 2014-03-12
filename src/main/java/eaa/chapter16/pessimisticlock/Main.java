package eaa.chapter16.pessimisticlock;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Thread t1 = new Thread( new Updater1(), "1:: " );
		Thread t2 = new Thread( new Updater2(), "2:: " );
		
		t1.start();
		t2.start();
		
		while( t1.isAlive() && t2.isAlive() ) {
			try {
				t1.join();
				t2.join();
			} catch ( Exception ex ) {}
		}
		
	}
	
	private synchronized static void say( String a ) {
		System.out.println( 
				Thread.currentThread().getName() + a );
	}
	
	private synchronized static void printAccount( Account a ) {
		say ( "Account id=" + a.getId() + 
				" desc=" + a.getDescription() +
				" balance=" + a.getBalance() );
	}
	
	private static class Updater1 implements Runnable {
		public void run() {
			try {
				DB.startTransaction("user one");
				
				Account a = Account.find(1);
				printAccount(a);
				
				try { Thread.sleep(10000); } catch ( InterruptedException iex ) {}
				
				a.setBalance( a.getBalance() + 20.0f );
				say( "trying to credit 20.0 to the account ");
				a.update();
				
				printAccount(a);
				
				DB.commit();
			} catch ( Exception ex ) {
				say("ERROR: " + ex.getMessage() + "::" + ex.getCause().getMessage() );
				DB.rollback();
			}
		}
	}

	private static class Updater2 implements Runnable {
		public void run() {
			try {
				DB.startTransaction("user two");
				
				Account a = Account.find(1);
				printAccount(a);
				
				try { Thread.sleep(3000); } catch ( InterruptedException iex ) {}
				
				a.setBalance( a.getBalance() - 15.0f );
				say( "trying to debit 15.0 from the account ");
				a.update();
				
				printAccount(a);
				
				DB.commit();
			} catch ( Exception ex ) {
				say("ERROR: " + ex.getMessage() + "::" + ex.getCause().getMessage() );
				DB.rollback();
			}			
		}
	}
	
}
