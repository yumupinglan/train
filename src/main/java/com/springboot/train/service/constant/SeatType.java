package com.springboot.train.service.constant;

/**
 * 
 * @author willie
 *	
 * 2016年12月18日 下午1:16:04
 *
 */
public enum SeatType {
	
	
	swz_num("swz_num","9"),
	tz_num("tz_num","P"),
	zy_num("zy_num","M"),
	ze_num("ze_num","O"),
	gr_num("gr_num","6"),
	rw_num("rw_num","4"),
	yw_num("yw_num","3"),
	rz_num("rz_num","2"),
	yz_num("yz_num","1");
	
	private String type; 
	private String value; 
	SeatType(String type,String value){
		this.type = type;
		this.value = value;
	}
	
	public static String getTypeByValue(String value){
		switch(value){
		case "9":
			return swz_num.type;
		case "P":
			return tz_num.type;
		case "M":
			return zy_num.type;
		case "O":
			return ze_num.type;
		case "6":
			return gr_num.type;
		case "4":
			return rw_num.type;
		case "3":
			return yw_num.type;
		case "2":
			return rz_num.type;
		case "1":
			return yz_num.type;
		default:
			return "";
		}
	}
}
