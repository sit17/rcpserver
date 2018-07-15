package com.i5i58.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IChannelPlay;

@Component
@ConfigurationProperties(prefix = "my.zk")
public class BeanContext {

	private static ClassPathXmlApplicationContext context;

	@Reference
	private static IChannelPlay channelPlay;

	static private String env;

	public static String getEnv() {
		return env;
	}

	public static void setEnv(String env) {
		BeanContext.env = env;
	}

	public static void LoadDubboContext() {
		String xmlPath = "dubbo_consumer-" + env + ".xml";
		context = new ClassPathXmlApplicationContext(xmlPath);
	}

	public static ClassPathXmlApplicationContext getDubboContext() {
		return context;
	}

	public static IChannelPlay getChannelPlay() {
		if (channelPlay == null) {
			channelPlay = (IChannelPlay) BeanContext.getDubboContext().getBean("channelPlay");
		}
		return channelPlay;
	}
}
