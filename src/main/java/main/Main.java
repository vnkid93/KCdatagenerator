package main;





import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bridge.EwsBridge;
import engine.SimpleAccount;
import engine.TimeInput;
import generators.EventGen;
import generators.MailGen;
import generators.TaskGen;
import microsoft.exchange.webservices.data.core.ExchangeService;
import textgenerators.ITextGenerator;
import textgenerators.TextGen;

public class Main {
	
	private EwsBridge bridge;
	private ExchangeService service;
	
	public Main(){
		long time = System.currentTimeMillis();
		bridge = new EwsBridge("test@tony.lab", "a");
		bridge.setHostAndUrl("192.168.65.110");
		service = bridge.getService();
		
		
		//KCBridge client = new KCBridge();
		//client.login("https://vm-ubuntu16-fresh:4040", "admin", "a");
		MailGen m = new MailGen(bridge);
		//m.sendEmail(m.createEmail("Z Javy", "Toto je text", new String[]{"test03@tony.lab"}));
		
		SimpleAccount acc1 = new SimpleAccount("Jan Tester", "test@tony.lab");
		SimpleAccount acc2 = new SimpleAccount("Bart Simpson", "test03@tony.lab");
		ITextGenerator textGen = new TextGen();
		
		
		EventGen gen = new EventGen(bridge);
		List<SimpleAccount> attendees = new ArrayList<SimpleAccount>();
		attendees.add(acc2);
		//gen.createEvent("My event", attendees, new TimeInput[]{new TimeInput(19,11,2016), new TimeInput(20,11,2016)});
		gen.createEvent(textGen.genSubject(true), attendees, 
				new TimeInput[]{new TimeInput(19,11,2016), new TimeInput(20,11,2016)},
				false, true);
		//new TimeInput[]{new TimeInput(TimeInput.getRandomDayBefore(new Date(),5)), new TimeInput(TimeInput.getRandomDayAfter(new Date(),5))}
		System.out.println(System.currentTimeMillis() - time);
	
	}
	
	
	
	public static void main(String[] args) {
		new Main();		
	}
}
