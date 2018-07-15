package com.i5i58.service.channel;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IChannelPlayInfo;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "频道-娱乐信息服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ChannelPlayInfoRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IChannelPlayInfo channelPlayInfo;

	@ApiOperation(value = "获取礼物设置", notes = "")
	@RequestMapping(value = "/channel/getGiftConfig", method = RequestMethod.GET)
	public ResultDataSet getChannelGiftConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelGiftConfig();
		return rds;
	}

	@ApiOperation(value = "获取坐骑设置", notes = "")
	@RequestMapping(value = "/channel/getMountConfig", method = RequestMethod.GET)
	public ResultDataSet getChannelMountConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelMountConfig();
		return rds;
	}

	@ApiOperation(value = "获取频道动画设置", notes = "")
	@RequestMapping(value = "/channel/getAnimationConfig", method = RequestMethod.GET)
	public ResultDataSet getAnimationConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getAnimationConfig();
		return rds;
	}

	@ApiOperation(value = "获取频道设置", notes = "")
	@RequestMapping(value = "/channel/getChannelConfig", method = RequestMethod.GET)
	public ResultDataSet getChannelConfig(@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "本地礼物版本") @RequestParam(value = "giftVersion") String giftVersion,
			@ApiParam(value = "本地坐骑版本") @RequestParam(value = "mountVersion") String mountVersion,
			@ApiParam(value = "本地公话版本") @RequestParam(value = "animationVersion") String animationVersion)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelConfig(device, giftVersion, mountVersion, animationVersion);
		return rds;
	}

	@ApiOperation(value = "获取频道状态", notes = "0：空闲； 1：直播； 2：禁用； 3：直播录制")
	@RequestMapping(value = "/channel/getChannelStatus", method = RequestMethod.GET)
	public ResultDataSet getChannelStatus(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelStatus(cId);
		return rds;
	}

	@ApiOperation(value = "获取频道爱心信息", notes = "")
	@RequestMapping(value = "/channel/getHeart", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getHeart(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getHeart(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "领取爱心", notes = "")
	@RequestMapping(value = "/channel/takeHeart", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet takeHeart() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.takeHeart(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取一段时间内的签到获取爱心记录,最长间隔60天", notes = "")
	@RequestMapping(value = "/channel/getHeartTakenHistory", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getHeartTakenHistory(
			@ApiParam(value = "开始时间,utc毫秒数") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "截止时间,utc毫秒数") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getHeartTakenHistory(HttpUtils.getAccIdFromHeader(), fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "送心", notes = "")
	@RequestMapping(value = "/channel/giveHeart", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet giveHeart(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.giveHeart(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "粉丝团打卡", notes = "")
	@RequestMapping(value = "/channel/dailyClock", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet dailyClock(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.dailyClock(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取今天粉丝团打卡状态", notes = "")
	@RequestMapping(value = "/channel/dailyClockStatus", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet dailyClockStatus(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.dailyClockStatus(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取一段时间内的打卡记录,最长间隔60天", notes = "")
	@RequestMapping(value = "/channel/getDailyClockHistory", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getDailyClockHistory(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "开始时间,utc毫秒数") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "截止时间,utc毫秒数") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getDailyClockHistory(HttpUtils.getAccIdFromHeader(), cId, fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "获取打卡排名", notes = "")
	@RequestMapping(value = "/channel/getDailyClockRankList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getDailyClockRankList(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getDailyClockRankList(cId, pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "开通粉丝团", notes = "")
	@RequestMapping(value = "/channel/openClub", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet openClub(@RequestParam(value = "cId") String cId, @RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.openClub(HttpUtils.getAccIdFromHeader(), cId, month, HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "佩戴粉丝团", notes = "")
	@RequestMapping(value = "/channel/selectClub", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet selectClub(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.selectClub(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取加入的粉丝团", notes = "")
	@RequestMapping(value = "/channel/getJoinedClubs", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getJoinedClubs(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getJoinedClubs(accId, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "获取粉丝团成员", notes = "")
	@RequestMapping(value = "/channel/getClubFansList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getClubFansList(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getClubFansList(cId, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "是否开通团成员", notes = "")
	@RequestMapping(value = "/channel/isFansClubOpened", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet isFansClubOpened(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.isFansClubOpened(cId, HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取我在当前频道道的粉丝团信息", notes = "")
	@RequestMapping(value = "/channel/getClubInfoByChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getClubInfoByChannel(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getClubInfoByChannel(cId, HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取粉丝团配置", notes = "")
	@RequestMapping(value = "/channel/getFansClubConfig", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getFansClubConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getFansClubConfig();
		return rds;
	}

	@ApiOperation(value = "获取粉丝团任务配置", notes = "")
	@RequestMapping(value = "/channel/getClubTaskConfig", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getClubTaskConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getClubTaskConfig();
		return rds;
	}

	@ApiOperation(value = "获取我的任务详情", notes = "")
	@RequestMapping(value = "/channel/getMyTaskDetails", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyTaskDetails(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getMyTaskDetails(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "开通粉丝团", notes = "")
	@RequestMapping(value = "/channel/openGuard", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet openGuard(@RequestParam(value = "cId") String cId, @RequestParam(value = "level") int level,
			@RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.openGuard(HttpUtils.getAccIdFromHeader(), cId, level, month,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "购买守护坐骑", notes = "")
	@RequestMapping(value = "/channel/buyMount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet buyMount(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "mountId") int mountId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.buyMount(HttpUtils.getAccIdFromHeader(),mountId, cId,HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "获取观众", notes = "")
	@RequestMapping(value = "/channel/getViewer", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getViewer(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getViewer(cId, accId);
		return rds;
	}

	@ApiOperation(value = "获取频道信息", notes = "")
	@RequestMapping(value = "/channel/getChannelInfo", method = RequestMethod.GET)
	public ResultDataSet getChannelInfo(@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelInfo(cId);
		return rds;
	}

	@ApiOperation(value = "获取守护设置", notes = "")
	@RequestMapping(value = "/channel/getChannelGuardConfig", method = RequestMethod.GET)
	public ResultDataSet getChannelGuardConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelGuardConfig();
		return rds;
	}

	@ApiOperation(value = "获取频道右侧信息", notes = "")
	@RequestMapping(value = "/channel/getChannelRightInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getChannelRightInfo(@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelRightInfo(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取频道人气", notes = "")
	@RequestMapping(value = "/channel/getPlayerTimes", method = RequestMethod.GET)
	public ResultDataSet getPlayerTimes(@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getPlayerTimes(cId);
		return rds;
	}

	@ApiOperation(value = "频道内关注", notes = "")
	@RequestMapping(value = "/channel/follow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet follow(@RequestParam(value = "attAccId") String attAccId,
			@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.follow(HttpUtils.getAccIdFromHeader(), attAccId, cId);
		return rds;
	}

	@ApiOperation(value = "获取用户频道内的特权", notes = "返回json{guardLevel, guardDeadLine,clubName,clubDeadLine}")
	@RequestMapping(value = "/channel/getUserChannelRights", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getUserChannelRights(@ApiParam(value = "当前频道id") @RequestParam(value = "cId") String cId)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getUserChannelRights(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "客户端分享成功", notes = "返回json")
	@RequestMapping(value = "/channel/socailShareSuccess", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet socailShareSuccess(@ApiParam(value = "当前频道id") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.socailShareSuccess(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "获取用户拥有的频道", notes = "")
	@RequestMapping(value = "/channel/getChannelByOwner", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getChannelByOwner(@ApiParam(value = "频道owner") @RequestParam(value = "owner") String owner) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelByOwner(owner);
		return rds;
	}

	@ApiOperation(value = "修改粉丝团名称", notes = "")
	@RequestMapping(value = "/channel/editFansClubName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editFansClubName(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "频道名称，只能为3字符中文") @RequestParam(value = "clubName") String clubName) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.editFansClubName(HttpUtils.getAccIdFromHeader(), cId, clubName);
		return rds;
	}

	@ApiOperation(value = "修改粉丝团图标", notes = "")
	@RequestMapping(value = "/channel/editFansClubIcon", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editFansClubIcon(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "粉丝团图标") @RequestParam(value = "clubIcon") String clubIcon) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.editFansClubIcon(HttpUtils.getAccIdFromHeader(), cId, clubIcon);
		return rds;
	}

	@ApiOperation(value = "主播获取售出骑士列表", notes = "")
	@RequestMapping(value = "/channel/getAchorGuardList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getAchorGuardList(@ApiParam(value = "主播cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelPlayInfo.getAchorGuardList(HttpUtils.getAccIdFromHeader(), cId, pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value = "获取频道音效列表", notes = "")
	@RequestMapping(value = "/channel/getChannelSoundConfig", method = RequestMethod.POST)
	public ResultDataSet getChannelSoundConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getChannelSoundConfig();
		return rds;
	}
	
	@ApiOperation(value = "获取主播所属公会", notes = "")
	@RequestMapping(value = "/channel/getTopGroupByAnchor", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTopGroupByAnchor(@ApiParam(value = "主播Id") 
					@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelPlayInfo.getTopGroupByAnchor(accId);
		return rds;
	}
}
