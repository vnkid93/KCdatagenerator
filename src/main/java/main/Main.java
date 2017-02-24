package main;


import java.util.ArrayList;
import java.util.HashMap;

import bridge.EwsBridge;
import bridge.NodejsBridge;
import engine.Constants;
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
		
		NodejsBridge br = new NodejsBridge("192.168.65.110", "admin", "a", "test@tony.lab", "a");
		HashMap<String, Boolean> checkboxes = new HashMap<String, Boolean>();
		checkboxes.put(Constants.HTMLCODE, false);
		checkboxes.put(Constants.LINKS, false);
		checkboxes.put(Constants.SUBJECT, true);
		checkboxes.put(Constants.COLOR, true);
		
		HashMap<String, Integer> sliderValues = new HashMap<String, Integer>();
		sliderValues.put(Constants.CONTENT_SIZE, 3);
		sliderValues.put(Constants.FLAG_PROB, 10);
		sliderValues.put(Constants.HIGH_PRIORITY, 10);
		sliderValues.put(Constants.ATTACHMENT_PROB, 90);
		sliderValues.put(Constants.INLINE_IMG_PROB, 0);
		
		checkboxes.put(Constants.READCONFIRM, false);
		checkboxes.put(Constants.DELIVERY, false);
		checkboxes.put(Constants.SUBJECT, true);
		checkboxes.put(Constants.HTMLCODE, false);
		checkboxes.put(Constants.LINKS, false);
		ArrayList<SimpleAccount> rep = new ArrayList<SimpleAccount>();
		rep.add(new SimpleAccount("test@tony.lab"));
		br.genEmails(10, true, checkboxes, sliderValues, rep);
		
		//br.genNotes(15, true, checkboxes, sliderValues);
		
		
		
		/*TextGen tGen = TextGen.getInstance();
		KCBridge client = new KCBridge();
		client.login("https://192.168.65.110:4040", "admin", "a");
		
		
		long time = System.currentTimeMillis();
		bridge = new EwsBridge("test@tony.lab", "a");
		bridge.setHostAndUrl("192.168.65.110");
		bridge.setFullName(client.getFullName(bridge.getEmail()));
		
		service = bridge.getService();
		
		accountGen = new AccountGen(client);
		contactGen = new ContactGen(bridge);
		noteGen = new NoteGen(bridge);
		mailGen = new MailGen(bridge);
		taskGen = new TaskGen(bridge);
		eventGen = new EventGen(bridge);

		
		MailGen m = new MailGen(bridge);
		//m.sendEmail(m.createEmail("Z Javy", "Toto je text", new String[]{"test03@tony.lab"}));
		
		SimpleAccount acc1 = new SimpleAccount("Jan Tester", "test@tony.lab");
		SimpleAccount acc2 = new SimpleAccount("Bart Simpson", "test03@tony.lab");
		
		HashMap<String, Boolean> checkboxes = new HashMap<String, Boolean>();
		checkboxes.put(Constants.READCONFIRM, false);
		checkboxes.put(Constants.DELIVERY, false);
		checkboxes.put(Constants.SUBJECT, false);
		checkboxes.put(Constants.HTMLCODE, false);
		checkboxes.put(Constants.LINKS, false);
		
		HashMap<String, Integer> sliderValues = new HashMap<String, Integer>();
		sliderValues.put(Constants.FLAG_PROB, 5);
		sliderValues.put(Constants.HIGH_PRIORITY, 5);
		sliderValues.put(Constants.ATTACHMENT_PROB, 0);
		sliderValues.put(Constants.CONTENT_SIZE, 3);
		sliderValues.put(Constants.INLINE_IMG_PROB, 0);
		
		m.generateAndSend(2, false, checkboxes, sliderValues, new SimpleAccount[]{acc1, acc2});*/

		
		
		/*for (int i = 0; i < 1; i++) {
			contactGen.createContact(true);
			noteGen.createNote(tGen.genSubject(), tGen.genParagraph(4));
			try {
				mailGen.createEmail(tGen.genSubject(), tGen.genShortContent(), new SimpleAccount[]{acc1, acc1});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		//taskGen.generateTasks(500);
		
		
		/*AccountGen gen = new AccountGen(client);
		gen.genUsers(5, true, client.getDomainId("tony.lab"));*/
		
		
		
		
		//client.logout();
		//System.out.println(System.currentTimeMillis() - time);
	}
	
	
	
	public static void main(String[] args) {
		new Main();		
	}
}
