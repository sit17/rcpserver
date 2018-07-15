package com.i5i58.connectionService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.i5i58.base.util.JedisWrapper;
import com.i5i58.base.util.PropertiesUtil;
import com.i5i58.connectionService.starter.ConnectionStarter;

@Component
public class MyStartRunner implements CommandLineRunner {

	@Autowired
	ConnectionStarter connectionStarter;
	
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		PropertiesUtil.setActiveKey("spring.profiles.active");
		JedisWrapper.init();
		connectionStarter.init();
		connectionStarter.start();
		
		BeanContext.LoadDubboContext();
		BeanContext.getChannelPlay();
	}

}
