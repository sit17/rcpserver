package com.i5i58.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.i5i58.util.JedisUtils;

@Component
public class MyStartRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		JedisUtils.init();
	}

}