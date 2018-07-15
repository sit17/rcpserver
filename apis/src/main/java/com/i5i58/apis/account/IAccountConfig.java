package com.i5i58.apis.account;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 账户配置服务接口
 * 
 * @author frank
 *
 */
public interface IAccountConfig {

	/**
	 * 更新直播通知设置
	 * 
	 * @author frank
	 * @param notifyEnable
	 * @return
	 */
	ResultDataSet updateLivingNotify(boolean notifyEnable, String accId);

	/**
	 * 更新免打扰设置
	 * 
	 * @author frank
	 * @param noDisturbEnable
	 * @return
	 */
	ResultDataSet updateNoDisturb(boolean noDisturbEnable, String accId);

	/**
	 * 查询用户设置
	 * @author songfl
	 * @param accId
	 * @return
	 * */
	ResultDataSet queryAccountConfig(String accId);
	
	/**
	 * 编辑用户头像
	 * 
	 * @author frank
	 * @param accId
	 * @param faceSmallUrl
	 * @param faceOrgUrl
	 * @return
	 */
	ResultDataSet editFaceImage(String accId, String faceSmallUrl, String faceOrgUrl);
	
	/**
	 * 用户换号
	 * 
	 * @author frank
	 * @param accId
	 * @param changedAccId
	 * @return
	 */
	ResultDataSet changeOpenId(String accId, String changedAccId,String clientIP);
}
