package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;

import com.snaplogic.mongodb.eval.utils.MathUtils;

public class DiffResult implements Serializable {
		
	private static final long serialVersionUID = 7470725505586840417L;

	private String cmd;
	
	private Double improvment = Double.valueOf(0);
	private Integer newAverageDuration = Integer.valueOf(0);
	private Integer newHighDuration = Integer.valueOf(0);
	private String newPlanSummary;
	private Integer newSum = Integer.valueOf(0);
	private Integer orgAverageDuration = Integer.valueOf(0);
	private Integer orgHighDuration = Integer.valueOf(0);
	private String orgPlanSummary;
	private Integer orgSum = Integer.valueOf(0);
	private String queryHash;

	public DiffResult()
	{}

	public DiffResult(SummaryLogEntry orgEntry, SummaryLogEntry newEntry)
	{
		if(orgEntry.getQueryHash().equalsIgnoreCase(newEntry.getQueryHash()))
		{
			this.setCmd(orgEntry.getCmd());
			this.setQueryHash(newEntry.getQueryHash());
			
			this.setNewAverageDuration(newEntry.getAverageDuration());
			this.setOrgAverageDuration(orgEntry.getAverageDuration());

			this.setNewHighDuration(newEntry.getHighDuration());
			this.setOrgHighDuration(orgEntry.getHighDuration());
			
			this.setNewPlanSummary(newEntry.getPlanSummary());
			this.setOrgPlanSummary(orgEntry.getPlanSummary());
			
			this.setNewSum(newEntry.getSum());
			this.setOrgSum(orgEntry.getSum());
			
			
			this.setImprovment(MathUtils.calculatePercentage(orgEntry.getHighDuration(), newEntry.getHighDuration()));
			
		}
	}

	public String getCmd() {
		return cmd;
	}

	public Double getImprovment() {
		return improvment;
	}

	public Integer getNewAverageDuration() {
		return newAverageDuration;
	}

	public Integer getNewHighDuration() {
		return newHighDuration;
	}

	public String getNewPlanSummary() {
		return newPlanSummary;
	}

	public Integer getNewSum() {
		return newSum;
	}

	public Integer getOrgAverageDuration() {
		return orgAverageDuration;
	}

	public Integer getOrgHighDuration() {
		return orgHighDuration;
	}

	public String getOrgPlanSummary() {
		return orgPlanSummary;
	}

	public Integer getOrgSum() {
		return orgSum;
	}

	public String getQueryHash() {
		return queryHash;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public void setImprovment(Double improvment) {
		this.improvment = improvment;
	}
	public void setNewAverageDuration(Integer newAverageDuration) {
		this.newAverageDuration = newAverageDuration;
	}

	public void setNewHighDuration(Integer newHighDuration) {
		this.newHighDuration = newHighDuration;
	}


	public void setNewPlanSummary(String newPlanSummary) {
		this.newPlanSummary = newPlanSummary;
	}
	public void setNewSum(Integer newSum) {
		this.newSum = newSum;
	}
	public void setOrgAverageDuration(Integer orgAverageDuration) {
		this.orgAverageDuration = orgAverageDuration;
	}

	public void setOrgHighDuration(Integer orgHighDuration) {
		this.orgHighDuration = orgHighDuration;
	}
	
	public void setOrgPlanSummary(String orgPlanSummary) {
		this.orgPlanSummary = orgPlanSummary;
	}
	
	public void setOrgSum(Integer orgSum) {
		this.orgSum = orgSum;
	}
	
	public void setQueryHash(String queryHash) {
		this.queryHash = queryHash;
	}
	
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
		
		sb.append(tabs + "QueryHash:       " + this.getQueryHash() + "\n");
		sb.append(tabs + "Query:           " + this.getCmd() + "\n");
		sb.append(tabs + "org Count:       " + this.getOrgSum() + "\n");
		sb.append(tabs + "new Count:       " + this.getNewSum() + "\n");

		sb.append(tabs + "Improvement:     " + this.getImprovment() + "\n");
		sb.append(tabs + "orgAveDuration:  " + this.getOrgAverageDuration() + "\n");
		sb.append(tabs + "newAveDuration:  " + this.getNewAverageDuration() + "\n");
		sb.append(tabs + "orgHighDuration: " + this.getOrgHighDuration() + "\n");
		sb.append(tabs + "newHighDuration: " + this.getNewHighDuration() + "\n");
		
		sb.append(tabs + "org Query Plan : " + this.getOrgPlanSummary() + "\n");
		sb.append(tabs + "new Query Plan : " + this.getNewPlanSummary() + "\n");
		
		return (sb.toString());
	}
}