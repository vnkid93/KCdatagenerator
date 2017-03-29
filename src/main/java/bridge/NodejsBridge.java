package bridge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//import org.apache.log4j.Logger;

import engine.SimpleAccount;
import generators.AccountGen;
import generators.ContactGen;
import generators.EventGen;
import generators.MailGen;
import generators.NoteGen;
import generators.TaskGen;

public class NodejsBridge {
	//static final Logger logger = Logger.getLogger(NodejsBridge.class);
	
	private KCBridge webadminBridge;
	private EwsBridge clientBridge;
	private String domainId;
	private String host;
	
	final static String PORT = "4040";
	
	public NodejsBridge(){
		//logger.debug("Initializing");
		webadminBridge = null;
		clientBridge = null;
		domainId = null;
		host = null;
	}
	
	public NodejsBridge(String host, String adminLogin, String adminPass, String clientLogin, String clientPass){
		//logger.debug("Start initializing");
		webadminBridge = new KCBridge();
		clientBridge = new EwsBridge(clientLogin, clientPass);
		clientBridge.setHostAndUrl(host);
		clientBridge.setFullName(webadminBridge.getFullName(clientLogin));
		//logger.debug("End initializing");
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public boolean logAdmin(String user, String pass){
		//logger.debug("Log in to Webadmin: "+user);
		if(webadminBridge != null){
			webadminBridge.logout();
		}else{			
			webadminBridge = new KCBridge();
		}
		return webadminBridge.loginWithReturn("https://" + this.host + ":"+ PORT, user, pass);
	}
	
	public boolean logClient(String user, String pass){
		//logger.debug("Log in to Webclient");
		if(clientBridge == null){
			clientBridge = new EwsBridge(user, pass);
		}else{
			clientBridge.setCredentials(user, pass);
		}
		clientBridge.setHostAndUrl(this.host);
				
		String fullName = (webadminBridge != null) ? webadminBridge.getFullName(user) : null;
		if(fullName == null){
			return false;
		}
		clientBridge.setFullName(fullName);
		return true;
	}
	
	public boolean loginToAll(String host, String adminLogin, String adminPass, String userLogin, String userPass){
		boolean result = false;
		this.host = host;
		if(logAdmin(adminLogin, adminPass) && logClient(userLogin, userPass)){
			result = true;
		}
		return result;
	}
	
	public boolean setDomainId(String domainName){
		//logger.debug("Setting domain id by name: "+domainName);
		boolean result = true;
		try {
			this.domainId = webadminBridge.getDomainId(domainName);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public void genEmails(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, ArrayList<SimpleAccount> recipients){
		//logger.debug("Starting generating emails");
		MailGen gen = new MailGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, recipients);
	}
	
	public void genContacts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		//logger.debug("Starting generating contacts");
		ContactGen gen = new ContactGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue);
	}
	
	public void genEvents(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange, ArrayList<SimpleAccount> attendees){
		//logger.debug("Starting generating events");
		EventGen gen = new EventGen(clientBridge);
		gen.generateAndSave(count, nationalChar, checkboxes, sliderValue, dateRange, attendees);
	}
	
	public void genTasks(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue, Date[] dateRange){
		//logger.debug("Starting generating tasks");
		TaskGen gen = new TaskGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue, dateRange);
	}
	
	public void genNotes(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> sliderValue){
		//logger.debug("Starting generating notes");
		NoteGen gen = new NoteGen(clientBridge);
		gen.generateAndSend(count, nationalChar, checkboxes, sliderValue);
	}
	
	public boolean genAccounts(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, String domainName){
		//logger.debug("Starting generating accounts");
		AccountGen gen = new AccountGen(webadminBridge);
		return gen.genenerateAndSave(count, nationalChar, domainName, checkboxes);
	}
	
	public void logout(){
		//logger.debug("Logging out");
		webadminBridge.logout();
	}
	
}
