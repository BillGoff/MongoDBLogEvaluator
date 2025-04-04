package com.snaplogic.mongodb.eval.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MathUtilsTest {

	@Test
	void testCalculatePositivePercentage() 
	{
		try
		{
			Integer orgValue = Integer.valueOf(699890);
			Integer newValue = Integer.valueOf(26617);
		
			Double expectedResult = Double.valueOf(96.19);
		
			Double actualResult = MathUtils.calculatePercentage(orgValue, newValue);
			
			if(expectedResult.equals(actualResult))
				System.out.println("Got the expected percetnage (" + expectedResult + ")");
			else
				System.out.println("Did NOT get the expected percetnage!\n" + 
						"	expected:  " + expectedResult + "\n" +
						"	  actual:  " + actualResult);
			assertTrue(expectedResult.equals(actualResult));
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			fail("Unexpected error occurred while attempting to parse values into percentage!");
		}
	}
	
	@Test
	void testCalculateNegativePercentage() 
	{
		try
		{

			Integer orgValue = Integer.valueOf(13553);
			Integer newValue = Integer.valueOf(39065);
		
			Double expectedResult = Double.valueOf(-188.23);
		
			Double actualResult = MathUtils.calculatePercentage(orgValue, newValue);
			
			if(expectedResult.equals(actualResult))
				System.out.println("Got the expected percetnage (" + expectedResult + ")");
			else
				System.out.println("Did NOT get the expected percetnage!\n" + 
						"	expected:  " + expectedResult + "\n" +
						"	  actual:  " + actualResult);
			assertTrue(expectedResult.equals(actualResult));
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			fail("Unexpected error occurred while attempting to parse values into percentage!");
		}
	}
}
