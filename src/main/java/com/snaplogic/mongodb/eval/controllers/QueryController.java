package com.snaplogic.mongodb.eval.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.snaplogic.mongodb.eval.dtos.LogEntry;
import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.dtos.SummaryLogEntry;
import com.snaplogic.mongodb.eval.repositories.LogEntryRepo;
import com.snaplogic.mongodb.eval.utils.DateUtils;
import com.snaplogic.mongodb.eval.utils.DistinctRepoUtil;
import com.snaplogic.mongodb.eval.utils.StringUtils;
import com.snaplogic.mongodb.eval.utils.DistinctRepoUtil.DistictFields;
import com.snaplogic.mongodb.eval.utils.SummaryUtils;

/**
 * This Controller handles all the queries.
 * @author bgoff
 * @since 26 Nov 2024
 */
@Controller
public class QueryController {

	private static final Logger logger = LogManager.getLogger(QueryController.class);

	@Autowired
	LogEntryRepo logEntryRepo;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	/**
	 * Default query page.  This end point loads all the data for the default query page. 
	 * @param model Model used to load the needed data for the user to choose from.
	 */
	@GetMapping("/home")
	public void home(Model model)
	{
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;
	
		try
		{
			List<String> queryTypes = new ArrayList<String>();
			queryTypes.add("summary");
			queryTypes.add("verbose");
			
			DistinctRepoUtil distictUtils = new DistinctRepoUtil();
			
			Query defaultQuery = new Query();
			
			defaultQuery.setStartDateString(DateUtils.toString(DateUtils.rightNowDate(), DateUtils.defaultDateFormat));
			
			defaultQuery.setStartTime("00:00:00");			
			
			defaultQuery.setEndDateString(DateUtils.toString(DateUtils.getNDaysBeforeDate(DateUtils.rightNowDate(), 3), 
					DateUtils.defaultDateFormat));
			defaultQuery.setEndTime("23:30:00");			
			
		    model.addAttribute("query", defaultQuery);
		    			
			model.addAttribute("availableQueryTypes", queryTypes);
			
			model.addAttribute("availableCollections", distictUtils.getDistinctValues(mongoTemplate, 
					DistictFields.COLLECTION));
			model.addAttribute("availableEnvs", distictUtils.getDistinctValues(mongoTemplate, DistictFields.ENV));
			model.addAttribute("availableNodes", distictUtils.getDistinctValues(mongoTemplate, DistictFields.NODE));

			System.out.println("Default Query: \n" + defaultQuery.toString("	"));			
		}
		catch(Exception e)
		{
			logger.error("Failed to get the needed default values for the query page!", e);
			failed = true;
		}
		finally
		{
			StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
					DateUtils.rightNowDate()));
			if (failed)
				msg.append(" to fail to ");
			else
				msg.append(" to successfully ");
		
				
			msg.append("Built the needed resources for the query page.");
			
