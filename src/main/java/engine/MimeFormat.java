package engine;

public class MimeFormat {
	
	public static String createMimeFormat(SimpleAccount sender, SimpleAccount[] recipients,
									String subject, String body){
		StringBuilder builder = new StringBuilder();
		builder.append("Mime-Version: 1.0\r\nFrom: ");
		
		builder.append(sender.fullName +"<"+ sender.emailAddress +">\r\nTo: ");
		
		if(recipients.length > 0){
			builder.append(recipients[0].fullName+"<"+recipients[0].emailAddress+">");
			for (int i = 1; i < recipients.length; i++) {
				builder.append(", "+recipients[i].fullName+"<"+recipients[i].emailAddress+">");
			}
		}
		
		builder.append("\r\n"+subject+"\r\n");
		
		builder.append("Date: Tue, 15 Feb 2011 22:06:21 -0000\r\n");
		builder.append("Message-ID: <{0}>\r\n");
		builder.append("X-Experimental: some value\r\n\r\n");
		
		builder.append(body);
		
		System.out.println(builder);
		return builder.toString();
	}
}
