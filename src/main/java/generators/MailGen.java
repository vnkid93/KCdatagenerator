package generators;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

import bridge.EwsBridge;
import engine.Engine;
import engine.SimpleAccount;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MimeContent;

public class MailGen {
	private ExchangeService service;
	private EwsBridge bridge;
	
	public MailGen(EwsBridge bridge){
		this.bridge = bridge;
		this.service = bridge.getService();
	}
	
	/**
	 * Creating single email.
	 * @param subject
	 * @param body
	 * @param recipients
	 * @return
	 * @throws Exception
	 */
	public EmailMessage createEmail(String subject, String body, SimpleAccount[] recipients, boolean flag, boolean highPriority) throws Exception{
		EmailMessage msg = null;
		final int USER_FROM = 0, USER_TO = 1;
		
		HtmlEmail email = new HtmlEmail();
		email.setCharset(Engine.CHARSET); // not sure if it is necessary
		email.setHostName(bridge.getHost());
		email.setFrom(recipients[USER_FROM].emailAddress, recipients[USER_FROM].fullName);
		email.addTo(recipients[USER_TO].emailAddress, recipients[USER_TO].fullName);
		email.setSubject(subject);
		
		email.setTextMsg(body);
		
		StringBuilder str = new StringBuilder();
		str.append("<font face=\"");

		for (int i = 0; i < Engine.FONT_FAMILIES.length - 1 ; i++) {
			str.append(Engine.FONT_FAMILIES[i]+",");
		}
		str.append(Engine.FONT_FAMILIES[Engine.FONT_FAMILIES.length - 1]);	// avoiding last comma
		str.append("\">" + body + "</font>");
		
		email.setHtmlMsg(str.toString());
				
		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Mailer", Engine.APP_NAME);
		if(highPriority){			
			header.put("X-Priority", "1");
		}
		email.setHeaders(header);
		
		
		email.buildMimeMessage();
		
		
		msg = new EmailMessage(service);
		msg.setMimeContent(new MimeContent(Engine.CHARSET, serialize(email)));
		
		return msg;
	}
	
	/**
	 * Generating an array of email that fits the count.
	 * @param subject
	 * @param body
	 * @param recipients
	 * @param count
	 * @return array of email
	 * @throws Exception
	 */
	public EmailMessage[] genEmails(String subject, String body, SimpleAccount[] recipients, int count) throws Exception{
		EmailMessage [] msgs = new EmailMessage[count];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = createEmail(subject, body, recipients, false, false);
		}
		return msgs;
	}
	
	
	/**
	 * Serialization. It is used for casting email to array of bytes.
	 * @param obj Object that will be casted to byte array
	 * @return the serialization result - byte array
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	private byte[] serialize(MultiPartEmail email) throws IOException, MessagingException{
		ByteArrayOutputStream byteOut =  new ByteArrayOutputStream();
		email.getMimeMessage().writeTo(byteOut);
		return byteOut.toByteArray();
	}

}
