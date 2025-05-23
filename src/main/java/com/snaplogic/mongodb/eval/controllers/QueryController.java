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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snaplogic.mongodb.eval.dtos.DiffResult;
import com.snaplogic.mongodb.eval.dtos.LogEntry;
import com.snaplogic.mongodb.eval.dtos.NodeCount;
import com.snaplogic.mongodb.eval.dtos.Query;
import com.snaplogic.mongodb.eval.dtos.Stat;
import com.snaplogic.mongodb.eval.dtos.SummaryLogEntry;
import com.snaplogic.mongodb.eval.repositories.FrequencyRepo;
import com.snaplogic.mongodb.eval.repositories.LogEntryRepo;
import com.snaplogic.mongodb.eval.repositories.StatsRepo;
import com.snaplogic.mongodb.eval.repositories.SummaryRepo;
import com.snaplogic.mongodb.eval.utils.DateUtils;
import com.snaplogic.mongodb.eval.utils.DiffResultParser;
import com.snaplogic.mongodb.eval.utils.DistinctRepoUtil;
import com.snaplogic.mongodb.eval.utils.DistinctRepoUtil.DistictFields;
import com.snaplogic.mongodb.eval.utils.StringUtils;

/**
 * This Controller handles all the queries.
 * @author bgoff
 * @since 26 Nov 2024
 */
@Controller
public class QueryController {

	private static final Logger logger = LogManager.getLogger(QueryController.class);

