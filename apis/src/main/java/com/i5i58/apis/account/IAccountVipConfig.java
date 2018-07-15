package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAccountVipConfig {
	/**
	 * CW
	 * 获取VIP配置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getAccountVipConfig() throws IOException;

	/**
	 * CW
	 * 购买VIP
	 * @param accId
	 * @param VipId
	 * @param clientIP
	 * @param month
	 * @return
	 */
	public ResultDataSet buyAccountVip(String accId, int level, String clientIP, int month);

	/**
	 * CW
	 * 升级VIP
	 * @param accId
	 * @return
	 */
	public ResultDataSet upgradeAccountVip(String accId, int level, String clientIP);
}
