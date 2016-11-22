package generators;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import bridge.EwsBridge;
import engine.Constants;
import engine.SimpleAccount;
import engine.TimeInput;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.DefaultExtendedPropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.Sensitivity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.property.time.DayOfTheWeek;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;
import textgenerators.ITextGenerator;
import textgenerators.TextGen;

public class EventGen extends BasicItemGenerator{
	
	private ExtendedPropertyDefinition labelColor;
	
	private static final int MAX_DAY_BEFORE = 20;
	private static final int MAX_DAY_AFTER 	= 20;
	private static final int MAX_OCCURENCE_NUMBER = 20;
	
	
	public EventGen(EwsBridge bridge){
		super(bridge);
		initProperties();
	}
	
	public void createEvent(String subject, List<SimpleAccount> attendees, TimeInput[] duration, boolean isAllday, boolean isPrivate, boolean nationalChar){
		try {
			ITextGenerator tGen = TextGen.getInstance();
			tGen.setNationalChar(nationalChar);
			
			Appointment event = new Appointment(service);
			event.setSubject(subject);
			// duration, start and end time
			event.setStart(duration[Constants.EVENT_DURATION_START_INDEX].getDate());
			event.setEnd(duration[Constants.EVENT_DURATION_END_INDEX].getDate());
			event.setIsAllDayEvent(isAllday);
			
			// free busy status
			setRandomBusySatus(event);
			
			// privacy
			if(isPrivate){
				event.setSensitivity(Sensitivity.Private);				
			}
			
			// recurrence
			setRandomReccurence(event);
			
			// description
			event.setBody(new MessageBody(BodyType.Text, tGen.genShortContent()));
			
			// location
			event.setLocation(TextGen.getInstance().genLocation());
			
			// attendees
			AttendeeCollection eventAttendees = event.getRequiredAttendees();
			for (SimpleAccount attendee : attendees) {
				eventAttendees.add(attendee.fullName, attendee.emailAddress);
			}
			
			// reminder
			event.setIsReminderSet(true);
			event.setReminderMinutesBeforeStart(rand.nextInt(60));
			
			// label
			event.setExtendedProperty(labelColor, rand.nextInt(11));
			
			
			event.save(WellKnownFolderName.Calendar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
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
