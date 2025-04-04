package com.snaplogic.mongodb.eval.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.snaplogic.mongodb.eval.utils.DateUtils;
import com.snaplogic.mongodb.eval.dtos.Query;


/**
 * Test Class for the QueryRepo class.
 * 
 * @author bgoff
 * @since 20 Feb 2025
 */
class QueryRepoTest {

	/**
	 * This test method checks to make sure we can parse the date and time strings into an actual date.
	 */
//	@Test
//	void buildDateTest() 
//	{
//		try
//		{
//			Date expectedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2025-02-20 23:59:59");
//
//			String dateString = "02/20/2025";
//			String timeString = "23:59:59";
//			
//			QueryRepo repo = new QueryRepo();
//			Date actualDate = repo.buildDate(dateString, timeString);
//			
//			if(expectedDate.equals(actualDate))
//			{
//				System.out.println("Got the expected Date (" + DateUtils.toString(actualDate) + ")");
//			}
//			else
//			{	
//				StringBuilder sb = new StringBuilder("Did NOT get the expected date from (" + dateString + " " + 
//						timeString + ")]n");
//				sb.append("Expected (" + DateUtils.toString(expectedDate) + ")\n");
//				sb.append("But got (" + DateUtils.toString(actualDate) + ")");
//				System.out.println(sb.toString());
//			}
//			assertTrue(expectedDate.equals(actualDate));
//		}
//		catch(Exception pe)
//		{	
//			pe.printStackTrace();
//			fail("Failed to parse the date and time strings into an actual date!");
//		}
//	}
	
//	@Test
//	void buildDateRangeTest()
//	{
//		try
//		{
//			Query query = new Query();
//			query.setStartDateString("02/20/2025");
//			query.setStartTime("00:00:00");
//			
//			query.setEndDateString("02/24/2025");
//			query.setEndTime("23:59:59");
//			
//			QueryRepo repo = new QueryRepo();
//
//			Criteria dateRangeCriteria = repo.buildDateRange(query);
//			MatchOperation matchStage = Aggregation.match(dateRangeCriteria);
//			Aggregation agg = Aggregation.newAggregation(matchStage);
//
//			System.out.println("dateRangeCriteria: " + agg.toString());
//			
//		}
//		catch(Exception pe)
//		{	
//			pe.printStackTrace();
//			fail("Failed to create the Criteria for the date range!");
//		}
//	}
}
