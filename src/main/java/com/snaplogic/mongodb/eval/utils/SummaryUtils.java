package com.snaplogic.mongodb.eval.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snaplogic.mongodb.eval.dtos.LogEntry;
import com.snaplogic.mongodb.eval.dtos.SummaryLogEntry;

public class SummaryUtils {

	public static List<SummaryLogEntry> parseQueryResults(List<LogEntry> logEntries) 
	{
		List<SummaryLogEntry> summaryLogEntries = new ArrayList<SummaryLogEntry>();
		
		Map<String, SummaryLogEntry> uniqueLogEntries = new HashMap<String, SummaryLogEntry>();

		SummaryLogEntry summaryLogEntry = null;
		for(LogEntry logEntry: logEntries)
		{
			if(!uniqueLogEntries.containsKey(logEntry.getQueryHash()))
			{
				summaryLogEntry = new SummaryLogEntry(logEntry);
				uniqueLogEntries.put(logEntry.getQueryHash(), summaryLogEntry);
			}
			else
			{
				summaryLogEntry = uniqueLogEntries.get(logEntry.getQueryHash());
				summaryLogEntry.addOne(logEntry);
				uniqueLogEntries.replace(logEntry.getQueryHash(), summaryLogEntry);
			}
		}
		
		for(SummaryLogEntry sle : uniqueLogEntries.values())
		{
			sle.setAverageDuration(sle.calculateAverage());
			summaryLogEntries.add(sle);
		}
		
		return summaryLogEntries;
	}
}
