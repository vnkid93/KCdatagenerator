package bridge;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;


/**
 * Bridge for communication between Application and Webmail client
 * @author ttran
 *
 */
public class EwsBridge {

	private ExchangeService service;
	private ExchangeCredentials credentials;
	private String host;
	private String fullName;
	private String emailAddress;
	

	public EwsBridge(String login, String password){
		this(new WebCredentials(login, password));
		this.emailAddress = login;
	}
	private EwsBridge(ExchangeCredentials credentials) {
		this(new ExchangeService(ExchangeVersion.Exchange2010_SP2), credentials);
	}
	private  EwsBridge(ExchangeService service, ExchangeCredentials credentials) {
		this.service = service;
		this.credentials = credentials;
		this.service.setCredentials(credentials);
		this.host = null;
		this.fullName = null;
	}
	
	
	public void setHostAndUrl(String host){
		this.host = host;
		try {
			setEwsUrl(new URI("http://"+ host +"/ews/Exchange.asmx"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void setEwsUrl(URI url){
		service.setUrl(url);
	}
	
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
	
	public Item getItemById(ItemId id){
		Item item = null;
		try {
			item = Item.bind(service, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}
	
	public Folder getFolderById(FolderId id){
		Folder folder = null;
		try {
			folder = Folder.bind(service, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folder;
	}
	
	public Folder createFolder(String folderName, FolderId parentFolderId){
		Folder folder = null;
		try {
			folder = new Folder(service);
			folder.setDisplayName(folderName);
			folder.save(parentFolderId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folder;
	}
	
	
	public void setCredentials(String login, String password){
		setCredentials(new WebCredentials(login, password));
	}
	
	public void setCredentials(ExchangeCredentials credentials){
		this.credentials = credentials;
		service.setCredentials(this.credentials);
	}
	
	public ExchangeCredentials getCredentials(){
		return credentials;
	}
	
	public ExchangeService getService(){
		return this.service;
	}
	public String getHost() {
		return host;
	}
	
	public String getFullName(){
		return fullName;
	}
	public String getEmail(){
		return emailAddress;
	}

}
