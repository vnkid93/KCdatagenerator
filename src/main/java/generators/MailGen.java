package generators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

import bridge.EwsBridge;
import engine.Constants;
import engine.Engine;
import engine.Probability;
import engine.SimpleAccount;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MimeContent;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;

public class MailGen extends BasicItemGenerator {

	public MailGen(EwsBridge bridge) {
		super(bridge);
	}
	/**
	 * 
	 * @param count
	 * @param nationalChar
	 * @param checkboxes
	 * @param sliderValue
	 * @param recipients
	 */
	public void generateAndSend(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes,
			HashMap<String, Integer> sliderValue, ArrayList<SimpleAccount> recipients) {

		Probability probability = Probability.getInstance();
		tGen.setNationalChar(nationalChar);
		int contentSize = sliderValue.get(Constants.CONTENT_SIZE);
		int flagProb = sliderValue.get(Constants.FLAG_PROB);
		int highPriProb = sliderValue.get(Constants.HIGH_PRIORITY);
		int attachmentProb = sliderValue.get(Constants.ATTACHMENT_PROB);
		int inlineImgProb = sliderValue.get(Constants.INLINE_IMG_PROB);

		boolean readingConfirm = checkboxes.get(Constants.READCONFIRM);
		boolean deliveryConfirm = checkboxes.get(Constants.DELIVERY);
		boolean subject = checkboxes.get(Constants.SUBJECT);
		boolean htmlCode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		for (int i = 0; i < count; i++) {
			String subjectText = (subject) ? tGen.genSubject() : "";
			StringBuilder body = new StringBuilder();
			String priority = toPriority(probability.tryLuck(highPriProb));
			boolean attachment = probability.tryLuck(attachmentProb);
			boolean inlineImg = probability.tryLuck(inlineImgProb);
			body.append(tGen.genParagraph(contentSize));
			if (htmlCode) {
				body.append(Constants.NEW_LINE);
				body.append(Constants.HTML_CODE_TO_APPEND);
			}
			if (link) {
				body.append(Constants.NEW_LINE);
				body.append(Constants.LINK_TO_APPEND);
			}
			try {
				if(attachment){
					createEmailWithAttachment(subjectText, body.toString(), recipients, probability.tryLuck(flagProb), priority, inlineImg, readingConfirm, deliveryConfirm)
					.send();
				}else{
					createEmail(subjectText, body.toString(), recipients, probability.tryLuck(flagProb),
							priority , inlineImg, readingConfirm, deliveryConfirm)
					.send();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	
	public EmailMessage createEmail(String subject, String body, ArrayList<SimpleAccount> recipients) throws Exception {
		String priority = (Probability.getInstance().tryLuck(5)) ? "1" : "3";
		return createEmail(subject, body, recipients, Probability.getInstance().tryLuck(10), priority, false, false, false);
	}

	/**
	 * 
	 * @param subject
	 * @param body
	 * @param recipients
	 * @param flag
	 * @param priority
	 * @param attachment
	 * @param inlineImg
	 * @param readingConfirm
	 * @param deliveryConfirm
	 * @return
	 * @throws Exception
	 */
	public EmailMessage createEmail(String subject, String body, ArrayList<SimpleAccount> recipients, boolean flag,
			String priority, boolean inlineImg, boolean readingConfirm, boolean deliveryConfirm) throws Exception {
		EmailMessage msg = null;
		HtmlEmail email = new HtmlEmail();
		
		
		email.setCharset(Engine.CHARSET); // not sure if it is necessary
		email.setHostName(bridge.getHost());
		email.setFrom(bridge.getEmail(), bridge.getFullName());
		for (SimpleAccount simpleAccount : recipients) {
			//email.addTo(simpleAccount.emailAddress, simpleAccount.fullName);
			email.addTo(simpleAccount.emailAddress);
		}
		email.setSubject(tGen.genSubject());

		email.setTextMsg(body);

		StringBuilder str = new StringBuilder();
		str.append("<font face=\"");

		for (int i = 0; i < Engine.FONT_FAMILIES.length - 1; i++) {
			str.append(Engine.FONT_FAMILIES[i] + ",");
		}
		str.append(Engine.FONT_FAMILIES[Engine.FONT_FAMILIES.length - 1]); // avoiding
																			// last
																			// comma
		str.append("\">" + body + "</font>");

		email.setHtmlMsg(str.toString());

		
		// if(inlineImg){}
		// if(flag){}
		// if(readingConfirm){}
		// if(deliveryConfirm){}

		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Mailer", Engine.APP_NAME);
		header.put("X-Priority", priority);
		email.setHeaders(header);

		email.buildMimeMessage();

		msg = new EmailMessage(service);
		
		msg.setMimeContent(new MimeContent(Engine.CHARSET, serialize(email)));
		
		System.out.println("flaggg");

		return msg;
	}
	
	/**
	 * 
	 * @param subject
	 * @param body
	 * @param recipients
	 * @param flag
	 * @param priority
	 * @param attachment
	 * @param inlineImg
	 * @param readingConfirm
	 * @param deliveryConfirm
	 * @return
	 * @throws Exception
	 */
	public EmailMessage createEmailWithAttachment(String subject, String body, ArrayList<SimpleAccount> recipients, boolean flag,
			String priority, boolean inlineImg, boolean readingConfirm, boolean deliveryConfirm) throws Exception {
		EmailMessage msg = null;
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(Constants.ATTACHMENT_PATH);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("A simple text file");
		attachment.setName("Attachment");

		
		MultiPartEmail email = new MultiPartEmail();
		
		
		email.setCharset(Engine.CHARSET); // not sure if it is necessary
		email.setHostName(bridge.getHost());
		email.setFrom(bridge.getEmail(), bridge.getFullName());
		for (SimpleAccount simpleAccount : recipients) {
			//email.addTo(simpleAccount.emailAddress, simpleAccount.fullName);
			email.addTo(simpleAccount.emailAddress);
		}
		email.setSubject(tGen.genSubject());
		email.setMsg(body);
		
		// if(inlineImg){}
		// if(flag){}
		// if(readingConfirm){}
		// if(deliveryConfirm){}

		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Mailer", Engine.APP_NAME);
		header.put("X-Priority", priority);
		email.setHeaders(header);

		email.buildMimeMessage();
		email.attach(attachment);

		msg = new EmailMessage(service);
		msg.setMimeContent(new MimeContent(Engine.CHARSET, serialize(email)));
		
		return msg;
	}
	

	public String toPriority(boolean high) {
		return (high) ? "1" : "3";
	}
	
	private void setFlag(EmailMessage msg) throws Exception{
		msg.setExtendedProperty(new ExtendedPropertyDefinition(0x1090, MapiPropertyType.Integer), 2);
		msg.setExtendedProperty(new ExtendedPropertyDefinition(0x0E2B, MapiPropertyType.Integer), 1);
	}

	/**
	 * Serialization. It is used for casting email to array of bytes.
	 * 
	 * @param obj
	 *            Object that will be casted to byte array
	 * @return the serialization result - byte array
	 * @throws IOException
	 * @throws MessagingException
	 */
	private byte[] serialize(Email email) throws IOException, MessagingException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		email.getMimeMessage().writeTo(byteOut);
		return byteOut.toByteArray();
	}

}
