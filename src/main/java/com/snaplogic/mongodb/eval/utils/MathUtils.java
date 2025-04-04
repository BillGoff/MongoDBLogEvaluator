package com.snaplogic.mongodb.eval.utils;

import java.math.*;

public class MathUtils {

	
	public static Double calculatePercentage(Integer orgValue, Integer newValue)
	{
		double improvemenmt = ( (orgValue.doubleValue() - newValue.doubleValue()) / orgValue.doubleValue());
		
		improvemenmt = improvemenmt * 100;
		
		BigDecimal temp = new BigDecimal(improvemenmt).setScale(2, RoundingMode.DOWN);
		
		return temp.doubleValue();
	}
	
}
