package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformAnchorAdmin {

	/**
	 * 工作人员审核主播认证
	 * 
	 * @author frank
	 * @param superAccid
	 * @param accId
	 * @param agree
	 * @return
	 * @throws IOException
	 */
	ResultDataSet verifyAnchorAuth(String superAccid, String accId, boolean agree) throws IOException;

	/**
	 * 主播公告
	 * 
	 * @author frank
	 * @param superAccid
	 * @param title
	 * @param content
	 * @return
	 * @throws IOException
	 */
	ResultDataSet noticeAnchor(String superAccid, String title, String content) throws IOException;

	/**
	 * 按ID，名称等查询主播
	 * 
	 * @author frank
	 * @param param
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryAnchorList(String param, int pageSize, int pageNum) throws IOException;

	/**
	 * 查询主播审核列表
	 * 
	 * @param sortDir
	 *            asc,desc
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryAnchorVerifyList(String sortDir, int pageSize, int pageNum) throws IOException;

	/**
	 * 获取主播信息
	 * 
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAnchorInfo(String accId) throws IOException;

	/**
	 * 获取主播所属公会和所拥有的频道
	 * 
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAnchorChannelAndGroup(String accId) throws IOException;
	
	/**
	 * 获取主播直播历史记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param fromTime 本次查询的最早开播时间，毫秒
	 * @param toTime 本次查询的最晚开播时间，毫秒
	 * */
	public ResultDataSet getAnchorPushRecord(String accId, long fromTime, long toTime);
	
	/**
	 * 统计主播直播时长
	 * @author songfl
	 * @param fromTime 本次统计的最早开播时间，毫秒
	 * @param toTime 本次统计的最晚开播时间，毫秒
	 * */
	public ResultDataSet calcAnchorActiveTime(String accId, long fromTime, long toTime);
	
	/**
	 * 查询主播佣金记录
	 * @author caoyi
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet getAnchorCommissionInfo(int pageSize,int pageNum);
	
	/**
	 * 查询主播提现信息
	 * @author caoyi
	 * @param status
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet getAnchorWithdrawCashsInfo(int status,int pageSize,int pageNum);

	/**
	 * 处理提现
	 * @author songfl
	 * 
	 * @param superAccId
	 * @param recordId
	 * @param status
	 * @return
	 */
	public ResultDataSet processWithdrawCash(String superAccId, String withdrawId, int status);
	/**
	 * 查询主播的状态
	 * 
	 * */
	public ResultDataSet queryAnchorStatus(String superAccId,String accId);
	
	
	/**
	 * 
	 * 指定时间段，查询主播的守护开通情况
	 * @param adminAccId
	 * @param accId
	 * @param fromTime
	 * @param toTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryOpenGuardRecord(String accId, long fromTime, long toTime, int pageNum, int pageSize);
	
	/**
	 * 获取当月新增守护数量
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet getIncreasedGuardCount(String accId,long fromTime, long toTime);
	
	/**
	 * 获取守护总数量
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet getTotalGuardCount(String accId);
	
	/**
	 *指定时间段，查询粉丝团开通情况
	 *@author songfl
	 * @param adminAccId
	 * @param accId
	 * @param fromTime
	 * @param toTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryOpenClubRecord(String accId, long fromTime, long toTime, int pageNum, int pageSize);
	
	/**
	 * 查询当月新增粉丝团人数
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet getIncreasedClubMemberCount(String accId,long fromTime, long toTime);
	
	/**
	 * 查询粉丝团总人数
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet getTotalClubMemberCount(String accId);
	
	/**
	 * 获取主播频道信息
	 * @param accId
	 * @return
	 */
	ResultDataSet getAnchorChannel(String accId);
	
	/**
	 * 超管分配主播的经纪公司-公会-频道
	 * @author songfl
	 * @param superAccId
	 * @param accId
	 * @param fId
	 * @param gId
	 * @param type
	 * @param groupRate
	 * @param settleMode
	 * @return
	 */
	ResultDataSet assignChannel(String superAccId, String accId, String fId, String gId, String cId, int type, int groupRate, int settleMode);
}
