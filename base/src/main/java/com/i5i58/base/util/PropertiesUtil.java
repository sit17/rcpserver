package com.i5i58.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties config;
	private static String activeKey = "profiles.active";
	
	public static String getActiveKey() {
		return activeKey;
	}

	public static void setActiveKey(String activeKey) {
		PropertiesUtil.activeKey = activeKey;
	}

	public static Properties getProperties(){
		if (config != null){
			return config;
		}
		
		InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
		Properties cfg = new Properties();
		try {
			cfg.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String env = cfg.getProperty(activeKey, "");
		if (env.isEmpty()){
			config = cfg;
			return config;
		}
		InputStream inputStream2 = PropertiesUtil.class.getClassLoader().getResourceAsStream("application-" + env + ".properties");
		try {
			config = new Properties();
			config.load(inputStream2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}

	public static String getProperty(String key){
		if (config == null){
			config = getProperties();
		}
		return config.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue){
		if (config == null){
			config = getProperties();
		}
		return config.getProperty(key, defaultValue);
	}
}
