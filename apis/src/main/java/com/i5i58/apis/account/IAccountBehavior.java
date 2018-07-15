package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAccountBehavior {

	ResultDataSet addCollectChannel(String accId, String cId) throws IOException;

	ResultDataSet getCollectChannel(String accId, int pageNum, int pageSize) throws IOException;
	
	/**
	 * 取消收藏
	 * @author songfl
	 * */
	ResultDataSet cancelCollectChannel(String accId, String cId) throws IOException;
	
	/**
	 * 是否收藏
	 * @author songfl
	 * */
	ResultDataSet isChannelCollected(String accId, String cId) throws IOException;

	ResultDataSet addNearWatchChannel(String accId, String cId) throws IOException;

	ResultDataSet getNearWatchChannel(String accId, int pageNum, int pageSize) throws IOException;

	ResultDataSet addMyGame(String accId, int kindId) throws IOException;

	ResultDataSet getMyGame(String accId, int pageNum, int pageSize) throws IOException;
	/**
	 * 举报主播/用户
	 * 
	 * @author songfl
	 * @param accId
	 * @param reportedUser
	 * @param reason
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet reportUser(String accId, String reportedUser, String reason) throws IOException;
	
	/**
	 * 游戏中使用直播头像
	 * @author songfl
	 * @param
	 * */
	ResultDataSet useFaceInGame(String accId, int useFace) throws IOException;
}
