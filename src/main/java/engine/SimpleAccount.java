package engine;

public class SimpleAccount {

	public String fullName;
	public String emailAddress;
	
	public SimpleAccount(String fullName, String emailAddress){
		this.fullName = fullName;
		this.emailAddress = emailAddress;
	}
	
	public SimpleAccount(String emailAddress){
		this(null, emailAddress);
	}
}
