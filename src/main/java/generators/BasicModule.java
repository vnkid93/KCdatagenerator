package generators;

import java.util.List;

import bridge.EwsBridge;
import engine.Item;
import microsoft.exchange.webservices.data.core.ExchangeService;

public abstract class BasicModule {

	public EwsBridge bridge;
	public ExchangeService service;
	
	abstract void generateItems(String outputPath);
	
	abstract List<Item> generateItems();
	
}
