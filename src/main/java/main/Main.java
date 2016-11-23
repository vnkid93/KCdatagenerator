package main;


import bridge.EwsBridge;
import bridge.KCBridge;
import engine.SimpleAccount;
import generators.AccountGen;
import generators.ContactGen;
import generators.EventGen;
import generators.MailGen;
import generators.NoteGen;
import generators.TaskGen;
import microsoft.exchange.webservices.data.core.ExchangeService;

public class Main {
	
	private EwsBridge bridge;
	private ExchangeService service;
	// gnerators
	private AccountGen accountGen;
	private ContactGen contactGen;
	private NoteGen noteGen;
	private MailGen mailGen;
	private TaskGen taskGen;
	private EventGen eventGen;
	
	public Main(){
		long time = System.currentTimeMillis();
		bridge = new EwsBridge("test@tony.lab", "a");
		bridge.setHostAndUrl("192.168.65.110");
		service = bridge.getService();
		
		MailGen m = new MailGen(bridge);
		//m.sendEmail(m.createEmail("Z Javy", "Toto je text", new String[]{"test03@tony.lab"}));
		
		SimpleAccount acc1 = new SimpleAccount("Jan Tester", "test@tony.lab");
		SimpleAccount acc2 = new SimpleAccount("Bart Simpson", "test03@tony.lab");
		
		
		KCBridge client = new KCBridge();
		client.login("https://192.168.65.110:4040", "admin", "a");
		
		AccountGen gen = new AccountGen(client);
		gen.genUsers(5, true, client.getDomainId("tony.lab"));
		
		
		client.logout();
		System.out.println(System.currentTimeMillis() - time);
	}
	
	
	
	public static void main(String[] args) {
		new Main();		
	}
}
