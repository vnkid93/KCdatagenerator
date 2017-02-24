package generators;

import java.util.HashMap;

import com.kerio.lib.json.api.connect.admin.iface.Users;
import com.kerio.lib.json.api.connect.admin.struct.User;
import com.kerio.lib.json.api.connect.admin.struct.common.SearchQuery;

import bridge.KCBridge;
import engine.Constants;
import textgenerators.TextGen;


public class AccountGen {

	public static final String DEFAULT_LOGIN 	= "testxx";
	
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
	
	public User createUser(String domainId, String login){
		return createUser(domainId, login, tGen.genPassword(false), tGen.genFullName(), tGen.genSentence(1));
	}
	
	public User createUser(String domainId, String login, String password, String fullName, String desc){
		User u = new User();
		u.setDomainId(domainId);
		u.setLoginName(login);
		u.setFullName(fullName);
		u.setDescription(desc);
		u.setPassword(password);
		return u;
	}
	
	public void genenerateAndSave(int count, boolean nationalChar, String domainName, HashMap<String, Boolean> checkboxes){
		tGen.setNationalChar(nationalChar);
		String domainId = (domainName == null || domainName.length() == 0) ? kcBridge.getPrimaryDomainId() : kcBridge.getDomainId(domainName);
		int number = kcBridge.getUserList(domainId).length;
		String password = tGen.genPassword(checkboxes.get(Constants.PASSWORD));
		boolean containsFullName = checkboxes.get(Constants.FULLNAME);
		boolean containsDesc = checkboxes.get(Constants.DESCRIPTION);
		User[] users = new User[count];
		for (int i = 0; i < count; i++) {
			String fullName = (containsFullName) ? tGen.genFullName() : "";
			String desc = (containsDesc) ? tGen.genSentence(1) : "";
			users[i] = createUser(domainId, DEFAULT_LOGIN+(++number), password, fullName, desc);
		}
		uClass.create(users);
	}
	
	public void defaultGenerating(int count){
		tGen.setNationalChar(false);
		User[] users = new User[count];
		String domainId = kcBridge.getPrimaryDomainId();
		int number = kcBridge.getUserList(domainId).length;
		for (int i = 0; i < count; i++) {
			users[i] = createUser(domainId, DEFAULT_LOGIN+(++number));
		}
		uClass.create(users);
	}
	

}
