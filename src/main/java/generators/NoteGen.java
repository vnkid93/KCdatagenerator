package generators;


import java.util.UUID;

import bridge.EwsBridge;
import engine.Engine;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.MimeContent;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;

public class NoteGen extends BasicItemGenerator{
	
	ExtendedPropertyDefinition colorProp, widthProp, heightProp, leftProp, topProp;
	
	public NoteGen(EwsBridge bridge){
		super(bridge);
		initNoteProperties();
	}
	
	/**
	 * Method that create note with specific content and properties.
	 * @param subject Note subject
	 * @param content Note content (text under subject)
	 * @param colorValue int value of color. It is ranged from 0 to {@code Engine.NOTES_COLOR_COUNT}
	 */
	public void createNote(String subject, String content, int colorValue){
		try {
			EmailMessage note = new EmailMessage(service);
			note.setItemClass("IPM.StickyNote");
			
			note.setExtendedProperty(colorProp, colorValue);
			/* these properties are not necessary, 
			 * but it can be needed for KOFF or other clients that have floating sticky notes
			 */
			note.setExtendedProperty(widthProp, 200); 
			note.setExtendedProperty(heightProp, 166);
			note.setExtendedProperty(leftProp, 200);
			note.setExtendedProperty(topProp, 200);
			
			note.setSubject(subject);
			note.setBody(new MessageBody(BodyType.Text, content));
			note.setMimeContent(new MimeContent(Engine.CHARSET, content.getBytes()));

			note.save(WellKnownFolderName.Notes);		
		} catch(ServiceRequestException e){
			/*
			 * Expected exception. PostItem is not match EmailMessage.
			 * Does not have any effect - it works well.
			 */
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * it call the other createNote method. This method is used for randomly colored note.
	 * @param subject Note subject
	 * @param content Note content (text under subject)
	 */
	public void createNote(String subject, String content){
		createNote(subject, content, rand.nextInt(Engine.NOTES_COLOR_COUNT));
	}
	
	/**
	 * The note init properties. It need to be called before the note is created.
	 */
	private void initNoteProperties(){
		UUID uuid = UUID.fromString("0006200E-0000-0000-C000-000000000046");
		colorProp 	= new ExtendedPropertyDefinition(uuid, 0x8B00, MapiPropertyType.Integer);
		widthProp 	= new ExtendedPropertyDefinition(uuid, 0x8B02, MapiPropertyType.Integer);
		heightProp 	= new ExtendedPropertyDefinition(uuid, 0x8B03, MapiPropertyType.Integer);
		leftProp	= new ExtendedPropertyDefinition(uuid, 0x8B04, MapiPropertyType.Integer);
		topProp 	= new ExtendedPropertyDefinition(uuid, 0x8B05, MapiPropertyType.Integer);
	}
	
	
	
}
