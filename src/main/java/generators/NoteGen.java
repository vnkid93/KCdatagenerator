package generators;

import java.util.HashMap;
import java.util.UUID;

import bridge.EwsBridge;
import engine.Constants;
import engine.Engine;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.MimeContent;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;

public class NoteGen extends BasicItemGenerator {

	ExtendedPropertyDefinition colorProp, widthProp, heightProp, leftProp, topProp;

	public NoteGen(EwsBridge bridge) {
		super(bridge);
		initNoteProperties();
	}

	public EmailMessage[] generate(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider){
		EmailMessage[] notes = new EmailMessage[count];
		tGen.setNationalChar(nationalChar);
		boolean subjectExist = checkboxes.get(Constants.SUBJECT);
		boolean htmlCode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		boolean randomColor = checkboxes.get(Constants.COLOR);
		int contentSize = slider.get(Constants.CONTENT_SIZE);
		
		for (int i = 0; i < count; i++) {
			String subject = (subjectExist) ? tGen.genSubject() : "";
			StringBuilder content = new StringBuilder();
			content.append(tGen.genParagraph(contentSize));
			if(htmlCode){
				content.append(Constants.NEW_LINE);
				content.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				content.append(Constants.NEW_LINE);
				content.append(Constants.LINK_TO_APPEND);
			}
			int colorValue = (randomColor) ? rand.nextInt(Constants.NOTES_COLOR_COUNT) : 0;
			
			try {
				notes[i] = createNote(subject, content.toString(), colorValue);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return notes;
	}

	
	public void generateAndSend(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider){
		tGen.setNationalChar(nationalChar);
		boolean subjectExist = checkboxes.get(Constants.SUBJECT);
		boolean htmlCode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		boolean randomColor = checkboxes.get(Constants.COLOR);
		int contentSize = slider.get(Constants.CONTENT_SIZE);
		
		for (int i = 0; i < count; i++) {
			String subject = (subjectExist) ? tGen.genSubject() : "";
			StringBuilder content = new StringBuilder();
			content.append(tGen.genParagraph(contentSize));
			if(htmlCode){
				content.append(Constants.NEW_LINE);
				content.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				content.append(Constants.NEW_LINE);
				content.append(Constants.LINK_TO_APPEND);
			}
			int colorValue = (randomColor) ? rand.nextInt(Constants.NOTES_COLOR_COUNT) : 0;
			try {
				createNote(subject, content.toString(), colorValue).save(WellKnownFolderName.Notes);
			} catch (ServiceRequestException e) {
				/*
				 * Expected exception. PostItem is not match EmailMessage. Does not
				 * have any effect - it works well.
				 */
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
	}

	/**
	 * Method that create note with specific content and properties.
	 * @param subject Note subject
	 * @param content Note content (text under subject)
	 * @param colorValue int value of color. It is ranged from 0 to {@code Engine.NOTES_COLOR_COUNT}
	 * @throws Exception 
	 */
	public EmailMessage createNote(String subject, String content, int colorValue) throws Exception {
		EmailMessage note = null;
		note = new EmailMessage(service);
		note.setItemClass("IPM.StickyNote");

		note.setExtendedProperty(colorProp, colorValue);
		/*
		 * these properties are not necessary, but it can be needed for KOFF
		 * or other clients that have floating sticky notes
		 */
		note.setExtendedProperty(widthProp, 200);
		note.setExtendedProperty(heightProp, 166);
		note.setExtendedProperty(leftProp, 200);
		note.setExtendedProperty(topProp, 200);

		note.setSubject(subject);
		note.setBody(new MessageBody(BodyType.Text, content));
		note.setMimeContent(new MimeContent(Engine.CHARSET, content.getBytes()));

		return note;
	}

	/**
	 * it call the other createNote method. This method is used for randomly
	 * colored note.
	 * 
	 * @param subject Note subject
	 * @param content Note content (text under subject)
	 * @throws Exception 
	 */
	public void createNote(String subject, String content) throws Exception {
		createNote(subject, content, rand.nextInt(Constants.NOTES_COLOR_COUNT));
	}

	/**
	 * The note init properties. It need to be called before the note is
	 * created.
	 */
	private void initNoteProperties() {
		UUID uuid = UUID.fromString("0006200E-0000-0000-C000-000000000046");
		colorProp = new ExtendedPropertyDefinition(uuid, 0x8B00, MapiPropertyType.Integer);
		widthProp = new ExtendedPropertyDefinition(uuid, 0x8B02, MapiPropertyType.Integer);
		heightProp = new ExtendedPropertyDefinition(uuid, 0x8B03, MapiPropertyType.Integer);
		leftProp = new ExtendedPropertyDefinition(uuid, 0x8B04, MapiPropertyType.Integer);
		topProp = new ExtendedPropertyDefinition(uuid, 0x8B05, MapiPropertyType.Integer);
	}

}
