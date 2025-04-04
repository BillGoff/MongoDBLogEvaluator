package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snaplogic.mongodb.eval.utils.DateUtils;

@Document("logEntry")
public class Stat implements Serializable {
	
	private static final long serialVersionUID = -8827797480902219653L;
	
	private String cluster;
	private int count;
	private Date logEntryDate;
	private long millieseconds;

	
	public String getCluster() {
		return cluster;
	}
	public int getCount() {
		return count;
	}
	public Date getLogEntryDate() {
		return logEntryDate;
	}
	public long getMillieseconds() {
		return millieseconds;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setLogEntryDate(Date logEntryDate) {
		this.logEntryDate = logEntryDate;
	}
	public void setMillieseconds(long millieseconds) {
		this.millieseconds = millieseconds;
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

		sb.append(tabs + "cluster:      " + this.getCluster() + "\n");
		sb.append(tabs + "logEntryDate: " + DateUtils.toString(this.getLogEntryDate()) + "/n");
		sb.append(tabs + "count:        " + this.getCount() + "\n");

		return (sb.toString());
	}
}
