package com.i5i58.apis.group;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IChannelGroupAdmin {

	/**
	 * 查询经纪公司档案列表
	 * 
	 * @param param
	 * @param status
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryProfileList(String param, int status, int pageSize, int pageNum) throws IOException;

	/**
	 * 创公会、经纪公司档案
	 * 
	 * @author frank
	 * @param accId
	 * @param name
	 * @param description
	 * @param type
	 * @param licenseUrl
	 *            执照url
	 * @param address
	 * @param legalPerson
	 *            法人
	 * @param regCapital
	 *            注册资金
	 * @param fixedPhone
	 *            固定电话
	 * @param createIp
	 * @return
	 */
	public ResultDataSet createProfile(String accId, String name, String description, int type, String registerId,
			String operRange, String licenseUrl, String taxCertificateUrl, String organizationCodeUrl,
			String bankLicenseUrl, String address, String legalPerson, String legalPersonUrl, String dataUrl,
			String legalPersonBackUrl, double regCapital, String fixedPhone, String email, String createIp);

	/**
	 * 建立顶级组（公会）
	 * 
	 * @author frank
	 * @param superAccid
	 * @param gfId
	 *            档案ID
	 * @return
	 * @throws IOException
	 */
	ResultDataSet createChannelTopGroup(String accId, String name, String faceUrl, String fId, String createIp)
			throws IOException;

	/**
	 * 创建次级频道组
	 * 
	 * @author frank
	 * @param accId
	 * @param name
	 * @param gId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet createChannelSubGroup(String accId, String name, String gId, String createIp);

	/**
	 * 指派频道组拥有者，top组只有超管才可以
	 * 
	 * @author frank
	 * @param owner
	 *            指派的拥有者
	 * @param accId
	 *            指派者
	 * @param gId
	 *            组id
	 * @return
	 */
	public ResultDataSet assignChannelGroupOwner(String owner, String accId, String gId);

	/**
	 * 指派频道组管理员，只有本频道组拥有者（超管可以）
	 * 
	 * @author frank
	 * @param admin
	 * @param accId
	 * @param gId
	 * @return
	 */
	public ResultDataSet assignChannelGroupAdmin(String admin, String accId, String gId);

	/**
	 * 在组内创建频道
	 * 
	 * @author frank
	 * @param accId
	 * @param gId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet createChannelInGroup(String accId, String gId, String name, String createIp);

	/**
	 * 获取我的公会档案列表
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyGroupFiles(String accId);

	/**
	 * 获取我的公会列表
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyGroups(String accId);

	/**
	 * 获取我的经纪公司下属的公会/频道分组列表
	 * 
	 * @author songfl
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyAgencyGroups(String accId);

	/**
	 * 修改组名称
	 * 
	 * @author frank
	 * @param accId
	 * @param gId
	 * @param name
	 * @return
	 */
	public ResultDataSet editGroupName(String accId, String gId, String name);

	/**
	 * 修改组描述
	 * 
	 * @author frank
	 * @param accId
	 * @param gId
	 * @param desc
	 * @return
	 */
	public ResultDataSet editGroupInfo(String accId, String gId, String groupIconUrl, String groupName, String desc,
			String groupNotice);

	/**
	 * 修改组描述
	 * 
	 * @author huangl
	 * @param accId
	 * @param gId
	 * @param desc
	 * @return
	 */
	public ResultDataSet editGroupDesc(String accId, String gId, String desc);

	/**
	 * 发起合约
	 * 
	 * @author frank
	 * @param gId
	 * @param accId
	 * @param groupRate
	 * @param endTime
	 * @param settleMode
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet requestContract(String adminAccId, String gId, String accId, int groupRate, long endDate,
			int settleMode) throws IOException;

	/**
	 * 公会回应合约
	 * 
	 * @author frank
	 * @param ctId
	 * @param agree
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet responseContract(String adminAccId, String ctId, boolean agree) throws IOException;

	/**
	 * 获取合约
	 * 
	 * @author frank
	 * @param ctId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getContract(String adminAccId, String ctId) throws IOException;

	/**
	 * 查询合约
	 * 
	 * @author frank
	 * @param gId
	 * @param status
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryContractByGId(String adminAccId, String gId, int status, int pageSize, int pageNum)
			throws IOException;

	/**
	 * 查询即将到期合约
	 * 
	 * @author frank
	 * @param gId
	 * @param Expire
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryContractExpire(String adminAccId, String gId, long Expire, int pageSize, int pageNum)
			throws IOException;

	/**
	 * 查询经纪公司主播
	 * 
	 * @author frank
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryAnchorByTopGroup(String adminAccId, String gId, int pageSize, int pageNum)
			throws IOException;

	/**
	 * 查询公会分组
	 * 
	 * @author frank
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet querySubGroup(String adminAccId, String gId) throws IOException;

	/**
	 * 查询公会内频道
	 * 
	 * @author frank
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryChannelInTopGroup(String adminAccId, String gId) throws IOException;

	/**
	 * 查詢公会内未指派给分组的频道
	 * 
	 * @author cw
	 * @param gId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryChannelInTopGroupNoAssign(String adminAccId, String gId) throws IOException;

	/**
	 * 获取公会
	 * 
	 * @author frank
	 * @param gId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getTopGroup(String adminAccId, String gId) throws IOException;

	/**
	 * 查询公会管理员列表
	 * 
	 * @author 王森涛
	 * @param adminAccId
	 * @param gId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryAdminorList(String adminAccId, String gId, int pageNum, int pageSize) throws IOException;

	/**
	 * 查询公会主播列表
	 * 
	 * @author 王森涛
	 * @param adminAccId
	 * @param gId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryAnchorList(String adminAccId, String gId, int pageNum, int pageSize) throws IOException;

	/**
	 * 移除次级频道组
	 * 
	 * @author cw
	 * @param accId
	 * @param name
	 * @param gId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet removeChannelSubGroup(String accId, String name, String gId, String createIp);

	/**
	 * 移动频道分组
	 * 
	 * @author cw
	 * @param accId
	 * @param cId
	 * @param gId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet rechangeChannelGroup(String accId, String cId, String gId, String createIp);

	/**
	 * 发起解除合约
	 * 
	 * @author cw
	 * @param gId
	 * @param accId
	 * @param groupRate
	 * @param endTime
	 * @param settleMode
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet requestCancelContract(String requestAccId, String anchorAccId, String ctId) throws IOException;

	/**
	 * 同意解除合约
	 * 
	 * @author cw
	 * @param ctId
	 * @param agree
	 * @return
	 * @throws IOException
	 *//*
		 * public ResultDataSet responseCancelContract(String adminAccId, String
		 * ctId) throws IOException;
		 */

	/**
	 * 同意解除合约
	 * 
	 * @author Hl
	 * @param ctId
	 * @param agree
	 * @param responseType
	 *            0为不同意 1 为同意
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet responseCancelContractNormal(String adminAccId, String ctId, boolean agree) throws IOException;

	/**
	 * 移除无效的合约
	 * 
	 * @author songfl
	 * @param accId
	 *            主播或工会管理员
	 * @param ctId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet hideContract(String accId, String ctId) throws IOException;

	/**
	 * 解绑频道
	 * 
	 * @author cw
	 * @param adminAccId
	 * @param cId
	 * @return
	 */
	public ResultDataSet unbundlingChannelGroup(String adminAccId, String cId);

	/**
	 * 取消频道组管理员，只有本频道组拥有者（超管可以）
	 * 
	 * @author cw
	 * @param adminAccId
	 * @param accId
	 * @param gId
	 * @return
	 */
	public ResultDataSet cancelChannelGroupAdmin(String adminAccId, String accId, String gId);

	/**
	 * 移除次级组中的频道
	 * 
	 * @author cw
	 * @param accId
	 * @param name
	 * @param gId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet removeChannelInSubGroup(String accId, String cId, String gId, String createIp);

	/**
	 * 获取工会或分组的展示信息
	 * 
	 * @author songfl
	 * @param accId
	 * @param gId
	 * @return
	 */
	public ResultDataSet getGroupInfo(String accId, String gId) throws IOException;

	/**
	 * 获取工自己的权限
	 * 
	 * @author songfl
	 * @param accId
	 * @param gId
	 * @return
	 */
	public ResultDataSet getMyGroupRight(String accId, String gId) throws IOException;

	/**
	 * 查询公会里没有频道的主播
	 * 
	 * @author songfl
	 * @param accId
	 */
	public ResultDataSet queryAnchorNoChannelByTopGroup(String adminAccId, String gId, int pageSize, int pageNum)
			throws IOException;

	/**
	 * 获取主播直播历史记录
	 * 
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @param fromTime
	 *            本次查询的最早开播时间，毫秒
	 * @param toTime
	 *            本次查询的最晚开播时间，毫秒
	 */
	public ResultDataSet getAnchorPushRecord(String adminAccId, String accId, long fromTime, long toTime);

	/**
	 * 统计主播直播时长
	 * 
	 * @author songfl
	 * @param adminAccId
	 * @param accId
	 * @param fromTime
	 *            本次统计的最早开播时间，毫秒
	 * @param toTime
	 *            本次统计的最晚开播时间，毫秒
	 */
	public ResultDataSet calcAnchorActiveTime(String adminAccId, String accId, long fromTime, long toTime);

	/**
	 * 查看等待回复的合约（签约和解约）
	 * 
	 * @author huangling
	 * @param adminAccId
	 * @param gId
	 *            公会Id
	 */

	public ResultDataSet queryRequestedContracts(String accId, String gId, int direction, int pageSize, int pageNum);
	
	/***
	 * 获取公会操作记录
	 * 
	 * @author songfl
	 * @param addminAccId
	 * @param gId	公会id
	 * @param pageSize
	 * @param pageNum
	 * */
	public ResultDataSet queryTopGroupRecord(String adminAccId, String gId, int pageSize, int pageNum);
	
	/***
	 * 获取公会内频道分组操作记录
	 * 
	 * @author songfl
	 * @param addminAccId
	 * @param gId	公会id
	 * @param pageSize
	 * @param pageNum
	 * */
	public ResultDataSet querySubGroupRecord(String adminAccId, String gId, int pageSize, int pageNum);
	/***
	 * 获取公会内频道操作记录
	 * 
	 * @author songfl
	 * @param addminAccId
	 * @param gId	公会id
	 * @param pageSize
	 * @param pageNum
	 * */
	public ResultDataSet queryChannelOpeRecord(String adminAccId, String gId, int pageSize, int pageNum);
	
	/***
	 * 获取公会内合约记录
	 * 
	 * @author songfl
	 * @param addminAccId
	 * @param gId	公会id
	 * @param pageSize
	 * @param pageNum
	 * */
	public ResultDataSet queryContractOpeRecord(String adminAccId, String gId, int pageSize, int pageNum);
	
	/**
	 * 删除空闲频道
	 * @param adminAccId
	 * @param cId
	 * @return
	 * */
	public ResultDataSet deleteFreeChannel(String adminAccId,String gId,String cId);
	
	/**
 	 * 公会强制解约
	 * @param requestAccId
	 * @param gId
	 * @param ctId
	 * @return
	 */
	public ResultDataSet forceCancelContract(String requestAccId, String gId,String ctId);
	
	/**
	 * 查询主播的状态列表
	 * @author Cy
	 * @param adminAccId
	 * @param accId
	 * @return
	 */
	public ResultDataSet queryAnchorStatus(String adminAccId,String accId);
	
//	/**
//	 * 查询主播俸禄
//	 * 
//	 * */
//	public ResultDataSet queryAnchorCommission(String accId);
	
	/**
	 * 查询公会频道数
	 * @param adminAccId
	 * @param cId
	 * @return
	 */
	public ResultDataSet queryChannelCount(String adminAccId,String gId);
	
	/**
	 * 
	 * 查询主播钱包
	 * @author songfl
	 * @param adminAccId
	 * @param gId
	 * @param accId
	 * @return
	 */
	ResultDataSet queryAnchorWallet(String adminAccId, String gId, String accId);
	
	/**
	 * 查询主播上个月月均俸禄信息
	 * @author songfl
	 * @param adminAccId
	 * @param gId
	 * @param accId
	 * @return
	 */
	ResultDataSet queryAverageGiftCommision(String adminAccId, String gId, String accId,long fromTime,long toTime);
}
