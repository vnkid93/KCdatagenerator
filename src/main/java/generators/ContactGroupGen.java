package generators;

import java.util.List;

import bridge.EwsBridge;
import engine.SimpleAccount;
import microsoft.exchange.webservices.data.core.service.item.ContactGroup;
public class ContactGroupGen extends BasicItemGenerator{

	public ContactGroupGen(EwsBridge bridge) {
		super(bridge);
		// TODO Auto-generated constructor stub
	}
	
	public void createContactGroup(String groupName, List<SimpleAccount> contacts){
		try {
			ContactGroup group = new ContactGroup(service);
			group.setDisplayName(groupName);
			//group.getMembers().add(new GroupMember("test03@tony.lab"));
			/*for (SimpleAccount simpleAccount : contacts) {
				group.getMembers().add(new GroupMember(simpleAccount.emailAddress));
			}*/
			
			group.save();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
