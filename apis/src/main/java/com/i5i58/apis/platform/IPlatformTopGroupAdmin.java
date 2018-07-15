package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformTopGroupAdmin {

	/**
	 * 按ID,名称等查找公会
	 * @author frank
	 * @param topGName
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryTopGroupList(String param, int pageSize, int pageNum) throws IOException;

	/**
	 * 开启/禁封公会(TopGroup)
	 * @author frank
	 * @param superAccid
	 * @param gId
	 * @param enable
	 * @return
	 * @throws IOException
	 */
	ResultDataSet enableTopGroup(String superAccid, String gId, boolean enable) throws IOException;

	/**
	 * 查询该公会隶属的经纪公司
	 * @author frank
	 * @param gId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryProfileByTopGroup(String gId) throws IOException;
	
	/**
	 * 查询管理员列表
	 * @author frank
	 * @param gId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryAdminorsByTopGroup(String gId) throws IOException;

	/**
	 * 公会公告
	 * @author frank
	 * @param superAccid
	 * @param title
	 * @param content
	 * @return
	 * @throws IOException
	 */
	ResultDataSet noticeTopGroup(String superAccid, String title, String content) throws IOException;

	/**
	 * 查找工会下属频道
	 * @author caowei
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelByTopGroup(String gId, int pageSize, int pageNum) throws IOException;

	/**
	 * 查询工会下属主播
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAnchorByTopGroup(String gId, int pageSize, int pageNum) throws IOException;
	
	/**
	 * 同意解除合约
	 * 
	 * @author cw
	 * @param ctId
	 * @param agree
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet responseCancelContract(String ctId) throws IOException;
	
	/**
	 * 根据分组Id或工会Id查询工会信息
	 * 
	 * @author songfl
	 * @param gId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getTopGroupByGId(String gId) throws IOException;
	
	/**
	 * 根据分组Id或工会Id查询工会信息
	 * 
	 * @author wangsentao
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryCancelContract(int pageSize, int pageNum) throws IOException;
	
	/**
	 * 查询强制解约的请求
	 * */
	public  ResultDataSet queryForceCancelContract(int pageSize,int pageNum) throws IOException;
	
	/**
	 * 同意强制解约
	 * */
	public ResultDataSet responseForceCancelContract(String ctId) throws IOException;
	
	/**
	 * 查询主播上个月月均俸禄信息
	 * @author songfl
	 * @param adminAccId
	 * @param gId
	 * @param accId
	 * @return
	 */
	ResultDataSet queryAverageGiftCommision(String accId,long fromTime,long toTime);
	
	/**
	 * 创建频道
	 * @author songfl
	 * @param superAccId
	 * @param gId
	 * @param name
	 * @param createIp
	 * @return
	 */
	ResultDataSet createChannel(String superAccId, String gId, String name, String createIp);
}
