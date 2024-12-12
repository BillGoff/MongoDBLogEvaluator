package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;

public class Query implements Serializable {
	
	private static final long serialVersionUID = 6008647628545478911L;
	
	private String cluster;
	private String collection;
	private String dateRange;
	private String env;
	private String queryType;
	
	public String getCluster() {
		return cluster;
	}
	public String getCollection() {
		return collection;
	}
	public String getDateRange() {
		return dateRange;
	}
	public String getEnv() {
		return env;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
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

		sb.append(tabs + "cluster:    " + this.getCluster() + "\n");
		sb.append(tabs + "collection: " + this.getCollection() + "\n");
		sb.append(tabs + "dateRange:  " + this.getDateRange() + "\n");
		sb.append(tabs + "env:        " + this.getEnv() + "\n");
		sb.append(tabs + "queryType:  " + this.getQueryType() + "\n");
		
		return (sb.toString());
	}
}
