package com.i5i58.apis.platform;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformAccount {

	/**
	 * 根据ID,名称等查询账号
	 * 
	 * @author frank
	 * @param param
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet queryAccountList(String param, int pageSize, int pageNum);

	/**
	 * 查询账号信息
	 * @author frank
	 * @param accId
	 * @return
	 */
	public ResultDataSet getAccountInfo(String accId);

	/**
	 * 查询用户订单
	 * @author songfl
	 * @param
	 * */
	public ResultDataSet queryOnlineOrder(String accId, int pageSize, int pageNum);
	
	/**
	 * 查询用户现金流向
	 * */
	public ResultDataSet queryMoneyFlow(String accId, int pageSize, int pageNum);
	
	/**
	 * 查询账号充值记录
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet queryAccountPay(String accId, int pageSize, int pageNum);

	/**
	 * 查询账号消费记录
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet queryAccountConsume(String accId, int pageSize, int pageNum);

	/**
	 * 开启/禁封账号
	 * 
	 * @author frank
	 * @param superAccId
	 * @param accId
	 * @return
	 */
	public ResultDataSet enableAccount(String superAccId, String accId, boolean nullity, boolean needKick);

	/**
	 * 回复申诉
	 * 
	 * @author frank
	 * @param superAccId
	 * @param appealId
	 * @param agree
	 * @param reason
	 * @return
	 */
	public ResultDataSet answerAppeal(String superAccId, String appealId, boolean agree, String reason);
	
	/**
	 * 更新礼物券数量
	 * 
	 * @author songfl
	 * @param adminAccId
	 * @param targetAccId
	 * @param ope 0 增加; 1 减少; 2 重置为;
	 * @param giftTickets
	 * @return
	 */
	public ResultDataSet updateGiftTicket(String adminAccId, String targetAccId, byte ope, long giftTickets, String clientIp);
	
	/**
	 * 更新I币数量
	 * 
	 * @author songfl
	 * @param adminAccId
	 * @param targetAccId
	 * @param ope 0 增加; 1 减少; 2 重置为;
	 * @param iGolds
	 * @return
	 */
	public ResultDataSet updateIGold(String adminAccId, String targetAccId, byte ope, long iGolds);
	
	/**
	 * 查询实名信息
	 * 
	 * @author songfl
	 * @param adminAccId
	 * @param targetAccId
	 * @return
	 */
	public ResultDataSet queryAccountAuthInfo(String adminAccId, String targetAccId);
	
	/**
	 * 查询个人礼物赠送记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * */
	public ResultDataSet queryAccountGiftRecord(String accId, int pageSize, int pageNum);
	
	/**
	 * 查询个人钱包
	 * 
	 * @author songfl
	 * @param accId
	 * */
	public ResultDataSet queryAccountWallet(String accId);
	
	/**
	 * 通知用户上传app log
	 * 
	 * @author songfl
	 * @param admin
	 * @param accId
	 * @param bucketName
	 * */
	public ResultDataSet appLogRequirement(String admin, String accId, String bucketName);
	
	/**
	 * 用户充值排行
	 * @author songfl
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultDataSet queryAccountPayList(int status, int pageSize, int pageNum);
}
