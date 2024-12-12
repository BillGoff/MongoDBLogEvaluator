package com.snaplogic.mongodb.eval.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snaplogic.mongodb.eval.dtos.LogEntry;

public interface LogEntryRepo extends MongoRepository<LogEntry, String> {

	List<LogEntry> findBylogEntryDateBetween(Date startDate, Date endDate);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollection(Date startDate, Date endDate, String collection);
	
	List<LogEntry> findBylogEntryDateBetweenAndEnv(Date startDate, Date endDate, String env);
	
	List<LogEntry> findBylogEntryDateBetweenAndNode(Date startDate, Date endDate, String node);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndEnv(Date startDate, Date endDate, String collection, String env);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndNode(Date startDate, Date endDate, String collection, String node);

	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndEnvAndNode(Date startDate, Date endDate, String collection, String env, String node);
	
	List<LogEntry> findBylogEntryDateBetweenAndEnvAndNode(Date startDate, Date endDate, String env, String node);

}
