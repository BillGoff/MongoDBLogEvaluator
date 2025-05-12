package com.snaplogic.mongodb.eval.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.dtos.SummaryLogEntry;
import com.snaplogic.mongodb.eval.utils.DateUtils;


/**
 * This Repository is designed to run the Summary Query.
 * @author bgoff
 *
 */
public class SummaryRepo extends QueryRepo{	
		
	private static final Logger logger = LogManager.getLogger(StatsRepo.class);


	public List<SummaryLogEntry> getSummary(Query query, MongoTemplate mongoTemplate) throws Exception
	{
		Date envStartDate = DateUtils.rightNowDate();
		
		List<SummaryLogEntry> results = new ArrayList<SummaryLogEntry>();
		
		MatchOperation matchStage = buildMatchStage(query);
		
		GroupOperation group = Aggregation.group("queryHash").
				count().as("sum").
				avg("duration").as("averageDuration").
				max("duration").as("highDuration").
				min("duration").as("lowDuration").
				first("cmd").as("cmd").
				first("planSummary").as("planSummary").
				first("queryHash").as("queryHash");
		
		SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "highDuration");
		
		Aggregation agg = Aggregation.newAggregation(matchStage, group, sort);

		logger.debug("agg: " + agg.toString());
		
		AggregationResults <SummaryLogEntry> mongoDBResults = mongoTemplate.aggregate(agg, "logEntry", SummaryLogEntry.class);
		
		results = mongoDBResults.getMappedResults();
		
		logger.debug("It took " + DateUtils.computeDiff(envStartDate,
				DateUtils.rightNowDate()) + " to get the Stats for " + query.getCluster() + "!");
		
		return(results); 
	}

}
