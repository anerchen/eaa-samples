package eaa.chapter10.rowdatagateway;

public interface Person {

	public abstract String getAddress();

	public abstract void setAddress(String address);

	public abstract String getCity();

	public abstract void setCity(String city);

	public abstract String getFirstName();

	public abstract void setFirstName(String firstName);

	public abstract long getId();

	public abstract String getLastName();

	public abstract void setLastName(String lastName);

	public abstract String getState();

	public abstract void setState(String state);

	public abstract String getZipCode();

	public abstract void setZipCode(String zipCode);

	public abstract void insert();

	public abstract void update();

	public abstract void delete();

}