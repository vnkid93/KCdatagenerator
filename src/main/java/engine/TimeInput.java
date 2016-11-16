package engine;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.Constants;

public class TimeInput {
	// instance of date
	private Date date;
	
	/**
	 * Constructor that use default date format
	 * @param date
	 */
	public TimeInput(String date){
		this(Constants.DEFAULT_TIME_FORMAT, date);
	}
	
	/**
	 * Constructor that use user defined date format
	 * @param format
	 * @param date
	 */
	public TimeInput(String format, String date){
		DateFormat formatter = new SimpleDateFormat(format);
		try {
			this.date = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public TimeInput(Date date){
		this.date = date;
	}
	
	/**
	 * Returning date
	 * @return
	 */
	public Date getDate(){
		return date;
	}
}
