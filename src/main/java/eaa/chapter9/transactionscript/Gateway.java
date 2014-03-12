package eaa.chapter9.transactionscript;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gateway {

  private Connection db;

  private static final String FIND_PURCHASES_BY_PERSON = " SELECT id,created_at,gallons,reward_gallons "
      + "  FROM purchases " + "  WHERE person_id = ? order by created_at asc";

  private static final String FIND_REWARDS_BY_PERSON = " SELECT id,created_at,gallons "
      + "  FROM rewards " + "  WHERE person_id = ? order by created_at asc";

  private static final String FIND_REWARD_PLAN_BY_PERSON = " SELECT id,plan_id " + "  FROM people "
      + "  WHERE id=? ";

  private static final String CREATE_PURCHASE = " INSERT INTO purchases (id,person_id,created_at,gallons,reward_gallons) "
      + " VALUES (?,?,current_timestamp,?,?) ";

  private static final String GET_MAX_PURCHASE_ID = " SELECT MAX(id) FROM purchases ";

  private static final String CREATE_REWARD = " INSERT INTO rewards (id,person_id,created_at,gallons)"
      + " VALUES (?,?,current_timestamp,?) ";

  private static final String GET_MAX_REWARD_ID = " SELECT MAX(id) FROM rewards ";

  private static final String GET_REWARD_PLAN_BY_ID = " SELECT purchase_requirement,reward_gallons "
      + "  FROM plans" + "  WHERE id=? ";

  public void createReward(long person, double gallons) throws SQLException {
    // get the current max id
    PreparedStatement stmt = db.prepareStatement(GET_MAX_REWARD_ID);
    ResultSet result = stmt.executeQuery();
    result.next();
    long nextId = result.getLong(1) + 1;

    // create the purchase
    stmt = db.prepareStatement(CREATE_REWARD);
    stmt.setLong(1, nextId);
    stmt.setLong(2, person);
    stmt.setDouble(3, gallons);

    int rows = stmt.executeUpdate();

    if (rows != 1) {
      throw new SQLException("insert failed");
    }
  }

  public Gateway(Connection db) {
    this.db = db;
  }

  public ResultSet findPurchases(long personId) throws SQLException {
    PreparedStatement stmt = db.prepareStatement(FIND_PURCHASES_BY_PERSON);
    stmt.setLong(1, personId);
    ResultSet result = stmt.executeQuery();
    return result;
  }

  public ResultSet findRewards(long personId) throws SQLException {
    PreparedStatement stmt = db.prepareStatement(FIND_REWARDS_BY_PERSON);
    stmt.setLong(1, personId);
    ResultSet result = stmt.executeQuery();
    return result;
  }

  public void createPurchase(long person, double gallons, double rewardGallons) throws SQLException {
    // get the current max id
    PreparedStatement stmt = db.prepareStatement(GET_MAX_PURCHASE_ID);
    ResultSet result = stmt.executeQuery();
    result.next();
    long nextId = result.getLong(1) + 1;

    // create the purchase
    stmt = db.prepareStatement(CREATE_PURCHASE);
    stmt.setLong(1, nextId);
    stmt.setLong(2, person);
    stmt.setDouble(3, gallons);
    stmt.setDouble(4, rewardGallons);

    int rows = stmt.executeUpdate();

    if (rows != 1) {
      throw new SQLException("insert failed");
    }
  }

  public ResultSet findRewardPlanId(long personId) throws SQLException {
    PreparedStatement stmt = db.prepareStatement(FIND_REWARD_PLAN_BY_PERSON);
    stmt.setLong(1, personId);
    ResultSet result = stmt.executeQuery();
    return result;
  }

  public ResultSet getRewardPlan(long rewardPlan) throws SQLException {
    PreparedStatement stmt = db.prepareStatement(GET_REWARD_PLAN_BY_ID);
    stmt.setLong(1, rewardPlan);
    ResultSet result = stmt.executeQuery();
    return result;
  }

}
