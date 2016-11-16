package generators;

import bridge.EwsBridge;
import microsoft.exchange.webservices.data.core.ExchangeService;

public class CalendarGen {
	private EwsBridge bridge;
	private ExchangeService service;
	
	public CalendarGen(EwsBridge bridge){
		this.bridge = bridge;
		service = bridge.getService();
	}
	
	public void createEvent(){
		
	}
	
}
