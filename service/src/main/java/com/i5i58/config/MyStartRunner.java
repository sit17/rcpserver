package com.i5i58.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.i5i58.util.JedisUtils;
import com.i5i58.util.ZookeeperConfig;
import com.i5i58.zookpeeper.ZookeeperService;

@Component
public class MyStartRunner implements CommandLineRunner {
	@Autowired
	ZookeeperService zookeeperService;
	
	@Autowired
	ZookeeperConfig zookeeperConfig;
	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		MyThreadPool.initThreadPool();
		JedisUtils.init();
		zookeeperService.init(zookeeperConfig);
		SqlserverConfig.init();
	}

}