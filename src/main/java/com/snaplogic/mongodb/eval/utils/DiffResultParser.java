package com.snaplogic.mongodb.eval.utils;

import java.util.ArrayList;
import java.util.List;


import com.snaplogic.mongodb.eval.dtos.DiffResult;
import com.snaplogic.mongodb.eval.dtos.SummaryLogEntry;

public class DiffResultParser {
	
	public static List<DiffResult> parseSummaryResultsIntoDiffResults(List<SummaryLogEntry> orgEntries, 
			List<SummaryLogEntry> newEntries)
	{
		List<DiffResult> diffResults = new ArrayList<DiffResult>();
		DiffResult diffEntry = null;
		
		for(SummaryLogEntry newEntry: newEntries)
		{
			for(SummaryLogEntry orgEntry: orgEntries)
			{
				if(newEntry.getQueryHash().equalsIgnoreCase(orgEntry.getQueryHash()))
				{
					diffEntry = new DiffResult(orgEntry, newEntry);
					diffResults.add(diffEntry);
					break;
				}
			}
		}
		return diffResults;
	}

}
