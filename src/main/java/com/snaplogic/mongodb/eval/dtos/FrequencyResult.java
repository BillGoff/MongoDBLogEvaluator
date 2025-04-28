package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;

/**
 * This Data Transfer Object (DTO) contains the fields that tells us how often each query is seen.
 * @author bgoff
 *
 */
public class FrequencyResult implements Serializable {
	
	private static final long serialVersionUID = 5125913863198964634L;
	
	private Integer count;
	private String queryHash;

	public FrequencyResult()
	{}

	public Integer getCount() {
		return count;
	}

	public String getQueryHash() {
		return queryHash;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setQueryHash(String queryHash) {
		this.queryHash = queryHash;
	}
	
	/**
	 * (U) Method used to printout the values of this Class.
	 *
	 * @return String nice readable string value of this class.
	 */
	@Override
	public String toString()
	{
		return (toString(""));
	}

	/**
	 * (U) Convenience method to make the string printed out indent as intended.
	 *
	 * @param tabs String value for the tabs used for indentation.
	 * @return String nice readable string value of this class.
	 */
	public String toString(String tabs)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(tabs + "QueryHash: " + getQueryHash() + ", count: " + getCount() + "\n");
		return sb.toString();
	}

}