package com.snaplogic.mongodb.eval.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

	@Test
	void testGetHashes() {
		String queryHashes = "CED8E558, 3051D46A, DC80E27C, 3B2A5814";
		try
		{
			List<String> expectedList = new ArrayList<String>();
			
			expectedList.add("3051D46A");
			expectedList.add("3B2A5814");
			expectedList.add("CED8E558");
			expectedList.add("DC80E27C");
			
			List<String> actualList = StringUtils.getHashes(queryHashes);
			
//			for(String queryHash: actualList)
//				System.out.println(queryHash);
			
	        assertEquals(expectedList, actualList);
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			fail("Failed in the evaluation of the parsing of the query hashes!");
		}
	}

}
