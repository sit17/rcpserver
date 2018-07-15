package com.i5i58.apis.information;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 游戏版本服务接口
 * @author frank
 *
 */
public interface IGameInfo {

	/**
	 * 验证游戏信息版本
	 * 登陆时，客户端版本与数据库不一致，下发最新游戏信息，否则无数据 
	 * @author frank
	 * @param openId
	 * @param password
	 * @param version
	 * @return
	 */
	ResultDataSet verifyVersion(String openId, String password, String version);
	
	/**
	 * 在线时，客户端收到游戏更新通知，获取账户信息
	 * @author frank
	 * @param openId
	 * @param password
	 * @param version
	 * @return
	 */
	ResultDataSet getGameInfo(String openId, String password, String version);
}
