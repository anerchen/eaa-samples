package eaa.chapter9.domainmodel;

import java.util.Set;
import java.util.TreeSet;

public class Person {
	
	private long id;

	private Plan rewardPlan;
	private Set<Purchase> purchases = new TreeSet<Purchase>();
	private Set<Reward>   rewards   = new TreeSet<Reward>();

	public Person( long id ) {
		this.id = id;
	}
	
	public Person() {
		this.id = -1;
	}
	
	public double processFillup( double gallons ) {
		// sum up the purchases
		double rewardGallons = 0;
		double totalPurchasedGallons = 0;
		for( Purchase purchase : purchases ) {
			rewardGallons += purchase.getRewardApplied();
			totalPurchasedGallons += purchase.getGallons();
		}
		
		// calculated the earned rewards
		double totalEarnedRewards = 0;
		for( Reward reward : rewards ) {
			totalEarnedRewards += reward.getGallons();
		}
		
		double rewardBalance = totalEarnedRewards - rewardGallons;
		
		// create the new purchase
		double paidGallons = (gallons - rewardBalance < 0) ? 0 : gallons - rewardBalance;
		double rewardApplied = gallons - paidGallons;
		
		Purchase newPurchase = new Purchase( paidGallons, rewardApplied );
		newPurchase.setPerson( this );
		purchases.add( newPurchase );
		
		// add in today's purchase
		totalPurchasedGallons += paidGallons;
		rewardGallons += rewardApplied;
		
		// calculare the new reward gallons
		double newRewardGallons = rewardPlan.calculateNewReward( totalPurchasedGallons, rewardGallons );
		Reward newReward = new Reward( newRewardGallons, this );
		rewards.add( newReward );
		
		return paidGallons;
	}
	
	public double getRewardBalance() {
		double rewardGallons = 0;
		for( Purchase purchase : purchases ){
			rewardGallons += purchase.getRewardApplied();
		}
		
		double earnedRewards = 0;
		for( Reward reward : rewards ) {
			earnedRewards += reward.getGallons();
		}
		
		double balance = earnedRewards - rewardGallons;
		return balance;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}

	public Plan getRewardPlan() {
		return rewardPlan;
	}

	public void setRewardPlan(Plan rewardPlan) {
		this.rewardPlan = rewardPlan;
	}

	public Set<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(Set<Reward> rewards) {
		this.rewards = rewards;
	}
	
	
	
}
