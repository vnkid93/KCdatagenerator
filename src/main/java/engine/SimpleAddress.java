package engine;

public class SimpleAddress {
	public String street;
	public String city;
	public String state;
	public String zip;
	public String country;
	
	
	public SimpleAddress(){
		this(null, null, null, null);
	}
	
	public SimpleAddress(String street, String city, String country, String zip){
		this.street = street;
		this.city = city;
		this.country = country;
		this.state = country;
		this.zip = zip;
	}
}
