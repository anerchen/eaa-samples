package eaa.chapter9.transactionscript;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class GasService {
	
	/**
	 * A person is putting X gallons into their tank
	 * This method applies any earned reward gallons 
	 *  - marks the purchasae
	 *  - creates new reward gallons (for a future purchase)
	 * @param person
	 * @return
	 */
	public double processFillup( long person, double gallons ) {
		double paidGallons = 0.0;
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			// create the gateway
			Gateway gw = new Gateway( c );
			
			// get all of their purchases
			ResultSet purchasesSet = gw.findPurchases( person );
			double rewardGallons = 0; 
			double totalPurchasedGallons = 0;
			while( purchasesSet.next() ) {
				rewardGallons += purchasesSet.getDouble( "reward_gallons" );
				totalPurchasedGallons += purchasesSet.getDouble( "gallons" );
			}
			
			// calculate the earned rewards
			ResultSet rewardSet = gw.findRewards( person );
			double totalEarnedRewards = 0;
			while( rewardSet.next() ) {
				totalEarnedRewards += rewardSet.getDouble( "gallons" );
			}
			
			double rewardBalance = totalEarnedRewards - rewardGallons;
			
			// create the purchase
			paidGallons = (gallons - rewardBalance < 0) ? 0 : gallons - rewardBalance;
			double rewardApplied = gallons - paidGallons;
			
			gw.createPurchase( person, paidGallons, rewardApplied );
			
			// find the purchase plan id
			ResultSet rewardPlanResults = gw.findRewardPlanId( person );
			rewardPlanResults.next();
			long planId = rewardPlanResults.getLong( "plan_id" );
			
			// load the purchase plan
			rewardPlanResults = gw.getRewardPlan( planId );
			rewardPlanResults.next();
			double plan_purchaseRequirement = rewardPlanResults.getDouble( "purchase_requirement" );
			double plan_rewardGallons = rewardPlanResults.getDouble( "reward_gallons" );
			
			// calculate the new reward gallons
			totalPurchasedGallons += paidGallons; // add today's purchase
			double newEarnedReward = Math.floor(totalPurchasedGallons / plan_purchaseRequirement)
					                * plan_rewardGallons;
			double newReward = newEarnedReward - totalEarnedRewards;
			
			// insert the new reward record - for next time
			gw.createReward( person, newReward );

			c.commit();
			return paidGallons;
		} catch ( SQLException ex ) {
			try { c.rollback(); } catch ( Exception rbex ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
	}
	
	public double getRewardBalance( long person ) {
		double balance = 0;
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			// get the gateway
			Gateway gw = new Gateway( c );
			
			// get all of their purchases
			ResultSet purchasesSet = gw.findPurchases( person );
			double rewardGallons = 0;
			while( purchasesSet.next() ) {
				rewardGallons += purchasesSet.getDouble( "reward_gallons" );
			}
			
			// calculate the earned rewards
			ResultSet rewardSet = gw.findRewards( person );
			double earnedRewards = 0;
			while( rewardSet.next() ) {
				earnedRewards += rewardSet.getDouble( "gallons" );
			}
			
			balance = earnedRewards - rewardGallons;
			
			return balance;
		} catch ( SQLException ex ) {
			try { c.rollback(); } catch ( Exception rbex ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
	}
	
	
}
