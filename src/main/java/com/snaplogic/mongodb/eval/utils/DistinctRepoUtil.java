package com.snaplogic.mongodb.eval.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.snaplogic.mongodb.eval.dtos.LogEntry;

/**
 * This helper class encapsulates the query for distinct field values into one place.
 * @author bgoff
 * @since 2 Dec 2024
 */
public class DistinctRepoUtil 
{
	private static final Logger logger = LogManager.getLogger(DistinctRepoUtil.class);

	/**
	 * List of Disctict Fields, that we allow the user to query for.
	 * @author bgoff
	 */
	public enum DistictFields {
		COLLECTION,
		ENV,
		NODE,
	}
	
	/**
	 * This method is used to get all the distinct values from the MongoDB for a specific field.
	 * @param mongoTemplate MongoTemplate to use.
	 * @param fieldName String value of the field we want all the distinct values for.
	 * @return List of distinct String values.
	 */
	public List<String> getDistinctValues(MongoTemplate mongoTemplate, DistictFields fieldName)
	{
		Date envStartDate = DateUtils.rightNowDate();
		
		logger.debug("Getting distinct values for " + fieldName.toString());
		
		List<String> distinctValues = new ArrayList<String>();
		
		List<String> temp = mongoTemplate.query(LogEntry.class).distinct(fieldName.toString().toLowerCase()).as(
				String.class).all();
		
		for(String tempString: temp)
		{
			//Lets remove nulls and empty values from the list.
			if((tempString != null) && (!tempString.trim().isEmpty()))
				distinctValues.add(tempString.trim());
		}
		//Sort these..  Before we add "All" to the beginning.
		Collections.sort(distinctValues);

		distinctValues.add(0, "All");
				
		logger.debug("It took " + DateUtils.computeDiff(envStartDate,
				DateUtils.rightNowDate()) + " to get the list of Distinct " + fieldName.toString() + "!");
		
		return(distinctValues); 
	}
	
}
