package com.i5i58.service.channel;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IZegoChannelPush;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "即构视频服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ZegoChannelPushRestful {

	@Reference
	private IZegoChannelPush zegoChannelPush;
	
	@ApiOperation(value = "创建直播", notes = "")
	@RequestMapping(value = "/zego/createLive", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createLive(@ApiParam(value="客户端类型(如Android,iOS,Windows)") @RequestParam(value="term_type") String term_type, 
			@ApiParam(value="网络类型(如有线,无线,4G,3G,2G)") @RequestParam(value="net_type") String net_type) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = zegoChannelPush.createLive(HttpUtils.getAccIdFromHeader(), term_type, net_type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}
	
	
	@ApiOperation(value = "关闭直播", notes = "")
	@RequestMapping(value = "/zego/closeLive", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet closeLive() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = zegoChannelPush.closeLive(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}
	
	
	@ApiOperation(value = "禁用直播", notes = "")
	@RequestMapping(value = "/zego/forbidLive", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet forbidLive(@ApiParam(value="频道id") @RequestParam(value="cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = zegoChannelPush.forbidLive(HttpUtils.getAccIdFromHeader(),cId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}
	
	@ApiOperation(value = "恢复直播", notes = "")
	@RequestMapping(value = "/zego/resumeLive", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet resumeLive(@ApiParam(value="频道id") @RequestParam(value="cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = zegoChannelPush.resumeLive(HttpUtils.getAccIdFromHeader(),cId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}

	@ApiOperation(value = "流创建回调", notes = "")
	@RequestMapping(value = "/zego/ceateCallback", method = RequestMethod.POST)
	public String ceateCallback() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		String inputLine = "";
		String notityJson = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityJson += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
//			logger.error("", e);
		}
		System.out.println(notityJson);
		
		try {
			System.out.println(new JsonUtils().toJson(paramMap));
			return zegoChannelPush.ceateCallback(paramMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	@ApiOperation(value = "流创建关闭", notes = "")
	@RequestMapping(value = "/zego/closeCallback", method = RequestMethod.POST)
	public String closeCallback() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		try {
			System.out.println(new JsonUtils().toJson(paramMap));
			return zegoChannelPush.closeCallback(paramMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	@ApiOperation(value = "会看地址生成", notes = "")
	@RequestMapping(value = "/zego/replayCallback", method = RequestMethod.POST)
	public String replayCallback() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		try {
			System.out.println(new JsonUtils().toJson(paramMap));
			return zegoChannelPush.replayCallback(paramMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	@ApiOperation(value = "OBS开始直播", notes = "")
	@RequestMapping(value = "/zego/openPush", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet openPush(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "设备ID, {1.pc, 2:web, 3:IOS, 4:android}，只能是1") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = zegoChannelPush.openPush(HttpUtils.getAccIdFromHeader(), cId, device);
		return rds;
	}

	@ApiOperation(value = "OBS关闭直播", notes = "")
	@RequestMapping(value = "/zego/closePush", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet closePush(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = zegoChannelPush.closePush(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}
}
