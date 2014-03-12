package eaa.chapter9.domainmodel;

import java.util.Date;

public class Reward implements Comparable<Reward> {

  private long id;
  private double gallons;
  private Date createdAt;
  private Person person;

  public Reward(double gallons, Person person) {
    this.id = -1;
    this.gallons = gallons;
    this.createdAt = new Date();
    this.person = person;
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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public int compareTo(Reward arg0) {
    return this.createdAt.compareTo(arg0.createdAt);
  }

}
