package generators;


import java.util.UUID;

import javax.mail.internet.MimeMessage;

import bridge.EwsBridge;
import bridge.KCBridge;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.DefaultExtendedPropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.PostItem;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.MimeContent;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;

public class NoteGen{
	private EwsBridge bridge;
	private ExchangeService service;
	
	public NoteGen(EwsBridge bridge){
		this.bridge = bridge;
		service = bridge.getService();
		
		
	}
	
	public void createNote(){
	
		try {
			EmailMessage mess = new EmailMessage(service);
			mess.setItemClass("IPM.StickyNote");
			UUID uuid = UUID.fromString("0006200E-0000-0000-C000-000000000046");
			MapiPropertyType type = MapiPropertyType.Integer;
			mess.setExtendedProperty(new ExtendedPropertyDefinition(uuid, 0x8B00, type), 1);
			mess.setExtendedProperty(new ExtendedPropertyDefinition(uuid, 0x8B02, type), 200);
			mess.setExtendedProperty(new ExtendedPropertyDefinition(uuid, 0x8B03, type), 166);
			mess.setExtendedProperty(new ExtendedPropertyDefinition(uuid, 0x8B04, type), 200);
			mess.setExtendedProperty(new ExtendedPropertyDefinition(uuid, 0x8B05, type), 200);
			mess.setSubject("Oh yeah");
			mess.setMimeContent(new MimeContent("utf-8", "xxxxxxx".getBytes()));
			//mess.save(WellKnownFolderName.Notes);
			
			//MimeMessage mime = new 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
