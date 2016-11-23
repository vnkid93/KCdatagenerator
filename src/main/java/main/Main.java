package main;

import bridge.EwsBridge;
import engine.SimpleAccount;
import generators.ContactGen;
import generators.MailGen;
import microsoft.exchange.webservices.data.core.ExchangeService;

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
