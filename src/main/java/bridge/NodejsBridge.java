package bridge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import engine.SimpleAccount;
import generators.AccountGen;
import generators.ContactGen;
import generators.EventGen;
import generators.MailGen;
import generators.NoteGen;
import generators.TaskGen;

public class NodejsBridge {
	private KCBridge webadminBridge;
	private EwsBridge clientBridge;
	private String domainId;
	private String host;
	
	final static String PORT = "4040";
	
	public NodejsBridge(){
		webadminBridge = null;
		clientBridge = null;
		domainId = null;
		host = null;
	}
	
	public NodejsBridge(String host, String adminLogin, String adminPass, String clientLogin, String clientPass){
		webadminBridge = new KCBridge();
		webadminBridge.login("https://" + host+ ":"+ PORT, adminLogin, adminPass);
		clientBridge = new EwsBridge(clientLogin, clientPass);
		clientBridge.setHostAndUrl(host);
		clientBridge.setFullName(webadminBridge.getFullName(clientLogin));
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public void logAdmin(String user, String pass){
		if(webadminBridge != null){
			webadminBridge.logout();
		}else{			
			webadminBridge = new KCBridge();
		}
		webadminBridge.login("https://" + this.host + ":"+ PORT, user, pass);
	}
	
	public void logClient(String user, String pass){
		if(clientBridge == null){
			clientBridge = new EwsBridge(user, pass);
		}else{
			clientBridge.setCredentials(user, pass);
		}
		clientBridge.setHostAndUrl(this.host);
		String fullName = (webadminBridge != null && webadminBridge.getFullName(user) != null) ? webadminBridge.getFullName(user) : "";
		clientBridge.setFullName(fullName);
	}
	
	public int loginToAll(String host, String adminLogin, String adminPass, String userLogin, String userPass){
		int result = 0;
		this.host = host;
		logAdmin(adminLogin, adminPass);
		logClient(userLogin, userPass);
		return result;
	}
	
	public void setDomainId(String domainName){
		this.domainId = webadminBridge.getDomainId(domainName);
	}
	
	public void genEmails(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, ArrayList<SimpleAccount> recipients){
		MailGen gen = new MailGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, recipients);
	}
	
	public void genContacts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		ContactGen gen = new ContactGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue);
	}
	
	public void genEvents(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange, ArrayList<SimpleAccount> attendees){
		EventGen gen = new EventGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue, dateRange, attendees);
	}
	
	public void genTasks(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange){
		TaskGen gen = new TaskGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, dateRange);
	}
	
	public void genNotes(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		NoteGen gen = new NoteGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue);
	}
	
	public void genAccounts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, String domainName){
		AccountGen gen = new AccountGen(webadminBridge);
		gen.genenerateAndSave(count, nationalChar, domainName, checkboxes);
	}
	
	public void logout(){
		webadminBridge.logout();
	}
	
}
