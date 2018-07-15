package com.i5i58.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 业务线程池组建
 * 
 * @author frank
 *
 */
@Component
@ConfigurationProperties(prefix = "my.threadpool")
public class MyThreadPool {

	private static int highPriorityThreadCount;
	private static int lowPriorityThreadCount;
	private static int yunxinThreadCount;

	private static ExecutorService highPriorityPool;

	private static ExecutorService lowPrioritytPool;

	private static ExecutorService yunxinPool;

	public static int getHighPriorityThreadCount() {
		return highPriorityThreadCount;
	}

	public static void setHighPriorityThreadCount(int highPriorityThreadCount) {
		MyThreadPool.highPriorityThreadCount = highPriorityThreadCount;
	}

	public static int getLowPriorityThreadCount() {
		return lowPriorityThreadCount;
	}

	public static void setLowPriorityThreadCount(int lowPriorityThreadCount) {
		MyThreadPool.lowPriorityThreadCount = lowPriorityThreadCount;
	}

	public int getYunxinThreadCount() {
		return yunxinThreadCount;
	}

	public void setYunxinThreadCount(int yunxinThreadCount) {
		MyThreadPool.yunxinThreadCount = yunxinThreadCount;
	}

	public static void initThreadPool() {
		System.out.println(
				"ThreadPool: " + lowPriorityThreadCount + ";" + highPriorityThreadCount + ";" + yunxinThreadCount);
		lowPrioritytPool = Executors.newFixedThreadPool(lowPriorityThreadCount);

		highPriorityPool = Executors.newFixedThreadPool(highPriorityThreadCount);

		yunxinPool = Executors.newFixedThreadPool(yunxinThreadCount);
	}

	@Bean
	public Executor getYunxinPool() {
		return yunxinPool;
	}

	@Bean
	public ExecutorService getHighPriorityPool() {
		return highPriorityPool;
	}

	@Bean
	public ExecutorService getLowPrioritytPool() {
		return lowPrioritytPool;
	}
}