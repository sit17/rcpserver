package com.i5i58.apis.channel;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IChannelPlayInfo {

	/**
	 * 获取礼物设置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getChannelGiftConfig() throws IOException;

	/**
	 * 获取坐骑设置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getChannelMountConfig() throws IOException;

	/**
	 * 获取动画配置
	 * 
	 * @return
	 */
	public ResultDataSet getAnimationConfig();

	/**
	 * 获取频道配置
	 * 
	 * @param device
	 * @param giftVersion
	 * @param mountVersion
	 * @param animationVersion
	 * @return
	 */
	public ResultDataSet getChannelConfig(int device, String giftVersion, String mountVersion, String animationVersion);

	/**
	 * 获取频道状态
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet getChannelStatus(String cId);

	/**
	 * 获取爱心信息
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet getHeart(String accId, String cId);

	/**
	 * 爱心签到，获取爱心
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet takeHeart(String accId);

	/**
	 * 获取一段时间内的签到获取爱心记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public ResultDataSet getHeartTakenHistory(String accId, long fromTime, long toTime);

	/**
	 * 送爱心
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet giveHeart(String accId, String cId);

	/**
	 * 开通粉丝团
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @param clientIp
	 * @return
	 */
	public ResultDataSet openClub(String accId, String cId, int month, String clientIp);

	/**
	 * 佩戴粉丝团
	 * 
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet selectClub(String accId, String cId);

	/**
	 * 获取加入的粉丝团
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultDataSet getJoinedClubs(String accId, int pageNum, int pageSize);

	/**
	 * 获取粉丝团成员
	 * 
	 * @author songfl
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 */
	public ResultDataSet getClubFansList(String cId, int pageNum, int pageSize);

	/**
	 * 主播获取售出骑士列表
	 * 
	 * @author huangl
	 * @param accId
	 * @param pageNum
	 * @param pageSize
	 */
	public ResultDataSet getAchorGuardList(String accId, String cId, int pageNum, int pageSiz) throws IOException;

	/**
	 * 是否开通粉丝团
	 * 
	 * @author songfl
	 * @param cId
	 * @return
	 */
	public ResultDataSet isFansClubOpened(String cId, String accId);

	/**
	 * 获取我在当前频道道的粉丝团信息
	 */
	public ResultDataSet getClubInfoByChannel(String cId, String accId);

	/**
	 * 获取粉丝团购买配置
	 */
	public ResultDataSet getFansClubConfig();

	/**
	 * 获取粉丝团任务配置
	 */
	public ResultDataSet getClubTaskConfig();

	/**
	 * 获取我的任务详情
	 * 
	 * @author songfl
	 * @param accId
	 * @param cId
	 */
	public ResultDataSet getMyTaskDetails(String accId, String cId);

	/**
	 * 开通守护
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @param level
	 * @param month
	 * @param clientIp
	 * @return
	 */
	public ResultDataSet openGuard(String accId, String cId, int level, int month, String clientIp);

	/**
	 * 打卡
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet dailyClock(String accId, String cId);

	/**
	 * 今天打卡状态
	 * 
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet dailyClockStatus(String accId, String cId);

	/**
	 * 获取一段时间内的打卡记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public ResultDataSet getDailyClockHistory(String accId, String cId, long fromTime, long toTime);

	/**
	 * 获取打卡排名
	 * 
	 * @author songfl
	 * @param cId
	 * @param startRankIndex
	 * @param num
	 * @return
	 */
	public ResultDataSet getDailyClockRankList(String cId, int pageSize, int pageNum);

	/**
	 * 頻道內關注
	 * 
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @param cId
	 * @return
	 */
	public ResultDataSet follow(String accId, String attAccId, String cId);

	/**
	 * 购买粉丝团坐骑
	 * 
	 * @author frank
	 * @param accId
	 * @param mountId
	 * @param cId
	 * @return
	 */
	public ResultDataSet buyMount(String accId, int mountId, String cId,String clientIp);

	/**
	 * 获取观众
	 * 
	 * @param cId
	 * @param accId
	 * @return
	 */
	public ResultDataSet getViewer(String cId, String accId);

	/**
	 * 获取频道信息
	 * 
	 * @author cw
	 * @param cId
	 * @return
	 */
	public ResultDataSet getChannelInfo(String cId);

	/**
	 * 获取频道人数
	 * 
	 * @param cId
	 * @return
	 */
	public ResultDataSet getPlayerTimes(String cId);

	/**
	 * CW 获取守护设置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getChannelGuardConfig() throws IOException;

	/**
	 * 获取频道右侧栏信息
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getChannelRightInfo(String accId, String cId) throws IOException;

	/**
	 * 获取用户频道内的特权
	 * 
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getUserChannelRights(String accId, String cId) throws IOException;

	/**
	 * 客户端分享成功
	 * 
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet socailShareSuccess(String accId, String cId);

	/**
	 * 获取用户频道
	 * 
	 * @author songfl
	 * @param accId
	 * @return
	 */
	public ResultDataSet getChannelByOwner(String owner);

	/**
	 * 修改粉丝团名称
	 * 
	 * @author songfl
	 * @param
	 */
	public ResultDataSet editFansClubName(String accId, String cId, String clubName);

	/**
	 * 修改粉丝团Icon
	 * 
	 * @author songfl
	 * @param
	 */
	public ResultDataSet editFansClubIcon(String accId, String cId, String clubIcon);

	
	/**
	 * 获取音效配置
	 * */
	public ResultDataSet getChannelSoundConfig();
	
	/**
	 * 获取主播所属公会
	 * 
	 * @author songfl
	 * 
	 * @param accId
	 * @return 
	 * */
	public ResultDataSet getTopGroupByAnchor(String accId);
}
