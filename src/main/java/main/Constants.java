package main;

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
															EmailAddressKey.EmailAddress2};
	
	public static final int ADDRESS_INDEX_HOME		= 0;
	public static final int ADDRESS_INDEX_BUSINESS	= 1;
	public static final int ADDRESS_INDEX_OTHER		= 2;
	public static final	PhysicalAddressKey[] ADDRESS_INDEXES = {PhysicalAddressKey.Home, PhysicalAddressKey.Business, 
															PhysicalAddressKey.Other};
	
	
	public static final String DEFAULT_TIME_FORMAT = "dd/M/yyyy";
	
	
}
