package generators;

import java.util.List;
import java.util.Random;

import bridge.EwsBridge;
import microsoft.exchange.webservices.data.core.ExchangeService;
import textgenerators.TextGen;

public abstract class BasicItemGenerator {

	public EwsBridge bridge;
	public ExchangeService service;
	public Random rand;
	protected TextGen tGen;
	
	protected BasicItemGenerator(EwsBridge bridge){
		this.bridge = bridge;
		this.service = bridge.getService();
		rand = new Random();
		tGen = TextGen.getInstance();
	}
	
	public void setService(ExchangeService service){
		this.service = service;
	}
	
	
}
