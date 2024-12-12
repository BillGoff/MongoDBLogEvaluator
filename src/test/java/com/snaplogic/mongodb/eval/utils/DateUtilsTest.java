package com.snaplogic.mongodb.eval.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;


class DateUtilsTest {

	@Test
	void testGetStartDateFromGuiString() 
	{	
		try
		{
			String guiDateString = "11/20/2024 - 12/04/2024";
			Date expectedDate = DateUtils.toDate("11/20/2024");
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
	
	@Test
	void testGetEndDateFromGuiString() {
	}

}
