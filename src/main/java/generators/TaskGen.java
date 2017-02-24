package generators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bridge.EwsBridge;
import engine.Constants;
import engine.Probability;
import engine.TimeInput;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.TaskStatus;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.item.Task;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class TaskGen extends BasicItemGenerator{
	// subject, due day, reminder, description, status (done/not done)

	
	public TaskGen(EwsBridge bridge) {
		super(bridge);
	}
	
	public Task createTask(String subject, String body, boolean completed, Date dueDate, Date reminderDate){
		Task task = null;
		try {
			task = new Task(service);
			task.setSubject(subject);
			task.setBody(new MessageBody(body));	//TODO set body type
			if(completed){
				task.setStatus(TaskStatus.Completed);				
			}
			if(dueDate != null){
				task.setDueDate(dueDate);
			}
			if(reminderDate != null){
				task.setReminderDueBy(reminderDate);
				task.setIsReminderSet(true);
			}			
			//task.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}
	
	public Task[] generate(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider, Date[] dateRange){
		Task[] tasks = new Task[count];
		tGen.setNationalChar(nationalChar);
		Probability probability = Probability.getInstance();
		
		boolean htmlCode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		int contentSize = slider.get(Constants.CONTENT_SIZE);
		int completeProb = slider.get(Constants.COMPLETED);
		int reminderProb = slider.get(Constants.REMINDER);
		
		
		for (int i = 0; i < count; i++) {
			String subject = tGen.genSubject();
			StringBuilder body = new StringBuilder();
			body.append(tGen.genParagraph(contentSize));
			if(htmlCode){
				body.append(Constants.NEW_LINE);
				body.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				body.append(Constants.NEW_LINE);
				body.append(Constants.LINK_TO_APPEND);
			}
			
			boolean completed = probability.tryLuck(completeProb);
			Date dueDate = TimeInput.getRandomDayInRange(dateRange[0], dateRange[1]);
			Date reminderDate = (probability.tryLuck(reminderProb)) ? TimeInput.getRandomDayBefore(dueDate, Constants.TASK_REMINDER_DAY_BEFORE) : null;
			
			tasks[i] = createTask(subject, body.toString(), completed, dueDate, reminderDate);
		}
		return tasks;
	}
	
	
	public void generateAndSend(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider, Date[] dateRange){
		tGen.setNationalChar(nationalChar);
		Probability probability = Probability.getInstance();
		
		boolean htmlCode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		int contentSize = slider.get(Constants.CONTENT_SIZE);
		int completeProb = slider.get(Constants.COMPLETED);
		int reminderProb = slider.get(Constants.REMINDER);
		
		
		for (int i = 0; i < count; i++) {
			String subject = tGen.genSubject();
			StringBuilder body = new StringBuilder();
			body.append(tGen.genParagraph(contentSize));
			if(htmlCode){
				body.append(Constants.NEW_LINE);
				body.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				body.append(Constants.NEW_LINE);
				body.append(Constants.LINK_TO_APPEND);
			}
			
			boolean completed = probability.tryLuck(completeProb);
			Date dueDate = TimeInput.getRandomDayInRange(dateRange[0], dateRange[1]);
			Date reminderDate = (probability.tryLuck(reminderProb)) ? TimeInput.getRandomDayBefore(dueDate, Constants.TASK_REMINDER_DAY_BEFORE) : null;
			
			try {
				createTask(subject, body.toString(), completed, dueDate, reminderDate).save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}
	
	
	
	
	
	/**
	 * Method that return all tasks that are presented in mian Task folder.
	 * @return list of tasks found.
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
