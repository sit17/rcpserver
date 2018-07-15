package com.i5i58.apis.im;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IIMp2p {

	/**
	 * 用户请求加好友
	 * @param accId
	 * @param faccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addFriendRequest(String accId, String faccid) throws IOException;

	/**
	 * 用户同意加好友
	 * @param accId 是添加好友请求接收方
	 * @param faccid 是添加好友请求的发起者
	 * @return
	 * @throws IOException
	 */
	ResultDataSet agreeFriendRequest(String accId, String faccid) throws IOException;

	/**
	 * 用户拒绝加好友
	 * @param accId
	 * @param faccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet disagreeFriendRequest(String accId, String faccid) throws IOException;

	/**
	 * 拉黑
	 * @param accId
	 * @param targetAccId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet pullBlackRequest(String accId, String targetAccId) throws IOException;

	/**
	 * 取消拉黑
	 * @param accId
	 * @param targetAccId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet CancelPullBlackRequest(String accId, String targetAccId) throws IOException;
	
	/**
	 * 获取用户信息
	 * @param queryAccIds
	 * @return
	 */
	ResultDataSet getAccount(String queryAccIds);
	
	/**
	 * 删除好友
	 * @param accId
	 * @param targetAccId
	 * @return
	 */
	ResultDataSet deleteFriendRequest(String accId, String faccid);
	
	/**
	 * 更新好友信息
	 * @param accId
	 * @param faccid
	 * @param alias  好友备注信息
	 * @return
	 */
	ResultDataSet updateFriendRequest(String accId, String faccid, String alias);
	
	/**
	 * 获取好友信息
	 * @param accId
	 * @param createTime
	 * @return
	 */
	ResultDataSet getFriendRequest(String accId, String createTime);
	
	/**
	 * 查询黑名单和静音列表
	 * @param accId
	 * @return
	 */
	ResultDataSet listBlackAndMuteListRequest(String accId);

}
