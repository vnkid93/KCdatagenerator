package engine;

import microsoft.exchange.webservices.data.core.enumeration.property.EmailAddressKey;
import microsoft.exchange.webservices.data.core.enumeration.property.PhoneNumberKey;
import microsoft.exchange.webservices.data.core.enumeration.property.PhysicalAddressKey;

public class Constants {

	/******************************* CONTACTS *****************************/
	public static final int PHONE_INDEX_MOBILE 		= 0;
	public static final int PHONE_INDEX_BUSINESS 	= 1;
	public static final int PHONE_INDEX_HOME		= 2;
	public static final int PHONE_INDEX_BUSINESS_FAX= 3;
	public static final int PHONE_INDEX_OTHER 		= 4;
	public static final PhoneNumberKey[] PHONE_INDEXES = {	PhoneNumberKey.MobilePhone, PhoneNumberKey.BusinessPhone, 
															PhoneNumberKey.HomePhone, PhoneNumberKey.BusinessFax, 
															PhoneNumberKey.OtherTelephone};
	
	public static final int EMAIL_INDEX_1 		= 0;
	public static final int EMAIL_INDEX_2 		= 1;
	public static final int EMAIL_INDEX_3 		= 2;
	public static final EmailAddressKey[] EMAIL_INDEXES = {	EmailAddressKey.EmailAddress1, EmailAddressKey.EmailAddress2,
															EmailAddressKey.EmailAddress3};
	
	public static final int ADDRESS_INDEX_HOME		= 0;
	public static final int ADDRESS_INDEX_BUSINESS	= 1;
	public static final int ADDRESS_INDEX_OTHER		= 2;
	public static final	PhysicalAddressKey[] ADDRESS_INDEXES = {PhysicalAddressKey.Home, PhysicalAddressKey.Business, 
															PhysicalAddressKey.Other};
	
	
	public static final String DEFAULT_TIME_FORMAT = "d/M/yyyy";
	
	/* EVENTS */
	public static final int EVENT_DURATION_START_INDEX = 0;
	public static final int EVENT_DURATION_END_INDEX = 1;
	
	public static final String AVATAR_IMG_MALE = "src/main/resources/img/male-icon.gif";
	public static final String AVATAR_IMG_FEMALE = "src/main/resources/img/female-icon.gif";
	public static final String ATTACHMENT_PATH	= "src/main/resources/attachment.txt";
	
	public static final String CONTENT_SIZE		= "CONTENT_SIZE";
	public static final String FLAG_PROB		= "FLAG_PROB";
	public static final String HIGH_PRIORITY	= "HIGH_PRIORITY";
	public static final String ATTACHMENT_PROB	= "ATTACHMENT_PROB";
	public static final String INLINE_IMG_PROB	= "INLINE_IMG_PROB";
	public static final String SUBJECT 			= "SUBJECT";
	public static final String DELIVERY 		= "DELIVERY";
	public static final String READCONFIRM 		= "READCONFIRM";
	public static final String HTMLCODE 		= "HTMLCODE";
	public static final String LINKS			= "LINKS";
	public static final String COLOR			= "COLOR";
	public static final String COMPLETED		= "COMPLETED";
	public static final String REMINDER			= "REMINDER";
	public static final String FIRSTNAME		= "FIRSTNAME";
	public static final String LASTNAME			= "LASTNAME";
	public static final String MIDDLENAME		= "MIDDLENAME";
	public static final String NICKNAME			= "NICKNAME";
	public static final String WEBSITE			= "WEBSITE";
	public static final String COMPANY			= "COMPANY";
	public static final String JOBTITLE			= "JOBTITLE";
	public static final String IMADDRESS		= "IMADDRESS";
	public static final String BIRTHDAY			= "BIRTHDAY";
	public static final String DEPARTMENT		= "DEPARTMENT";
	public static final String MANAGER			= "MANAGER";
	public static final String EMAIL_COUNT		= "EMAIL_COUNT";
	public static final String PHONE_COUNT		= "PHONE_COUNT";
	public static final String ADDRESS_COUNT	= "ADDRESS_COUNT";
	public static final String AVATAR			= "AVATAR";
	public static final String LOCATION			= "LOCATION";
	public static final String LABEL			= "LABEL";
	public static final String SHOWAS			= "SHOWAS";
	public static final String RESOURCE			= "RESOURCE";
	public static final String PRIVATE			= "PRIVATE";
	public static final String ALL_DAY			= "ALL_DAY";
	public static final String PAST_EVENTS		= "PAST_EVENTS";
	public static final String RECURRENCE		= "RECURRENCE";
	public static final String ATTENDEES		= "ATTENDEES";
	public static final String PASSWORD		 	= "PASSWORD";
	public static final String DESCRIPTION		= "DESCRIPTION";
	public static final String FULLNAME			= "FULLNAME";
	
	
	
	public static final int NOTES_COLOR_COUNT = 5;
	
	public static final int TASK_REMINDER_DAY_BEFORE = 5;
	
	
	public static final String HTML_CODE_TO_APPEND = "<script>alert('BOOOOOMM!!');</script>";
	public static final String LINK_TO_APPEND = "http://www.kerio.eu";
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	
}
