package generators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bridge.EwsBridge;
import engine.Constants;
import engine.Probability;
import engine.SimpleAddress;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.ImAddressKey;
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
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class ContactGen extends BasicItemGenerator{
	
	private final String[] generation = {"Junior", "Senior"};
	
	public ContactGen(EwsBridge bridge){
		super(bridge);
	}
	
	/**
	 * 
	 * @param count
	 * @param nationalChar
	 * @param checkboxes
	 * @param slider
	 */
	public void generateAndSave(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider){
		Probability probability = Probability.getInstance();
		tGen.setNationalChar(nationalChar);
		boolean firstName = checkboxes.get(Constants.FIRSTNAME);
		boolean lastName = checkboxes.get(Constants.LASTNAME);
		boolean middleName = checkboxes.get(Constants.MIDDLENAME);
		boolean nickName = checkboxes.get(Constants.NICKNAME);
		boolean website = checkboxes.get(Constants.WEBSITE);
		boolean company = checkboxes.get(Constants.COMPANY);
		boolean jobTitle = checkboxes.get(Constants.JOBTITLE);
		boolean htmlcode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		boolean imaddr	= checkboxes.get(Constants.IMADDRESS);
		boolean birthday = checkboxes.get(Constants.BIRTHDAY);
		boolean department = checkboxes.get(Constants.DEPARTMENT);
		boolean manager = checkboxes.get(Constants.MANAGER);
		
		int emailCount = slider.get(Constants.EMAIL_COUNT);
		int phoneCount = slider.get(Constants.PHONE_COUNT);
		int addressCount = slider.get(Constants.ADDRESS_COUNT);
		int contentSize = slider.get(Constants.DESCRIPTION);
		int avatarProb = slider.get(Constants.AVATAR);
		
		// generating
		for (int i = 0; i < count; i++) {
			String firstNameText 	= (firstName) ? tGen.genFirstName() : "";
			String lastNameText 	= (lastName) ? tGen.genLastName() : "" ; 
			String middleNameText 	= (middleName) ? tGen.genMiddleName() : "" ;
			String nickNameText 	= (nickName) ? tGen.genNickName() : "" ;
			String websiteText 		= (website) ? tGen.genWebsite() : "" ;
			String companyText  	= (company) ? tGen.genCompany() : "" ;
			String jobTitleText  	= (jobTitle) ? tGen.genJobTitle() : "" ;
			StringBuilder note  	= new StringBuilder();
			note.append(tGen.genSentence(contentSize));
			if(htmlcode){
				note.append(Constants.NEW_LINE);
				note.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				note.append(Constants.NEW_LINE);
				note.append(Constants.LINK_TO_APPEND);
			}
			
			String imAddressText  	= (imaddr) ? tGen.genEmail(firstNameText, lastNameText) : "" ;
			Date birthdayDate 		= (birthday) ? tGen.genBirthDay() : null ;
			Date anniversaryDate 	= (birthday) ? tGen.genAnniversary() : null ;
			String departmentText 	= (department) ? "Department" : "" ;
			String officeText 		= (department) ? tGen.genLocation() : "" ;
			String managerText 		= (manager) ? tGen.genFullName() : "" ;
			String assistantText 	= (manager) ? tGen.genFullName() : "" ;
			
			String[] emails = new String[emailCount];
			for (int j = 0; j < emails.length; j++) {
				emails[j] = tGen.genEmail(firstNameText, lastNameText);
			}
			String[] phones = new String[phoneCount];
			for (int j = 0; j < phones.length; j++) {
				phones[j] = tGen.genTelNumber();
			}
			SimpleAddress[] addresses = new SimpleAddress[addressCount];
			for (int j = 0; j < addresses.length; j++) {
				addresses[j] = tGen.genAddress();
			}
			
			try {
				createContact(firstNameText, lastNameText, emails, phones, addresses, middleNameText, nickNameText, 
						websiteText, companyText,jobTitleText, note.toString(), imAddressText, birthdayDate, 
						anniversaryDate, departmentText, officeText, managerText, assistantText, probability.tryLuck(avatarProb)).save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param emails
	 * @param phones
	 * @param addresses
	 * @param middleName
	 * @param nickName
	 * @param website
	 * @param company
	 * @param jobTitle
	 * @param note
	 * @param imAddress
	 * @param birthday
	 * @param anniversary
	 * @param department
	 * @param office
	 * @param manager
	 * @param assistant
	 * @param avatar
	 */
	public Contact createContact(String firstName, String lastName, String[] emails, String[] phones, SimpleAddress[] addresses,
			String middleName, String nickName, String website, String company, String jobTitle, 
			String note, String imAddress, Date birthday, Date anniversary,String department, 
			String office, String manager, String assistant, boolean avatar){
		
		Contact contact = null;
		try {
			contact = new Contact(service);
			
			contact.setGivenName(firstName);
			contact.setSurname(lastName);
			contact.setCompanyName(company);
			contact.setMiddleName(middleName);
			
			if(Probability.getInstance().tryLuck(5)){
				contact.setGeneration(generation[rand.nextInt(2)]);
			}
			
			contact.setBody(new MessageBody(BodyType.Text, note));
			contact.setNickName(nickName);
			contact.setAssistantName(assistant);
			contact.setManager(manager);
			contact.setBusinessHomePage(website);
			contact.setJobTitle(jobTitle);		
			contact.setBirthday(birthday);
			contact.setWeddingAnniversary(anniversary);
					
			EmailAddressDictionary emailDict = contact.getEmailAddresses();
			for (int i = 0; i < emails.length; i++) {
				emailDict.setEmailAddress(Constants.EMAIL_INDEXES[i], new EmailAddress(emails[i]));
			}
					
			PhoneNumberDictionary phoneDict = contact.getPhoneNumbers();
			for (int i = 0; i < phones.length; i++) {
				phoneDict.setPhoneNumber(Constants.PHONE_INDEXES[i], phones[i]);
			}
			
			contact.getImAddresses().setImAddressKey(ImAddressKey.ImAddress1, tGen.genEmail(contact.getGivenName(), contact.getSurname()));
		
			PhysicalAddressDictionary addressDict = contact.getPhysicalAddresses();
			
			for (int i = 0; i < addresses.length; i++) {
				PhysicalAddressEntry entry = new PhysicalAddressEntry();
				SimpleAddress address = addresses[i];
				entry.setCity(address.city);
				entry.setStreet(address.street);
				entry.setPostalCode(address.zip);
				entry.setCountryOrRegion(address.country);
				entry.setState(address.state);
				addressDict.setPhysicalAddress(Constants.ADDRESS_INDEXES[i], entry);
			}			
			
			if(avatar){
				String path = Constants.AVATAR_IMG_MALE;
				if(Probability.getInstance().tryLuck(50)){
					path = Constants.AVATAR_IMG_FEMALE;
				}		
				contact.setContactPicture(path);
			}
			
			
			contact.setDepartment(department);
			contact.setOfficeLocation(office);
						
//			ExtendedPropertyDefinition titlePropDef = new ExtendedPropertyDefinition(0x3A45, MapiPropertyType.String);
//			contact.setExtendedProperty(titlePropDef, title);
			
			//contact.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
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
