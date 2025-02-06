package com.snaplogic.mongodb.eval.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReadPreference;

import com.snaplogic.mongodb.eval.dtos.LogEntry;

@ReadPreference("secondaryPreferred") 
public interface LogEntryRepo extends MongoRepository<LogEntry, String> {

	List<LogEntry> findBylogEntryDateBetween(Date startDate, Date endDate);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollection(Date startDate, Date endDate, String collection);
	
	List<LogEntry> findBylogEntryDateBetweenAndEnv(Date startDate, Date endDate, String env);
	
	List<LogEntry> findBylogEntryDateBetweenAndNode(Date startDate, Date endDate, String node);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndEnv(Date startDate, Date endDate, String collection, String env);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndNode(Date startDate, Date endDate, String collection, String node);

	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndEnvAndNode(Date startDate, Date endDate, String collection, String env, String node);
	
	List<LogEntry> findBylogEntryDateBetweenAndEnvAndNode(Date startDate, Date endDate, String env, String node);
	
	List<LogEntry> findBylogEntryDateBetweenAndQueryHashIn(Date startDate, Date endDate, List<String> queryHashes);
	
	List<LogEntry> findBylogEntryDateBetweenAndNodeAndQueryHashIn(Date startDate, Date endDate, String node, List<String> queryHashes);

	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndQueryHashIn(Date startDate, Date endDate, String collection, List<String> queryHashes);

	List<LogEntry> findBylogEntryDateBetweenAndEnvAndQueryHashIn(Date startDate, Date endDate, String env, List<String> queryHashes);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndEnvAndQueryHashIn(Date startDate, Date endDate, String collection, String env, List<String> queryHashes);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndNodeAndQueryHashIn(Date startDate, Date endDate, String collection, String node, List<String> queryHashes);
	
	List<LogEntry> findBylogEntryDateBetweenAndNodeAndEnvAndQueryHashIn(Date startDate, Date endDate, String node, String env, List<String> queryHashes);
	
	List<LogEntry> findBylogEntryDateBetweenAndCollectionAndNodeAndEnvAndQueryHashIn(Date startDate, Date endDate, String collection, String node, String env, List<String> queryHashes);
}

