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
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;

import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.dtos.Stat;
import com.snaplogic.mongodb.eval.utils.DateUtils;



public class StatsRepo {	
		
	private static final Logger logger = LogManager.getLogger(StatsRepo.class);


	public List<Stat> getStats(Query query, MongoTemplate mongoTemplate) throws Exception
	{
		Date envStartDate = DateUtils.rightNowDate();
		
		List<Stat> stats = new ArrayList<Stat>();
		
		String startDateString = query.getStartDateString() + " " + query.getStartTime();
		Date startDate = DateUtils.toDate(startDateString);
		
		String endDateString = query.getEndDateString() + " " + query.getEndTime();
		Date endDate = DateUtils.toDate(endDateString);
		
//		String collection = query.getCollection();
//		String env = query.getEnv();
		String cluster = query.getCluster();
		
		MatchOperation matchStage = Aggregation.match(new 
				Criteria("node").is(cluster).and("logEntryDate").gte(startDate).lte(endDate));
		
		GroupOperation group = Aggregation.group("cluster", "logEntryDate").count().as("count");
		
		ProjectionOperation project = Aggregation.project("count").
				andExpression(cluster).as("cluster").
				andExpression("$_id.logEntryDate").as("logEntryDate");
		
		SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "logEntryDate");
		
		Aggregation agg = Aggregation.newAggregation(matchStage, group, project, sort);
		
		AggregationResults <Stat> results = mongoTemplate.aggregate(agg, "logEntry", Stat.class);
		
		stats = results.getMappedResults();
		
		for(Stat stat: stats)
		{
			stat.setCluster(cluster);
			stat.setMillieseconds(stat.getLogEntryDate().getTime());
		}
		
		logger.debug("It took " + DateUtils.computeDiff(envStartDate,
				DateUtils.rightNowDate()) + " to get the Stats for " + query.getCluster() + "!");
		
		return(stats); 
	}

}
