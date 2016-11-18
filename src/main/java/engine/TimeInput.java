package engine;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TimeInput {
	// instance of date
	private Date date;

	public TimeInput(int day, int month, int year) {
		this("d/M/yyyy", day + "/" + month + "/" + year);
	}

	public TimeInput(int day, int month, int year, int hour, int minute) {
		this("d/M/yyyy h:m", day + "/" + month + "/" + year + " " + hour + ":" + minute);
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
		long startingTime = dayBefore.getTime();
		long range = maxDayAfter * (24 * 60 * 60 * 1000);
		long randomTime = startingTime + (new Random().nextLong() % range);
		randomTime += startingTime; 
		return new Date(randomTime);
	}

	public static Date getRandomDayBefore(Date dayAfter, int maxDayBefore) {
		long endingTime = dayAfter.getTime();
		long range = maxDayBefore * (24 * 60 * 60 * 1000);
		long startingTime = endingTime - range;
		long randomTime = startingTime + (new Random().nextLong() % range);
		randomTime += startingTime; 
		return new Date(randomTime);
	}
}
