package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAccountWechat {
	/****
	 * 绑定微信账号
	 * @param accId
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet bindWechatAccount(String accId, String openId)throws IOException;
	
	/***
	 * 设定默认微信号
	 * @param accId
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet setCurWechatAccount(String accId, String openId)throws IOException;
	
	/***
	 * 通过openId获取accId
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAccIdByOpenId(String openId)throws IOException;
	
	/***
	 * 通过openId获取accId
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	
	ResultDataSet getAccIdByLiveOpenId(String liveOpenId)throws IOException;
	
	/**
	 * 添加抽奖功能
	 * @param accId
	 * @return
	 */
	ResultDataSet lottery(String accId,String clientIp,String serialNum);
	
	/**
	 * 查询用户抽奖机会
	 * @param accId
	 * @return
	 */
	ResultDataSet queryLotteryChance(String accId);
	
	/**
	 * 查询奖品
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
}
