package com.i5i58.apis.platform;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformWechat {
	/**
	 * 查询已绑定的用户列表
	 * @author cy
	 * @param param
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	ResultDataSet queryWechatAccount(int pageSize,int pageNum);
	
	/**
	 * 查询已绑定用户
	 * @param accId
	 * @return
	 */
	ResultDataSet getBindedWechatAccount(String param,int pageSize,int pageNum);
	
	
	/**
	 * 添加用户抽奖机会
	 * @author cy
	 * @param SuperAccId
	 * @param accId
	 * @param lotteryCount
	 * @param nullity
	 * @return
	 */
	ResultDataSet setLotteryChance(String SuperAccId,String accId,int lotteryCount);
	
	/**
	 * 删除抽奖机会
	 * @author cy
	 * @param SuperAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet deleteLotreryChance(String SuperAccId,String accId);
	
	/**
	 * 查询用户抽奖机会
	 * @author cy
	 * @param accId
	 * @return
	 */
	ResultDataSet queryLotteryChance(String accId);
	
	/**
	 * 添加奖品
	 * @author cy
	 * @param SuperAccId
	 * @param id
	 * @param name
	 * @param probability
	 * @param nullity
	 * @return
	 */
	ResultDataSet setAwardConfig(String SuperAccId,int id,long amount,int unit,String description, int rate,String url,boolean nullity);
//	
//	/**
//	 * 更新奖品
//	 * @author cy
//	 * @param SuperAccId
//	 * @param id
//	 * @param name
//	 * @param probability
//	 * @param nullity
//	 * @return
//	 */
//	ResultDataSet updateAwardConfig(String SuperAccId, int id, int type, long amount, String description, int rate, boolean nullity);
	
	/**
	 * 删除奖品
	 * @author cy
	 * @param SuperAccId
	 * @param id
	 * @return
	 */
	ResultDataSet deleteAwardConfig(String SuperAccId,int id);
	
	/**
	 * 查询奖品列表
	 * @author cy
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	ResultDataSet queryAwardConfig(int pageSize,int pageNum);
	
	/**
	 * 查询用户抽奖记录表
	 * @param accId
	 * @return
	 */
	ResultDataSet queryAwardOpeRecord(String accId);
	
//	/**
//	 * 添加用户抽奖记录
//	 * @param SuperAccId
//	 * @param id
//	 * @param accId
//	 * @param rewardDateTime
//	 * @param description
//	 * @param deliveryDateTime
//	 * @return
//	 */
//	ResultDataSet setAwardOpeRecord(String SuperAccId,int id,String accId,int awardId,long amount,int unit,long rewardDateTime,long deliveryDateTime);
//	
//	/**
//	 * 删除用户抽奖记录
//	 * @param SuperAccId
//	 * @param id
//	 * @return
//	 */
//	ResultDataSet deleteAwardOpeRecord(String SuperAccId,int id);
	}
