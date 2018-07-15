package com.i5i58.apis.android;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAndroidAction {

	/**
	 * 添加机器人
	 * 
	 * @author frank
	 * @param nickName
	 * @param faceUrl
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addAndroidAccount(String nickName, String gameName, byte gender, String location,
			String faceUrl) throws IOException;

	/**
	 * 机器人登录
	 * 
	 * @author frank
	 * @param accId
	 * @param password
	 * @return
	 * @throws IOException
	 */
	ResultDataSet androidLogin(String accId, String password) throws IOException;

	/**
	 * 获取机器人列表
	 * 
	 * @author frank
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAndroidList(int pageNum, int pageSize) throws IOException;

	/**
	 * 获取频道列表
	 * 
	 * @author frank
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelList(int pageNum, int pageSize) throws IOException;

	/**
	 * 进入频道
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet enterChannel(String accId, String cId) throws IOException;

	/**
	 * 退出频道
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet exitChannel(String accId, String cId) throws IOException;

	/**
	 * 获取频道机器人观众
	 * 
	 * @author frank
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAndroidViewerList(int pageNum, int pageSize) throws IOException;

	/**
	 * 送礼物
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @param giftId
	 * @param giftCount
	 * @param continuous
	 * @return
	 * @throws IOException
	 */
	ResultDataSet giveGift(String accId, String cId, int giftId, int giftCount, int continuous) throws IOException;

	/**
	 * 频道内关注
	 * 
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet followInChannel(String accId, String attAccId, String cId) throws IOException;

	/**
	 * 送星
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet giveHeart(String accId, String cId) throws IOException;
}
