package com.snaplogic.mongodb.eval.utils;

import java.util.concurrent.TimeUnit;

public class StringUtils {

	public static String msToTime(long milliseconds)
	{
		// Using TimeUnit
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        long millis = milliseconds % 1000;

       return(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis));
	}
}
