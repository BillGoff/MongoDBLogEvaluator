package com.snaplogic.mongodb.eval.dtos;

/**
 * This Data Transfer Object (DTO) is used to hold a Summary of Log Entries.
 * @author bgoff
 *
 */
public class SummaryLogEntry implements Comparable <SummaryLogEntry> {
	
	private Integer averageDuration = Integer.valueOf(0);

	private String cmd;
	private Integer duration = Integer.valueOf(0);

	private Integer highDuration = Integer.valueOf(0);
	
	private Integer lowDuration = Integer.valueOf(0);

	private String planSummary;
	private String queryHash;

	private Integer sum = Integer.valueOf(0);

	public SummaryLogEntry()
	{}

	public SummaryLogEntry(LogEntry entry)
	{
		this.setQueryHash(entry.getQueryHash());
		this.setPlanSummary(entry.getPlanSummary());
		this.setCmd(entry.getCmd());
		
		this.sum = Integer.valueOf(1);
		this.duration = entry.getDuration();
		this.highDuration = entry.getDuration();
		this.lowDuration = entry.getDuration();
	}

	public void addOne(LogEntry entry)
	{
		this.setSum(this.getSum() + 1);
		
		if(entry.getDuration() > this.highDuration)
			this.highDuration = entry.getDuration();
		if(entry.getDuration() < this.lowDuration)
			this.lowDuration = entry.getDuration();
		
		this.setDuration(this.getDuration() + entry.getDuration());
	}
	
	public Integer calculateAverage()
	{
		return (duration.intValue() / sum.intValue());
	}
	
	@Override
	public int compareTo(SummaryLogEntry o) {
		if(this.calculateAverage() >= o.calculateAverage())
			return 0;
		else return 1;
	}
	public Integer getAverageDuration() {
		return averageDuration;
	}
	public String getCmd() {
		return cmd;
	}
	public Integer getDuration() {
		return duration;
	}

	public Integer getHighDuration() {
		return highDuration;
	}

	public Integer getLowDuration() {
		return lowDuration;
	}

	public String getPlanSummary() {
		return planSummary;
	}

	public String getQueryHash() {
		return queryHash;
	}

	public Integer getSum() {
		return sum;
	}

	public void setAverageDuration(Integer averageDuration) {
		this.averageDuration = averageDuration;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public void setHighDuration(Integer highDuration) {
		this.highDuration = highDuration;
	}
	
	public void setLowDuration(Integer lowDuration) {
		this.lowDuration = lowDuration;
	}
	
	public void setPlanSummary(String planSummary) {
		this.planSummary = planSummary;
	}
	
	public void setQueryHash(String queryHash) {
		this.queryHash = queryHash;
	}
	
	public void setSum(Integer sum) {
		this.sum = sum;
	}

}
