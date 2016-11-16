package generators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bridge.EwsBridge;
import engine.TimeInput;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.TaskStatus;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.item.Task;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class TaskGen {
	// subject, due day, reminder, description, status (done/not done)
	
	private EwsBridge bridge;
	private ExchangeService service;
	
	public TaskGen(EwsBridge bridge){
		this.bridge = bridge;
		service = bridge.getService();
	}
	
	public void createTask(String subject, String body, boolean completed, TimeInput dueDate, TimeInput reminderDate){
		try {
			Task task = new Task(service);
			task.setSubject(subject);
			task.setBody(new MessageBody(body));	//TODO set body type
			if(completed){
				task.setStatus(TaskStatus.Completed);				
			}
			if(dueDate != null){
				task.setDueDate(dueDate.getDate());
			}
			if(reminderDate != null){
				task.setReminderDueBy(reminderDate.getDate());
				task.setIsReminderSet(true);
			}			
			task.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void generateTasks(int count, String subjectPrefix){
		try {	// longer code, but more effective
			if(subjectPrefix != null && subjectPrefix.length() > 0){				
				for (int i = 0; i < count; i++) {
					Task task = new Task(service);
					task.setSubject(subjectPrefix + i);
					task.setBody(new MessageBody("neco... nevim co"));
					task.setDueDate(new Date());
					task.save();					
				}
			}else{
				for (int i = 0; i < count; i++) {
					Task task = new Task(service);
					task.save();					
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Task> getTasks(){
		ArrayList<Task> taskList = new ArrayList<Task>();
		ItemView view = new ItemView(50);
		FindItemsResults<Item> results;
		try {
			do{
				results = service.findItems(WellKnownFolderName.Tasks, view);
				for (Item item : results) {
					taskList.add(Task.bind(service, item.getId()));
				}
			}while(results.isMoreAvailable());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskList;
	}
	
	/**
	 * Get all tasks from exact folder by folder id
	 * @param id the id of folder
	 * @return
	 */
	public ArrayList<Task> getTasks(FolderId id){
		ArrayList<Task> taskList = new ArrayList<Task>();
		ItemView view = new ItemView(20);
		FindItemsResults<Item> results;
		try {
			do{
				results = service.findItems(id, view);
				for (Item item : results) {
					taskList.add(Task.bind(service, item.getId()));
				}
			}while(results.isMoreAvailable());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskList;
	}

}
