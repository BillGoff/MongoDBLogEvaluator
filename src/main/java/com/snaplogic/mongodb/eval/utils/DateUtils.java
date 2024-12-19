package com.snaplogic.mongodb.eval.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	//11/25/2024 - 12/02/2024
	private static final String dateFormatPattern = "MM/dd/yyyy HH:mm:ss";
	
	public static final String defaultDateFormat = "MM/dd/yyyy";
	
	public enum DATE_FIELD {
		START (0),
		END (1);

        private final int value;

		DATE_FIELD (final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }
	
	
	/**
	 * (U) This method is used to compute the difference between dates. It produces a nice readable formatted
	 * String.
	 *
	 * @param date1 Date to compare to the second date passed in.
	 * @param date2 Date to compare to the first date passed in.
	 * @return String a nicely formatted human readable string stating the difference between date 1 and date 2
	 *         passed in.
	 */
	public static String computeDiff(Date date1, Date date2)
	{
		Objects.requireNonNull(date1, "Unable to compute the difference from a null date (date1)!");
		Objects.requireNonNull(date2, "Unable to compute the difference from a null date (date2)!");
		
		long diffInMillies = date2.getTime() - date1.getTime();
		List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
		
		Collections.reverse(units);
		Map<TimeUnit, Long> result = new TreeMap<>();
		long milliesRest = diffInMillies;
		
		for (TimeUnit unit : units)
		{
			long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
			long diffInMilliesForUnit = unit.toMillis(diff);
			milliesRest = milliesRest - diffInMilliesForUnit;
			result.put(unit, diff);
		}
		
		StringBuilder message = new StringBuilder();
		Set<TimeUnit> timeUnitsSet = result.keySet();
		List<TimeUnit> timeUnits = new ArrayList<>(timeUnitsSet);
		
		Collections.sort(timeUnits, Collections.reverseOrder());
		
		long timeUnitValue;
		
		for (TimeUnit timeUnit : timeUnits)
		{
			timeUnitValue = result.get(timeUnit);
			
			if (timeUnitValue > 0)
			{
				if (message.length() > 0)
				{
					message.append(", ");
				}
				message.append(timeUnitValue + " " + timeUnit);
			}
		}
		
		if (message.length() == 0)
		{
			message.append("0 MILLISECONDS");
		}
		return message.toString();
	}
	/**
	 * Convenience method used by the gui.
	 * @param date Date that we are attempting to get two weeks before.
	 * @return Date that is two weeks before the date supplied.
	 */
	public static Date getDateBeforeTwoWeeks(Date date) {
	    return getNDaysBeforeDate(date, 14);
	}

	/**
	 * This method can be used to get the GUI string for a two week period.
	 * @return
	 */
	public static String getGuiDateForNDaysFromNow (Integer days)
	{
		Date rightNow = Calendar.getInstance().getTime();
		Date startDate = getNDaysBeforeDate(rightNow, days);
		
		StringBuilder sb = new StringBuilder(toString(startDate) + " - " + toString(rightNow));
		
		return sb.toString();
	}
	
	/**
	 * This method is used to get the date that is N days from date supplied.
	 * @param date Date to get new date from.
	 * @param days Integer for the number of days we want the date to be before the date supplied.
	 * @return Date the date that is N days before the date supplied.
	 */
	public static Date getNDaysBeforeDate(Date date, Integer days)
	{
		 Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.DATE, -days); //2 weeks
		    return calendar.getTime();
	}
	
	/**
	 * This method is used to parse out the start date from the GUI's form.
	 * @param guiString String value, something like (11/20/2024 - 12/04/2024).  We want the first date in this case.
	 * @return Return the Date representation of the first String date value.
	 * @throws ParseException if we are unable to parse the string value.
	 */
	public static Date getStartDateFromGuiString(String guiString) throws ParseException
	{
		return (getDateFromGuiString(guiString, DATE_FIELD.START));
	}
	
	/**
	 * This method is used to parse out the end date from the GUI's form.
	 * @param guiString String value, something like (11/20/2024 - 12/04/2024).  We want the second date in this case.
	 * @return Return the Date representation of the second String date value.
	 * @throws ParseException if we are unable to parse the string value.
	 */
	public static Date getEndDateFromGuiString(String guiString) throws ParseException
	{		
		return (getDateFromGuiString(guiString, DATE_FIELD.END));		
	}
	
	public static Date getDateFromGuiString(String guiString, DATE_FIELD dateWanted) throws ParseException
	{
		String [] tokens = guiString.split(" - ");
		
		return (toDate(tokens[dateWanted.getValue()]));
		
	}
	
	/**
	 * (U) This method gets the current date.
	 *
	 * @return Date the current date/time.
	 */
	public static Date rightNowDate()
	{
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	/**
	 * This method is used to convert a String to a java.util.Date.
	 * @param dateString String to convert to the date.
	 * @return Date 
	 * @throws ParseException 
	 */
	public static Date toDate(String dateString) throws ParseException 
	{
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatPattern, Locale.ENGLISH);

		return formatter.parse(dateString);
	}
	
	/**
	 * (U) This method converts a date into a nice readable String.
	 *
	 * @param myDate java.util.Date to produce a readable string from.
	 * @return String the readable format of the date.
	 */
	public static String toString(Date myDate)
	{
		Objects.requireNonNull(myDate, "Cannot convert a null java.util.Date to a String!");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
		
		return (simpleDateFormat.format(myDate));
	}
	
	/**
	 * (U) This method converts a date into a nice readable String.
	 *
	 * @param myDate java.util.Date to produce a readable string from.
	 * @return String the readable format of the date.
	 */
	public static String toString(Date myDate, String format)
	{
		Objects.requireNonNull(myDate, "Cannot convert a null java.util.Date to a String!");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		
		return (simpleDateFormat.format(myDate));
	}
}
