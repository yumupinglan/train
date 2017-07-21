package com.springboot.train.service;

/**
 * 
 * @author willie
 *	
 * 2016年12月18日 下午4:26:27
 *
 */
public class TrainServiceFactory {
	private static TrainService trainService = new TrainService();
	
	public static TrainService getTrainService(){
		return trainService;
	}
}