			logger.info(msg.toString());
		}	
	}
	
	/**
	 * Default query page.  This end point loads all the data for the default query page. 
	 * @param model Model used to load the needed data for the user to choose from.
	 */
	@PostMapping("/query")
	public String query(@ModelAttribute Query query, Model model)
	{		
		System.out.println(query.toString());
				
		List<String> queryTypes = new ArrayList<String>();
		queryTypes.add("summary");
		queryTypes.add("verbose");
		
		DistinctRepoUtil distictUtils = new DistinctRepoUtil();
	    			
		model.addAttribute("availableQueryTypes", queryTypes);
		
		model.addAttribute("availableCollections", distictUtils.getDistinctValues(mongoTemplate, 
				DistictFields.COLLECTION));
		model.addAttribute("availableEnvs", distictUtils.getDistinctValues(mongoTemplate, DistictFields.ENV));
		model.addAttribute("availableNodes", distictUtils.getDistinctValues(mongoTemplate, DistictFields.NODE));
		
		model.addAttribute("query", query);

		try
		{
			List<LogEntry> logEntries = getLogEntries(query);

			if(query.getQueryType().equalsIgnoreCase("summary"))
			{
				try
				{
					List<SummaryLogEntry> summaryLogEntries = SummaryUtils.parseQueryResults(logEntries);
			
					model.addAttribute("summaryLogEntries", summaryLogEntries);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return "summary";
			}
			else
			{
				try
				{	
					model.addAttribute("logEntries", logEntries);		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "verbose";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "verbose";
	}
	
	/**
	 * Default query page.  This end point loads all the data for the default query page. 
	 * @param model Model used to load the needed data for the user to choose from.
	 */
	@PostMapping("/verboseQuery")
	public String verboseQuery(@ModelAttribute Query query, Model model)
	{
		System.out.println("Verbose Query: " + query.toString());

		return "";
	}
	
	/**
	 * Default query page.  This end point loads all the data for the default query page. 
	 * @param model Model used to load the needed data for the user to choose from.
	 */
	@PostMapping("/summaryQuery")
	public ResponseEntity<Object> summaryQuery(@RequestBody Query query)
	{
		System.out.println("***************************************");
		System.out.println("Summary Query: " + query.toString());

		SummaryLogEntry test = new SummaryLogEntry();
		test.setCmd("test");
		test.setDuration(50);
		test.setHighDuration(140);
		test.setLowDuration(10);
		test.setAverageDuration(10);
		test.setPlanSummary("Testing Plan");
		test.setQueryHash("SomeQuery");
		test.setSum(5);
		
		List<SummaryLogEntry> summaryLogEntries = new ArrayList<SummaryLogEntry>();
		summaryLogEntries.add(test);
		
		
		return new ResponseEntity<Object>(summaryLogEntries, HttpStatus.OK);
	}
	
	/**
	 * This method is used to figure out what to query on, and then queries the Database for the correct log entries.
	 * @param query Query object that tells us what to query on.
	 * @return List of LogEntries from the database that matched the query passed in.
	 * @throws ParseException in the event we are unable to parse the start and end dates from the query.
	 */
	private List<LogEntry> getLogEntries(Query query) throws ParseException
	{
		String startDateString = query.getStartDateString() + " " + query.getStartTime();
		Date startDate = DateUtils.toDate(startDateString);
		
		String endDateString = query.getEndDateString() + " " + query.getEndTime();
		Date endDate = DateUtils.toDate(endDateString);
		
		String collection = query.getCollection();
		String env = query.getEnv();
		String cluster = query.getCluster();
		List<LogEntry> logEntries = new ArrayList<LogEntry>();
		
		String queryHashes = query.getQueryHashes();
		
		if ((queryHashes != null) && (queryHashes.length() > 0))
		{
			List<String> queryHashList = StringUtils.getHashes(queryHashes);
			logEntries = logEntryRepo.findBylogEntryDateBetweenAndQueryHashIn(startDate, endDate, queryHashList);
		}
		else //No Query Hashes.
		{
			//Just need start and endDates.
			if ((collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetween(startDate, endDate);
			
			//Start, end Dates, and Collection.
			else if ((!collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollection(startDate, endDate, collection);
			
			//Start, End Dates, and env
			else if ((collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndEnv(startDate, endDate, env);
			
			//Start, End Dates, and Cluster or Node.
			else if ((collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndNode(startDate, endDate, cluster);
			
			//Start, end Dates, Collection and ENV.
			else if ((!collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndEnv(startDate, endDate, collection, env);
			
			//Start, end Dates, Collection and Cluster..
			else if ((!collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndNode(startDate, endDate, collection, cluster);
	
			//By start, end dates, Env and cluster.
			else if ((collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndEnvAndNode(startDate, endDate, env, cluster);
	
			else
				//By all.
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndEnvAndNode(startDate, endDate, collection, env, cluster);
		}
		return (logEntries);
	}
}
