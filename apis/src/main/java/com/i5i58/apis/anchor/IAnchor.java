package com.i5i58.apis.anchor;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAnchor {

	/**
	 * 主播向公会发起签约
	 * 
	 * @author frank
	 * @param accId
	 * @param gId
	 * @param groupRate
	 * @param endDate
	 * @param settleMode
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet requestContract(String accId, String gId, int groupRate, long endDate, int settleMode)
			throws IOException;

	/**
	 * 查询合约
	 * 
	 * @author frank
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryContractByAccId(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 回应签约主播
	 * 
	 * @author frank
	 * @param ctId
	 * @param agree
	 * @return
	 */
	public ResultDataSet responseContract(String ctId, boolean agree) throws IOException;

	/**
	 * 获取我的频道
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMyPush(String accId) throws IOException;

	/**
	 * 获取我的频道-移动设备
	 * @author frank
	 * @param accId
	 * @param device
	 * @param serialNum
	 * @param model
	 * @param osVersion
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMyPushMobile(String accId, int device, String serialNum, String model, String osVersion)
			throws IOException;

	/**
	 * 提现
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet withdrawCash(String accId, long amount, String clientIp) throws IOException;

	/**
	 * 查询提现
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryWithdrawCash(String accId, int pageNum, int pageSize) throws IOException;

	/**
	 * 查询主播佣金信息
	 * 
	 * @author songfl
	 * @param accId
	 */
	public ResultDataSet getCommissionInfo(String accId) throws IOException;

	/**
	 * 查询骑士/佣金配置
	 * 
	 * @author HL
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getGuardCommissionConfigAll() throws IOException;

	/**
	 * 主播佣金兑换钻石
	 * 
	 * @author HL
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet commissionExchangeToDiamond(String accId, long diamond,String clientIP) throws IOException;

	/**
	 * 主播获取绑定银行卡信息
	 * 
	 * @author HL
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getAnchorBankInfo(String accId) throws IOException;

	/**
	 * 主播获取俸禄兑换明细
	 * 
	 * @author HL
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getCommissionExchangeDetail(String accId) throws IOException;

	/**
	 * 获取主播直播历史记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param fromTime
	 *            本次查询的最早开播时间
	 * @param toTime
	 *            本次查询的最晚开播时间
	 */
	public ResultDataSet getPushRecord(String accId, long fromTime, long toTime);

	/**
	 * 统计主播直播时长
	 * 
	 * @author songfl
	 * @param fromTime
	 *            本次统计的最早开播时间，毫秒
	 * @param toTime
	 *            本次统计的最晚开播时间，毫秒
	 */
	public ResultDataSet calcActiveTime(String accId, long fromTime, long toTime);

	/**
	 * 获取主播守护
	 * 
	 * @author songfl
	 * 
	 * @param accId
	 *            主播id
	 * @param fromTime
	 *            购买时间
	 * @param toTime
	 *            购买时间
	 */
	public ResultDataSet queryGuardByTime(String accId, long fromTime, long toTime);
	
	/**
	 * 获取主播所属公会信息
	 * @author songfl
	 * @param anchorId
	 * */
	public ResultDataSet queryMyTopGroup(String anchorId);
	
	/**
	 * 主播强制解约
	 * 
	 * */
	public ResultDataSet forceCancelContract(String accId,String ctId);
}