	private LogEntryRepo logEntryRepo;
	
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate)
	{
		this.mongoTemplate = mongoTemplate;
	}
	
	@Autowired
	public void setLogEntryRepo(LogEntryRepo logEntryRepo)
	{
		this.logEntryRepo = logEntryRepo;
	}
	
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
			queryTypes.add("countStats");
			queryTypes.add("frequency");
			queryTypes.add("summary");
			queryTypes.add("verbose");
			queryTypes.add("stats");
			
			DistinctRepoUtil distictUtils = new DistinctRepoUtil();
			
			Query defaultQuery = new Query();
			
			defaultQuery.setStartDateString(DateUtils.toString(DateUtils.getNDaysBeforeDate(DateUtils.rightNowDate(), 3), 
					DateUtils.defaultDateFormat));
			
			defaultQuery.setStartTime("00:00:00");			
			
			defaultQuery.setEndDateString(DateUtils.toString(DateUtils.rightNowDate(), DateUtils.defaultDateFormat));

			defaultQuery.setEndTime("23:59:59");			
			
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
		Date startDate = DateUtils.rightNowDate();
		boolean failed = false;

		System.out.println(query.toString());
				
		List<String> queryTypes = new ArrayList<String>();
		queryTypes.add("diff");
		queryTypes.add("frequency");
		queryTypes.add("stats");
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
			if(query.getQueryType().equalsIgnoreCase("diff"))
			{
				System.out.println("Attempting to do a diff query with: ");
				System.out.println(query.toString("	"));

				List<DiffResult> diffResults = new ArrayList<DiffResult>();
				
				FrequencyRepo repo = new FrequencyRepo();
				List<SummaryLogEntry> summaryLogEntries = repo.getFrequency(query, mongoTemplate);
				
				Query query2 = query.clone();
				
				query2.setEndDateString(query.getOrgEndDateString());
				query2.setStartDateString(query.getOrgStartDateString());
				
				List<SummaryLogEntry> summaryLogEntries2 = repo.getFrequency(query2, mongoTemplate);
				
				if (DateUtils.toDate(query.getStartDateString(), DateUtils.defaultDateFormat).after(
						DateUtils.toDate(query2.getStartDateString(), DateUtils.defaultDateFormat)))
				{
					diffResults = DiffResultParser.parseSummaryResultsIntoDiffResults(
							summaryLogEntries2, summaryLogEntries);
				}
				else
				{	
					diffResults = DiffResultParser.parseSummaryResultsIntoDiffResults(
							summaryLogEntries, summaryLogEntries2);
				}
				
				model.addAttribute("diffLogEntries", diffResults);
				
				return "diff";
			}
			else if (query.getQueryType().equalsIgnoreCase("stats"))
			{
				StatsRepo statsRepo = new StatsRepo ();
				List<Stat> stats = statsRepo.getStats(query, mongoTemplate);
				
				System.out.println("returning " + stats.size() + " cluster stats");
				
				model.addAttribute("clusterStats", stats);
				
				return "stats";
			}
			else if(query.getQueryType().equalsIgnoreCase("countStats"))
			{
//				StatsRepo statsRepo = new StatsRepo ();
//				List<NodeCount> stats = statsRepo.getCounts(query, mongoTemplate);
//				
				String queryRange = query.getDateRange();
				
				model.addAttribute("dateRange", queryRange);
//				model.addAttribute("clusterStats", stats);
				
				return "count";
			}
			else
			{	
				if(query.getQueryType().equalsIgnoreCase("summary"))
				{
					SummaryRepo summaryRepo = new SummaryRepo();
					List<SummaryLogEntry> summaryLogEntries = summaryRepo.getSummary(query, mongoTemplate);
					try
					{				
						model.addAttribute("summaryLogEntries", summaryLogEntries);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						failed = true;
					}
					finally
					{
						StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
								DateUtils.rightNowDate()));
						if (failed)
							msg.append(" to fail to ");
						else
							msg.append(" to successfully (returning " + summaryLogEntries.size() + ") ");
					
							
						msg.append("run a summary query.");
						
						logger.info(msg.toString());
					}	
					return "summary";
				}
				else if(query.getQueryType().equalsIgnoreCase("frequency"))
				{
					FrequencyRepo repo = new FrequencyRepo();
					List<SummaryLogEntry> summaryLogEntries = repo.getFrequency(query, mongoTemplate);
					try
					{				
						model.addAttribute("summaryLogEntries", summaryLogEntries);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						failed = true;
					}
					finally
					{
						StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
								DateUtils.rightNowDate()));
						if (failed)
							msg.append(" to fail to ");
						else
							msg.append(" to successfully (returning " + summaryLogEntries.size() + ") ");
					
							
						msg.append("run a summary query.");
						
						logger.info(msg.toString());
					}	
					return "summary";
				}
				else
				{
					List<LogEntry> logEntries = getLogEntries(query);

					try
					{							
						logger.info("found " + logEntries.size() + " log entries!");
						
						model.addAttribute("logEntries", logEntries);		
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
								DateUtils.rightNowDate()));
						if (failed)
							msg.append(" to fail to ");
						else
							msg.append(" to successfully (returning " + logEntries.size() + ") ");
					
							
						msg.append("run a verbose query.");
						
						logger.info(msg.toString());
					}	
					return "verbose";
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "verbose";
	}
	
	/**
	 * This method is used to get the Write Operations count.
	 * @param startDate String value of the start date.
	 * @param startTime String value of the start time.
	 * @param endDate String value of the end date.
	 * @param endTime String value of the end time.
	 * @param collection String value of the Collection selected (or All if none were selected).
	 * @param env String value of the environment selected (or All if none where selected).
	 * @param cluster String value of the cluster (or all if none were selected.
	 * @return Returns the Json which contains the write Operations metadata.
	 */
	@GetMapping("/getWriteCounts")
	@ResponseBody
	public List<NodeCount> getWriteCounts(@RequestParam String startDate, @RequestParam String startTime,
			@RequestParam String endDate, @RequestParam String endTime, @RequestParam String collection,
			@RequestParam String env, @RequestParam String cluster)
	{
		List<NodeCount> stats = new ArrayList<NodeCount>();
		try
		{
			System.out.println("Got a getWriteCounts Request!!!!!");
			
			Query query = new Query();
			query.setStartDateString(startDate);
			query.setStartTime(startTime);
			query.setEndDateString(endDate);
			query.setEndTime(endTime);
			query.setCluster(cluster);
			query.setCollection(collection);
			query.setEnv(env);
			
			System.out.println("Query: \n" + query.toString("	"));
			
			StatsRepo statsRepo = new StatsRepo ();
			stats = statsRepo.getCounts(query, StatsRepo.OPERATION.WRITE, mongoTemplate);
			
			System.out.println("Returning with " + stats.size() + " stats");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stats;
	}
	
	/**
	 * This method is used to get the Write Operations count.
	 * @param startDate String value of the start date.
	 * @param startTime String value of the start time.
	 * @param endDate String value of the end date.
	 * @param endTime String value of the end time.
	 * @param collection String value of the Collection selected (or All if none were selected).
	 * @param env String value of the environment selected (or All if none where selected).
	 * @param cluster String value of the cluster (or all if none were selected.
	 * @return Returns the Json which contains the write Operations metadata.
	 */
	@GetMapping("/getReadCounts")
	@ResponseBody
	public List<NodeCount> getReadCounts(@RequestParam String startDate, @RequestParam String startTime,
			@RequestParam String endDate, @RequestParam String endTime, @RequestParam String collection,
			@RequestParam String env, @RequestParam String cluster)
	{
		List<NodeCount> stats = new ArrayList<NodeCount>();
		try
		{
			System.out.println("Got a getReadCounts Request!!!!!");
			
			Query query = new Query();
			query.setStartDateString(startDate);
			query.setStartTime(startTime);
			query.setEndDateString(endDate);
			query.setEndTime(endTime);
			query.setCluster(cluster);
			query.setCollection(collection);
			query.setEnv(env);
			
			System.out.println("Query: \n" + query.toString("	"));
			
			StatsRepo statsRepo = new StatsRepo ();
			stats = statsRepo.getCounts(query, StatsRepo.OPERATION.READ, mongoTemplate);
			
			System.out.println("Returning with " + stats.size() + " stats");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stats;
	}
	
	/**
	 * This method is used to get the Write Operations count.
	 * @param startDate String value of the start date.
	 * @param startTime String value of the start time.
	 * @param endDate String value of the end date.
	 * @param endTime String value of the end time.
	 * @param collection String value of the Collection selected (or All if none were selected).
	 * @param env String value of the environment selected (or All if none where selected).
	 * @param cluster String value of the cluster (or all if none were selected.
	 * @return Returns the Json which contains the write Operations metadata.
	 */
	@GetMapping("/getTotalCounts")
	@ResponseBody
	public List<NodeCount> getTotalCounts(@RequestParam String startDate, @RequestParam String startTime,
			@RequestParam String endDate, @RequestParam String endTime, @RequestParam String collection,
			@RequestParam String env, @RequestParam String cluster)
	{
		List<NodeCount> stats = new ArrayList<NodeCount>();
		try
		{
			System.out.println("Got a getWriteCounts Request!!!!!");
			
			Query query = new Query();
			query.setStartDateString(startDate);
			query.setStartTime(startTime);
			query.setEndDateString(endDate);
			query.setEndTime(endTime);
			query.setCluster(cluster);
			query.setCollection(collection);
			query.setEnv(env);
			
			System.out.println("Query: \n" + query.toString("	"));
			
			StatsRepo statsRepo = new StatsRepo ();
			stats = statsRepo.getCounts(query, StatsRepo.OPERATION.ALL, mongoTemplate);
			
			System.out.println("Returning with " + stats.size() + " stats");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stats;
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
			if ((collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndQueryHashIn(startDate, endDate, queryHashList);

			//Start, end Dates, queryHashList and Collection.
			else if ((!collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndQueryHashIn(startDate, endDate, collection, queryHashList);
			}
			//Start, End Dates, queryHashList and env
			else if ((collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndEnvAndQueryHashIn(startDate, endDate, env, queryHashList);
			}
			//Start, End Dates, queryHashList and Cluster or Node.
			else if ((collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndNodeAndQueryHashIn(startDate, endDate, cluster, queryHashList);
			}
			//Start, end Dates, queryHashList, Collection and ENV.
			else if ((!collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndEnvAndQueryHashIn(startDate, endDate, collection, env, queryHashList);
			}
			//Start, end Dates, queryHashList, Collection and Cluster..
			else if ((!collection.equalsIgnoreCase("all")) && (env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndNodeAndQueryHashIn(startDate, endDate, collection, cluster, queryHashList);
			}
			//By start, end dates, queryHashList, Env and cluster.
			else if ((collection.equalsIgnoreCase("all")) && (!env.equalsIgnoreCase("all")) && (!cluster.equalsIgnoreCase("all")))
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndNodeAndEnvAndQueryHashIn(startDate, endDate, cluster, env, queryHashList);
			}
			else
			{
				logEntries = logEntryRepo.findBylogEntryDateBetweenAndCollectionAndNodeAndEnvAndQueryHashIn(startDate, endDate, collection, cluster, env, queryHashList);
			}
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
