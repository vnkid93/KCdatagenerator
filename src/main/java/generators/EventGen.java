package generators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;

import bridge.EwsBridge;
import engine.Constants;
import engine.Probability;
import engine.SimpleAccount;
import engine.TimeInput;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.DefaultExtendedPropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.Sensitivity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.property.time.DayOfTheWeek;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;

public class EventGen extends BasicItemGenerator{
	
	private ExtendedPropertyDefinition labelColor;
	
	
	public EventGen(EwsBridge bridge){
		super(bridge);
		initProperties();
	}
	
	public void generateAndSave(int count, boolean nationalChar, HashMap<String, Boolean> checkboxes, HashMap<String, Integer> slider, Date[] dateRange, ArrayList<SimpleAccount> attendees){
		Probability probability = Probability.getInstance();
		tGen.setNationalChar(nationalChar);
		boolean subject = checkboxes.get(Constants.SUBJECT);
		boolean location = checkboxes.get(Constants.LOCATION);
		boolean label = checkboxes.get(Constants.LABEL);
		boolean showas = checkboxes.get(Constants.SHOWAS);
		boolean reminder = checkboxes.get(Constants.REMINDER);
		boolean htmlcode = checkboxes.get(Constants.HTMLCODE);
		boolean link = checkboxes.get(Constants.LINKS);
		
		//TODO resource location
		int resouceProb = slider.get(Constants.RESOURCE);
		int privateProb = slider.get(Constants.PRIVATE);
		int allDayProb = slider.get(Constants.ALL_DAY);
		int pastEventsProb = slider.get(Constants.PAST_EVENTS);
		int recurrenceProb = slider.get(Constants.RECURRENCE);
		int attendeesProb = slider.get(Constants.ATTENDEES);
		int descSize = slider.get(Constants.CONTENT_SIZE);
		
		for (int i = 0; i < count; i++) {
			String subjectText = (subject) ? tGen.genSubject() : "";
			String locationText = (location) ? tGen.genLocation() : "";
			boolean allday = probability.tryLuck(allDayProb);
			Sensitivity privateValue = (probability.tryLuck(privateProb)) ? Sensitivity.Private : Sensitivity.Normal;
			boolean pastEvent = probability.tryLuck(pastEventsProb);
			boolean recurrence = probability.tryLuck(recurrenceProb);
			boolean containsAttendee = probability.tryLuck(attendeesProb);
			StringBuilder desc = new StringBuilder();
			desc.append(tGen.genParagraph(descSize));
			if(htmlcode){
				desc.append(Constants.NEW_LINE);
				desc.append(Constants.HTML_CODE_TO_APPEND);
			}
			if(link){
				desc.append(Constants.NEW_LINE);
				desc.append(Constants.LINK_TO_APPEND);
			}
			int labelValue = (label) ? rand.nextInt(11) : 0;
			
			ArrayList<SimpleAccount> atts = (containsAttendee) ? attendees : new ArrayList<SimpleAccount>();
			
			Date startDate = TimeInput.getRandomDayInRange(dateRange[0], (pastEvent) ? new Date() : dateRange[1]);
			Date endDate = TimeInput.getRandomDayAfter(startDate, probability.tryLuck(90) ? 1 : 2);
			
			
			Appointment event = createEvent(subjectText, locationText, atts, new Date[]{startDate, endDate}, allday, privateValue, reminder, recurrence, desc.toString(), labelValue, showas);

			if(event != null){
				try {
					if(event.getIsNew()){
						event.save(WellKnownFolderName.Calendar);						
					}else{
						event.update(ConflictResolutionMode.AutoResolve);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
	public Appointment createDefault(boolean nationalChar, Date[] duration){
		tGen.setNationalChar(nationalChar);
		return createEvent(tGen.genSubject(), tGen.genLocation(), null, duration, false, Sensitivity.Normal, true, false, tGen.genParagraph(1), 0, true);
	}
	public Appointment createEvent(String subject, String location, ArrayList<SimpleAccount> attendees, Date[] duration, boolean isAllday, 
			Sensitivity privateValue, boolean reminder, boolean recurrence, String description, int labelValue, boolean showAs){
		
		Appointment event = null;
		try {
			
			event = new Appointment(service);
			event.setSubject(subject);
			// duration, start and end time
			event.setStart(duration[0]);
			event.setEnd(duration[1]);
			event.setIsAllDayEvent(isAllday);
			
			// free busy status
			if(showAs){				
				setRandomBusySatus(event);
			}
			
			// privacy
			event.setSensitivity(privateValue);				
			
			// recurrence
			if(recurrence){				
				setRandomReccurence(event);
			}
			
			// description
			event.setBody(new MessageBody(BodyType.Text, description));
			
			// location
			event.setLocation(location);
			
			// attendees
			if(attendees != null){
				AttendeeCollection eventAttendees = event.getRequiredAttendees();
				for (SimpleAccount attendee : attendees) {
					eventAttendees.add(attendee.fullName, attendee.emailAddress);
				}				
			}
			
			// reminder
			if(reminder){
				event.setIsReminderSet(true);
				event.setReminderMinutesBeforeStart(rand.nextInt(60));				
			}
			
			// label
			event.setExtendedProperty(labelColor, labelValue);
			
			
			event.save(WellKnownFolderName.Calendar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return event;
	}
	
	/**
	 * Set random recurrence.
	 * @param event
	 * @throws Exception
	 */
	private void setRandomReccurence(Appointment event) throws Exception{
		Date startingDay = event.getStart();
		Recurrence.WeeklyPattern wPattern = new Recurrence.WeeklyPattern();
		wPattern.setStartDate(startingDay);
		wPattern.setEndDate(TimeInput.getMonthAfter(startingDay, 1));		
		wPattern.getDaysOfTheWeek().add(DayOfTheWeek.values()[new DateTime(startingDay).dayOfWeek().get()]);
		event.setRecurrence(wPattern);
	}
	
	
	private void setRandomBusySatus(Appointment event) throws Exception{
		final LegacyFreeBusyStatus[] states = {	LegacyFreeBusyStatus.Free,
												LegacyFreeBusyStatus.Busy,
												LegacyFreeBusyStatus.Tentative,
												LegacyFreeBusyStatus.OOF};
		event.setLegacyFreeBusyStatus(states[rand.nextInt(states.length)]);
	}
	
	
	private void initProperties(){
		labelColor = new ExtendedPropertyDefinition(DefaultExtendedPropertySet.Appointment, 0x8214, MapiPropertyType.Integer);
	}
	
}
