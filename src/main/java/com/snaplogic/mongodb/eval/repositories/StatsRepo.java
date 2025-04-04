package com.snaplogic.mongodb.eval.repositories;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.snaplogic.mongodb.eval.dtos.NodeCount;
import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.dtos.Stat;
import com.snaplogic.mongodb.eval.exception.QueryException;
import com.snaplogic.mongodb.eval.utils.DateUtils;
import com.snaplogic.mongodb.eval.utils.StringUtils;

/**
 * 
 * @author bgoff
 *

 * 
 */

public class StatsRepo extends QueryRepo {	
		
	private static final Logger logger = LogManager.getLogger(StatsRepo.class);

/**
 *  * db.logEntry.aggregate([
 * {    $match: {
 *          "node": "shard01",
 *          "logEntryDate": {
 *               $gte: ISODate("2025-03-17T00:00:00Z"),
 *               $lte: ISODate("2025-03-17T00:15:00Z")
 *          }
 *      }
 * },{  $project: {
 *           node: 1,
 *           hour: { $hour: "$logEntryDate" },
 *           minute: { $minute: "$logEntryDate" },
 *           logEntryDate: 1
 *      }
 * },{  $group: {
 *           _id: {
 *                node: "$node",
 *                hour: "$hour",
 *                minute: "$minute"
 *           },
 *           node: { $first: "$node" },
 *           count: { $sum : 1},
 *           startDate: { $first: "$logEntryDate" },
 *      }
 * },{  $sort: { "startDate": 1 }
 * }])
 * 
 * @param query
 * @param mongoTemplate
 * @return
 * @throws Exception
 */
	public List<Stat> getStats(Query query, MongoTemplate mongoTemplate) throws Exception
	{
		List<Stat> stats = new ArrayList<Stat>();
		MatchOperation matchStage = buildMatchStage(query);
		
				
		ProjectionOperation projectStage = Aggregation.project().and("node").as("node").
			and("logEntryDate").as("logEntryDate")
                .andExpression("hour(logEntryDate)").as("hour")
                .andExpression("minute(logEntryDate)").as("minute");
		
		GroupOperation group = Aggregation.group("node", "hour", "minute").
				count().as("count").
				first("node").as("cluster").
				first("logEntryDate").as("logEntryDate");
		
//		AggregationExpression hourExpression = 
		
//		GroupOperation groupStage = Aggregation.group("node").and("hour", "hour(logEntryDate)").and("minute", "minute(logEntryDate)").
//				count().as("count").
//				first("node").as("cluster").
//				first("logEntryDate").as("logEntryDate");
		
		
		SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "logEntryDate");
		
		Aggregation agg = Aggregation.newAggregation(matchStage, projectStage, group, sort );

		logger.debug("agg: " + agg.toString());
		
		AggregationResults <Stat> results = mongoTemplate.aggregate(agg, "logEntry", Stat.class);
		
		stats = results.getMappedResults();
		
		for(Stat stat: stats)
		{
			stat.setMillieseconds(stat.getLogEntryDate().getTime());
		}
//		
//		logger.debug("It took " + DateUtils.computeDiff(envStartDate,
//				DateUtils.rightNowDate()) + " to get the Stats for " + query.getCluster() + "!");
		
		return(stats); 
	}
	
	/**
	 * This method runs the get count stats.  This tells us how many operations Ie WRITE or QUERY where committed during
	 * a period of time across all nodes.  Per node.
	 * 
	 * db.logEntry.aggregate([
	 * {	$match: {
	 * 			"operation": "WRITE",
	 * 			"logEntryDate": {
	 * 				$gte: ISODate("2025-03-17"),
	 * 				$lte: ISODate("2025-03-21")
	 *			}
	 *		}
	 * },{	$group: {
	 * 			_id: { node: "$node" },
	 * 			node: { $first: "$node" },
	 * 			count: { $sum : 1}
	 * 		}
	 * },{     $sort: { count: 1 }
	 * }])
	 * @param query 
	 * @param mongoTemplate
	 * @return
	 * @throws Exception
	 */
	public List<NodeCount> getCounts(Query query, MongoTemplate mongoTemplate) throws Exception
	{
		List<NodeCount> stats = new ArrayList<NodeCount>();
		
		MatchOperation matchStage = buildMatchStage(query, "WRITE");
		
		GroupOperation group = Aggregation.group("node").count().as("count").first("node").as("node");
				
		SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "count");
		
		Aggregation agg = Aggregation.newAggregation(matchStage, group, sort );

		logger.debug("agg: " + agg.toString());
		
		AggregationResults <NodeCount> results = mongoTemplate.aggregate(agg, "logEntry", NodeCount.class);
		
		stats = results.getMappedResults();
		
		return(stats); 
	}

	/**
	 * This method is used to build the query match portion of the mongodb query.
	 * @param query Query object to pull the query parameters from.
	 * @return MatchOperation to apply to the mongodb query.
	 * @throws QueryException if we are unable to build the MatchOperation from the query passed in.
	 */
	protected MatchOperation buildMatchStage(Query query, String operation) throws QueryException
	{
		List<Criteria> queryCriteria = new ArrayList<Criteria>();
		
		MatchOperation matchStage = null;
		Date startDate = DateUtils.rightNowDate();
		boolean errored = false;
		try
		{		
			Criteria matchCriteria = buildDateRange(query);
			
			
			switch (operation) {
				case "READ" :
					//TODO
					queryCriteria.add(buildCriteria("operation", "WRITE"));
					break;
				default:
					queryCriteria.add(buildCriteria("operation", "WRITE"));


			}
			
		
			if(!query.getEnv().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("env", query.getEnv()));
			
			if(!query.getCollection().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("collection", query.getCollection()));
		
			if(!query.getCluster().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("node", query.getCluster()));
			
			matchCriteria.andOperator(queryCriteria);
			
			matchStage = Aggregation.match(matchCriteria);
		}
		catch(ParseException pe)
		{
			errored = true;
			throw new QueryException("Failed to build date query criteria!", pe);
		}
		finally
		{
			StringBuilder sb = new StringBuilder("It took " + 
					DateUtils.computeDiff(startDate, DateUtils.rightNowDate()) + " to ");
			if(errored)
				sb.append("fail to ");
			else
				sb.append("successfully ");
			sb.append("build the match operation for the query!");
			logger.info(sb.toString());
		}
		return (matchStage);
	}
	
}
