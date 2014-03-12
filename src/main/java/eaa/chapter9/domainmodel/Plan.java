package eaa.chapter9.domainmodel;

public class Plan {

	private long id;
	private double purchaseRequirement;
	private double rewardGallons;
	
	public Plan( long id, double purchaseRequirement, double rewardGallons ) {
		this.id = id;
		this.purchaseRequirement = purchaseRequirement;
		this.rewardGallons = rewardGallons;
	}
	
	public Plan( double purchaseRequirement, double rewardGallons ) {
		this( -1, purchaseRequirement, rewardGallons );
	}
	
	public double calculateNewReward( double totalGallonsPurchased, double rewardGallonsRedeemed ) {
		double newEarnedReward = Math.floor(totalGallonsPurchased / purchaseRequirement)
        							* rewardGallons 
        							- rewardGallonsRedeemed;
		return newEarnedReward;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getPurchaseRequirement() {
		return purchaseRequirement;
	}

	public void setPurchaseRequirement(double purchaseRequirement) {
		this.purchaseRequirement = purchaseRequirement;
	}

	public double getRewardGallons() {
		return rewardGallons;
	}

	public void setRewardGallons(double rewardGallons) {
		this.rewardGallons = rewardGallons;
	}
	
}
