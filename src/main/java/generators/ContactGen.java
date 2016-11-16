package generators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import bridge.EwsBridge;
import main.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.ImAddressKey;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.Contact;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressDictionary;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.PhoneNumberDictionary;
import microsoft.exchange.webservices.data.property.complex.PhysicalAddressDictionary;
import microsoft.exchange.webservices.data.property.complex.PhysicalAddressEntry;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class ContactGen {
	
	private EwsBridge bridge;
	private ExchangeService service;
	
	public ContactGen(EwsBridge bridge){
		this.bridge = bridge;
		this.service = bridge.getService();
	}
	
	/**
	 * Create contact will full information. Of course every single information can be skipped by assigning null or empty string
	 * @param name The contact name
	 * @param surname The contact surname
	 * @param middleName The contact middleName
	 * @param nickName The nickName of contact
	 * @param title The title that stand in before the name
	 * @param generation The title that stand after name
	 * @param company Company name
	 * @param emails List of email addresses, it is a array of string. For e.g {"email1@email.com", "email2@email.com"}. Only three email addresses are allowed.
	 * @param jobTitle The job title or profession
	 * @param homepage The contact website
	 * @param phoneNumbers Phone and fax numbers of contact. Same as email addresses, it is a array of string. Maximum 5 numbers
	 * @param description The short description about contact
	 * @param imAddress IM address. For IM.
	 * @param addresses Maximum three address entry. It is a array of {@code PhysicalAddressEntry}. Address of the contact.
	 * @param assistantName Name of an assistant
	 * @param managerName The managers name
	 * @param birthday Contact birthday written due format in {@code datePattern} parameter
	 * @param anniversaryDate Same as birthday, it will have same format
	 * @param department The contact department
	 * @param officeLocation The location of an office
	 * @param datePattern The date pattern. E.g. "dd-MM-yyyy hh:mm"
	 */
	public void createContact(String name, String surname, String middleName, String nickName, String title, String generation,	
			String company, String[] emails, String jobTitle, String homepage, String[] phoneNumbers,
			String description, String imAddress, PhysicalAddressEntry[] addresses,
			String assistantName, String managerName, String birthday, String anniversaryDate, 
			String department, String officeLocation, String datePattern){
		try {
			Contact contact = new Contact(service);
			if(name != null)		contact.setGivenName(name);
			if(middleName != null)	contact.setMiddleName(middleName);
			if(surname != null)		contact.setSurname(surname);
			if(company != null)		contact.setCompanyName(company);
			
			if(description != null)		contact.setBody(new MessageBody(description));
			if(assistantName != null)	contact.setAssistantName(assistantName);
			if(managerName != null)		contact.setManager(managerName);
			if(homepage != null)		contact.setBusinessHomePage(homepage);
			if(jobTitle != null)		contact.setJobTitle(jobTitle);
			if(datePattern != null){				
				DateFormat formatter = new SimpleDateFormat(datePattern);
				if(birthday != null)		contact.setBirthday(formatter.parse(birthday));
				if(anniversaryDate != null)	contact.setWeddingAnniversary(formatter.parse(anniversaryDate));
			}
			if(name != null)		contact.setNickName(nickName);
			
			if(emails != null && emails.length > 0){		
				EmailAddressDictionary emailDict = contact.getEmailAddresses();
				for (int i = 0; i < Constants.EMAIL_INDEXES.length; i++) {
					if(emails[i] != null)	emailDict.setEmailAddress(Constants.EMAIL_INDEXES[i], new EmailAddress(emails[i]));
				}
			}
			
			if(phoneNumbers != null && phoneNumbers.length > 0 && phoneNumbers.length <= Constants.PHONE_INDEXES.length){		
				PhoneNumberDictionary phoneDict = contact.getPhoneNumbers();
				for (int i = 0; i < phoneNumbers.length; i++) {
					if(phoneNumbers[i] != null)	phoneDict.setPhoneNumber(Constants.PHONE_INDEXES[i], phoneNumbers[i]);
				}
			}
			
			if(imAddress != null)		contact.getImAddresses().setImAddressKey(ImAddressKey.ImAddress1, imAddress);

			if(addresses != null && addresses.length > 0 && addresses.length <= Constants.ADDRESS_INDEXES.length){		
				PhysicalAddressDictionary addressDict = contact.getPhysicalAddresses();
				for (int i = 0; i < addresses.length; i++) {
					if(addresses[i] != null)	addressDict.setPhysicalAddress(Constants.ADDRESS_INDEXES[i], addresses[i]);
				}
			}
			if(department != null)		contact.setDepartment(department);
			if(generation != null)		contact.setGeneration(generation);
			if(officeLocation != null)		contact.setOfficeLocation(officeLocation);
			
			if(title != null) {				
				ExtendedPropertyDefinition titlePropDef = new ExtendedPropertyDefinition(0x3A45, MapiPropertyType.String);
				contact.setExtendedProperty(titlePropDef, title);
			}
			
			//TODO validate params
			//contact.getPhysicalAddresses().setPhysicalAddress(PhysicalAddressKey.Home, address);
			contact.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Contact> getContacts(FolderId folderId){
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		ItemView view = new ItemView(50);
		FindItemsResults<Item> results = new FindItemsResults<Item>();
		try {
			do {
				results = service.findItems(folderId, view);
				for (Item item : results) {
					contactList.add(Contact.bind(service, item.getId()));
				}
			} while (results.isMoreAvailable());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contactList;
	}
	
	public ArrayList<Contact> getContacts(){
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		ItemView view = new ItemView(50);
		FindItemsResults<Item> results = new FindItemsResults<Item>();
		try {
			do {
				results = service.findItems(WellKnownFolderName.Contacts, view);
				for (Item item : results) {
					contactList.add(Contact.bind(service, item.getId()));
				}
			} while (results.isMoreAvailable());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contactList;
	}

}
