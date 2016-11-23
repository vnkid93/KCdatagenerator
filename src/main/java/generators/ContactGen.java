package generators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import bridge.EwsBridge;
import engine.Constants;
import engine.Probability;
import engine.SimpleAddress;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
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
import textgenerators.TextGen;

public class ContactGen extends BasicItemGenerator{
	
	
	
	public ContactGen(EwsBridge bridge){
		super(bridge);
	}
	
	
	public void createContact(boolean nationalChar){
		try {
			Contact contact = new Contact(service);
			TextGen tGen = TextGen.getInstance();
			tGen.setNationalChar(nationalChar);
			
			contact.setGivenName(tGen.genFirstName());
			contact.setSurname(tGen.genLastName());
			contact.setCompanyName(tGen.genCompany());
			
			
			if(Probability.getInstance().tryLuck(5))	contact.setMiddleName(tGen.genMiddleName());
			
			if(Probability.getInstance().tryLuck(5)){
				contact.setGeneration(new String[]{"Junior", "Senior"}[rand.nextInt(2)]);
			}
			
			contact.setBody(new MessageBody(BodyType.Text, tGen.genSentence(3)));
			
			contact.setAssistantName(tGen.genFullName());
			
			
			contact.setManager(tGen.genFullName());
			contact.setBusinessHomePage(tGen.genWebsite());
			contact.setJobTitle(tGen.genJobTitle());		
			contact.setBirthday(tGen.genBirthDay());
			contact.setWeddingAnniversary(tGen.genAnniversary());
			contact.setNickName(tGen.genNickName());
					
			EmailAddressDictionary emailDict = contact.getEmailAddresses();
			for (int i = 0; i < Constants.EMAIL_INDEXES.length; i++) {
				emailDict.setEmailAddress(Constants.EMAIL_INDEXES[i], new EmailAddress(tGen.genEmail(contact.getGivenName(), contact.getSurname())));
			}
					
			PhoneNumberDictionary phoneDict = contact.getPhoneNumbers();
			for (int i = 0; i < Constants.PHONE_INDEXES.length; i++) {
				phoneDict.setPhoneNumber(Constants.PHONE_INDEXES[i], tGen.genTelNumber());
			}
			
			contact.getImAddresses().setImAddressKey(ImAddressKey.ImAddress1, tGen.genEmail(contact.getGivenName(), contact.getSurname()));
		
			PhysicalAddressDictionary addressDict = contact.getPhysicalAddresses();
			for (int i = 0; i < Constants.ADDRESS_INDEXES.length; i++) {
				PhysicalAddressEntry entry = new PhysicalAddressEntry();
				SimpleAddress address = tGen.genAddress();
				entry.setCity(address.city);
				entry.setStreet(address.street);
				entry.setPostalCode(address.zip);
				entry.setCountryOrRegion(address.country);
				entry.setState(address.state);
				addressDict.setPhysicalAddress(Constants.ADDRESS_INDEXES[i], entry);
			}			
			
			if(Probability.getInstance().tryLuck(3)){
				String path = "src/main/resources/img/male-icon.gif";
				if(Probability.getInstance().tryLuck(50)){
					path = "src/main/resources/img/female-icon.gif";
				}		
				contact.setContactPicture(path);
			}
			
			
			contact.setDepartment("Department");
			contact.setOfficeLocation(tGen.genLocation());
						
//			ExtendedPropertyDefinition titlePropDef = new ExtendedPropertyDefinition(0x3A45, MapiPropertyType.String);
//			contact.setExtendedProperty(titlePropDef, title);
			
			
			
			
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
