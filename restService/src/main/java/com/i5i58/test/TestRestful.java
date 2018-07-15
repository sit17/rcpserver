package com.i5i58.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.test.ITest;
import com.i5i58.util.web.IpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "测试-测试服务")
@RestController
public class TestRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	ITest test;

	@ApiOperation(value = "test", notes = "")
	@RequestMapping(value = "/test/test", method = RequestMethod.GET)
	public ResultDataSet test(@RequestParam(value = "name") String name, @RequestParam(value = "age") int age) {
		ResultDataSet rds = new ResultDataSet();

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// 获取客户端ip地址
		String clientIP = IpUtils.getAddr(request);
		String clientIpYun = request.getRemoteAddr();
		System.out.println(clientIP);
		rds = test.test(name, age,clientIP,clientIpYun);
		return rds;
	}
	
	@ApiOperation(value = "test", notes = "")
	@RequestMapping(value = "/test/redis", method = RequestMethod.GET)
	public String redis() {
		ResultDataSet rds = new ResultDataSet();
		rds = test.testRedis();
		return rds.getCode();
	}
	
	@ApiOperation(value = "test", notes = "")
	@RequestMapping(value = "/test/kick", method = RequestMethod.GET)
	public String kick() {
		ResultDataSet rds = new ResultDataSet();
		rds = test.kick();
		return rds.getCode();
	}
	
	@ApiOperation(value = "直播时通知", notes = "")
	@RequestMapping(value = "/test/channelNotice", method = RequestMethod.GET)
	public String channelNotice( @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = test.channelNotice(cId);
		return rds.getCode();
	}
}
