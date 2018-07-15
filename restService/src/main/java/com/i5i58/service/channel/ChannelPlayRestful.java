package com.i5i58.service.channel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IChannelPlay;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "频道-娱乐服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ChannelPlayRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IChannelPlay channelPlay;

	@ApiOperation(value = "进入频道", notes = "")
	@RequestMapping(value = "/channel/enterChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet enterChannel(@ApiParam(value = "device") @RequestParam(value = "device") int device,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "sessionId") @RequestParam(value = "sessionId") String sessionId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.enterChannel(HttpUtils.getAccIdFromHeader(), device, cId, sessionId);
		return rds;
	}

	@ApiOperation(value = "重新进入频道", notes = "")
	@RequestMapping(value = "/channel/reEnterChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet reEnterChannel(@ApiParam(value = "device") @RequestParam(value = "device") int device,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "sessionId") @RequestParam(value = "sessionId") String sessionId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.reEnterChannel(HttpUtils.getAccIdFromHeader(), device, cId, sessionId);
		return rds;
	}
	
	@ApiOperation(value = "送礼物", notes = "")
	@RequestMapping(value = "/channel/giveGift", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet giveChannelGift(@ApiParam(value = "device") @RequestParam(value = "device") int device,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "giftId") @RequestParam(value = "giftId") int giftId,
			@ApiParam(value = "giftCount") @RequestParam(value = "giftCount") int giftCount,
			@ApiParam(value = "continuous") @RequestParam(value = "continuous") int continuous) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.giveChannelGift(HttpUtils.getAccIdFromHeader(), device, cId, giftId, giftCount, continuous,HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "弹幕", notes = "")
	@RequestMapping(value = "/channel/driftComment", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet driftComment(@ApiParam(value = "device") @RequestParam(value = "device") int device,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "content") @RequestParam(value = "content") String content) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.driftComment(HttpUtils.getAccIdFromHeader(), device, cId, content,HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "退出频道", notes = "")
	@RequestMapping(value = "/channel/exitChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet exitChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "sessionId") @RequestParam(value = "sessionId") String sessionId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.exitChannel(HttpUtils.getAccIdFromHeader(), cId, sessionId);
		return rds;
	}

	@ApiOperation(value = "获取周贡献榜", notes = "")
	@RequestMapping(value = "/channel/getWeekOffer", method = RequestMethod.GET)
	public ResultDataSet getWeekOffer(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getWeekOffer(cId);
		return rds;
	}

	@ApiOperation(value = "获取沙发列表分页", notes = "")
	@RequestMapping(value = "/channel/getSofaList", method = RequestMethod.GET)
	public ResultDataSet getSofaList(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getSofaList(cId, pageNum);
		return rds;
	}

	@ApiOperation(value = "获取麦序", notes = "")
	@RequestMapping(value = "/channel/getMicSequence", method = RequestMethod.GET)
	public ResultDataSet getMicSequence(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getMicSequence(cId, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "获取观众", notes = "")
	@RequestMapping(value = "/channel/getViewerList", method = RequestMethod.GET)
	public ResultDataSet getViewerList(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getViewerList(cId, pageNum);
		return rds;
	}

	@ApiOperation(value = "直播时通知", notes = "")
	@RequestMapping(value = "/channel/noticeLive", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet noticeLive(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "是否通知") @RequestParam(value = "isNotify") boolean isNotify) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.noticeLive(HttpUtils.getAccIdFromHeader(), cId, isNotify);
		return rds;
	}

	@ApiOperation(value = "获取我的关注主播的个数", notes = "")
	@RequestMapping(value = "/channel/getNoticeCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getNoticeCount() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getNoticeCount(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取直播时通知状态", notes = "")
	@RequestMapping(value = "/channel/getNoticeLiveStatus", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getNoticeLiveStatus(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getNoticeLiveStatus(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取直播时通知我的且正在直播的频道", notes = "")
	@RequestMapping(value = "/channel/getLivingChannelNoticeMe", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getLivingChannelNoticeMe() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getLivingChannelNoticeMe(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "设置麦序", notes = "")
	@RequestMapping(value = "/channel/setMicSequence", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setMicSequence(
			@ApiParam(value = "上麦的用户accId") @RequestParam(value = "micAccId") String micAccId,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "麦序号") @RequestParam(value = "index") int index) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.setMicSequence(HttpUtils.getAccIdFromHeader(), micAccId, cId, index);
		return rds;
	}

	@ApiOperation(value = "移除麦序", notes = "")
	@RequestMapping(value = "/channel/removeMicSequence", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet removeMicSequence(
			@ApiParam(value = "上麦的用户accId") @RequestParam(value = "micAccId") String micAccId,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.removeMicSequence(HttpUtils.getAccIdFromHeader(), micAccId, cId);
		return rds;
	}

	@ApiOperation(value = "清空麦序", notes = "")
	@RequestMapping(value = "/channel/clearMicSequence", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet clearMicSequence(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.clearMicSequence(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "请求连麦", notes = "")
	@RequestMapping(value = "/channel/requestConnectMic", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet requestConnectMic(@ApiParam(value = "频道cId") @RequestParam(value = "cid") String cid,
			@ApiParam(value = "请求频道的cId") @RequestParam(value = "connCid") String connCid) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.requestConnectMic(HttpUtils.getAccIdFromHeader(), cid, connCid);
		return rds;
	}

	@ApiOperation(value = "请求连麦", notes = "")
	@RequestMapping(value = "/channel/responseConnectMic", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet responseConnectMic(
			@ApiParam(value = "连麦请求订单号") @RequestParam(value = "connMicOrderId") String connMicOrderId,
			@ApiParam(value = "是否同意") @RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.responseConnectMic(HttpUtils.getAccIdFromHeader(), connMicOrderId, agree);
		return rds;
	}

	@ApiOperation(value = "开始直播", notes = "")
	@RequestMapping(value = "/channel/openPush", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet openPush(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "设备ID, {1.pc, 2:web, 3:IOS, 4:android}") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.openPush(HttpUtils.getAccIdFromHeader(), cId, device);
		return rds;
	}

	@ApiOperation(value = "关闭直播", notes = "")
	@RequestMapping(value = "/channel/closePush", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet closePush(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.closePush(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "添加马甲", notes = "")
	@RequestMapping(value = "/channel/addMajia", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet addMajia(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "马甲id，1-3") @RequestParam(value = "majia") int majia) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.addMajia(HttpUtils.getAccIdFromHeader(), cId, accId, majia);
		return rds;
	}

	@ApiOperation(value = "移除马甲", notes = "")
	@RequestMapping(value = "/channel/removeMajia", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet removeMajia(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "accId") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.removeMajia(HttpUtils.getAccIdFromHeader(), cId, accId);
		return rds;
	}

	@ApiOperation(value = "获取老虎席", notes = "")
	@RequestMapping(value = "/channel/getMajiaList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMajiaList(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlay.getMajiaList(cId, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "频道禁言/解禁", notes = "")
	@RequestMapping(value = "/channel/setMute", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setMute(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "操作对象的accId") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "禁言(true),解禁(false)") @RequestParam(value = "optValue") String optValue) {
		ResultDataSet resultDataSet = new ResultDataSet();
		resultDataSet = channelPlay.setMute(HttpUtils.getAccIdFromHeader(), cId, targetAccId, optValue);
		return resultDataSet;
	}

	@ApiOperation(value = "频道临时禁言/解禁", notes = "")
	@RequestMapping(value = "/channel/setTempMute", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setTempMute(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "操作对象的accId") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "=0时解禁，>0时禁言,最大24小时，单位秒") @RequestParam(value = "duration") long duration) {
		ResultDataSet resultDataSet = new ResultDataSet();
		resultDataSet = channelPlay.setTempMute(HttpUtils.getAccIdFromHeader(), cId, targetAccId, duration);
		return resultDataSet;
	}

	@ApiOperation(value = "获取有马甲的频道列表", notes = "")
	@RequestMapping(value = "/channel/getChannelsByMajia", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getChannelsByMajia(
			@ApiParam(value = "目标用户id") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet resultDataSet = new ResultDataSet();
		resultDataSet = channelPlay.getChannelsByMajia(targetAccId, pageSize, pageNum);
		return resultDataSet;
	}

	@ApiOperation(value = "主播或管理员将用户踢出直播间", notes = "")
	@RequestMapping(value = "/channel/kickChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet kickChannel(
			@ApiParam(value = "目标用户id") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet resultDataSet = new ResultDataSet();
		resultDataSet = channelPlay.kickChannel(HttpUtils.getAccIdFromHeader(), targetAccId, cId);
		return resultDataSet;
	}

	@ApiOperation(value = "直播频道回调", notes = "")
	@RequestMapping(value = "/channel/chStatusCallabck", method = RequestMethod.POST)
	public ResultDataSet chStatusCallabck() {
		ResultDataSet resultDataSet = new ResultDataSet();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// long time = System.currentTimeMillis();
		// System.out.println("time = " + time);
		String inputLine = "";
		String notityJson = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityJson += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			logger.error("", e);
		}
		// Enumeration<String> names = request.getHeaderNames();
		// while (names.hasMoreElements()) {
		// String string = (String) names.nextElement();
		// System.out.println(string + " : " + request.getHeader(string));
		// }
		String sign = request.getHeader("sign");
		System.out.println("body " + notityJson);
		System.out.println("sign " + sign);
		resultDataSet = channelPlay.chStatusCallabck(notityJson, sign);
		try {
			System.out.println("resultDataSet = " + new JsonUtils().toJson(resultDataSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultDataSet;
	}
}
