package main;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bridge.EwsBridge;
import engine.SimpleAccount;
import generators.MailGen;
import generators.TextGen;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

public class Main {
	
	private EwsBridge bridge;
	private ExchangeService service;
	
	public Main(){
		bridge = new EwsBridge("test@tony.lab", "a");
		bridge.setHostAndUrl("192.168.65.110");
		service = bridge.getService();
		
		
		//KCBridge client = new KCBridge();
		//client.login("https://vm-ubuntu16-fresh:4040", "admin", "a");
		MailGen m = new MailGen(bridge);
		//m.sendEmail(m.createEmail("Z Javy", "Toto je text", new String[]{"test03@tony.lab"}));
		
		SimpleAccount acc1 = new SimpleAccount("Jan Tester", "test@tony.lab");
		SimpleAccount acc2 = new SimpleAccount("Bart Simpson", "test03@tony.lab");
		//m.sendEmail(m.createEmail("z Javy", "ahojjjjjjjky", new SimpleAccount[]{acc1, acc2}));
		
		try {
			EmailMessage msg = m.createEmail(TextGen.getInstance().genSentence(false, 1), 
					TextGen.getInstance().genSentence(false, 3), 
					new SimpleAccount[]{acc1, acc1}, false, false);
			msg.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Random r = new Random();
		for (int i = 0; i < 20; i++) {
			System.out.println(TextGen.getInstance().genTelNumber());
		}*/
		
		System.out.println();
		
	
	}
	
	
	
	public static void main(String[] args) {
		new Main();		
	}
}
