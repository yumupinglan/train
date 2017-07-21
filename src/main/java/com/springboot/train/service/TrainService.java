package com.springboot.train.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.springboot.train.controller.TrainController;
import com.springboot.train.query.job.QueryLeftTicketJob;

public class TrainService {
	private final static Logger logger = Logger
			.getLogger(TrainService.class);
	private final Map<String, QueryLeftTicketJob> jobMap = new HashMap<>();
	
	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	
	/**
	 * 启动一个job，执行查票操作
	 * @param userName
	 * @return
	 */
	public synchronized boolean startJob(String userName,String fromStation, String toStation,
			String startDate, String seatType){
		logger.info("start a new query job for: "+userName);
		logger.info("from station: "+fromStation+", to station: "+toStation+", date: "+startDate +", seat type: "+seatType);
		if(jobMap.containsKey(userName)){
			jobMap.remove(userName).setCancled(true);;
		}
		QueryLeftTicketJob userJob = new QueryLeftTicketJob(fromStation, toStation,
				startDate, seatType);
		pool.execute(userJob);
		jobMap.put(userName, userJob);
		return true;
	}
	
	/**
	 * 停止用户的查询操作
	 * @param userName
	 * @return
	 */
	public synchronized boolean stopAJob(String userName){
		logger.info("stop query job for: "+userName);
		if(!jobMap.containsKey(userName)){
			return false;
		}
		QueryLeftTicketJob userJob = jobMap.remove(userName);
		
		userJob.setCancled(true);
		return true;
	}
	
	/**
	 * 停止所有的查询操作
	 * @return
	 */
	public synchronized boolean stopAllJobs(){
		for(QueryLeftTicketJob userJob : jobMap.values()){
			userJob.setCancled(true);
		}
		jobMap.clear();
		return true;
	}
	
	/**
	 * 获得用户的查询结果
	 * @param userName
	 * @return
	 */
	public String getResult(String userName){
		logger.info("get result for: "+userName);
		if(!jobMap.containsKey(userName)){
			return "";
		}
		if(jobMap.get(userName).isCancled()){
			return jobMap.remove(userName).getTrainInfo();
		}
		return jobMap.get(userName).getTrainInfo();
	}
}
