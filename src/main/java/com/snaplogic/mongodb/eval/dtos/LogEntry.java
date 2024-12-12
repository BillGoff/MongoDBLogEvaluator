package com.snaplogic.mongodb.eval.dtos;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogEntry implements Serializable {

	private static final long serialVersionUID = -464978817922751351L;
	
	private String cmd;
	private String collection;
	private Integer docsExamined;
	private Integer duration;
	private String env;
	
	@Id
	public String id;
	
	private Integer keysExamined;
	private Date logEntryDate;
	private String msg;
	private String node;
	private Integer nreturned;
	private Integer numYelds;
	private String planCacheKey;
	private Integer planningTimeMicros;
	private String planSummary;
	private String queryHash;
	private String readPreference;
	private String remote;
	private Integer reslen;
	
	public String getCmd() {
		return cmd;
	}
	public String getCollection() {
		return collection;
	}
	public Integer getDocsExamined() {
		return docsExamined;
	}
	public Integer getDuration() {
		return duration;
	}
	public String getEnv() {
		return env;
	}
	public String getId() {
		return id;
	}
	public Integer getKeysExamined() {
		return keysExamined;
	}
	public Date getLogEntryDate() {
		return logEntryDate;
	}
	public String getMsg() {
		return msg;
	}
	public String getNode() {
		return node;
	}
	public Integer getNreturned() {
		return nreturned;
	}
	public Integer getNumYelds() {
		return numYelds;
	}
	public String getPlanCacheKey() {
		return planCacheKey;
	}
	public Integer getPlanningTimeMicros() {
		return planningTimeMicros;
	}
	public String getPlanSummary() {
		return planSummary;
	}
	public String getQueryHash() {
		return queryHash;
	}
	public String getReadPreference() {
		return readPreference;
	}
	public String getRemote() {
		return remote;
	}
	public Integer getReslen() {
		return reslen;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public void setDocsExamined(Integer docsExamined) {
		this.docsExamined = docsExamined;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setKeysExamined(Integer keysExamined) {
		this.keysExamined = keysExamined;
	}
	public void setLogEntryDate(Date logEntryDate) {
		this.logEntryDate = logEntryDate;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public void setNreturned(Integer nreturned) {
		this.nreturned = nreturned;
	}
	public void setNumYelds(Integer numYelds) {
		this.numYelds = numYelds;
	}
	public void setPlanCacheKey(String planCacheKey) {
		this.planCacheKey = planCacheKey;
	}
	public void setPlanningTimeMicros(Integer planningTimeMicros) {
		this.planningTimeMicros = planningTimeMicros;
	}
	public void setPlanSummary(String planSummary) {
		this.planSummary = planSummary;
	}
	public void setQueryHash(String queryHash) {
		this.queryHash = queryHash;
	}
	public void setReadPreference(String readPreference) {
		this.readPreference = readPreference;
	}
	public void setRemote(String remote) {
		this.remote = remote;
	}
	public void setReslen(Integer reslen) {
		this.reslen = reslen;
	}
	
}
