package bridge;

import com.kerio.lib.json.api.client.KApiClient;
import com.kerio.lib.json.api.connect.admin.iface.Session;
import com.kerio.lib.json.api.connect.admin.struct.common.ApiApplication;

/**
 * An api bridge for connecting to the Kerio Web Admin.
 * For creating email accoun (users)
 * @author ttran
 *
 */
public class KCBridge extends KApiClient {

	@Override
	public void login(String hostUrl, String username, String password) {
		getSession().setHostname(hostUrl); // updating host name
		getApi(Session.class).login(username, password, new ApiApplication() {
			{
				setName("ComponentAdmin");
				setVendor("AT");
				setVersion("1.0");
			}
		});
	}
	
	public void logout(){
		getApi(Session.class).logout();
	}
}
