package bridge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import engine.SimpleAccount;
import generators.AccountGen;
import generators.ContactGen;
import generators.EventGen;
import generators.MailGen;
import generators.NoteGen;
import generators.TaskGen;

public class NodejsBridge {
	static final Logger logger = Logger.getLogger(NodejsBridge.class);
	
	private KCBridge webadminBridge;
	private EwsBridge clientBridge;
	private String domainId;
	private String host;
	
	final static String PORT = "4040";
	
	public NodejsBridge(){
		logger.debug("Initializing");
		webadminBridge = null;
		clientBridge = null;
		domainId = null;
		host = null;
	}
	
	public NodejsBridge(String host, String adminLogin, String adminPass, String clientLogin, String clientPass){
		logger.debug("Start initializing");
		webadminBridge = new KCBridge();
		webadminBridge.login("https://" + host+ ":"+ PORT, adminLogin, adminPass);
		clientBridge = new EwsBridge(clientLogin, clientPass);
		clientBridge.setHostAndUrl(host);
		clientBridge.setFullName(webadminBridge.getFullName(clientLogin));
		logger.debug("End initializing");
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public void logAdmin(String user, String pass){
		logger.debug("Log in to Webadmin: "+user);
		if(webadminBridge != null){
			webadminBridge.logout();
		}else{			
			webadminBridge = new KCBridge();
		}
		webadminBridge.login("https://" + this.host + ":"+ PORT, user, pass);
	}
	
	public void logClient(String user, String pass){
		logger.debug("Log in to Webclient");
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
		logger.debug("Setting domain id by name: "+domainName);
		this.domainId = webadminBridge.getDomainId(domainName);
	}
	
	public void genEmails(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, ArrayList<SimpleAccount> recipients){
		logger.debug("Starting generating emails");
		MailGen gen = new MailGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, recipients);
	}
	
	public void genContacts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		logger.debug("Starting generating contacts");
		ContactGen gen = new ContactGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue);
	}
	
	public void genEvents(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange, ArrayList<SimpleAccount> attendees){
		logger.debug("Starting generating events");
		EventGen gen = new EventGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue, dateRange, attendees);
	}
	
	public void genTasks(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange){
		logger.debug("Starting generating tasks");
		TaskGen gen = new TaskGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, dateRange);
	}
	
	public void genNotes(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		logger.debug("Starting generating notes");
		NoteGen gen = new NoteGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue);
	}
	
	public void genAccounts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, String domainName){
		logger.debug("Starting generating accounts");
		AccountGen gen = new AccountGen(webadminBridge);
		gen.genenerateAndSave(count, nationalChar, domainName, checkboxes);
	}
	
	public void logout(){
		logger.debug("Logging out");
		webadminBridge.logout();
	}
	
}
