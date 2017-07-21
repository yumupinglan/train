package com.springboot.train;

import de.codecentric.boot.admin.config.EnableAdminServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.springboot.train.service.TrainServiceFactory;
import com.springboot.train.util.CityUtil;

/**
 * @author lijintao
 */
@EnableWebMvc
@EnableAdminServer
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		CityUtil.init();
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		context.addApplicationListener(new ApplicationListener(){

			@Override
			public void onApplicationEvent(ApplicationEvent event) {
				// TODO Auto-generated method stub
				if(event instanceof ContextClosedEvent ||event instanceof ContextStoppedEvent){
					TrainServiceFactory.getTrainService().stopAllJobs();
				}
			}
			
		});
//		HostUtils.addHostBinding("127.0.0.1", "test.kyfw.12306.cn");
//		String url = "http://test.kyfw.12306.cn/index";
//		String cmd = "cmd.exe /c start " + url;
//		try {
//			Process proc = Runtime.getRuntime().exec(cmd);
//			proc.waitFor();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
