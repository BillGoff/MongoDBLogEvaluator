package com.snaplogic.mongodb.eval.repositories;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.exception.QueryException;
import com.snaplogic.mongodb.eval.utils.DateUtils;
import com.snaplogic.mongodb.eval.utils.StringUtils;

/**
 * This class is used to build the various query operations.
 * @author bgoff
 * @since 20 Feb 2025
 */
public class QueryRepo {

	private static final Logger logger = LogManager.getLogger(QueryRepo.class);

	/**
	 * This method is used to build a Criteria for the field and its value passed in.
	 * @param field String value to assign to the field.
	 * @param value String value to assign to the query.
	 * @return Criteria used for the query of the field and value passed in.
	 */
	private Criteria buildCriteria(String field, String value)
	{
		return(new Criteria(field).is(value.trim()));
	}
	
	/**
	 * This convenience method is used to build a java.util.date from the date string and time string passed in.
	 * @param dateString String value of the date.  It should look like "02/20/2025"
	 * @param timeString String value of the time.  It should look like "23:59:59"
	 * @return java.util.Date created from the date string and date time passed in.
	 * @throws ParseException in the event we are unable to create the date from the date and time strings passed in.
	 */
	public Date buildDate(String dateString, String timeString) throws ParseException
	{
		String dateTimeString = dateString + " " + timeString;
		return DateUtils.toDate(dateTimeString);
	}
	
	/**
	 * This method is used to build the query Criteria for the date range.
	 * @param query Query Object used to pull the date range query criteria from.
	 * @return Criteria query criteria for the date range portion of the query.
	 * @throws ParseException in the event we are unable to parse the date range from the query passed in.
	 */
	private Criteria buildDateRange(Query query) throws ParseException
	{
		Date startDate = buildDate(query.getStartDateString(), query.getStartTime());
		Date endDate = buildDate(query.getEndDateString(), query.getEndTime());
		
		Criteria dateRangeCriteria = new Criteria("logEntryDate").gte(startDate).lte(endDate);
		return dateRangeCriteria;
	}
	
	/**
	 * This method is used to build the query match portion of the mongodb query.
	 * @param query Query object to pull the query parameters from.
	 * @return MatchOperation to apply to the mongodb query.
	 * @throws QueryException if we are unable to build the MatchOperation from the query passed in.
	 */
	protected MatchOperation buildMatchStage(Query query) throws QueryException
	{
		List<Criteria> queryCriteria = new ArrayList<Criteria>();
		
		MatchOperation matchStage = null;
		Date startDate = DateUtils.rightNowDate();
		boolean errored = false;
		try
		{		
			Criteria matchCriteria = buildDateRange(query);
		
			if(!query.getEnv().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("env", query.getEnv()));
			
			if(!query.getCollection().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("collection", query.getCollection()));
		
			if(!query.getCluster().equalsIgnoreCase("all"))
				queryCriteria.add(buildCriteria("node", query.getCluster()));

			if ((query.getQueryHashes() != null) && (query.getQueryHashes().trim().length() > 0))
			{
				List<String> queryHashList = StringUtils.getHashes(query.getQueryHashes());
				queryCriteria.add(buildQueryHashCriteria(queryHashList));
			}
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
	
	/**
	 * This method is used to build the query hash query criteria for the list of query hashes passed in.
	 * @param queryHashList List of Strings that make up the query hashes we are looking for.
	 * @return Criteria for this portion of the query.
	 */
	private Criteria buildQueryHashCriteria(List<String> queryHashList)
	{
		return(new Criteria("queryHash").in(queryHashList));
	}
}
