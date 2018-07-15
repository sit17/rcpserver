package com.i5i58.vendor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.vendor.IVendor;
import com.i5i58.util.web.HttpUtils;
import com.i5i58.util.web.IpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "运营商服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class VendorRestful {
	
	@Reference
	private IVendor vendor;

	@ApiOperation(value="记录用户行为", notes="")
	@RequestMapping(value = "/vendor/userAction", method = RequestMethod.POST)
	public ResultDataSet userAction(
			@ApiParam(value="运行商id")@RequestParam(value = "vendorId") String vendorId,
			@ApiParam(value="普通用户id")@RequestParam(value = "accId") String accId,
			@ApiParam(value="广告id")@RequestParam(value = "adId") String adId,
			@ApiParam(value="操作类型 1 注册，2 打开， 3 下载")@RequestParam(value = "actionId") int actionId,
			@ApiParam(value="客户端ip")@RequestParam(value = "clientIp") String clientIp,
			@ApiParam(value="客户端mac地址")@RequestParam(value = "macAddress") String macAddress){
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = vendor.userAction(vendorId, accId, adId, actionId, clientIp, macAddress);
		
		return rds;
	}
	
	@ApiOperation(value="查找用户记录", notes="")
	@RequestMapping(value = "/vendor/searchUserActions", method = RequestMethod.POST)
	public ResultDataSet searchUserActions(
			@ApiParam(value = "参数值，具体由type指定") @RequestParam(value = "param") String param,
			@ApiParam(value = "vendorId 运营商id, adId 广告id, actionId 操作类型") @RequestParam(value = "type") String type,
			@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize){
		
		ResultDataSet rds = vendor.searchUserActions(param, type, pageNum, pageSize);
		
		return rds;
	}
	
	@ApiOperation(value="记录用户行为", notes="")
	@RequestMapping(value = "/vendor/searchUserActionsByTime", method = RequestMethod.POST)
	public ResultDataSet searchUserActionsByTime(
			@ApiParam(value="开始时间，毫秒数") @RequestParam(value = "from") long from,
			@ApiParam(value="截止时间，毫秒数") @RequestParam(value = "to") long to,
			@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize){
		
		ResultDataSet rds = vendor.searchUserActionsByTime(from, to, pageNum, pageSize);
		
		return rds;
	}
	
	@ApiOperation(value="统计用户操作数量", notes="")
	@RequestMapping(value = "/vendor/countUserActions", method = RequestMethod.POST)
	public ResultDataSet countUserActions(
			@ApiParam(value = "参数值，具体由type指定") @RequestParam(value = "param") String param,
			@ApiParam(value = "vendorId 运营商id, adId 广告id, actionId 操作类型") @RequestParam(value = "type") String type){
		
		ResultDataSet rds = vendor.countUserActions(param, type);
		return rds;
	}
	
	@ApiOperation(value="统计用户操作数量", notes="")
	@RequestMapping(value = "/vendor/countUserActionsByTime", method = RequestMethod.POST)
	public ResultDataSet countUserActionsByTime(
			@ApiParam(value="开始时间，毫秒数") @RequestParam(value = "from") long from,
			@ApiParam(value="截止时间，毫秒数") @RequestParam(value = "to") long to){
		ResultDataSet rds = vendor.countUserActionsByTime(from, to);
		return rds;
	}
}
