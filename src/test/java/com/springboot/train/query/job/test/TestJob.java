package com.springboot.train.query.job.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

import com.springboot.train.query.job.QueryLeftTicketJob;
import com.springboot.train.util.CityUtil;

public class TestJob {

	@Test
	public void test() {
		CityUtil.init();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 20);
		QueryLeftTicketJob job = new QueryLeftTicketJob("SHH", "BJP",df.format(c.getTime()),"1,#,2,#,3,#,O");
		job.run();
		Thread t1 = new Thread(job);
		t1.start();
	}

}
