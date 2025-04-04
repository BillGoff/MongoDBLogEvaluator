package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;


public class Query implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 6008647628545478911L;
	
	public Query clone() throws CloneNotSupportedException {
		return (Query) super.clone();
	}
	
	private String cluster;
	private String collection;
	
	private String endDateString;

	private String endTime;
	private String env;
	private String orgEndDateString;
	private String orgStartDateString;
	private String queryHashes;
	
	private String queryType;
	
	private String startDateString;
	private String startTime;
	
	public String getCluster() {
		return cluster;
	}
	public String getCollection() {
		return collection;
	}
	public String getDateRange() {
		return (this.getStartDateString() + " " + this.getStartTime() + " - " + 
				this.getEndDateString() + " " + this.getEndTime());
	}
	public String getEndDateString() {
		return endDateString;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public String getEnv() {
		return env;
	}
	public String getOrgEndDateString() {
		return orgEndDateString;
	}

	public String getOrgStartDateString() {
		return orgStartDateString;
	}
	public String getQueryHashes() {
		return queryHashes;
	}
	
	public String getQueryType() {
		return queryType;
	}
	public String getStartDateString() {
		return startDateString;
	}

	public String getStartTime() {
		return startTime;
	}
	
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

//	public void setDateRange(String dateRange) {
//		this.dateRange = dateRange;
//	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setEnv(String env) {
		this.env = env;
	}
	public void setOrgEndDateString(String orgEndDateString) {
		this.orgEndDateString = orgEndDateString;
	}
	public void setOrgStartDateString(String orgStartDateString) {
		this.orgStartDateString = orgStartDateString;
	}
	public void setQueryHashes(String queryHashes) {
		this.queryHashes = queryHashes;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
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

		sb.append(tabs + "cluster:     " + this.getCluster() + "\n");
		sb.append(tabs + "collection:  " + this.getCollection() + "\n");
		sb.append(tabs + "env:         " + this.getEnv() + "\n");
		sb.append(tabs + "queryType:   " + this.getQueryType() + "\n");
		sb.append(tabs + "queryHashes: " + this.getQueryHashes() + "\n");
		
		sb.append(tabs + "startDateString: " + this.getStartDateString() + "\n"); 
		sb.append(tabs + "orgStartDateString: " + this.getOrgStartDateString() + "\n");
		
		sb.append(tabs + "startTime: " + this.getStartTime() + "\n");
			
		sb.append(tabs + "endDateString: " + this.getEndDateString() + "\n"); 
		sb.append(tabs + "orgEndDateString: " + this.getOrgEndDateString() + "\n");
		sb.append(tabs + "endime: " + this.getEndTime() + "\n");

		return (sb.toString());
	}
}
