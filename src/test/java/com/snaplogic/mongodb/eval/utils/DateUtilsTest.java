package com.snaplogic.mongodb.eval.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;


/**
 * Test Class for the DateUtils class.
 * @author bgoff
 * @since 18 Dec 2024
 */
class DateUtilsTest {

	/**
	 * Method to test getting the Start Date from the String supplied by the GUI.
	 */
	@Test
	void testGetStartDateFromGuiString() 
	{	
		try
		{
			String guiDateString = "12/15/2024 00:00:00  - 12/18/2024 23:59:00";
			Date expectedDate = DateUtils.toDate("12/15/2024 00:00:00");
			Date startDate = DateUtils.getStartDateFromGuiString(guiDateString);
			
			if(expectedDate.equals(startDate))
			{
				System.out.println("Got the expected StartDate (" + DateUtils.toString(startDate) + 
						": " + DateUtils.toString(expectedDate) + ")");
			}
			else
			{	
				StringBuilder sb = new StringBuilder("Did NOT get the expected start date from (" + guiDateString + ")]n");
				sb.append("Expected (" + DateUtils.toString(expectedDate) + ")\n");
				sb.append("But got (" + DateUtils.toString(startDate) + ")");
				System.out.println(sb.toString());
			}
			
			assertTrue(expectedDate.equals(startDate));
		}
		catch(ParseException pe)
		{	
			pe.printStackTrace();
			fail("Failed to parse the GUI Date String into a start date!");
		}
	}
	
	/**
	 * Method used to test the DateUtils method to get the expected start date.
	 */
	@Test
	void testGetDateBeforeNnumberOfDays() {
		try
		{
			String guiDateString = "12/10/2024 00:00:00";
			
			Date expectedDate = DateUtils.toDate("12/07/2024 00:00:00");
			Date startDate = DateUtils.getNDaysBeforeDate(DateUtils.toDate(guiDateString), 3);
			
			if(expectedDate.equals(startDate))
			{
				System.out.println("Got the expected StartDate (" + DateUtils.toString(startDate) + ": " + 
						DateUtils.toString(expectedDate) + ")");
			}
			else
			{	
				StringBuilder sb = new StringBuilder("Did NOT get the expected start date from (" + guiDateString + ")]n");
				sb.append("Expected (" + DateUtils.toString(expectedDate) + ")\n");
				sb.append("But got (" + DateUtils.toString(startDate) + ")");
				System.out.println(sb.toString());
			}
			
			assertTrue(expectedDate.equals(startDate));
		}
		catch(ParseException pe)
		{	
			pe.printStackTrace();
			fail("Failed to parse a date 3 days before date given!");
		}
	}

}
