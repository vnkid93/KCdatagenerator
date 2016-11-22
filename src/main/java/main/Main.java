package main;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bridge.EwsBridge;
import engine.SimpleAccount;
import engine.TimeInput;
import generators.ContactGen;
import generators.ContactGroupGen;
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
		ITextGenerator textGen = TextGen.getInstance();
		
		ContactGen gen = new ContactGen(bridge);
		for (int i = 0; i < 500; i++) {
			gen.createContact(true);			
		}
		
		System.out.println(System.currentTimeMillis() - time);
	}
	
	
	
	public static void main(String[] args) {
		new Main();		
	}
}
