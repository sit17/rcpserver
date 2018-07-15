package com.i5i58.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.config.PlatformConfig;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;

@Component
public class ConfigUtils {

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	PlatformConfigPriDao platformConfigDao;

	public String getPlatformConfig(String key) throws Exception {
		String value = "";
		if (!jedisUtils.exist(key)) {
			PlatformConfig newConfig = platformConfigDao.findOne(key);
			if (newConfig == null) {
				throw new Exception("系统参数异常:" + key);
			} else {
				jedisUtils.set(key, newConfig.getcValue());
				value = newConfig.getcValue();
			}
		} else {
			value = jedisUtils.get(key);
		}
		return value;
	}
}
