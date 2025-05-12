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
 * This Repository is designed to run the frequency query.
 * 
 * db.logEntry.aggregate([
 * {	$match: { "logEntryDate": { $gte: ISODate("2025-02-10"), $lte: ISODate("2025-02-14") } }
 * },{	$group: { _id: "$queryHash", count: { $sum: 1}}
 * },{	$sort: { count: -1 } }])
 * 
 * @author bgoff
 * @since 20 Feb 2025
 */
public class FrequencyRepo extends QueryRepo {	
		
	private static final Logger logger = LogManager.getLogger(FrequencyRepo.class);

	/**
	 * This method is used to build and run a "frequency" query.  A frequency query gets statistics on how many times,
	 * average duration, high duration, low duration a query is performed over a period of time.
	 * @param query Query Object that contains the query parameters.
	 * @param mongoTemplate MongoTemplate this is the MongoDB connection.
	 * @return List of SummaryLogEntries.
	 * @throws Exception If we fail to complete the query.
	 */
	public List<SummaryLogEntry> getFrequency(Query query, MongoTemplate mongoTemplate) throws Exception
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
		
		SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "sum");
		
		Aggregation.limit(100);
		
		Aggregation agg = Aggregation.newAggregation(matchStage, group, sort, Aggregation.limit(100));

		logger.debug("agg: " + agg.toString());
		
		AggregationResults <SummaryLogEntry> mongoDBResults = mongoTemplate.aggregate(agg, "logEntry", SummaryLogEntry.class);
		
		results = mongoDBResults.getMappedResults();
		
		logger.debug("It took " + DateUtils.computeDiff(envStartDate,
				DateUtils.rightNowDate()) + " to get the Stats for " + query.getCluster() + "!");
		
		return(results); 
	}
}
