package engine;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class TimeInput {
	// instance of date
	private Date date;

	public TimeInput(int day, int month, int year) {
		this("d/M/yyyy", day + "/" + month + "/" + year);
	}

	public TimeInput(int day, int month, int year, int hour, int minute) {
		this("d/M/yyyy HH:mm", day + "/" + month + "/" + year + " " + hour + ":" + minute);
	}

	/**
	 * Constructor that use default date format
	 * 
	 * @param date
	 */
	public TimeInput(String date) {
		this(Constants.DEFAULT_TIME_FORMAT, date);
	}

	/**
	 * Constructor that use user defined date format
	 * 
	 * @param format
	 * @param date
	 */
	public TimeInput(String format, String date) {
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
	public TimeInput(Date date) {
		this.date = date;
	}

	/**
	 * Returning date
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	public static Date getRandomDate(Date from, Date to) {
		if(from.after(to)){	// swap if it is wrong
			Date tmp = to;
			to = from;
			from = tmp;
		}
		long range = to.getTime() - from.getTime();
		long randomTime = from.getTime() + (new Random().nextLong() % range);
		return new Date(randomTime);
	}

	public static Date getRandomDayAfter(Date dayBefore, int maxDayAfter) {
		return getDayAfter(dayBefore, Probability.getInstance().getRadom().nextInt(maxDayAfter)+1);
	}

	public static Date getRandomDayBefore(Date dayAfter, int maxDayBefore) {
		return getDayAfter(dayAfter, -(Probability.getInstance().getRadom().nextInt(maxDayBefore)+1));
	}
	
	/**
	 * Also works for get day before if parameter dayAfter is negative
	 * @param actualDate
	 * @param dayAfter
	 * @return
	 */
	public static Date getDayAfter(Date actualDate, int dayAfter) {
		DateTime actual = new DateTime(actualDate);
		actual = actual.plusDays(dayAfter);
		return actual.toDate();
	}
	
	public static Date getMonthAfter(Date actualDate, int monthAfter) {
		DateTime actual = new DateTime(actualDate);
		actual = actual.plusMonths(monthAfter);
		return actual.toDate();
	}
	
	public static Date getRandomDayInRange(Date startDate, Date endDate){
		DateTime actual = new DateTime(startDate);
		int dayCount = Math.abs(Days.daysBetween(actual, new DateTime(endDate)).getDays());
		if(dayCount == 0){
			dayCount = 1;
		}
		actual = actual.plusDays(new Random().nextInt(dayCount));
		return actual.toDate();
	}
}
