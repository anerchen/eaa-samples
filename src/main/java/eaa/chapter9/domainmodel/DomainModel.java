package eaa.chapter9.domainmodel;



public class DomainModel  {
  
	public static void main(String[] args) throws Exception {
		
		 testOne();
		 testTwo();
		 testThree();
		 
	}
	
	private static void testOne() {
		bar();
		System.out.println("Checking ballance for person 1 - should be '0'");
		
		Person p = new Person(1);
		
		System.out.println("balance = " + p.getRewardBalance() );
	}
	
	private static void testTwo() {
		bar();
		
		Person p = new Person(1);
		p.setRewardPlan( new Plan( 1, 10, 1 ) );
		
		System.out.println("Purchasing 14.5 gallons for person 1");
		double purchaseGallons = p.processFillup( 14.5 );
		System.out.printf("  %5.2f were actually paid for%n", purchaseGallons );
		
		System.out.println("User should have a reward balance of around 1 now");
		double rewardBalance = p.getRewardBalance();
		System.out.printf("  %5.2f is the current reward balance%n", rewardBalance );
		
		System.out.println("Purchasing 7 gallons for person 1");
		System.out.println(" should only pay for 6 of them");
		purchaseGallons = p.processFillup( 14.5 );
		System.out.printf("  %5.2f were actually paid for%n", purchaseGallons );
		
		System.out.println("User should have a reward balance of around 1 now");
		rewardBalance = p.getRewardBalance( );
		System.out.printf("  %5.2f is the current reward balance%n", rewardBalance );
	}
	
	public static void testThree() {
		bar();
	
		Person p = new Person(2);
		p.setRewardPlan( new Plan( 2, 15.0, 1.0 ) );
		
		for( double gal = 1.0; gal < 200.0; gal = gal * 1.25 ) {
			System.out.printf("Purchasing %5.2f gallons for person %d %n", gal, p.getId() );
			double purchaseGallons = p.processFillup( gal );
			System.out.printf("  - paid for %5.2f gallons%n", purchaseGallons );
			double rewardBalance = p.getRewardBalance();
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
