package com.snaplogic.mongodb.eval.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
	
	
	/**
	 * This method is used to convert the comma separated list of Strings into a list of Strings.  Note: I do sort 
	 * it for repeatability and debugging.
	 * @param queryHashes String which contains the comma separated query hashes we want the list for.
	 * @return List of query hashes.
	 */
	public static List<String> getHashes(String queryHasheString)
	{
		HashSet<String> queryHashSet = new HashSet<String>();
		
		String[] elements = queryHasheString.split(",");
        
		for(String element: elements)
			queryHashSet.add(element.trim());
		
		List<String> queryHashList = new ArrayList<String> (queryHashSet);
		
		Collections.sort(queryHashList);
		
		return queryHashList;
	}
}
