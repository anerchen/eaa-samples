package eaa.chapter9.transactionscript;

public class TransactionScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GasService gs = new GasService();
		
		testOne( gs );
		testTwo( gs );
		testThree( gs );
	}
	
	private static void testOne( GasService gs ) {
		bar();
		System.out.println("Checking ballance for person 1 - should be '0'");
		System.out.println("balance = " + gs.getRewardBalance(1) );
	}
	
	private static void testTwo( GasService gs ) {
		bar();
		System.out.println("Purchasing 14.5 gallons for person 1");
		double purchaseGallons = gs.processFillup( 1, 14.5 );
		System.out.printf("  %5.2f were actually paid for%n", purchaseGallons );
		
		System.out.println("User should have a reward balance of around 1 now");
		double rewardBalance = gs.getRewardBalance( 1 );
		System.out.printf("  %5.2f is the current reward balance%n", rewardBalance );
		
		System.out.println("Purchasing 7 gallons for person 1");
		System.out.println(" should only pay for 6 of them");
		purchaseGallons = gs.processFillup( 1, 14.5 );
		System.out.printf("  %5.2f were actually paid for%n", purchaseGallons );
		
		System.out.println("User should have a reward balance of around 1 now");
		rewardBalance = gs.getRewardBalance( 1 );
		System.out.printf("  %5.2f is the current reward balance%n", rewardBalance );
		
	}
	
	private static void testThree( GasService gs ) {
		bar();
		long user_id = 2;
		
		for( double gal = 1.0; gal < 200.0; gal = gal * 1.25 ) {
			System.out.printf("Purchasing %5.2f gallons for person %d %n", gal, user_id );
			double purchaseGallons = gs.processFillup( user_id, gal );
			System.out.printf("  - paid for %5.2f gallons%n", purchaseGallons );
			double rewardBalance = gs.getRewardBalance( user_id );
			System.out.printf("  - reward balance %5.2f%n", rewardBalance );
		}
		
	}

	private static void bar() {
		for( int i = 0; i < 80; i++ ) {
			System.out.print("*");
		}
		System.out.println("");
	}

}
