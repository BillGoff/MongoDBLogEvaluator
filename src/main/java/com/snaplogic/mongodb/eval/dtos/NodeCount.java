package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;

public class NodeCount implements Serializable {
	
	private static final long serialVersionUID = 8613009644464462263L;
	
	private Integer count;
	private String node;
	private String operation;
	
	public Integer getCount() {
		return count;
	}
	public String getNode() {
		return node;
	}
	public String getOperation() {
		return operation;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public void setOperation(String operation) {
		this.operation = operation;
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
		
		sb.append(tabs + "Node: " + getNode() + "\n");
		sb.append(tabs + "Count" + getCount() + "\n");
		sb.append(tabs + "Operation: " + getOperation() + "\n");
		
		return sb.toString();
	}
	
}
