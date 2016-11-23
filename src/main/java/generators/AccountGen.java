package generators;

import com.kerio.lib.json.api.connect.admin.iface.Users;
import com.kerio.lib.json.api.connect.admin.struct.User;
import com.kerio.lib.json.api.connect.admin.struct.common.SearchQuery;

import bridge.KCBridge;
import textgenerators.TextGen;


public class AccountGen {

	public static final String DEFAULT_LOGIN 	= "testxx";
	public static final String DEFAULT_PASSWORD = "a";
	
	private KCBridge kcBridge;
	private Users uClass;
	private TextGen tGen;
	
	public AccountGen(KCBridge bridge){
		this.kcBridge = bridge;
		uClass = kcBridge.getApi(Users.class);
		tGen = TextGen.getInstance();
	}
	
	public void createUsers(User[] users){
		uClass.create(users);
	}
	
	public User[] getUsersInDomain(String domainId){
		return uClass.get(new SearchQuery(), domainId).getList();
	}
	
	public User createUser(boolean nationalChar, String domainId, String login){
		tGen.setNationalChar(nationalChar);
		User u = new User();
		u.setDomainId(domainId);
		u.setLoginName(login);
		u.setFullName(tGen.genFullName());
		u.setDescription(tGen.genSentence(1));
		u.setPassword(DEFAULT_PASSWORD);
		return u;
	}
	
	public void genUsers(int count, boolean nationalChar, String domainId){
		int number = kcBridge.getUserList(domainId).length;
		tGen.setNationalChar(nationalChar);
		User[] users = new User[count];
		for (int i = 0; i < count; i++) {
			users[i] = createUser(nationalChar, domainId, DEFAULT_LOGIN+(++number));
		}
		uClass.create(users);
	}
	

}
