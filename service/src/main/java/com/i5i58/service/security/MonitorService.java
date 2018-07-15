package com.i5i58.service.security;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.security.IMonitor;

@Service(protocol = "dubbo")
public class MonitorService implements IMonitor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public ResultDataSet reportProc(String accId, String key) {
		ResultDataSet rds = new ResultDataSet();
		logger.error(String.format("reportProc:{accId:%s, key:%s}", accId, key));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet reportPic(String accId, String key) {
		ResultDataSet rds = new ResultDataSet();
		logger.error(String.format("reportPic:{accId:%s, key:%s}", accId, key));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

}
