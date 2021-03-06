package bridge;

//import org.apache.log4j.Logger;

import com.kerio.lib.json.api.client.KApiClient;
import com.kerio.lib.json.api.client.KJsonRpcException;
import com.kerio.lib.json.api.connect.admin.iface.Domains;
import com.kerio.lib.json.api.connect.admin.iface.Session;
import com.kerio.lib.json.api.connect.admin.iface.Session.LoginResult;
import com.kerio.lib.json.api.connect.admin.iface.Users;
import com.kerio.lib.json.api.connect.admin.struct.Domain;
import com.kerio.lib.json.api.connect.admin.struct.User;
import com.kerio.lib.json.api.connect.admin.struct.common.ApiApplication;
import com.kerio.lib.json.api.connect.admin.struct.common.SearchQuery;

/**
 * An api bridge for connecting to the Kerio Web Admin.
 * For creating email accoun (users)
 * @author ttran
 *
 */
public class KCBridge extends KApiClient {
	
	//static final Logger logger = Logger.getLogger(KCBridge.class);
	
	@Override
	public void login(String hostUrl, String username, String password) {
		//logger.debug("Starting to log in");
		getSession().setHostname(hostUrl); // updating host name

		LoginResult loginResult = getApi(Session.class).login(username, password, new ApiApplication() {
			{
				setName("ComponentAdmin");
				setVendor("AT");
				setVersion("1.0");
			}
		});	
	}
	
	public boolean loginWithReturn(String hostUrl, String username, String password) {
		System.out.println("------===============================**=");
		try{
			login(hostUrl, username, password);		
		}catch(KJsonRpcException e){
			System.err.println(">>> WRONG LOGIN <<<<<");
			return false;
		}catch (Exception e) {
			System.err.println(">>> WRONG LOGIN!!!!!!!!!!! <<<<<");
			return false;
		}
		System.out.println("======================================");
		return true;
	}
	
	public void logout(){
		//logger.debug("Logging out");
		getApi(Session.class).logout();
	}

	/**
	 * Return domain id from passed domain name
	 * @param domainName the domain that will hav id returned
	 * @return id of domain or null if that domain name was not found.
	 */
	public String getDomainId(String domainName) throws Exception{
		//logger.debug("Getting domain id by name: "+domainName);
		String id = null;
		Domains domainClass = getApi(Domains.class);
		Domain[] domainList = domainClass.get(new SearchQuery()).getList();
		for (Domain domain : domainList) {
			if(domain.getName().equals(domainName)){
				id = domain.getId();
				break;
			}
		}
		if(id == null){
			throw new Exception("Domain not found");
		}
		return id;
	}
	
	public User[] getUserList(String domainId){
		//logger.debug("Getting user list by id: "+domainId);
		Users uClass = getApi(Users.class);
		return uClass.get(new SearchQuery(), domainId).getList();
	}
	
	public boolean isUserExist(String domainId, String userName){
		User[] users = getUserList(domainId);
		boolean found = false;
		for (User user : users) {
			if(user.getLoginName().equals(userName)){
				found = true;
				break;
			}
		}
		return found;
	}
	
	public String getFullName(String email){
		String domainName = email.split("@")[1];
		
		String domainId = null;
		
		try {
			domainId = getDomainId(domainName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getFullName(domainId, email);
	}
	
	public String getFullName(String domainId, String email){
		String fullName = "";
		User[] users = getUserList(domainId);
		for (User user : users) {
			if(user.getEmailAddresses().equals(email)){
				fullName = user.getFullName();
				break;
			}
		}
		return fullName;
	}
	
	public String getPrimaryDomainId(){
		//logger.debug("Getting primary domain id");
		Domains domainClass = getApi(Domains.class);
		Domain[] domainList = domainClass.get(new SearchQuery()).getList();
		return (domainList != null && domainList.length > 0) ? domainList[0].getId() : null;
		
	}
	
	
}
