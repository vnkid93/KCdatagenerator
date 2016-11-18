package generators;

import com.kerio.lib.json.api.connect.admin.iface.Users;
import com.kerio.lib.json.api.connect.admin.struct.User;
import com.kerio.lib.json.api.connect.admin.struct.common.SearchQuery;

import bridge.KCBridge;


public class AccountGen {
	private KCBridge kcBridge;
	private Users uClass;
	
	
	public AccountGen(KCBridge bridge){
		this.kcBridge = bridge;
		uClass = kcBridge.getApi(Users.class);
	}
	
	public void createUsers(User[] users){
		uClass.create(users);
	}
	
	public User[] getUsersInDomain(String domainId){
		return uClass.get(new SearchQuery(), domainId).getList();
	}
	
	public void createUser(String domainId, String loginName, String fullName, String description, String password){
		User u = new User();
		u.setDomainId(domainId);
		u.setLoginName(loginName);
		u.setFullName(fullName);
		u.setDescription(description);
		u.setPassword(password);
		uClass.create(new User[]{u});
	}
	

}
