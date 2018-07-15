package com.i5i58.service.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.security.IMonitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "监控")
@RestController
@RequestMapping
public class MonitorRestful {

	@Reference
	private IMonitor monitor;

	@ApiOperation(value = "上报进程", notes = "")
	@RequestMapping(value = "/security/reportProc", method = RequestMethod.POST)
	public ResultDataSet reportProc(@ApiParam(value = "accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "key") @RequestParam(value = "key") String key) {
		ResultDataSet rds = new ResultDataSet();
		rds = monitor.reportProc(accId, key);
		return rds;
	}

	@ApiOperation(value = "上报图片", notes = "")
	@RequestMapping(value = "/security/reportPic", method = RequestMethod.POST)
	public ResultDataSet reportPic(@ApiParam(value = "accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "key") @RequestParam(value = "key") String key) {
		ResultDataSet rds = new ResultDataSet();
		rds = monitor.reportPic(accId, key);
		return rds;
	}
}
