package com.i5i58.apis.account;

import java.io.IOException;
import java.util.List;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * @author songfl
 *
 */
public interface IAccountRecord {

	/**
	 * 查询充值记录
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	ResultDataSet queryPayRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 查询礼物消费记录
	 * 
	 * @author songfl
	 */
	ResultDataSet queryGiftRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * @author songfl 获取非礼物消费记录
	 */
	ResultDataSet queryConsumptionRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 获取vip购买记录
	 * 
	 * @author songfl
	 */
	ResultDataSet queryVipRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 获取守护购买记录
	 */
	ResultDataSet queryGuardRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 获取开通粉丝团记录
	 */
	ResultDataSet queryFansClubRecord(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 查询指定IP指定时间段的注册用户列表
	 * @param Ip
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryRegisterListByOwnerId(String accId, long startTime, long endTime) throws IOException;
}
