package com.springboot.train.query.job;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Stack;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.springboot.train.service.TrainService;
import com.springboot.train.service.constant.SeatType;
import com.springboot.train.util.CityUtil;
import com.springboot.train.util.HttpRequestNg;
import com.springboot.train.util.HttpsRequestNg;

public class QueryLeftTicketJob implements Runnable {
	private final static Logger logger = Logger
			.getLogger(QueryLeftTicketJob.class);
	private boolean isCancled = false;
	
	private final HttpRequestNg httpclient;
	private String fromStation;
	private String toStation;
	private String seatType;
	private final String startDate;
	
	private  List<String> fromStations = null;
	private  List<String> toStations = null;
	
	private Stack<String> stack = new Stack<>();

	public QueryLeftTicketJob(String fromStation, String toStation,
			String startDate, String seatType) {
		httpclient = new HttpsRequestNg();
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.startDate = startDate;
		fromStations = CityUtil.getStationInSameCity(fromStation);
		toStations = CityUtil.getStationInSameCity(toStation);
		this.seatType = seatType;
		String url = "https://kyfw.12306.cn/otn/login/init";
		try {
			httpclient.doGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			if(isCancled){
				logger.info("query job stop: "+Thread.currentThread().getName());
				break;
			}
			try {
				String type = "ADULT";
				String urlStr = "https://kyfw.12306.cn/otn/leftTicket/queryA?leftTicketDTO.train_date="
						+ startDate
						+ "&leftTicketDTO.from_station="
						+ fromStation
						+ "&leftTicketDTO.to_station="
						+ toStation + "&purpose_codes=" + type;
				String result = new String(httpclient.doGet(urlStr), "UTF-8");
				logger.info("query result： "+result);	
				addResult(result);
				try {
					Thread.sleep(1000);
					switchSations();					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setCancled(true);
					logger.info("query job stop: "+Thread.currentThread().getName());
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				setCancled(true);
				logger.info("query job stop: "+Thread.currentThread().getName());
				break;
			}
		}

	}
	
	private void addResult(String result){
		if (!result.equals("-1") && result.startsWith("{")) {
			JSONObject fromObject = JSONObject.fromObject(result);
			System.out.println(fromObject);
			//List<Train> list = (List<Train>) JSONArray.toCollection(fromObject.getJSONArray("data"), Train.class);
			JSONArray trainInfos = fromObject.getJSONArray("data");
			//System.out.println(list.size());
			Object[] trains = trainInfos.toArray();
			System.out.println(trains);
			for(Object obj : trains){
				JSONObject train = ((JSONObject)obj);
				String secretStr = train.getString("secretStr");
				String canWebBuy = train.getJSONObject("queryLeftNewDTO").getString("canWebBuy");
				if("Y".equals(canWebBuy)&& !"".equals(secretStr)){
					for(String seatValuye : this.seatType.split(",#,") ){
						String seattype = SeatType.getTypeByValue(seatValuye);
						String seat = train.getJSONObject("queryLeftNewDTO").getString(seattype);
						if(!"--".equals(seat) && !"无".equals(seat)){
							logger.info("query an available result to stack： "+result);
							this.stack.add(train.toString());
							System.out.println("stack:"+stack.size()+", top:"+stack.peek());
						}
					}
				}				
			}
		}
	}
	
	private void switchSations(){
		int fromStationIndex = (int) (Math.random() * fromStations.size());
		int toStationIndex = (int) (Math.random() * toStations.size());
		this.fromStation = fromStations.get(fromStationIndex);
		this.toStation = toStations.get(toStationIndex);
	}
	
	public String getTrainInfo(){
		if(stack.isEmpty()){
			return "";
		}
		return stack.pop();
	}

	public boolean isCancled() {
		return isCancled;
	}

	public void setCancled(boolean isCancled) {
		logger.info("query job cancled: "+Thread.currentThread().getName());
		this.isCancled = isCancled;
	}
	
	
}
