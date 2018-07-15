package com.i5i58.apis.social;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 关注并开播服务接口
 * @author frank
 *
 */
public interface IAttentionAndLiving {

	/**
	 * 获取关注并开播
	 * @author frank
	 * @param openId
	 * @param password
	 * @return
	 */
	ResultDataSet getAttentionAndLiving(String accId);
}
