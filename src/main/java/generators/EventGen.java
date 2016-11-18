package generators;

import java.util.ArrayList;
import java.util.List;

import bridge.EwsBridge;
import engine.Constants;
import engine.SimpleAccount;
import engine.TimeInput;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.Sensitivity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class EventGen extends BasicItemGenerator{
	private static final String[] CZ_CITIES = {"Plzeň", "České Budějovice", "Mariánské Lázně", "Hradec Králové", "Telč", "Litoměřice", "Děčín", "Ústí nad Labem"};
	private static final String[] OTHER_CITIES = {"New York", "Los Angeles", "Praha", "Hanoi", "Chicago", "London", "Brno", "Saigon"};
	
	public EventGen(EwsBridge bridge){
		super(bridge);
	}
	
	public void createEvent(String subject, List<SimpleAccount> attendees, TimeInput[] duration, boolean isPrivate, boolean nationalChar){
		try {
			Appointment event = new Appointment(service);
			event.setSubject(subject);
			// duration, start and end time
			event.setStart(duration[Constants.EVENT_DURATION_START_INDEX].getDate());
			event.setEnd(duration[Constants.EVENT_DURATION_END_INDEX].getDate());
			
			// location
			String []cities = (nationalChar) ? CZ_CITIES : OTHER_CITIES;
			event.setLocation(cities[rand.nextInt(cities.length)]);
			
			// description
			event.setBody(new MessageBody(BodyType.Text, "helll sadfasd fsadf asdf asdf"));
			
			if(isPrivate){
				event.setSensitivity(Sensitivity.Private);				
			}
			
			// attendees
			AttendeeCollection eventAttendees = event.getRequiredAttendees();
			for (SimpleAccount attendee : attendees) {
				eventAttendees.add(attendee.fullName, attendee.emailAddress);
			}
			
			event.setReminderMinutesBeforeStart(rand.nextInt(60));
			
			event.save(WellKnownFolderName.Calendar);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
}
