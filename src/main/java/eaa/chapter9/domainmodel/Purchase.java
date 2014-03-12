package eaa.chapter9.domainmodel;

import java.util.Date;

public class Purchase implements Comparable<Purchase> {

  private long id;
  private double gallons;
  private double rewardApplied;
  private Date createdAt;
  private Person person;

  public Purchase(long id, double gallons, double rewardApplied) {
    this.id = id;
    this.gallons = gallons;
    this.rewardApplied = rewardApplied;
    this.createdAt = new Date();
  }

  public double getFueledGallons() {
    return gallons + rewardApplied;
  }

  public Purchase(double gallons, double rewardApplied) {
    this(-1, gallons, rewardApplied);
  }

  public Purchase(double gallons) {
    this(gallons, 0.0);
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public double getGallons() {
    return gallons;
  }

  public void setGallons(double gallons) {
    this.gallons = gallons;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getRewardApplied() {
    return rewardApplied;
  }

  public void setRewardApplied(double rewardApplied) {
    this.rewardApplied = rewardApplied;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public int compareTo(Purchase arg0) {
    return this.createdAt.compareTo(arg0.createdAt);
  }

}
