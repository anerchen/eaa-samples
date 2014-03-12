package eaa.chapter12.singletableinheritance;

public class SingleTableInheritance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FootballPlayer p = new FootballPlayer();
		p.setName("My. Football");
		p.setReceptions( 32 );
		
		long fp_id = p.insert();
		
		BaseballPlayer p2 = new BaseballPlayer();
		p2.setName("Mr. Red");
		p2.setBattingAverage( 297 );
		
		long bp_id = p2.insert();
		
		
		System.out.println( Player.find(fp_id) );
		System.out.println( Player.find(bp_id) );
		
		// need to cast to use correctly
		p2 = (BaseballPlayer) Player.find(bp_id);
		System.out.println("Object loaded and cast appropriatley: " + p2 );

	}

}
