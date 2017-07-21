package com.springboot.train.query.job.test;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	@org.junit.Test
	public void test() {
		String leftTic ="aaleftTicketStr':'X8TVJP2Jh%2F%2FuzDOfzzfRQD6lnMsyIjUkmVscBmCB74a1P5qx<>";
		String reg = "leftTicketStr':'[0-9a-zA-Z]{0,56}";
		Matcher m = Pattern.compile(reg).matcher(leftTic);
		while (m.find()) {
			String r = m.group().trim();
			System.out.println(r);
			System.out.println(r.split(":")[1].trim().substring(1));
		}
		fail("Not yet implemented");
	}

}
